package jagoclient.igs;

import jagoclient.Dump;
import jagoclient.Move;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2004-12-16
 * Time: 10:34:45
 */
public class OOBCommand {
    public final static String PREFIX = "OOBCMD";
    public final static String SEPARATOR = ":";
    public final static int MOVE = 1;
    public final static int IGNORE_NEXT_PASS = 2;
    public final static int COUNT_RULE_SUGGEST = 10;
    public final static int COUNT_RULE_ACCEPT = 11;
    public final static int COUNT_RULE_DECLINE = 12;
    public final static int RESULT_SUGGEST = 20;
    public final static int RESULT_ACCEPT = 21;
    public final static int RESULT_DECLINE = 22;
    public final static int IGNORE_ME = 100;
    public static OOBCommand IGNORE_ME_COMMAND;
    public static OOBCommand IGNORE_NEXT_PASS_COMMAND;

    static {
        IGNORE_ME_COMMAND = new OOBCommand();
        IGNORE_ME_COMMAND.commandType = IGNORE_ME;
        IGNORE_NEXT_PASS_COMMAND = new OOBCommand();
        IGNORE_NEXT_PASS_COMMAND.commandType = IGNORE_NEXT_PASS;
    }

    protected int commandType;
    protected int gameNo;

    public String toString() {
        return PREFIX + commandType + SEPARATOR + gameNo;
    }

    /**
     * @return Returns the gameNo.
     */
    public int getGameNo() {
        return gameNo;
    }

    /**
     * @param gameNo The gameNo to set.
     */
    public void setGameNo(int gameNo) {
        this.gameNo = gameNo;
    }

    public static OOBCommand parse(String s) {
        if (s == null) {
            return null;
        }
        if (s.indexOf(PREFIX) >= 0) {
            s = s.substring(s.indexOf(PREFIX) + PREFIX.length());
        }
        s = s.trim();
        String[] parts = s.split(SEPARATOR);
        OOBCommand oobCmd = null;
        int index = 0;
        int commandType = Integer.parseInt(parts[index++]);
        int gameNo = Integer.parseInt(parts[index++]);
        Dump.println("OOBCommand.parse():gameNo=" + gameNo);
        switch (commandType) {
            case MOVE:
                int i = Integer.parseInt(parts[index++]);
                int j = Integer.parseInt(parts[index++]);
                Move move = new Move();
                move.setX(i);
                move.setY(j);
                oobCmd = new OOBMove(move);
                oobCmd.setGameNo(gameNo);
                break;
            case IGNORE_NEXT_PASS:
                oobCmd = IGNORE_NEXT_PASS_COMMAND;
                oobCmd.gameNo = gameNo;
                break;
            case IGNORE_ME:
                oobCmd = IGNORE_ME_COMMAND;
                break;
            case COUNT_RULE_SUGGEST:
                oobCmd = new OOBCountRule();
                oobCmd.commandType = commandType;
                ((OOBCountRule)oobCmd).setCountRule(Integer.parseInt(parts[index++]));
                break;
            case COUNT_RULE_ACCEPT:
                oobCmd = new OOBCountRule();
                oobCmd.commandType = commandType;
                ((OOBCountRule)oobCmd).setCountRule(Integer.parseInt(parts[index++]));
                break;
            case COUNT_RULE_DECLINE:
                oobCmd = new OOBCountRule();
                oobCmd.commandType = commandType;
                ((OOBCountRule)oobCmd).setCountRule(Integer.parseInt(parts[index++]));
                break;
            default:
                oobCmd = new OOBCommand();
                oobCmd.commandType = commandType;
                oobCmd.gameNo = gameNo;
        }
        return oobCmd;
    }

    /**
     * @return Returns the commandType.
     */
    public int getCommandType() {
        return commandType;
    }

    public void setCommandType(int commandType) {
        this.commandType = commandType;
    }
}
