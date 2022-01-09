package jagoclient.qigame;

import de.pxlab.pxl.FullScreenKeyboardFocusManager;
import jagoclient.Global;
import jagoclient.board.Board;
import jagoclient.board.BoardCommentPanel;
import jagoclient.board.GoFrame;
import jagoclient.gui.*;
import jagoclient.sound.JagoSound;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2005-4-6
 * Time: 18:01:46
 */
public class QigameGoFrame extends GoFrame {
    protected JTable commentTable;
    protected String username;
    protected String password;
    protected String gameId;

    public static void main(String args[]) {
        QigameGoFrame GF;
        Global.setApplet(false);
        Global.home(System.getProperty("user.home"));
        Global.readparameter("go.cfg");
        Global.createfonts();
        Global.frame(new Frame());
        JagoSound.play("high", "", true);
        GF = new QigameGoFrame(new Frame(), Global.resourceString("Local_Viewer"));
        Global.setcomponent(GF);
    }

    public QigameGoFrame(String s) {
        super(s);
        DefaultTitle = s;
        seticon("iboard.gif");
        setcolors();
    }

    public QigameGoFrame(Frame f, String s)
            // Constructur for local board menus.
    {
        super(s);
        DefaultTitle = s;
        // Colors
        setcolors();
        seticon("iboard.gif");
        setLayout(new BorderLayout());
        add("Center", rootPanel);
        // Menu
        MenuBar M = new MenuBar();
        setMenuBar(M);
        Menu file = new MyMenu(Global.resourceString("File"));
        M.add(file);
        file.add(new MenuItemAction(this, Global.resourceString("New")));
        file.add(new MenuItemAction(this, Global.resourceString("Load")));
        file.add(new MenuItemAction(this, Global.resourceString("Load_From_URL")));
        MenuItemAction loadFromQigameMenu = new MenuItemAction(this, Global.resourceString("Load_From_Qigame"));
        loadFromQigameMenu.setShortcut(new MenuShortcut(KeyEvent.VK_Q));
        file.add(loadFromQigameMenu);
        MenuItemAction refreshMenu = new MenuItemAction(this, Global.resourceString("Refresh"));
        refreshMenu.setShortcut(new MenuShortcut(KeyEvent.VK_R));
        file.add(refreshMenu);
        file.add(new MenuItemAction(this, Global.resourceString("Save")));
        file.add(new MenuItemAction(this, Global.resourceString("Save_Position")));
        file.addSeparator();
        file.add(new MenuItemAction(this, Global.resourceString("Copy_to_Clipboard")));
        file.addSeparator();
        file.add(new MenuItemAction(this, Global.resourceString("Mail")));
        file.add(new MenuItemAction(this, Global.resourceString("Ascii_Mail")));
        file.add(new MenuItemAction(this, Global.resourceString("Print")));
        file.add(new MenuItemAction(this, Global.resourceString("Save_Bitmap")));
        //file.addSeparator();
        //file.add(new MenuItemAction(this, Global.resourceString("Add_Game")));
        //file.add(new MenuItemAction(this, Global.resourceString("Remove_Game")));
        //file.add(new MenuItemAction(this, Global.resourceString("Next_Game")));
        //file.add(new MenuItemAction(this, Global.resourceString("Previous_Game")));
        file.addSeparator();
        file.add(new MenuItemAction(this, Global.resourceString("Close")));
        Menu set = new MyMenu(Global.resourceString("Set"));
        M.add(set);
        //set.add(new MenuItemAction(this, Global.resourceString("Undo_Adding_Removing")));
        //set.addSeparator();
        set.add(Black = new CheckboxMenuItemAction(this, Global.resourceString("Black_to_play")));
        set.add(White = new CheckboxMenuItemAction(this, Global.resourceString("White_to_play")));
        Menu score = new MyMenu(Global.resourceString("Finish_Game"));
        M.add(score);
        score.add(new MenuItemAction(this, Global.resourceString("Pass")));
        score.add(new MenuItemAction(this, Global.resourceString("Resign")));
        score.add(new MenuItemAction(this, Global.resourceString("Remove_groups")));
        score.add(new MenuItemAction(this, Global.resourceString("Local_Count")));
        score.add(new MenuItemAction(this, Global.resourceString("Prisoner_Count")));
        score.addSeparator();
        score.add(new MenuItemAction(this, Global.resourceString("Game_Information")));
        Menu options = new MyMenu(Global.resourceString("Options"));
        options.add(new MenuItemAction(this, Global.resourceString("Qigame_User_Info")));
        options.addSeparator();
        Menu mc = new MyMenu(Global.resourceString("Coordinates"));
        mc.add(Coordinates = new CheckboxMenuItemAction(this, Global.resourceString("On")));
        Coordinates.setState(Global.getParameter("coordinates", true));
        mc.add(UpperLeftCoordinates = new CheckboxMenuItemAction(this, Global.resourceString("Upper_Left")));
        UpperLeftCoordinates.setState(Global.getParameter("upperleftcoordinates", true));
        mc.add(LowerRightCoordinates = new CheckboxMenuItemAction(this, Global.resourceString("Lower_Right")));
        LowerRightCoordinates.setState(Global.getParameter("lowerrightcoordinates", false));
        options.add(mc);
        options.addSeparator();
        Menu colors = new MyMenu(Global.resourceString("Colors"));
        colors.add(new MenuItemAction(this, Global.resourceString("Board_Color")));
        colors.add(new MenuItemAction(this, Global.resourceString("Virtual_Board_Color")));
        colors.add(new MenuItemAction(this, Global.resourceString("Black_Color")));
        colors.add(new MenuItemAction(this, Global.resourceString("Black_Sparkle_Color")));
        colors.add(new MenuItemAction(this, Global.resourceString("White_Color")));
        colors.add(new MenuItemAction(this, Global.resourceString("White_Sparkle_Color")));
        colors.add(new MenuItemAction(this, Global.resourceString("Label_Color")));
        colors.add(new MenuItemAction(this, Global.resourceString("Marker_Color")));
        options.add(colors);
        options.add(MenuBWColor = new CheckboxMenuItemAction(this, Global.resourceString("Use_B_W_marks")));
        MenuBWColor.setState(Global.getParameter("bwcolor", false));
        BWColor = MenuBWColor.getState();
        options.add(PureSGF = new CheckboxMenuItemAction(this, Global.resourceString("Save_pure_SGF")));
        PureSGF.setState(Global.getParameter("puresgf", false));
        options.add(CommentSGF = new CheckboxMenuItemAction(this, Global.resourceString("Use_SGF_Comments")));
        CommentSGF.setState(Global.getParameter("sgfcomments", false));
        options.addSeparator();
        Menu fonts = new MyMenu(Global.resourceString("Fonts"));
        fonts.add(new MenuItemAction(this, Global.resourceString("Board_Font")));
        fonts.add(new MenuItemAction(this, Global.resourceString("Fixed_Font")));
        fonts.add(new MenuItemAction(this, Global.resourceString("Normal_Font")));
        options.add(fonts);
        Menu variations = new MyMenu(Global.resourceString("Variation_Display"));
        variations.add(VCurrent = new CheckboxMenuItemAction(this,
                Global.resourceString("To_Current")));
        VCurrent.setState(Global.getParameter("vcurrent", true));
        variations.add(VChild = new CheckboxMenuItemAction(this,
                Global.resourceString("To_Child")));
        VChild.setState(!Global.getParameter("vcurrent", true));
        variations.add(VHide = new CheckboxMenuItemAction(this,
                Global.resourceString("Hide")));
        VHide.setState(Global.getParameter("vhide", false));
        variations.addSeparator();
        variations.add(VNumbers = new CheckboxMenuItemAction(this,
                Global.resourceString("Continue_Numbers")));
        VNumbers.setState(Global.getParameter("variationnumbers", false));
        options.add(variations);
        options.addSeparator();
        options.add(MenuTarget = new CheckboxMenuItemAction(this, Global.resourceString("Show_Target")));
        MenuTarget.setState(Global.getParameter("showtarget", true));
        ShowTarget = MenuTarget.getState();
        options.add(new MenuItemAction(this, Global.resourceString("Last_Number")));
        options.add(new MenuItemAction(this, Global.resourceString("Show_Number")));
        options.add(new MenuItemAction(this, Global.resourceString("Hide_Number")));
        options.addSeparator();
        Menu abstractizeBoardMenu = new MyMenu(Global.resourceString("Abstractize_Board"));
        options.add(abstractizeBoardMenu);
        abstractizeBoardMenu.add(virtualBoardSameColor = new CheckboxMenuItemAction(this, Global.resourceString("Virtual_Board_Use_Same_Color")));
        virtualBoardSameColor.setState(Global.getParameter("virtualboardsamecolor", false));
        abstractizeBoardMenu.add(hideLinesOnBoard = new CheckboxMenuItemAction(this, Global.resourceString("Hide_Lines_On_Board")));
        hideLinesOnBoard.setState(Global.getParameter("hidelinesonboard", false));
        options.add(TrueColor = new CheckboxMenuItemAction(this, Global.resourceString("True_Color_Board")));
        TrueColor.setState(Global.getParameter("beauty", true));
        options.add(TrueColorStones = new CheckboxMenuItemAction(this, Global.resourceString("True_Color_Stones")));
        TrueColorStones.setState(Global.getParameter("beautystones", true));
        options.add(Alias = new CheckboxMenuItemAction(this, Global.resourceString("Anti_alias_Stones")));
        Alias.setState(Global.getParameter("alias", true));
        options.add(Shadows = new CheckboxMenuItemAction(this, Global.resourceString("Shadows")));
        Shadows.setState(Global.getParameter("shadows", true));
        options.add(SmallerStones = new CheckboxMenuItemAction(this, Global.resourceString("Smaller_Stones")));
        SmallerStones.setState(Global.getParameter("smallerstones", false));
        options.add(BlackOnly = new CheckboxMenuItemAction(this, Global.resourceString("Black_Only")));
        BlackOnly.setState(Global.getParameter("blackonly", false));
        options.addSeparator();
        options.add(new MenuItemAction(this, Global.resourceString("Set_Encoding")));
        options.add(ShowButtons = new CheckboxMenuItemAction(this,
                Global.resourceString("Show_Buttons")));
        ShowButtons.setState(Global.getParameter("showbuttons", true));
        Menu help = new MyMenu(Global.resourceString("Help"));
        help.add(new MenuItemAction(this, Global.resourceString("Board_Window")));
        help.add(new MenuItemAction(this, Global.resourceString("Making_Moves")));
        help.add(new MenuItemAction(this, Global.resourceString("Keyboard_Shortcuts")));
        help.add(new MenuItemAction(this, Global.resourceString("About_Variations")));
        help.add(new MenuItemAction(this, Global.resourceString("Playing_Games")));
        help.add(new MenuItemAction(this, Global.resourceString("Mailing_Games")));
        M.add(options);
        M.setHelpMenu(help);
        // Board
        L = new MyLabel(Global.resourceString("New_Game"));
        Lm = new MyLabel("--");
        B = new Board(19, this);
        B.setOffsetx(virtualBoardWidth());
        B.setOffsety(virtualBoardWidth());
        Panel BP = new MyPanel();
        BP.setLayout(new BorderLayout());
        BP.add("Center", B);
        // Add the label
        SimplePanel sp =
                new SimplePanel(L, 80, Lm, 20);
        BP.add("South", sp);
        sp.setBackground(Global.gray);
        Panel rightPanel = new MyPanel();
        rightPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(1, 2, 1, 2);
        c.ipadx = 2;
        c.ipady = 1;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        rightPanel.add(new ButtonAction(this, Global.resourceString("Refresh")), c);
        c.gridx = 1;
        c.gridy = 0;
        rightPanel.add(new ButtonAction(this, Global.resourceString("Submit")), c);
        c.gridx = 2;
        c.gridy = 0;
        rightPanel.add(new ButtonAction(this, Global.resourceString("Submit_All")), c);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        rightPanel.add(new MyLabel(Global.resourceString("Comment")), c);
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 1;
        rightPanel.add(new ButtonAction(this, Global.resourceString("Send_Comment")), c);
        Comment = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
        Comment.setFont(new Font(Global.SansSerif.getFamily(), 0, 13));
        Comment.setBackground(Global.gray);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 3;
        rightPanel.add(Comment, c);
        String[] columns = new String[]{Global.resourceString("Move"),
                                        Global.resourceString("Username"),
                                        Global.resourceString("Time"),
                                        Global.resourceString("Detail")};
        TableModel tableModel = new DefaultTableModel(columns, 0);
        commentTable = new JTable(tableModel);
        Panel tablePanel = new MyPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(commentTable.getTableHeader(), BorderLayout.PAGE_START);
        tablePanel.add(commentTable, BorderLayout.CENTER);
        commentTable.setShowGrid(true);
        JScrollPane scrollPane = new JScrollPane(commentTable);
        commentTable.setBackground(Global.ControlBackground);
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 3;
        c.weightx = 1;
        c.weighty = 1;
        rightPanel.add(scrollPane, c);
        Panel bcp;
        bcp = new BoardCommentPanel(BP, rightPanel, B);
        //if (Global.getParameter("shownavigationtree", true)) {
        //    Navigation = new NavigationPanel(B);
        //    bcp = new BoardCommentPanel(BP,
        //            new CommentNavigationPanel(Comment, new Panel3D(Navigation)), B);
        //} else
        //    bcp = new BoardCommentPanel(BP, Comment, B);
        rootPanel.add("Center", bcp);
        // Navigation panel
        IB = createIconBar();
        createVBWSlider();
        Panel p = new Panel(new FlowLayout(FlowLayout.LEFT));
        p.add(IB);
        p.add(VirtualBoardWidthSlider);
        ButtonP = new Panel3D(p);
        if (Global.getParameter("showbuttons", true)) {
            rootPanel.add("North", ButtonP);
        }
        // Directory for FileDialog
        Dir = "";
        Global.setwindow(this, "board", 500, 450, false);
        validate();
        Show = true;
        B.addKeyListener(this);
        if (Navigation != null) Navigation.addKeyListener(B);
        focusManager = new FullScreenKeyboardFocusManager(B);
        ((Graphics2D) getGraphics()).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        username = Util.getQigameUsername();
        password = Util.getQigamePassword();
        if (username == null || username.length() == 0) {
            new UserInfoDialog(this);
            username = Util.getQigameUsername();
            password = Util.getQigamePassword();
        }
        setVisible(true);
        repaint();
    }

    public void doAction(String o) {
        if (Global.resourceString("Qigame_User_Info").equals(o)) {
            new UserInfoDialog(this);
        } else if (Global.resourceString("Submit").equals(o)) {
        } else if (Global.resourceString("Submit_All").equals(o)) {
        } else if (Global.resourceString("Undo").equals(o)) {
            // if i'm one of the player
            // only undo moves not played yet.
		    B.undo();
        } else
            super.doAction(o);
    }

    public void doclose() {
        super.doclose();
        Global.writeparameter("go.cfg");
        if (!Global.isApplet()) System.exit(0);
    }

    public void setState(int i) {
        Black.setState(false);
        White.setState(false);
        switch (i) {
            case 1:
                Black.setState(true);
                IB.setState("black", true);
                break;
            case 2:
                White.setState(true);
                IB.setState("white", true);
                break;
        }
    }
}
