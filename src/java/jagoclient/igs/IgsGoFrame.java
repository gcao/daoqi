package jagoclient.igs;

import jagoclient.GameResult;
import jagoclient.Global;
import jagoclient.Move;
import jagoclient.Rule;
import jagoclient.board.*;
import jagoclient.dialogs.Message;
import jagoclient.dialogs.SharedLibertyQuestion;
import jagoclient.dialogs.SimpleMessage;
import jagoclient.gui.CheckboxMenuItemAction;
import jagoclient.igs.oob.OOBCommand;
import jagoclient.igs.oob.OOBCountRule;
import jagoclient.sound.JagoSound;
import rene.gui.IconBar;
import rene.util.parser.StringParser;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;

/**
 * This is a ConnectedGoFrame, which is used to display board
 * on the server (status, observing or playing). It takes care
 * of the timer and interprets menu actions (like send etc.)
 * <p/>
 * The board is connected to a distributor (e.g. PlayDistributor),
 * which communicates with the IgsStream. The distributor will
 * normally invoke a second object, which parses server input
 * and sends it to this frame.
 * <p/>
 * Note that there is a timer to count down the remaining seconds.
 */

public class IgsGoFrame extends ConnectedGoFrame
        implements TimedBoard, OutputListener, KeyListener {
// ------------------------------ FIELDS ------------------------------

    public static final int PLAYER = 0;
    public static final int OBSERVER = 1;
    public CheckboxMenuItem Playing, Terminal, ShortLines;
    public ConnectionFrame CF;
    Distributor Dis; // Distributor for this frame
    String BlackName = "?", WhiteName = "?";
    int BlackTime = 0, WhiteTime = 0, BlackMoves, WhiteMoves;
    int BlackRun = 0, WhiteRun = 0;
    int GameNumber;
    GoTimer Timer;
    long CurrentTime;
    String Title = "";
    boolean HaveTime = false;
    ConnectionFrame cf;
    boolean ignoreNextPass = false;
    String OldS = "";
    char form[] = new char[32];
    int lastbeep = 0;
    protected int myRole;
    protected String opponent;
    private int time_in_secs = 0;

// --------------------------- CONSTRUCTORS ---------------------------

    public IgsGoFrame(ConnectionFrame f, String s) {
        super(s, 19, Global.resourceString("Remove_groups"),
                Global.resourceString("Send_done"), true, true);
        Dis = null;
        CF = f;
        Timer = new GoTimer(this, 1000);
        FileMenu.addSeparator();
        FileMenu.add(Playing = new CheckboxMenuItemAction(this, Global.resourceString("Play")));
        Options.addSeparator();
        Options.add(Terminal = new CheckboxMenuItemAction(this, Global.resourceString("Display_Terminal_Output")));
        Terminal.setState(Global.getParameter("getterminal", true));
        setterminal();
        Options.add(ShortLines = new CheckboxMenuItemAction(this, Global.resourceString("Short_Lines_only")));
        ShortLines.setState(Global.getParameter("shortlinesonly", true));
        addKeyListener(this);
        B.addKeyListener(this);
        if (ExtraSendField) {
            SendField.addKeyListener(this);
            SendField.loadHistory("sendfield.history");
        }
        setCountRule(Rule.CHINESE_RULE);
    }

    public void setterminal() {
        if (Terminal.getState())
            CF.addOutputListener(this);
        else
            CF.removeOutputListener(this);
        Global.setParameter("getterminal", Terminal.getState());
    }

    public void setCountRule(int rule) {
        countRule = rule;
        /*
          switch (rule) {
          case Rule.CHINESE_RULE:
              countRule = rule;
              chineseCountRule.setState(true);
              jihuoziRule.setState(false);
              japaneseRule.setState(false);
              break;
          case Rule.JI_HUO_ZI_RULE:
              countRule = rule;
              chineseCountRule.setState(false);
              jihuoziRule.setState(true);
              japaneseRule.setState(false);
              break;
          case Rule.JAPANESE_RULE:
              countRule = rule;
              chineseCountRule.setState(false);
              jihuoziRule.setState(false);
              japaneseRule.setState(true);
              break;
          default:
              //throw new IllegalArgumentException(String.valueOf(rule));
              // warning: invalid rule.
              System.out.println("Invalid rule: "+rule);
          }
          */
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public int getMyRole() {
        return myRole;
    }

    public void setMyRole(int myRole) {
        this.myRole = myRole;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public boolean isIgnoreNextPass() {
        return ignoreNextPass;
    }

    public void setIgnoreNextPass(boolean ignoreNextPass) {
        this.ignoreNextPass = ignoreNextPass;
    }

// ------------------------ INTERFACE METHODS ------------------------

// --------------------- Interface BoardInterface ---------------------

    /**
     * an IgsGoFrame is blocked, when there is not Distributor left
     */
    public boolean blocked() {
        if (Dis != null)
            return Dis.blocked();
        else
            return false;
    }

    /**
     * called from the board to sound an alarm
     */
    public void yourMove(boolean notinpos) {
        if (Dis == null || !Dis.started()) return;
        if (notinpos) {
            if (Dis.wantsmove())
                JagoSound.play("yourmove", "stone", true);
            else
                JagoSound.play("stone", "click", false);
        } else if (Global.getParameter("sound.everymove", true))
            JagoSound.play("stone", "click", true);
        else if (Dis.Playing || Dis.newmove())
            JagoSound.play("click", "click", false);
    }

// --------------------- Interface DoActionListener ---------------------

    public void doAction(String o) {
        if (Global.resourceString("Send").equals(o) && Dis != null) {
            new SendQuestion(this, Dis);
        } else if (ExtraSendField && "SendField".equals(o) && Dis != null) {
            String s = SendField.getText();
            addComment("---> " + s);
            Dis.out(s);
            SendField.remember(s);
            SendField.setText("");
        } else if (Global.resourceString("Refresh").equals(o) && Dis != null) {
            B.deltree();
            Dis.refresh();
        } else if (Global.resourceString("Remove_groups").equals(o)) {
            B.score();
        } else if (Global.resourceString("Send_done").equals(o)) {
            if (B.canfinish() && Dis != null && Dis.wantsmove()) {
                // don't send "done" because we use different counting method!
                // if I lose, send "resign", otherwise wait until the opponent to resign.
                //Dis.out("done");
                //addComment("--> done <--");
            }
        } else if (Global.resourceString("Undo").equals(o)) {
            B.undo();
        } else if (Global.resourceString("Load_Teaching_Game").equals(o)) {
            if (Teaching.getState()) super.doAction(Global.resourceString("Load"));
        } else if (Global.resourceString("Adjourn").equals(o)) {
            Dis.out("adjourn");
        } else if (Global.resourceString("Resign").equals(o)) {
            Dis.out("resign");
        } else if (Global.resourceString("Local_Count").equals(o)) {
            SharedLibertyQuestion slQuestion = new SharedLibertyQuestion(this);
            B.setSharedLiberties(slQuestion.getSharedLiberties());
            new LocalResultDialog(CF, B.docount());
        } else
            super.doAction(o);
    }

    public void itemAction(String o, boolean flag) {
        if (Global.resourceString("Play").equals(o)) {
            Dis.Playing = flag;
        } else if (Global.resourceString("Display_Terminal_Output").equals(o)) {
            setterminal();
        } else if (Global.resourceString("Short_Lines_only").equals(o)) {
            Global.setParameter("shortlinesonly", flag);
        } else if (Global.resourceString("Chinese_Rule").equals(o)) {
            suggestRule(Rule.CHINESE_RULE);
        } else if (Global.resourceString("Ji_Huo_Zi_Rule").equals(o)) {
            suggestRule(Rule.JI_HUO_ZI_RULE);
        } else if (Global.resourceString("Japanese_Rule").equals(o)) {
            suggestRule(Rule.JAPANESE_RULE);
        } else
            super.itemAction(o, flag);
    }

// --------------------- Interface IconBarListener ---------------------

    public void iconPressed(String s) {
        if (s.equals("sendforward") && Dis != null) {
            Dis.out(">");
        } else
            super.iconPressed(s);
    }

// --------------------- Interface KeyListener ---------------------

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        String s;
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            s = "";
        else {
            s = Global.getFunctionKey(e.getKeyCode());
            if (s.equals("") || !ExtraSendField) return;
        }
        SendField.setText(s);
    }

// --------------------- Interface OutputListener ---------------------

    public void append(String s) {
        if (s.startsWith("Board is restored to what it was when you started scoring")) {
            B.clearremovals();
            s = Global.resourceString("Opponent_undid_removals_");
        } else if (s.indexOf("run out of time") >= 0) {
            String player = s.substring(0, s.indexOf(" ")).trim();
            if (player.equalsIgnoreCase(BlackName)) {
                finishGame(-1, 1);
            } else if (player.equalsIgnoreCase(WhiteName)) {
                finishGame(1, 1);
            }
        } else if (s.indexOf("{Game " + GameNumber + ":") >= 0 && s.indexOf("forfeits on time.}") >= 0) {
            if (s.indexOf("Black forfeits on time") >= 0) {
                finishGame(-1, 1);
            } else if (s.indexOf("White forfeits on time") >= 0) {
                finishGame(1, 1);
            }
        } else if (s.indexOf("resigned the game") >= 0) {
            String player = s.substring(0, s.indexOf(" ")).trim();
            if (player.equalsIgnoreCase(BlackName)) {
                finishGame(-1, 0);
            } else if (player.equalsIgnoreCase(WhiteName)) {
                finishGame(1, 0);
            }
        } else if (s.indexOf("{Game " + GameNumber + ":") >= 0 && s.indexOf("resigns.}") >= 0) {
            if (s.indexOf("Black resigns") >= 0) {
                finishGame(-1, 0);
            } else if (s.indexOf("White resigns") >= 0) {
                finishGame(1, 0);
            }
        }
        if (ShortLines.getState() && s.length() > 100) return;
        if (s.startsWith("*"))
            addComment(s);
        else
            addtoallcomments(s);
    }

    /**
     * @param winner 1: black
     *               -1: white
     * @param type   0: resign
     *               1: time
     */
    public void finishGame(int winner, int type) {
        String s = "";
        if (winner == -1) {
            s += Global.resourceString("Black") + "(" + BlackName + ")";
        } else {
            s += Global.resourceString("White") + "(" + WhiteName + ")";
        }
        if (type == 1) {
            s += Global.resourceString("Lost_By_Time");
        } else {
            s += Global.resourceString("Resign");
        }
        new Message(this, s).setVisible(true);
        GameResult result = new GameResult();
        result.setWinner(winner);
        if (type == 1)
            result.setWonByTime(true);
        else
            result.setWonByResign(true);
        B.setGameResult(result);
        addComment(result.toLongString());
        Timer.stopit();
    }

    public void finishGame(GameResult result) {
        B.setGameResult(result);
        addComment(result.toLongString());
        Timer.stopit();
    }

    public void append(String s, Color c) {
        append(s);
    }

// --------------------- Interface TimedBoard ---------------------

    public void alarm() {
        if (myRole == PLAYER) {
            CF.Out.println("time " + GameNumber);
        } else {
            time_in_secs++;
            if (time_in_secs >= 5) {
                time_in_secs = 0;
                CF.Out.println("time " + GameNumber);
            }
            
//            long now = System.currentTimeMillis();
//            if (B.maincolor() > 0) {
//                BlackRun = (int) ((now - CurrentTime) / 1000);
//                if (B.MyColor > 0) beep(BlackTime - BlackRun);
//            } else {
//                WhiteRun = (int) ((now - CurrentTime) / 1000);
//                if (B.MyColor < 0) beep(WhiteTime - WhiteRun);
//            }
//            settitle1();
        }
    }

// --------------------- Interface WindowListener ---------------------

    public void windowOpened(WindowEvent e) {
        if (SendField != null) SendField.requestFocus();
    }

// -------------------------- OTHER METHODS --------------------------

    public void addSendForward(IconBar I) {
        I.addLeft("sendforward");
    }

    public void beep(int s) {
        if (s < 0 || !Global.getParameter("warning", true))
            return;
        else if (s < 31 && s != lastbeep) {
            if (s % 10 == 0) {
                getToolkit().beep();
                lastbeep = s;
            }
        }
    }

    public void distributor(Distributor o) {
        Dis = o;
        if (Dis != null) Playing.setState(Dis.Playing);
    }

    public void doclose() {
        if (Dis != null && !Dis.once()) Dis.remove();
        CF.removeOutputListener(this);
        if (Timer != null && Timer.isAlive()) Timer.stopit();
        if (ExtraSendField) SendField.saveHistory("sendfield.history");
        super.doclose();
        CF.removeGoFrame(GameNumber);
    }

    public ConnectionFrame getConnectionFrame() {
        return CF;
    }

    public void movepass() {
        if (Dis != null) Dis.pass();
    }

    public boolean moveset(int i, int j) {
        if (Dis != null) {
            CF.setLastMove(i, j);
            if (B.maincolor() > 0) {
                Dis.set(i, j, BlackRun);
            } else {
                Dis.set(i, j, WhiteRun);
            }
        }
        return true;
    }

    public void setConnectionFrame(ConnectionFrame cf) {
        if (cf != null)
            this.CF = cf;
    }

    public void setOppositeMove(Move move) {
        ((ConnectedBoard) B).setOppositeMove(move.getX(), move.getY());
    }

    public void setSelfMove(Move move) {
        ((ConnectedBoard) B).setSelfMove(move.getX(), move.getY());
    }

    /**
     * Called from player to set the board information. This is passed
     * to the board, which stores this information in the SGF tree
     * (root node).
     */
    public void setinformation(String black, String blackrank,
                               String white, String whiterank,
                               String komi, String handicap) {
        WhiteLabel.setText(Global.resourceString("White") + "\n" + white);
        BlackLabel.setText(Global.resourceString("Black") + "\n" + black);
        BlackLabel.getParent().getParent().doLayout();
        B.setinformation(black, blackrank, white, whiterank, komi, handicap);
    }

    /**
     * This is called by Player to determine the time from
     * the move information.
     *
     * @see jagoclient.igs.Player
     */
    public void settime(String s) {
        StringParser p = new StringParser(s);
        if (!p.skip("Game")) return;
        int g = p.parseint();
        if (p.error()) return;
        p.skipblanks();
        if (!p.skip("I:")) return;
        String w = p.parseword();
        p.skipblanks();
        if (!p.skip("(")) return;
        int w1 = p.parseint();
        int w2 = p.parseint();
        int w3 = p.parseint(')');
        p.skip(")");
        if (p.error()) return;
        p.skipblanks();
        if (!p.skip("vs")) return;
        String b = p.parseword();
        p.skipblanks();
        if (!p.skip("(")) return;
        int b1 = p.parseint();
        int b2 = p.parseint();
        int b3 = p.parseint(')');
        if (!p.skip(")")) return;
        BlackName = b;
        WhiteName = w;
        BlackTime = b2;
        BlackMoves = b3;
        WhiteTime = w2;
        WhiteMoves = w3;
        GameNumber = g;
        BlackRun = 0;
        WhiteRun = 0;
        CurrentTime = System.currentTimeMillis();
        HaveTime = true;
        settitle1();
    }

    void settitle1() {
        String S;
        if (BigTimer)
            S = Global.resourceString("Game_") + GameNumber + ": " +
                    WhiteName + " [" + Global.resourceString("White") + "]" + formmoves(WhiteMoves) + " - " +
                    BlackName + " [" + Global.resourceString("Black") + "]" + formmoves(BlackMoves);
        else
            S = Global.resourceString("Game_") + GameNumber + ": " +
                    WhiteName + " [" + Global.resourceString("White") + "]" + formtime(WhiteTime - WhiteRun) + " " + formmoves(WhiteMoves) + " - " +
                    BlackName + " [" + Global.resourceString("White") + "]" + formtime(BlackTime - BlackRun) + " " + formmoves(BlackMoves);
        if (Global.getParameter("extrainformation", true))
            S = S + " " + B.extraInformation();
        if (!S.equals(OldS)) {
            if (!TimerInTitle)
                TL.setText(S);
            else
                setTitle(S);
            OldS = S;
        }
        if (BigTimer && HaveTime) {
            BL.setTime(WhiteTime - WhiteRun, BlackTime - BlackRun, WhiteMoves, BlackMoves, B.MyColor);
            BL.repaint();
        }
    }

    String formtime(int sec) {
        int n = OutputFormatter.formtime(form, sec);
        return new String(form, 0, n);
    }

    String formmoves(int m) {
        if (m < 0) return "";
        form[0] = '(';
        int n = OutputFormatter.formint(form, 1, m);
        form[n++] = ')';
        return new String(form, 0, n);
    }

    /**
     * called by Player to set the game title
     */
    void settitle() {
        HaveTime = true;
        settitle1();
    }

    void settitle(String s) {
        Title = s;
        setTitle(s);
    }

    public void suggestRule(int rule) {
        OOBCountRule ruleCommand = new OOBCountRule();
        ruleCommand.setCommandType(OOBCommand.COUNT_RULE_SUGGEST);
        ruleCommand.setCountRule(rule);
        CF.oobSendCommand(OOBCommand.IGNORE_ME_COMMAND);
        CF.oobSendCommand(ruleCommand);
        setCountRule(countRule);
        new SimpleMessage(this, Global.resourceString("Rule_Suggested"), 300, 100, true);
    }

    public void undo() {
        if (Dis != null) Dis.out("undo");
    }

    public boolean wantsmove() {
        if (Dis != null)
            return Dis.wantsmove();
        else
            return false;
    }
}
