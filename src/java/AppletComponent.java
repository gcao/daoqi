import jagoclient.Global;
import jagoclient.board.Board;
import jagoclient.board.BoardInterface;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2006-3-3
 * Time: 20:10:50
 * To change this template use File | Settings | File Templates.
 */
public class AppletComponent extends JPanel
        implements BoardInterface, ActionListener, ChangeListener, KeyListener {
// ------------------------------ FIELDS ------------------------------

    private static final String WEIQI = "weiqi";
    private static final String DAOQI = "daoqi";
    private static final String YUANDAO_WEIQI = "yuandao";
    private static final String PARAM_GAME_TYPE = "gametype";
    private static final String PARAM_GAME = "game";
    private static final String PARAM_SHOW_NUMBER = "show";
    private static final String PARAM_MOVE = "move";
    private static final String PARAM_BOARD_FONT_SIZE = "bfs";
    //private static final String PARAM_WOOD_BOARD = "woodboard";
    private static final String PARAM_VIRTUAL_BOARD_WIDTH = "vbw";
    private static final String PARAM_OFFSET_X = "offsetx";
    private static final String PARAM_OFFSET_Y = "offsety";
    private static final Pattern IGNORABLE_TAG_PATTERN = Pattern.compile("<(br|p)>", Pattern.CASE_INSENSITIVE);
    Properties params;
    Applet applet;
    int gameType = Global.DAOQI;
    String help;
    Font defaultFont = new Font("\u4EFF\u5B8B_GB2312", 0, 12);
    //Font defaultFont = new Font("SansSerif", 0, 12);
    Font defaultTextCompFont = new Font("\u4EFF\u5B8B_GB2312", 0, 14);
    //Font defaultTextCompFont = new Font("SansSerif", 0, 14);
    Font boardFont;
    Reader gameReader;
    JPanel cardPanel;
    CardLayout cardLayout;
    TextArea helpArea;
    TextArea gameInfo = new TextArea("", 0, 0, TextArea.SCROLLBARS_NONE); // For game info
    Board board; // The board itself
    JSlider virtualBoardWidthSlider;
    Label vbwLabel;
    Button refreshBtn = new Button(resourceString("Restore"));
    Button helpBtn = new Button(resourceString("Help"));
    Button showNumberBtn = new Button(resourceString("Show"));
    TextField showNumberField = new TextField("0");
    Button findMoveBtn = new Button(resourceString("Find"));
    TextField findMoveField = new TextField();
    Button backBtn = new Button(resourceString("Back"));
    Button fastBackBtn = new Button(resourceString("Fast_Back"));
    Button prevCommentBtn = new Button(resourceString("Prev_Comment"));
    Button backToBeginBtn = new Button(resourceString("Back_To_Begin"));
    Button forwardBtn = new Button(resourceString("Forward"));
    Button fastForwardBtn = new Button(resourceString("Fast_Forward"));
    Button nextCommentBtn = new Button(resourceString("Next_Comment"));
    Button forwardToEndBtn = new Button(resourceString("Forward_To_End"));
    /*
     JButton backJBtn;
     JButton forwardJBtn;
     JButton fastBackJBtn;
     JButton fastForwardJBtn;
     JButton allBackJBtn;
     JButton allForwardJBtn;
     JButton prevCommentJBtn;
     JButton nextCommentJBtn;
     */
    Color boardColor;
    Color virtualBoardColor;
    Color blackColor;
    Color blackSparkleColor;
    Color whiteColor;
    Color whiteSparkleColor;
    Color markerColor;
    Color labelColor;

// --------------------------- CONSTRUCTORS ---------------------------

    public AppletComponent(Applet applet) {
        this.applet = applet;
        this.init();
    }

    public AppletComponent(Properties params) {
        this.params = params;
        this.init();
    }

    public void init() {
        setGameType();
        Global.gray = new Color(220, 220, 220);
        Global.createfonts();
        resetBoardFontSize();
        // trying to get true-color stones.
        Global.setParameter("shadows", true);
        Global.setParameter("beauty", false);
        if (gameType == Global.WEIQI) {
            // To void the applet using too much memory, do not support this!
            //String woodBoardParam = getParameter(PARAM_WOOD_BOARD);
            //if (woodBoardParam != null && (woodBoardParam.trim().equalsIgnoreCase("yes") ||
            //		woodBoardParam.trim().equalsIgnoreCase("true"))) {
            //	Global.setParameter("beauty", true);
            //}
        }
        Global.setParameter("beautystones", true);
        Global.setParameter("alias", true);
        Global.setParameter("lowerrightcoordinates", false);
        // Take colors from Global parameters.
        boardColor = Global.getColor("boardcolor", 170, 120, 70);
        virtualBoardColor = Global.getColor("virtualboardcolor", 175, 175, 125);
        blackColor = Global.getColor("blackcolor", 30, 30, 30);
        blackSparkleColor = Global.getColor("blacksparklecolor", 120, 120, 120);
        whiteColor = Global.getColor("whitecolor", 210, 210, 210);
        whiteSparkleColor = Global.getColor("whitesparklecolor", 250, 250, 250);
        markerColor = Color.pink.brighter();
        labelColor = Color.pink.brighter();
        // layout components
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.darkGray));
        Panel rightPanel = new Panel(new BorderLayout());
        board = new Board(gameType, 19, this);
        board.addKeyListener(this);
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(board, "board");
        helpArea = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
        helpArea.setBackground(Global.gray);
        helpArea.setFont(defaultTextCompFont);
        helpArea.setEditable(false);
        cardPanel.add(helpArea, "help");
        add(new MyPanel(cardPanel, rightPanel));
        Panel buttonsPanel = new Panel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 2;
        c.ipady = 1;
        c.weightx = 0.5;
        //
        c.gridx = 0;
        c.gridy = 0;
        buttonsPanel.add(refreshBtn, c);
        refreshBtn.setFont(defaultFont);
        refreshBtn.addActionListener(this);
        //
        c.gridx = 1;
        buttonsPanel.add(helpBtn, c);
        helpBtn.setFont(defaultFont);
        helpBtn.addActionListener(this);
        //
        c.gridx = 0;
        c.gridy ++;
        buttonsPanel.add(showNumberBtn, c);
        showNumberBtn.setFont(defaultFont);
        showNumberBtn.addActionListener(this);
        c.gridx = 1;
        buttonsPanel.add(showNumberField, c);
        showNumberField.setFont(defaultFont);
        showNumberField.addActionListener(this);
        //
        c.gridx = 0;
        c.gridy ++;
        buttonsPanel.add(findMoveBtn, c);
        findMoveBtn.setFont(defaultFont);
        findMoveBtn.addActionListener(this);
        c.gridx = 1;
        buttonsPanel.add(findMoveField, c);
        findMoveField.setFont(defaultFont);
        findMoveField.addActionListener(this);
        //
        c.gridx = 0;
        c.gridy ++;
        buttonsPanel.add(backBtn, c);
        backBtn.setFont(defaultFont);
        backBtn.addActionListener(this);
        c.gridx = 1;
        buttonsPanel.add(forwardBtn, c);
        forwardBtn.setFont(defaultFont);
        forwardBtn.addActionListener(this);
        //
        c.gridx = 0;
        c.gridy ++;
        buttonsPanel.add(fastBackBtn, c);
        fastBackBtn.setFont(defaultFont);
        fastBackBtn.addActionListener(this);
        c.gridx = 1;
        buttonsPanel.add(fastForwardBtn, c);
        fastForwardBtn.setFont(defaultFont);
        fastForwardBtn.addActionListener(this);
        //
        c.gridx = 0;
        c.gridy ++;
        buttonsPanel.add(prevCommentBtn, c);
        prevCommentBtn.setFont(defaultFont);
        prevCommentBtn.addActionListener(this);
        c.gridx = 1;
        buttonsPanel.add(nextCommentBtn, c);
        nextCommentBtn.setFont(defaultFont);
        nextCommentBtn.addActionListener(this);
        //
        c.gridx = 0;
        c.gridy ++;
        buttonsPanel.add(backToBeginBtn, c);
        backToBeginBtn.setFont(defaultFont);
        backToBeginBtn.addActionListener(this);
        c.gridx = 1;
        buttonsPanel.add(forwardToEndBtn, c);
        forwardToEndBtn.setFont(defaultFont);
        forwardToEndBtn.addActionListener(this);
        /*
          c.gridx = 0;
          c.gridy ++;
          c.gridwidth = 2;
          FlowLayout iconBarLayout = new FlowLayout(FlowLayout.CENTER);
          iconBarLayout.setHgap(1);
          Panel panel = new Panel(iconBarLayout);
          allBackJBtn = new JButton();
          allBackJBtn.setIcon(new ImageIcon(getImageFromResource("toolbar/allback.gif")));
          allBackJBtn.setBorderPainted(false);
          allBackJBtn.setMargin(null);
          allBackJBtn.setPreferredSize(new Dimension(17,15));
          panel.add(allBackJBtn);
          prevCommentJBtn = new JButton();
          prevCommentJBtn.setIcon(new ImageIcon(getImageFromResource("toolbar/prevcomment.gif")));
          prevCommentJBtn.setBorderPainted(false);
          prevCommentJBtn.setMargin(null);
          prevCommentJBtn.setPreferredSize(new Dimension(17, 15));
          panel.add(prevCommentJBtn);
          fastBackJBtn = new JButton();
          fastBackJBtn.setIcon(new ImageIcon(getImageFromResource("toolbar/fastback.gif")));
          fastBackJBtn.setBorderPainted(false);
          fastBackJBtn.setMargin(null);
          fastBackJBtn.setPreferredSize(new Dimension(17, 15));
          panel.add(fastBackJBtn);
          backJBtn = new JButton();
          backJBtn.setIcon(new ImageIcon(getImageFromResource("toolbar/back.gif")));
          backJBtn.setBorderPainted(false);
          backJBtn.setMargin(null);
          backJBtn.setPreferredSize(new Dimension(17, 15));
          panel.add(backJBtn);
          forwardJBtn = new JButton();
          forwardJBtn.setIcon(new ImageIcon(getImageFromResource("toolbar/forward.gif")));
          forwardJBtn.setBorderPainted(false);
          forwardJBtn.setMargin(null);
          forwardJBtn.setPreferredSize(new Dimension(17, 15));
          panel.add(forwardJBtn);
          fastForwardJBtn = new JButton();
          fastForwardJBtn.setIcon(new ImageIcon(getImageFromResource("toolbar/fastforward.gif")));
          fastForwardJBtn.setBorderPainted(false);
          fastForwardJBtn.setMargin(null);
          fastForwardJBtn.setPreferredSize(new Dimension(17, 15));
          panel.add(fastForwardJBtn);
          nextCommentJBtn = new JButton();
          nextCommentJBtn.setIcon(new ImageIcon(getImageFromResource("toolbar/nextcomment.gif")));
          nextCommentJBtn.setBorderPainted(false);
          nextCommentJBtn.setMargin(null);
          nextCommentJBtn.setPreferredSize(new Dimension(17, 15));
          panel.add(nextCommentJBtn);
          allForwardJBtn = new JButton();
          allForwardJBtn.setIcon(new ImageIcon(getImageFromResource("toolbar/allforward.gif")));
          allForwardJBtn.setBorderPainted(false);
          allForwardJBtn.setMargin(null);
          allForwardJBtn.setPreferredSize(new Dimension(17, 15));
          panel.add(allForwardJBtn);
          buttonsPanel.add(panel, c);
          */
        if (gameType == Global.DAOQI) {
            c.gridwidth = 2;
            c.gridx = 0;
            c.gridy ++;
            c.weightx = 1;
            vbwLabel = new Label(resourceString("Virtual_board_width_") + 0);
            vbwLabel.setFont(defaultFont);
            buttonsPanel.add(vbwLabel, c);
            virtualBoardWidthSlider = new JSlider(JSlider.HORIZONTAL, 0, 9, 0);
            virtualBoardWidthSlider.setMajorTickSpacing(2);
            virtualBoardWidthSlider.setMinorTickSpacing(1);
            virtualBoardWidthSlider.setPaintTicks(true);
            virtualBoardWidthSlider.setPaintLabels(false);
            virtualBoardWidthSlider.setSnapToTicks(true);
            virtualBoardWidthSlider.setBackground(buttonsPanel.getBackground());
            virtualBoardWidthSlider.addChangeListener(this);
            c.gridwidth = 2;
            c.gridx = 0;
            c.gridy ++;
            buttonsPanel.add(virtualBoardWidthSlider, c);
        }
        //
        rightPanel.add(buttonsPanel, BorderLayout.NORTH);
        rightPanel.add(gameInfo, BorderLayout.CENTER);
        gameInfo.setBackground(Global.gray);
        gameInfo.setFont(defaultTextCompFont);
        gameInfo.setEditable(false);
        validate();
        repaint();
        reset();
    }

    private void setGameType() {
        String s = getParameter(PARAM_GAME_TYPE);
        if (s == null) return;
        if (s.equalsIgnoreCase(WEIQI)) {
            gameType = Global.WEIQI;
        } else if (s.equalsIgnoreCase(YUANDAO_WEIQI)) {
            gameType = Global.YUANDAO;
        } else if (s.equalsIgnoreCase(DAOQI)) {
            gameType = Global.DAOQI;
        }
    }

    public String getParameter(String propName) {
        if (params != null) {
            return params.getProperty(propName);
        } else if (applet != null) {
            return applet.getParameter(propName);
        } else
            throw new IllegalStateException("Applet or params must be present.");
    }

    private void resetBoardFontSize() {
        if (boardFont == null) {
            int fontSize = getParameterValue(PARAM_BOARD_FONT_SIZE, 11);
            if (fontSize < 9)
                fontSize = 9;
            if (fontSize > 16)
                fontSize = 16;
            boardFont = new Font(Global.BoardFont.getName(), Global.BoardFont.getStyle(), fontSize);
        }
        Global.BoardFont = boardFont;
    }

    private int getParameterValue(String paramName, int defaultValue) {
        int paramValue = defaultValue;
        try {
            String s = getParameter(paramName);
            if (s != null && s.length() > 0) paramValue = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            // ignore error
        }
        return paramValue;
    }

    public String resourceString(String S) {
        return Global.resourceString(S);
    }

    private void reset() {
        reloadGame();
        if (gameType == Global.DAOQI) {
            resetVirtualBoardWidth();
            resetOffsetX();
            resetOffsetY();
        }
        resetShowNumber(getParameter(PARAM_SHOW_NUMBER));
        findMove(getParameterValue(PARAM_MOVE, -1));
        board.updateboard();
    }

    private void reloadGame() {
        String game = getParameter(PARAM_GAME);
        if (game == null) {
            gameInfo.setText(resourceString("Did_not_find_game"));
        } else {
            try {
                game = game.trim();
                if (game.matches("\\d+")) {
                    load(new URL(resourceString("Qigame_URL_Prefix") + game));
                    return;
                }
                Matcher matcher = IGNORABLE_TAG_PATTERN.matcher(game);
                game = matcher.replaceAll("");
                if (game.startsWith("(")) load(game);
                else if (game.startsWith("http")) load(new URL(game));
                else load(new URL(applet.getDocumentBase(), game));
            } catch (MalformedURLException e) {
                gameInfo.setText(resourceString("URL_incorrect_") + e.getMessage());
            }
        }
    }

    public void load(String sgfData) {
        gameReader = new BufferedReader(new StringReader(sgfData));
        activate();
    }

    public void activate() {
        if (gameReader != null) {
            doload(gameReader);
        }
        gameReader = null;
    }

    /**
     * Note that the board must load a file, when it is ready.
     * This is to interpret a command line argument SGF filename.
     */
    public void load(URL file) {
        try {
            // TODO
            //gameReader = new BufferedReader(new InputStreamReader(file.openStream(), resourceString("Encoding")));
            gameReader = new BufferedReader(new InputStreamReader(file.openStream(), "GBK"));
        }
        catch (Exception e) {
            e.printStackTrace();
            gameInfo.setText(resourceString("Error_") + e.getMessage());
            gameReader = null;
        }
        if (gameReader != null) activate();
    }

    private void resetVirtualBoardWidth() {
        int virtualBoardWidth = getParameterValue(PARAM_VIRTUAL_BOARD_WIDTH, 4);
        if (virtualBoardWidth < 0) {
            virtualBoardWidth = 0;
        } else if (virtualBoardWidth > 9) {
            virtualBoardWidth = 9;
        }
        vbwLabel.setText(resourceString("Virtual_board_width_") + virtualBoardWidth);
        virtualBoardWidthSlider.setValue(virtualBoardWidth);
        board.setVirtualBoardWidth(virtualBoardWidth);
    }

    private void resetOffsetX() {
        board.setOffsetx(getParameterValue(PARAM_OFFSET_X, 0));
    }

    private void resetOffsetY() {
        board.setOffsety(getParameterValue(PARAM_OFFSET_Y, 0));
    }

    private void resetShowNumber(String s) {
        if (s == null || s.trim().length() == 0)
            s = "0";
        board.setShowNumber(s);
        showNumberField.setText(s);
    }

    private void findMove(int moveNo) {
        board.gotoMove(moveNo);
        findMoveField.setText(String.valueOf(moveNo));
    }

// ------------------------ INTERFACE METHODS ------------------------

// --------------------- Interface ActionListener ---------------------

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn)
            board.back();
        else if (e.getSource() == forwardBtn)
            board.forward();
        else if (e.getSource() == fastBackBtn)
            board.fastback();
        else if (e.getSource() == fastForwardBtn)
            board.fastforward();
        else if (e.getSource() == prevCommentBtn)
            board.commentBack();
        else if (e.getSource() == nextCommentBtn)
            board.commentForward();
        else if (e.getSource() == backToBeginBtn)
            board.allback();
        else if (e.getSource() == forwardToEndBtn)
            board.allforward();
        else if (e.getSource() == findMoveBtn || e.getSource() == findMoveField) {
            try {
                if (findMoveField.getText().trim().length() > 0) {
                    findMove(Integer.parseInt(findMoveField.getText().trim()));
                    board.updateall();
                    board.copy();
                }
            } catch (Exception ex) {
                gameInfo.setText(resourceString("Error_") + ex.getMessage());
            }
        } else if (e.getSource() == showNumberBtn || e.getSource() == showNumberField) {
            try {
                if (showNumberBtn.getLabel().trim().length() > 0) {
                    board.setShowNumber(showNumberField.getText());
                    board.updateall();
                    board.copy();
                }
            } catch (Exception ex) {
                gameInfo.setText(resourceString("Error_") + ex.getMessage());
            }
        } else if (e.getSource() == refreshBtn)
            reset();
        else if (e.getSource() == helpBtn) {
            if (helpBtn.getLabel().equals(resourceString("Help"))) {
                helpBtn.setLabel(resourceString("Hide"));
                cardLayout.show(cardPanel, "help");
                try {
                    if (help == null) {
                        help = read(GoApplet.class.getResourceAsStream("GoAppletHelp.txt"));
                    }
                    helpArea.setText(help);
                } catch (Exception ex) {
                    helpArea.setText(resourceString("Did_not_find_help_file"));
                }
            } else {
                helpBtn.setLabel(resourceString("Help"));
                cardLayout.show(cardPanel, "board");
            }
        }
    }

// --------------------- Interface BoardInterface ---------------------

    public boolean boardShowing() {
        return true;
    }

    // Various Color settings:
    public boolean bwColor() // black and white only?
    {
        return false;
    }

    public boolean blackOnly() {
        return false;
    }

    public Color boardColor() {
        return boardColor;
    }

    public Color virtualBoardColor() {
        return virtualBoardColor;
    }

    public Color blackColor() {
        return blackColor;
    }

    public Color blackSparkleColor() {
        return blackSparkleColor;
    }

    public Color whiteColor() {
        return whiteColor;
    }

    public Color whiteSparkleColor() {
        return whiteSparkleColor;
    }

    public Color markerColor(int color) {
        switch (color) {
            case 1 :
                return markerColor.brighter().brighter().brighter();
            case -1 :
                return markerColor.darker().darker().darker();
            default :
                return markerColor;
        }
    }

    public Color labelColor(int color) {
        return labelColor;
    }

    public Color backgroundColor() {
        return Global.gray;
    }

    public boolean blocked() {
        return false;
    }

    // Board sets two labels, which may be used in a frame
    public void setLabelM(String s) // position of cursor
    {
    }

    public void setLabel(String s) // next move prompt
    {
    }

    public void advanceTextmark() {
    }

    public void setState(int n, boolean flag) {
    }

    public void setState(int n) {
    }

    public void setMarkState(int marker) {
    }

    // Comment area:
    public String getComment() {
        return gameInfo.getText();
    }

    // set the content of the comment area
    public void setComment(String s) {
        gameInfo.setText(s);
    }

    // append something to the comment area only
    public void appendComment(String s) {
        String comment = "";
        if (gameInfo.getText() != null) comment = gameInfo.getText();
        comment += s;
        gameInfo.setText(comment);
    }

    // append something to the comment area only
    public void addComment(String s) {
        String comment = "";
        if (gameInfo.getText() != null) comment = gameInfo.getText();
        comment += s;
        gameInfo.setText(comment);
    }

    // get flags:
    public boolean showTarget() // flag for target rectangle
    {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean lastNumber() // flag to show last number
    {
        return false;
    }

    public boolean askUndo() {
        return true;
    }

    public boolean askInsert() {
        return false;
    }

    public void yourMove(boolean f) {
    }

    public void result(float b, float w) {
    }

    public boolean getParameter(String S, boolean f) {
        return Global.getParameter(S, f);
    }

    public Color getColor(String S, int red, int green, int blue) {
        return new Color(red, green, blue);
    }

    public String version() {
        return Global.Version;
    }

    public Font boardFont() {
        return this.boardFont;
    }

    public boolean isDaoqiGame() {
        return true;
    }

    public boolean useVirtualBoard() {
        return virtualBoardWidth() > 0;
    }

    public int virtualBoardWidth() {
        if (gameType == Global.WEIQI)
            return 0;
        return virtualBoardWidthSlider.getValue();
    }

    public void changeToFullscreen() {
    }

    public void exitFullscreen() {
    }

    public int getCountRule() {
        return 1;
    }

// --------------------- Interface ChangeListener ---------------------

    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == virtualBoardWidthSlider) {
            if (!virtualBoardWidthSlider.getValueIsAdjusting()) {
                int vbw = virtualBoardWidthSlider.getValue();
                board.setVirtualBoardWidth(vbw);
                vbwLabel.setText(resourceString("Virtual_board_width_") + vbw);
                board.updateboard();
            }
        }
    }

// --------------------- Interface KeyListener ---------------------

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_G:
                if (e.isControlDown() && e.isShiftDown()) {
                    StringWriter writer = new StringWriter();
                    board.save(new PrintWriter(writer));
                    helpArea.setText(writer.toString());
                    helpBtn.setLabel(resourceString("Hide"));
                    cardLayout.show(cardPanel, "help");
                }
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * Actually do the loading, when the board is ready.
     */
    public void doload(Reader file) {
        validate();
        try {
            board.load(new BufferedReader(file));
            file.close();
        }
        catch (Exception ex) {
            gameInfo.setText(resourceString("Error_") + ex.getMessage());
        }
    }

    /*
     private Image getImageFromResource(String path) {
         try {
             InputStream in = getClass().getResourceAsStream(path);
             int pos = 0;
             int n = in.available();
             byte b[] = new byte[20000];
             while (n > 0) {
                 int k = in.read(b, pos, n);
                 if (k < 0) break;
                 pos += k;
                 n = in.available();
             }
             in.close();
             return Toolkit.getDefaultToolkit().createImage(b, 0, pos);
         } catch (Exception e) {
             return null;
         }
     }
     */

    private String read(InputStream is) throws Exception {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(is, resourceString("Encoding")));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
                sb.append(System.getProperty("line.separator"));
            }
            return sb.toString();
        } catch (IOException e) {
            return resourceString("Did_not_find_help_file");
        } finally {
            if (in != null)
                in.close();
        }
    }

// -------------------------- INNER CLASSES --------------------------

    private static class MyPanel extends Panel {
        Component C1;
        Component C2;

        public MyPanel(Component c1, Component c2) {
            C1 = c1;
            C2 = c2;
            add(C1);
            add(C2);
        }

        public void doLayout() {
            C1.setSize(getSize().width, getSize().height);
            C1.doLayout();
            Dimension d = getSize();
            C1.setSize(d.height, d.height);
            C1.setLocation(0, 0);
            C2.setSize(d.width - d.height, d.height);
            C2.setLocation(d.height, 0);
            C1.doLayout();
            C2.doLayout();
        }
    }

// --------------------------- main() method ---------------------------

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Properties properties = new Properties();
        properties.setProperty(PARAM_BOARD_FONT_SIZE, "13");
        properties.setProperty(PARAM_GAME, "http://localhost/daoqi/tournaments/qireninvitation1/games/round1/dqdqyq-dqdqcaogl.sgf");
        AppletComponent appletComp = new AppletComponent(properties);
        frame.getContentPane().add(appletComp);
        frame.setSize(new Dimension(700, 580));
        frame.setVisible(true);
    }
}