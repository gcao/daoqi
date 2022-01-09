// $Id: GoApplet.java,v 1.3 2005/02/23 21:08:25 biclinton Exp $
package jagoclient;

import jagoclient.board.BoardInterface;
import jagoclient.board.Board;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.*;


public class GoApplet extends JApplet
	implements BoardInterface
{
    String SgfFile;
    int Move;
    int ShowNumber;
    int VirtualBoardWidth;
    Reader GameReader;
    String Game; // Applet parameter
    TextArea GameInfo = new TextArea("",0,0,TextArea.SCROLLBARS_VERTICAL_ONLY); // For game info
    Board B; // The board itself
    JSlider VirtualBoardWidthSlider;
    JSlider ShowNumberSlider;
    JButton BackButton = new JButton("<");
    JButton FastBackButton = new JButton("<<");
    JButton BackToBeginButton = new JButton("|<<");
    JButton ForwardButton = new JButton(">");
    JButton FastForwardButton = new JButton(">>");
    JButton ForwardToEndButton = new JButton(">>|");
    Color BoardColor,VirtualBoardColor,BlackColor,BlackSparkleColor,
		WhiteColor,WhiteSparkleColor,MarkerColor,LabelColor;

    synchronized public void init() {
        String sgfData = getParameter("sgfdata");
        Game=getParameter("game");
        try {
            String moveS = getParameter("move");
            if (moveS != null) Move = Integer.parseInt(moveS);
        } catch (NumberFormatException e) {
            // ignore error
        }
        try { // set initial virutal board width
            VirtualBoardWidth = getParameter("virtualboard") == null? 0:Integer.parseInt(getParameter("virtualboard"));
            if (VirtualBoardWidth < 0) {
                VirtualBoardWidth = 0;
            } else if (VirtualBoardWidth > 9) {
                VirtualBoardWidth = 9;
            }
        } catch (NumberFormatException e) {
            // ignore error
        }
        try { // set show-number-from
            ShowNumber = getParameter("shownumber") == null? 0:Integer.parseInt(getParameter("shownumber"));
            if (ShowNumber < 0) {
                ShowNumber = 0;
            }
            else if (ShowNumber > 400) {
                ShowNumber = 400;
            }
        } catch (NumberFormatException e) {
            // ignore error
        }
        Global.gray = new Color(220, 220, 220);
        Global.createfonts();
        // trying to get true-color stones.
        Global.setParameter("shadows",true);
        Global.setParameter("beauty",true);
        Global.setParameter("beautystones",true);
        Global.setParameter("alias",true);
        Global.setParameter("lowerrightcoordinates",false);
        // Take colors from Global parameters.
		BoardColor=Global.getColor("boardcolor",170,120,70);
        VirtualBoardColor=Global.getColor("virtualboardcolor",175,175,125);
		BlackColor=Global.getColor("blackcolor",30,30,30);
		BlackSparkleColor=Global.getColor("blacksparklecolor",120,120,120);
		WhiteColor=Global.getColor("whitecolor",210,210,210);
		WhiteSparkleColor=Global.getColor("whitesparklecolor",250,250,250);
		MarkerColor=Color.pink.brighter();
		LabelColor=Color.pink.brighter();
        // layout components
        getContentPane().setLayout(new BorderLayout());
        Panel rightPanel = new Panel(new BorderLayout());
        B = new Board(19,this);
        B.setSize(getContentPane().getHeight(), getContentPane().getHeight());
        try {
            int offsetx = getParameter("offsetx") == null? 0:Integer.parseInt(getParameter("offsetx"));
            int offsety = getParameter("offsety") == null? 0:Integer.parseInt(getParameter("offsety"));
            B.setOffsetx(B.normalize(offsetx));
            B.setOffsety(B.normalize(offsety));
        } catch (NumberFormatException e) {
            // ignore error
        }
        getContentPane().add(new MyPanel(B, rightPanel));
        //getContentPane().add(B, BorderLayout.CENTER);
        //getContentPane().add(rightPanel, BorderLayout.EAST);
        Panel buttonsPanel = new Panel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 2;
        c.ipady = 1;
        c.gridx = 0; c.gridy = 0; c.weightx = 0.5;
        buttonsPanel.add(BackButton, c);
        BackButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                B.back();
            }
        });
        c.gridx = 1; c.gridy = 0; c.weightx = 0.5;
        buttonsPanel.add(ForwardButton, c);
        ForwardButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                B.forward();
            }
        });
        //
        c.gridx = 0; c.gridy = 1; c.weightx = 0.5;
        buttonsPanel.add(FastBackButton, c);
        FastBackButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                B.fastback();
            }
        });
        c.gridx = 1; c.gridy = 1; c.weightx = 0.5;
        buttonsPanel.add(FastForwardButton, c);
        FastForwardButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                B.fastforward();
            }
        });
        //
        c.gridx = 0; c.gridy = 2; c.weightx = 0.5;
        buttonsPanel.add(BackToBeginButton, c);
        BackToBeginButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                B.allback();
            }
        });
        c.gridx = 1; c.gridy = 2; c.weightx = 0.5;
        buttonsPanel.add(ForwardToEndButton, c);
        ForwardToEndButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                B.allforward();
            }
        });
        c.gridwidth=2; c.gridx = 0; c.gridy = 3;
        final JLabel vbwLabel = new JLabel(Global.resourceString("Virtual_Board_Width")+": "+VirtualBoardWidth);
        buttonsPanel.add(vbwLabel, c);
        VirtualBoardWidthSlider = new JSlider(JSlider.HORIZONTAL, 0, 9, VirtualBoardWidth);
        VirtualBoardWidthSlider.setToolTipText(Global.resourceString("Virtual_Board_Width"));
        VirtualBoardWidthSlider.setMajorTickSpacing(2);
        VirtualBoardWidthSlider.setMinorTickSpacing(1);
        VirtualBoardWidthSlider.setPaintTicks(true);
        VirtualBoardWidthSlider.setPaintLabels(false);
        VirtualBoardWidthSlider.setSnapToTicks(true);
        VirtualBoardWidthSlider.setBackground(buttonsPanel.getBackground());
        VirtualBoardWidthSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                if (!source.getValueIsAdjusting()) {
                    VirtualBoardWidth = (int)source.getValue(); // Virtual Board Width
                    vbwLabel.setText(Global.resourceString("Virtual_Board_Width")+": "+VirtualBoardWidth);
                    B.updateboard();
                }
            }
        });
        c.gridwidth = 2; c.gridx = 0; c.gridy = 4;
        buttonsPanel.add(VirtualBoardWidthSlider, c);

        c.gridwidth=2; c.gridx = 0; c.gridy = 5;
        final JLabel showNumberLabel = new JLabel(Global.resourceString("Show_Number")+": "+ShowNumber);
        buttonsPanel.add(showNumberLabel, c);
        ShowNumberSlider = new JSlider(JSlider.HORIZONTAL, 0, 400, ShowNumber);
        ShowNumberSlider.setMajorTickSpacing(80);
        ShowNumberSlider.setMinorTickSpacing(40);
        ShowNumberSlider.setPaintTicks(true);
        ShowNumberSlider.setPaintLabels(false);
        ShowNumberSlider.setSnapToTicks(false);
        ShowNumberSlider.setBackground(buttonsPanel.getBackground());
        ShowNumberSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                if (!source.getValueIsAdjusting()) {
                    ShowNumber = (int)source.getValue();
                    showNumberLabel.setText(Global.resourceString("Show_Number")+": "+ShowNumber);
                    B.lastrange(ShowNumber);
                }
            }
        });
        c.gridwidth = 2; c.gridx = 0; c.gridy = 6;
        buttonsPanel.add(ShowNumberSlider, c);

        rightPanel.add(buttonsPanel, BorderLayout.NORTH);
        rightPanel.add(GameInfo, BorderLayout.CENTER);
        GameInfo.setFont(new Font(Global.SansSerif.getFamily(), 0, 13));
        GameInfo.setEditable(false);
        GameInfo.setRows(15);
        validate();
        repaint();
        B.updateboard();

        if (sgfData != null) {
            load(sgfData);
        } else if (Game!=null) {
            try {
                if (Game.startsWith("http")) load(new URL(Game));
                else load(new URL(getDocumentBase(),Game));
            } catch (MalformedURLException e) {
                GameInfo.setText("MalformedURLException: "+e.getMessage());
            }
        } else {
            GameInfo.setText("No game is passed in.");
        }

        B.lastrange(ShowNumber);
    }

	/**
	Note that the board must load a file, when it is ready.
	This is to interpret a command line argument SGF filename.
	*/
	public void load (URL file)
	{	SgfFile=file.toString();
		try
		{
			GameReader=new BufferedReader(new InputStreamReader(file.openStream(), "GBK"));
		}
		catch (Exception e) {
            e.printStackTrace();
            GameInfo.setText("Error: "+e.getMessage());
            GameReader=null;
        }
        if (GameReader!=null) activate();
	}

    public void load(String sgfData) {
        GameReader=new BufferedReader(new StringReader(sgfData));
        if (GameReader != null) activate();
    }

	/** Actually do the loading, when the board is ready. */
	public void doload (Reader file)
	{	validate();
		try
		{	B.load(new BufferedReader(file));
			file.close();
			B.gotoMove(Move);
		}
		catch (Exception ex)
        {
            GameInfo.setText("Exception: "+ex.getMessage());
		}
	}

    public boolean boardShowing() {
        return true;
    }

    public void activate() {
        if (GameReader != null) {
            doload(GameReader);
        }
        GameReader = null;
    }

    // Various Color settings:
    public boolean bwColor() // black and white only?
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean blackOnly() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Color boardColor() {
        return BoardColor;
    }

    public Color virtualBoardColor() {
        return VirtualBoardColor;
    }

    public Color blackColor() {
        return BlackColor;
    }

    public Color blackSparkleColor() {
        return BlackSparkleColor;
    }

    public Color whiteColor() {
        return WhiteColor;
    }

    public Color whiteSparkleColor() {
        return WhiteSparkleColor;
    }

    public Color markerColor(int color) {
        switch (color)
		{	case 1 : return MarkerColor.brighter().brighter().brighter();
			case -1 : return MarkerColor.darker().darker().darker();
			default : return MarkerColor;
		}
    }

    public Color labelColor(int color) {
        return LabelColor;
    }

    public Color backgroundColor() {
        return Global.gray;
    }

    public boolean blocked() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    // Board sets two labels, which may be used in a frame
    public void setLabelM(String s) // position of cursor
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setLabel(String s) // next move prompt
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void advanceTextmark() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setState(int n, boolean flag) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setState(int n) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setMarkState(int marker) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    // Comment area:
    public String getComment() {
        return GameInfo.getText();
    }

    // set the content of the comment area
    public void setComment(String s) {
        GameInfo.setText(s);
    }

    // append something to the comment area only
    public void appendComment(String s) {
        String comment = "";
        if (GameInfo.getText() != null) comment = GameInfo.getText();
        comment += s;
        GameInfo.setText(comment);
    }

    // append something to the comment area only
    public void addComment(String s) {
        String comment = "";
        if (GameInfo.getText() != null) comment = GameInfo.getText();
        comment += s;
        GameInfo.setText(comment);
    }

    // get flags:
    public boolean showTarget() // flag for target rectangle
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean lastNumber() // flag to show last number
    {
        return true;
    }

    public boolean askUndo() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean askInsert() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void yourMove(boolean f) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void result(float b, float w) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String resourceString(String S) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean getParameter(String S, boolean f) {
        return Global.getParameter(S, f);
    }

    public Color getColor(String S, int red, int green, int blue) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String version() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Font boardFont() {
        return Global.BoardFont;
    }

    public boolean isDaoqiGame() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean useVirtualBoard() {
        return virtualBoardWidth() > 0;
    }

    public int virtualBoardWidth() {
        return VirtualBoardWidth;
    }

    public void changeToFullscreen() {
    }

    public void exitFullscreen() {
    }

    private static class MyPanel extends Panel
    {	Component C1,C2;
        public MyPanel (Component c1, Component c2)
        {	C1=c1;
            C2=c2;
            add(C1);
            add(C2);
        }
        public void doLayout ()
        {	C1.setSize(getSize().width,getSize().height);
            C1.doLayout();
            Dimension d = getSize();
            C1.setSize(d.height,d.height);
            C1.setLocation(0,0);
            C2.setSize(d.width-d.height, d.height);
            C2.setLocation(d.height,0);
            C1.doLayout();
            C2.doLayout();
        }
    }

    public int getCountRule() {
        throw new UnsupportedOperationException("getCountRule()");
    }
}