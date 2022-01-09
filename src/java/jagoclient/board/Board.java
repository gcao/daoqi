package jagoclient.board;

import jagoclient.*;
import jagoclient.sound.JagoSound;
import rene.util.list.ListElement;
import rene.util.xml.XmlReader;
import rene.util.xml.XmlReaderException;
import rene.util.xml.XmlWriter;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Vector;

//******************* Board ***********************

/**
 * This is the main file for presenting a Go board.
 * <p/>
 * Handles the complete display and storage of a Go board.
 * The display is kept on an offscreen image.
 * Stores a Go game in a node list (with variants).
 * Handles the display of the current node.
 * <p/>
 * This class handles mouse input to set the next move.
 * It also has methods to move in the node tree from external sources.
 * <p/>
 * A BoardInterface is used to encorporate the board
 * into an environment.
 */

public class Board extends Canvas
		implements MouseListener, MouseMotionListener, KeyListener {
	private int gameType = Global.DAOQI;
	// O=offset, W=total width, D=field width, S=board size (9,11,13,19)
	// OT=offset for coordinates to the right and below
	// OTU=offset above the board and below for coordinates
	int O;
	int W;
	int D;
	int S;
	int OT;
	int OTU;
	int OP; // pixel coordinates
	int lasti = -1;
	int lastj = 0; // last move (used to highlight the move)
	private int virtualBoardWidth;
	// offsets that are used to compute coordinates on the board
	// assume (i,j) is the position used in the SGF, (i+offseti, j+offsetj) is the coordinates on the board.
	private int offsetx = 0;
	private int offsety = 0;
	boolean showlast; // internal flag, if last move is to be highlighted
	Image Empty;
	Image EmptyShadow;
	Image ActiveImage;
	// offscreen images of empty and current board
	SGFTree T; // the game tree
	Vector Trees; // the game trees (one of them is T)
	int CurrentTree; // the currently displayed tree
	Position P; // current board position
	int number; // number of the next move
	TreeNode Pos; // the current board position in this presentation
	int State; // states: 1 is black, 2 is white, 3 is set black etc.
	// see GoFrame.setState(int)
	Font font; // Font for board letters and coordinates
	Font largeFont; // Font for board letters and coordinates
	FontMetrics fontmetrics; // metrics of this font
	FontMetrics largeFontmetrics; // metrics of this font
	BoardInterface GF; // frame containing the board
	boolean Active;
	int MainColor = 1;
	public int MyColor = 0;
	int sendi = -1;
	int sendj;
	// board position which has been sended to server
	Dimension Dim; // Note size to check for resizeing at paint
	int SpecialMarker = Field.SQUARE;
	String TextMarker = "A";
	public int Pw;
	public int Pb; // Prisoners (white and black)
	BufferedReader LaterLoad = null; // File to be loaded at repaint
	Image BlackStone;
	Image WhiteStone;
	Image vbWhiteStone;
	int Range = 0; // Numbers display from this one
	//boolean KeepRange = false;
	String NodeName = "";
	String LText = "";
	boolean DisplayNodeName = false;
	public boolean Removing = false;
	boolean Activated = false;
	public boolean Teaching = false; // enable teaching mode
	boolean VCurrent = false; // show variations to current move
	boolean VHide = false; // hide variation markers
	int lastNonPassMove;
	int currentMoveNumber;
	int sharedLiberties;
	int rotate = 0; // 1: rotate 90 degree, 2 rotate 180 degree 3 rotate 270 degree
	// set the first showed number to this.
	// <=0 means show its real move number
	// this won't affect variations display ?!
	int startNumber = 1;

	// ******************** initialize board *******************

	public Board(int size, BoardInterface gf) {
		this(Global.DAOQI, size, gf);
	}

	public Board(int gameType, int size, BoardInterface gf) {
		if (gameType != Global.DAOQI && gameType != Global.WEIQI)
			throw new IllegalArgumentException("Unsupported game type: " + gameType);
		this.gameType = gameType;
		S = size;
		D = 16;
		W = S * D; // D and W will always be overwritten before painting the board.
		Empty = null;
		EmptyShadow = null;
		showlast = true;
		GF = gf;
		State = 1;
		P = new Position(this.gameType, S);
		number = 1;
		T = new SGFTree(new Node(number));
		Trees = new Vector();
		Trees.addElement(T);
		CurrentTree = 0;
		Pos = T.top();
		Active = true;
		Dim = new Dimension();
		Dim.width = 0;
		Dim.height = 0;
		Pw = Pb = 0;
		setfonts();
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		VHide = GF.getParameter("vhide", false);
		VCurrent = GF.getParameter("vcurrent", true);
	}

	public int getOffsetx() {
		return offsetx;
	}

	public void setOffsetx(int offsetx) {
		if (gameType == Global.WEIQI) return;
		this.offsetx = normalize(offsetx);
	}

	public int getOffsety() {
		return offsety;
	}

	public void setOffsety(int offsety) {
		if (gameType == Global.WEIQI) return;
		this.offsety = normalize(offsety);
	}

	public int getStartNumber() {
		return startNumber;
	}

	public void setStartNumber(int startNumber) {
		this.startNumber = startNumber < 0 ? 0 : startNumber;
	}

	private Change getFirstChange(TreeNode treeNode) {
		if (treeNode == null)
			return null;
		Node node = (Node) treeNode.content();
		if (node == null)
			return null;
		ListElement le = node.changes();
		if (le == null)
			return null;
		return (Change) le.content();
	}

	public int getMoveNumberToShow(int i, int j) {
		/*
		if (!Pos.isMain()) {
			TreeNode treeNode = Pos;
			boolean match = false;
			int move = 0;
			while (!treeNode.isMain()) {
				Change change1 = getFirstChange(treeNode);
				if (change1 != null && change1.I == i && change1.J == j) {
					match = true;
					move = ((Node) treeNode.content()).number() - 1;
					break;
				}
				treeNode = treeNode.parentPos();
			}
			if (match) {
				return move;
			} else {
				return 0;
			}
		}
		*/
		int move = P.number(i, j) + 1;
		int numToShow = 0;
		if (Range > 0) {
			if (move >= Range) {
				numToShow = startNumber <= 0 ? move : move - Range + startNumber;
			}
		} else if (Range < 0) {
			if (lasti > 0) {
				int lastMove = P.number(lasti, lastj) + 1;
				if (move - lastMove > Range)
					numToShow = move;
			}
		}
		return numToShow - 1;
	}

	void setfonts()
	// get the font from the go frame
	{
		font = GF.boardFont();
		largeFont = new Font(font.getFamily(), font.getStyle(), font.getSize() + 5);
		fontmetrics = getFontMetrics(font);
		largeFontmetrics = getFontMetrics(largeFont);
	}

	public Dimension getMinimumSize()
	// for the layout menager of the containing component
	{
		Dimension d = getSize();
		if (d.width == 0) return d = Dim;
		d.width = d.height + 5;
		return d;
	}

	public Dimension getPreferredSize()
	// for the layout menager of the containing component
	{
		return getMinimumSize();
	}

	// ************** paint ************************

	public synchronized void makeimages()
	// create images and repaint, if ActiveImage is invalid.
	// uses parameters from the BoardInterface for coordinate layout.
	{
		Dim = getSize();
		boolean c = GF.getParameter("coordinates", true);
		boolean ulc = GF.getParameter("upperleftcoordinates", true);
		boolean lrc = GF.getParameter("lowerrightcoordinates", false);
		D = Dim.height / ((2 * virtualBoardWidth + S) + 1 + (c ? ((ulc ? 1 : 0) + (lrc ? 1 : 0)) : 0));
		OP = D / 4;
		O = D / 2 + OP;
		W = (2 * virtualBoardWidth + S) * D + 2 * O;
		if (c) {
			if (lrc)
				OT = D;
			else
				OT = 0;
			if (ulc)
				OTU = D;
			else
				OTU = 0;
		} else
			OT = OTU = 0;
		W += OTU + OT;
		if (!GF.boardShowing()) return;
		// create image and paint empty board
		synchronized (this) {
			Empty = createImage(W, W);
			EmptyShadow = createImage(W, W);
		}
		emptypaint();
		ActiveImage = createImage(W, W);
		// update the emtpy board
		updateall();
		repaint();
	}

	synchronized public void paint(Graphics g)
	// repaint the board (generate images at first call)
	{
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		Dimension d = getSize();
		// test if ActiveImage is valid.
		if (Dim.width != d.width || Dim.height != d.height) {
			Dim = d;
			makeimages();
			repaint();
			return;
		} else {
			if (ActiveImage != null) g.drawImage(ActiveImage, 0, 0, this);
		}
		if (!Activated && GF.boardShowing()) {
			Activated = true;
			GF.activate();
		}
		g.setColor(GF.backgroundColor());
		if (d.width > W)
			g.fillRect(W, 0, d.width - W, W);
		if (d.height > W)
			g.fillRect(0, W, d.width, d.height - W);
	}

	public void update(Graphics g) {
		paint(g);
	}

	// The following is for the thread, which tries to draw the
	// board on program start, to save time.

	public static WoodPaint woodpaint = null;

	// Now come the normal routine to draw a board.
	EmptyVirtualRealBoardPaint EPThread = null;

	/**
	 * Try to paint the wooden board. If the size is correct, use
	 * the predraw board. Otherwise generate an EmptyPaint thread
	 * to paint a board.
	 */
	public synchronized boolean trywood(Graphics gr, Graphics grs, int w) {
		int vbwInPixels = 0;
		if (virtualBoardWidth > 0)
			vbwInPixels = virtualBoardWidth * D + OP;
		if (!GF.useVirtualBoard()) vbwInPixels = 0;
		if (EmptyVirtualRealBoardPaint.haveImage(w, w, vbwInPixels, GF.boardColor(), GF.virtualBoardColor(),
				OP + OP / 2, OP - OP / 2, D, Global.getParameter("mosaicboard", Defaults.MOSAIC_BOARD))) {
			// use predrawn image
			gr.drawImage(EmptyVirtualRealBoardPaint.staticImage, O + OTU - OP, O + OTU - OP, this);
			if (EmptyVirtualRealBoardPaint.staticShadowImage != null && grs != null) {
				grs.drawImage(EmptyVirtualRealBoardPaint.staticShadowImage, O + OTU - OP, O + OTU - OP, this);
			}
			return true;
		} else {
			if (EPThread != null && EPThread.isAlive()) EPThread.stopit();
			EPThread = new EmptyVirtualRealBoardPaint(this, w, w, vbwInPixels, GF.boardColor(), GF.virtualBoardColor(),
					true, OP + OP / 2, OP - OP / 2, D, Global.getParameter("mosaicboard", Defaults.MOSAIC_BOARD));
		}
		return false;
	}

	final double pixel = 0.8;
	final double shadow = 0.7;

	public void stonespaint()
	// Create the (beauty) images of the stones (black and white)
	{
		int col = GF.boardColor().getRGB();
		int vbCol = GF.virtualBoardColor().getRGB();
		int blue = col & 0x0000FF, green = (col & 0x00FF00) >> 8, red = (col & 0xFF0000) >> 16;
		int vbBlue = vbCol & 0x0000FF, vbGreen = (vbCol & 0x00FF00) >> 8, vbRed = (vbCol & 0xFF0000) >> 16;
		boolean Alias = GF.getParameter("alias", true);
		if (BlackStone == null || BlackStone.getWidth(this) != D + 2) {
			int d = D + 2;
			int pb[] = new int[d * d];
			int pw[] = new int[d * d];
			int vbpw[] = new int[d * d];
			int i, j, g, k;
			double di, dj, d2 = (double) d / 2.0 - 5e-1, r = d2 - 2e-1, f = Math.sqrt(3);
			double x, y, z, xr, xg, hh;
			k = 0;
			if (GF.getParameter("smallerstones", false)) r -= 1;
			for (i = 0; i < d; i++)
				for (j = 0; j < d; j++) {
					di = i - d2;
					dj = j - d2;
					hh = r - Math.sqrt(di * di + dj * dj);
					if (hh >= 0) {
						z = r * r - di * di - dj * dj;
						if (z > 0)
							z = Math.sqrt(z) * f;
						else
							z = 0;
						x = di;
						y = dj;
						xr = Math.sqrt(6 * (x * x + y * y + z * z));
						xr = (2 * z - x + y) / xr;
						if (xr > 0.9)
							xg = (xr - 0.9) * 10;
						else
							xg = 0;
						if (hh > pixel || !Alias) {
							g = (int) (10 + 10 * xr + xg * 140);
							pb[k] = (255 << 24) | (g << 16) | (g << 8) | g;
							g = (int) (200 + 10 * xr + xg * 45);
							pw[k] = (255 << 24) | (g << 16) | (g << 8) | g;
							vbpw[k] = pw[k];
						} else {
							hh = (pixel - hh) / pixel;
							g = (int) (10 + 10 * xr + xg * 140);
							double shade;
							if (di - dj < r / 3)
								shade = 1;
							else
								shade = shadow;
							pb[k] = ((255 << 24)
									| (((int) ((1 - hh) * g + hh * shade * red)) << 16)
									| (((int) ((1 - hh) * g + hh * shade * green)) << 8)
									| ((int) ((1 - hh) * g + hh * shade * blue)));
							g = (int) (200 + 10 * xr + xg * 45);
							pw[k] = ((255 << 24)
									| (((int) ((1 - hh) * g + hh * shade * red)) << 16)
									| (((int) ((1 - hh) * g + hh * shade * green)) << 8)
									| ((int) ((1 - hh) * g + hh * shade * blue)));
							vbpw[k] = ((255 << 24)
									| (((int) ((1 - hh) * g + hh * shade * vbRed)) << 16)
									| (((int) ((1 - hh) * g + hh * shade * vbGreen)) << 8)
									| ((int) ((1 - hh) * g + hh * shade * vbBlue)));
						}
					} else
						pb[k] = pw[k] = vbpw[k] = 0;
					k++;
				}
			BlackStone = createImage(new MemoryImageSource(d, d, ColorModel.getRGBdefault(),
					pb, 0, d));
			WhiteStone = createImage(new MemoryImageSource(d, d, ColorModel.getRGBdefault(),
					pw, 0, d));
			vbWhiteStone = createImage(new MemoryImageSource(d, d, ColorModel.getRGBdefault(),
					vbpw, 0, d));
		}
	}

    private void drawLines(Graphics g, Graphics gs, boolean mosaic){
        Color boardLineColor = Global.getParameter("boardlinecolor", Defaults.BOARD_LINE_COLOR);
        Color virtualBoardLineColor = Global.getParameter("virtualboardlinecolor", Defaults.VIRTUAL_BOARD_LINE_COLOR);
        int vS = S + 2 * virtualBoardWidth;
        int p0 = O + OTU + D / 2;
        int p1 = p0 + virtualBoardWidth * D - D/2;
        int p2 = p1 + S * D;
        int p3 = p2 + virtualBoardWidth * D - D/2;
        g.setColor(virtualBoardLineColor);
        gs.setColor(virtualBoardLineColor);
        for (int i = 0; i < vS; i++) {
            int p = p0+i*D;
            g.drawLine(p, p0, p, p3);
            gs.drawLine(p, p0, p, p3);
            g.drawLine(p0, p, p3, p);
            gs.drawLine(p0, p, p3, p);
        }
        g.setColor(boardLineColor);
        gs.setColor(boardLineColor);
        for(int i=0; i<S; i++){
            int p = p1+i*D + D/2;
            g.drawLine(p, p1, p, p2);
            gs.drawLine(p, p1, p, p2);
            g.drawLine(p1, p, p2, p);
            gs.drawLine(p1, p, p2, p);
        }
        if (mosaic){
            for(int i=0; i<virtualBoardWidth; i++){
                int p = p0+i*D;
                g.drawLine(p, p0, p, p1);
                gs.drawLine(p, p0, p, p1);
                g.drawLine(p0, p, p1, p);
                gs.drawLine(p0, p, p1, p);
                //
                g.drawLine(p, p2, p, p3);
                gs.drawLine(p, p2, p, p3);
                g.drawLine(p2, p, p3, p);
                gs.drawLine(p2, p, p3, p);
                p = p2+i*D + D/2;
                g.drawLine(p, p0, p, p1);
                gs.drawLine(p, p0, p, p1);
                g.drawLine(p0, p, p1, p);
                gs.drawLine(p0, p, p1, p);
                //
                g.drawLine(p, p2, p, p3);
                gs.drawLine(p, p2, p, p3);
                g.drawLine(p2, p, p3, p);
                gs.drawLine(p2, p, p3, p);
            }
        }
    }

    public synchronized void emptypaint()
	// Draw an empty board onto the graphics context g.
	// Including lines, coordinates and markers.
	{
		if (woodpaint != null && woodpaint.isAlive()) woodpaint.stopit();
		synchronized (this) {
			if (Empty == null || EmptyShadow == null) return;
			int vS = S; // virtual size
			boolean uvb = GF.useVirtualBoard();
			if (uvb) vS = 2 * virtualBoardWidth + S;
			Graphics g = Empty.getGraphics(), gs = EmptyShadow.getGraphics();
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
			((Graphics2D)gs).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
			g.setColor(GF.backgroundColor());
			g.fillRect(0, 0, getWidth(), getHeight());
			Color c;
			if (uvb) {
				if (!GF.getParameter("beauty", true)
						|| !trywood(g, gs, vS * D + 2 * OP)) // beauty board not available
				{
					c = GF.virtualBoardColor();
					g.setColor(c);
					g.fillRect(O + OTU - OP, O + OTU - OP, vS * D + 2 * OP, vS * D + 2 * OP);
					gs.setColor(c);
					gs.fillRect(O + OTU - OP, O + OTU - OP, vS * D + 2 * OP, vS * D + 2 * OP);
                    g.setColor(GF.boardColor());
					g.fillRect(O + OTU + virtualBoardWidth * D, O + OTU + virtualBoardWidth * D, S * D, S * D);
					gs.setColor(GF.boardColor());
					gs.fillRect(O + OTU + virtualBoardWidth * D, O + OTU + virtualBoardWidth * D, S * D, S * D);
                    if (Global.getParameter("mosaicboard", Defaults.MOSAIC_BOARD)) {
                        g.fillRect(O + OTU - OP, O + OTU - OP, virtualBoardWidth * D + OP, virtualBoardWidth * D + OP);
					    gs.fillRect(O + OTU - OP, O + OTU - OP, virtualBoardWidth * D + OP, virtualBoardWidth * D + OP);
                        g.fillRect(O + OTU + (S + virtualBoardWidth)*D, O + OTU - OP, virtualBoardWidth * D + OP, virtualBoardWidth * D + OP);
					    gs.fillRect(O + OTU + (S + virtualBoardWidth)*D, O + OTU - OP, virtualBoardWidth * D + OP, virtualBoardWidth * D + OP);
                        g.fillRect(O + OTU - OP, O + OTU + (S + virtualBoardWidth)*D, virtualBoardWidth * D + OP, virtualBoardWidth * D + OP);
					    gs.fillRect(O + OTU - OP, O + OTU + (S + virtualBoardWidth)*D, virtualBoardWidth * D + OP, virtualBoardWidth * D + OP);
                        g.fillRect(O + OTU + (S + virtualBoardWidth)*D, O + OTU + (S + virtualBoardWidth)*D, virtualBoardWidth * D + OP, virtualBoardWidth * D + OP);
					    gs.fillRect(O + OTU + (S + virtualBoardWidth)*D, O + OTU + (S + virtualBoardWidth)*D, virtualBoardWidth * D + OP, virtualBoardWidth * D + OP);
                    }
				}
			} else {
				if (!GF.getParameter("beauty", true)
						|| !trywood(g, gs, S * D + 2 * OP)) // beauty board not available
				{
					g.setColor(GF.boardColor());
					g.fillRect(O + OTU - OP, O + OTU - OP, S * D + 2 * OP, S * D + 2 * OP);
					gs.setColor(GF.boardColor());
					gs.fillRect(O + OTU - OP, O + OTU - OP, S * D + 2 * OP, S * D + 2 * OP);
				}
			}
			if (GF.getParameter("beautystones", true)) stonespaint();
			g.setColor(Color.black);
			gs.setColor(Color.black);
			int i, j, x, y1, y2;
			if (!Global.getParameter("hidelinesonboard", false)) {
// Draw lines
                /*
                x = O + OTU + D / 2;
				y1 = O + OTU + D / 2;
				y2 = O + D / 2 + OTU + (vS - 1) * D;
				for (i = 0; i < vS; i++) {
					g.drawLine(x, y1, x, y2);
					g.drawLine(y1, x, y2, x);
					gs.drawLine(x, y1, x, y2);
					gs.drawLine(y1, x, y2, x);
					x += D;
				}
				*/
                drawLines(g, gs, Global.getParameter("mosaicboard", Defaults.MOSAIC_BOARD));
            }
			hand(g, gs, S);

            g.setColor(Color.black);
			gs.setColor(Color.black);

			int idx1 = virtualBoardWidth;
			int idx2 = idx1 + S;
// coordinates below and to the right
			if (OT > 0) {
				g.setFont(font);
				int y = O + OTU;
				int h = fontmetrics.getAscent() / 2 - 1;
				for (i = 0; i < vS; i++) {
					int labelIdx = rotate % 2 == 0 ? getOrigJ(0, i) : getOrigI(0, i);
					String s = getVLabel(labelIdx);
					if (i < idx1 || i >= idx2) {
						s = " ";
					}
					int w = fontmetrics.stringWidth(s) / 2;
					g.drawString(s, O + OTU + vS * D + D / 2 + OP - w, y + D / 2 + h);
					y += D;
				}
				x = O + OTU;
				for (i = 0; i < vS; i++) {
					int labelIdx = rotate % 2 == 0 ? getOrigI(i, 0) : getOrigJ(i, 0);
					String s = getHLabel(labelIdx);
					if (i < idx1 || i >= idx2) {
						s = " ";
					}
					int w = fontmetrics.stringWidth(s) / 2;
					g.drawString(s, x + D / 2 - w, O + OTU + vS * D + D / 2 + OP + h);
					x += D;
				}
			}
// coordinates to the left and above
			if (OTU > 0) {
				g.setFont(font);
				int y = O + OTU;
				int h = fontmetrics.getAscent() / 2 - 1;
				for (i = 0; i < vS; i++) {
					int labelIdx = rotate % 2 == 0 ? getOrigJ(0, i) : getOrigI(0, i);
					String s = getVLabel(labelIdx);
					if (i < idx1 || i >= idx2) {
						s = " ";
					}
					int w = fontmetrics.stringWidth(s) / 2;
					g.drawString(s, O + D / 2 - OP - w, y + D / 2 + h);
					y += D;
				}
				x = O + OTU;
				for (i = 0; i < vS; i++) {
					int labelIdx = rotate % 2 == 0 ? getOrigI(i, 0) : getOrigJ(i, 0);
					String s = getHLabel(labelIdx);
					if (i < idx1 || i >= idx2) {
						s = " ";
					}
					int w = fontmetrics.stringWidth(s) / 2;
					g.drawString(s, x + D / 2 - w, O + D / 2 - OP + h);
					x += D;
				}
			}
		}
	}

	private int[][][] handicaps = {// start from 3!
			{{1, 1}}, // 3
			{{1, 1}, {2, 2}}, // 4
			{{2, 2}}, // 5
			{{2, 2}, {3, 3}}, // 6
			{{2, 2}, {2, 4}, {4, 2}, {4, 4}}, // 7
			{{2, 2}, {2, 5}, {5, 2}, {5, 5}}, // 8
			{{2, 2}, {2, 6}, {6, 2}, {6, 6}, {4, 4}}, // 9
			{{2, 2}, {2, 7}, {7, 2}, {7, 7}}, // 10
			{{3, 3}, {3, 7}, {7, 3}, {7, 7}}, // 11
			{{3, 3}, {3, 8}, {8, 3}, {8, 8}}, // 12
			{{3, 3}, {3, 9}, {9, 3}, {9, 9}, {6, 6}}, // 13
			{{3, 3}, {3, 10}, {10, 3}, {10, 10}}, // 14
			{{3, 3}, {3, 7}, {3, 11}, {7, 3}, {7, 7}, {7, 11}, {11, 3}, {11, 7}, {11, 11}}, // 15
			{{3, 3}, {3, 12}, {12, 3}, {12, 12}}, // 16
			{{3, 3}, {3, 8}, {3, 13}, {8, 3}, {8, 8}, {8, 13}, {13, 3}, {13, 8}, {13, 13}}, // 17
			{{3, 3}, {3, 14}, {14, 3}, {14, 14}}, // 18
			{{3, 3}, {3, 9}, {3, 15}, {9, 3}, {9, 9}, {9, 15}, {15, 3}, {15, 9}, {15, 15}}, // 19
	};

	public void hand(Graphics g, Graphics gs, int boardSize) {
		if (boardSize < 3)
			return;
		if (boardSize > 19) {
			if (boardSize % 2 == 1) {
				int[] x = {virtualBoardWidth + 3, virtualBoardWidth + boardSize / 2, virtualBoardWidth + boardSize - 4};
				for (int i = 0; i < x.length; i++)
					for (int j = 0; j < x.length; j++) {
						hand(g, x[i], x[j]);
						hand(gs, x[i], x[j]);
					}

			} else {
				int[] x = {virtualBoardWidth + 3, virtualBoardWidth + boardSize - 4};
				for (int i = 0; i < x.length; i++)
					for (int j = 0; j < x.length; j++) {
						hand(g, x[i], x[j]);
						hand(gs, x[i], x[j]);
					}
			}
			return;
		}
		int[][] h = handicaps[boardSize - 3];
		for (int i = 0; i < h.length; i++) {
			hand(g, virtualBoardWidth + h[i][0], virtualBoardWidth + h[i][1]);
			hand(gs, virtualBoardWidth + h[i][0], virtualBoardWidth + h[i][1]);
		}
	}

	public void hand(Graphics g, int i, int j)
	// help function for emptypaint (Handicap point)
	{
		if (Global.getParameter("hidestarmarks", false))
			return;

		g.setColor(Global.getParameter("boardlinecolor", Defaults.BOARD_LINE_COLOR));
		int centerx = O + OTU + D / 2 + i * D;
		int centery = O + OTU + D / 2 + j * D;
		int s = D / 10;
		if (s < 1) {
			s = 1;
		} else if (s > 3) {
			s = 3;
		}
		if (s == 1) {
			g.fillRect(centerx - s - 1, centery - s - 1, 2 * s + 3, 2 * s + 3);
		} else if (s == 2) {
			g.fillRect(centerx - s - 1, centery - s, 2 * s + 3, 2 * s + 1);
			g.fillRect(centerx - s, centery - s - 1, 2 * s + 1, 2 * s + 3);
		} else {
			g.fillRect(centerx - s, centery - s, 2 * s + 1, 2 * s + 1);
			g.fillRect(centerx - s - 1, centery - s + 1, 2 * s + 3, 2 * s - 1);
			g.fillRect(centerx - s + 1, centery - s - 1, 2 * s - 1, 2 * s + 3);
		}
	}

	// *************** mouse events **********************

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
	}

	boolean MouseDown = false; // mouse release only, if mouse pressed
	int dragstarti,
			dragstartj;

	public synchronized void mousePressed(MouseEvent e) {
		MouseDown = true;
		requestFocusInWindow();
		int x = e.getX(), y = e.getY();
		x -= O + OTU;
		y -= O + OTU;
		dragstarti = x / D;
		dragstartj = y / D; // determine position
	}

	public synchronized void mouseReleased(MouseEvent e)
	// handle mouse input
	{
		if (!MouseDown) return;
		int x = e.getX(), y = e.getY();
		MouseDown = false;
		if (ActiveImage == null) return;
		if (!Active) return;
		getinformation();
		x -= O + OTU;
		y -= O + OTU;
		int i = x / D, j = y / D; // determine position
		/* if (i,j) is outside the board, do nothing. */
		if (x < 0 || y < 0 || i >= S + 2 * virtualBoardWidth || j >= S + 2 * virtualBoardWidth) return;
		if (i != dragstarti || j != dragstartj) { // check if it's drag and drop
			if (gameType == Global.DAOQI) {
				drag(i - dragstarti, j - dragstartj);
				updateboard();
			}
			return;
		}
		int origI = getOrigI(i, j);
		int origJ = getOrigJ(i, j);
		i = origI;
		j = origJ;
		if (e.isMetaDown()) { // right click
			if (gameType == Global.DAOQI) {
				recenter(i, j);
				updateboard();
			}
			return;
		}
		switch (State) {
			case 3: // set a black stone
				if (GF.blocked() && Pos.isLastMain()) return;
				if (e.isShiftDown() && e.isControlDown())
					setmousec(i, j, 1);
				else
					setmouse(i, j, 1);
				break;
			case 4: // set a white stone
				if (GF.blocked() && Pos.isLastMain()) return;
				if (e.isShiftDown() && e.isControlDown())
					setmousec(i, j, -1);
				else
					setmouse(i, j, -1);
				break;
			case 5:
				mark(i, j);
				break;
			case 6:
				letter(i, j);
				break;
			case 7: // delete a stone
				if (e.isShiftDown() && e.isControlDown())
					deletemousec(i, j);
				else
					deletemouse(i, j);
				break;
			case 8: // remove a group
				removemouse(i, j);
				break;
			case 9:
				specialmark(i, j);
				break;
			case 10:
				textmark(i, j);
				break;
			case 1:
			case 2: // normal move mode
				if (e.isShiftDown()) // create variation
				{
					if (e.isControlDown()) {
						//if (GF.blocked() && Pos.isLastMain()) return;
						//changemove(i, j);
						variation1(i, j);
					} else
						variation(i, j);
				} else if (e.isControlDown())
				// goto variation
				{
					if (P.tree(i, j) != null) {
						gotovariation(i, j);
					}
				} else // place a W or B stone
				{
					if (GF.blocked() && Pos.isLastMain()) return;
					movemouse(i, j);
				}
				break;
		}
		showinformation();
		copy(); // show position
	}

	// target rectangle things

	protected int iTarget = -1,
			jTarget = -1;

	public synchronized void mouseMoved(MouseEvent e)
	// show coordinates in the Lm Label
	{
		if (!Active) return;
		if (DisplayNodeName) {
			GF.setLabelM(LText);
			DisplayNodeName = false;
		}
		int x = e.getX(), y = e.getY();
		x -= O + OTU;
		y -= O + OTU;
		int i = x / D, j = y / D; // determine position
		int vS = S + 2 * virtualBoardWidth;
		if (i < 0 || j < 0 || i >= vS || j >= vS) {
			if (GF.showTarget()) {
				iTarget = jTarget = -1;
				copy();
			}
			return;
		}
		if (GF.showTarget() && (iTarget != i || jTarget != j)) {
			drawTarget(i, j);
			iTarget = i;
			jTarget = j;
		}
		GF.setLabelM(getLabel(getOrigI(i, j), getOrigJ(i, j)));
	}

	public void drawTarget(int i, int j) {
		copy();
		Graphics g = getGraphics();
		if (g == null) return;
		i = O + OTU + i * D + D / 2;
		j = O + OTU + j * D + D / 2;
		if (GF.bwColor())
			g.setColor(Color.white);
		else
			g.setColor(Color.gray.brighter());
		g.drawRect(i - D / 4, j - D / 4, D / 2, D / 2);
		g.dispose();
	}

	public void mouseEntered(MouseEvent e)
	// start showing coordinates
	{
		if (!Active) return;
		if (DisplayNodeName) {
			GF.setLabel(LText);
			DisplayNodeName = false;
		}
		int x = e.getX(), y = e.getY();
		x -= O + OTU;
		y -= O + OTU;
		int i = x / D, j = y / D; // determine position
		int vS = S + 2 * virtualBoardWidth;
		if (i < 0 || j < 0 || i >= vS || j >= vS) return;
		if (GF.showTarget()) {
			drawTarget(i, j);
			iTarget = i;
			jTarget = j;
		}
		GF.setLabelM(getLabel(getOrigI(i, j), getOrigJ(i, j)));
	}

	public void mouseExited(MouseEvent e)
	// stop showing coordinates
	{
		if (!Active) return;
		GF.setLabelM("--");
		if (!NodeName.equals("")) {
			GF.setLabel(NodeName);
			DisplayNodeName = true;
		}
		if (GF.showTarget()) {
			iTarget = jTarget = -1;
			copy();
		}
	}

	// *************** keyboard events ********************

	public synchronized void keyPressed(KeyEvent e) {
		if (e.isActionKey()) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_DOWN:
					if (e.isControlDown()) {
						if (e.isShiftDown()) {
							allforward();
						} else {
							fastforward();
						}
					} else if (e.isAltDown()) {
						commentForward();
					} else {
						forward();
					}
					break;
				case KeyEvent.VK_UP:
					if (e.isControlDown()) {
						if (e.isShiftDown()) {
							allback();
						} else {
							fastback();
						}
					} else if (e.isAltDown()) {
						commentBack();
					} else {
						back();
					}
					break;
				case KeyEvent.VK_LEFT:
					varleft();
					break;
				case KeyEvent.VK_RIGHT:
					varright();
					break;
				case KeyEvent.VK_PAGE_DOWN:
					fastforward();
					break;
				case KeyEvent.VK_PAGE_UP:
					fastback();
					break;
				case KeyEvent.VK_BACK_SPACE:
				case KeyEvent.VK_DELETE:
					if (e.isControlDown())
						doundo(Pos);
					else
						undo();
					break;
				case KeyEvent.VK_HOME:
					varmain();
					break;
				case KeyEvent.VK_END:
					varmaindown();
					break;
			}
		} else {
			switch (e.getKeyChar()) {
				case '*':
					varmain();
					break;
				case '/':
					varmaindown();
					break;
				case 'v':
				case 'V':
					varup();
					break;
				case 'm':
				case 'M':
					mark();
					break;
				case 'p':
				case 'P':
					resume();
					break;
				case 'c':
				case 'C':
					specialmark(Field.CIRCLE);
					break;
				case 's':
				case 'S':
					specialmark(Field.SQUARE);
					break;
				case 't':
				case 'T':
					specialmark(Field.TRIANGLE);
					break;
				case 'l':
				case 'L':
					letter();
					break;
				case 'r':
				case 'R':
					specialmark(Field.CROSS);
					break;
				case 'w':
					setwhite();
					break;
				case 'b':
					setblack();
					break;
				case 'W':
					white();
					break;
				case 'B':
					black();
					break;
				case '+':
					gotonext();
					break;
				case '-':
					gotoprevious();
					break;
					// Bug (VK_DELETE not reported as ActionEvent)
				case KeyEvent.VK_BACK_SPACE:
				case KeyEvent.VK_DELETE:
					undo();
					break;
			}
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	// set things on the board

	void gotovariation(int i, int j)
	// goto the variation at (i,j)
	{
		TreeNode newpos = P.tree(i, j);
		getinformation();
		if (VCurrent && newpos.parentPos() == Pos.parentPos()) {
			goback();
			Pos = newpos;
			setnode();
			setlast();
		} else if (!VCurrent && newpos.parentPos() == Pos) {
			Pos = newpos;
			setnode();
			setlast();
		}
		copy();
		showinformation();
	}

	public void set(int i, int j)
	// set a new move, if the board position is empty
	{
		Action a;
		synchronized (Pos) {
			if (P.color(i, j) == 0) // empty?
			{
				if (Pos.node().actions() != null || Pos.parentPos() == null)
// create a new node, if there the current node is not
// empty, else use the current node. exception is the first
// move, where we always create a new move.
				{
					Node n = new Node(++number);
					if (P.color() > 0) {
						a = new Action("B", Field.string(i, j));
					} else {
						a = new Action("W", Field.string(i, j));
					}
					n.addaction(a); // note the move action
					setaction(n, a, P.color()); // display the action
// !!! We alow for suicide moves
					TreeNode newpos = new TreeNode(n);
					Pos.addchild(newpos); // note the move
					n.main(Pos);
					Pos = newpos; // update current position pointer
				} else {
					Node n = Pos.node();
					if (P.color() > 0) {
						a = new Action("B", Field.string(i, j));
					} else {
						a = new Action("W", Field.string(i, j));
					}
					n.addaction(a); // note the move action
					setaction(n, a, P.color()); // display the action
// !!! We alow for suicide moves
				}
			}
		}
	}

	public void delete(int i, int j)
	// delete the stone and note it
	{
		if (P.color(i, j) == 0) return;
		synchronized (Pos) {
			Node n = Pos.node();
			if (GF.getParameter("puresgf", true) &&
					(n.contains("B") || n.contains("W")))
				n = newnode();
			String field = Field.string(i, j);
			if (n.contains("AB", field)) {
				undonode();
				n.toggleaction(new Action("AB", field));
				setnode();
			} else if (n.contains("AW", field)) {
				undonode();
				n.toggleaction(new Action("AW", field));
				setnode();
			} else if (n.contains("B", field)) {
				undonode();
				n.toggleaction(new Action("B", field));
				setnode();
			} else if (n.contains("W", field)) {
				undonode();
				n.toggleaction(new Action("W", field));
				setnode();
			} else {
				Action a = new Action("AE", field);
				n.expandaction(a);
				n.addchange(new Change(i, j, P.color(i, j)));
				P.color(i, j, 0);
				update(i, j);
			}
			showinformation();
			copy();
		}
	}

	public void changemove(int i, int j)
	// change a move to a new field (dangerous!)
	{
		if (P.color(i, j) != 0) return;
		synchronized (Pos) {
			ListElement la = Pos.node().actions();
			while (la != null) {
				Action a = (Action) la.content();
				if (a.type().equals("B") || a.type().equals("W")) {
					undonode();
					a.arguments().content(Field.string(i, j));
					setnode();
					break;
				}
			}
		}
	}

	public void removegroup(int i0, int j0)
	// completely remove a group (at end of game, before count)
	// note all removals
	{
		if (Pos.haschildren()) return;
		if (P.color(i0, j0) == 0) return;
		Action a;
		P.markgroup(i0, j0);
		int i, j;
		int c = P.color(i0, j0);
		Node n = Pos.node();
		if (n.contains("B") || n.contains("W")) n = newnode();
		for (i = 0; i < S; i++)
			for (j = 0; j < S; j++) {
				if (P.marked(i, j)) {
					a = new Action("AE", Field.string(i, j));
					n.addchange(new Change(i, j, P.color(i, j), P.number(i, j)));
					n.expandaction(a);
					if (P.color(i, j) > 0) {
						n.Pb++;
						Pb++;
					} else {
						n.Pw++;
						Pw++;
					}
					P.color(i, j, 0);
					update(i, j);
				}
			}
		copy();
	}

	public void mark(int i, int j)
	// Emphasize the field at i,j
	{
		Node n = Pos.node();
		Action a = new MarkAction(Field.string(i, j), GF);
		n.toggleaction(a);
		update(i, j);
	}

	public void specialmark(int i, int j)
	// Emphasize with the SpecialMarker
	{
		Node n = Pos.node();
		String s;
		switch (SpecialMarker) {
			case Field.SQUARE:
				s = "SQ";
				break;
			case Field.CIRCLE:
				s = "CR";
				break;
			case Field.TRIANGLE:
				s = "TR";
				break;
			default :
				s = "MA";
				break;
		}
		Action a = new Action(s, Field.string(i, j));
		n.toggleaction(a);
		update(i, j);
	}

	public void markterritory(int i, int j, int color) {
		Action a;
		if (color > 0)
			a = new Action("TB", Field.string(i, j));
		else
			a = new Action("TW", Field.string(i, j));
		Pos.node().expandaction(a);
		update(i, j);
	}

	public void textmark(int i, int j) {
		Action a = new Action("LB", Field.string(i, j) + ":" + TextMarker);
		Pos.node().expandaction(a);
		update(i, j);
		GF.advanceTextmark();
	}

	public void letter(int i, int j)
	// Write a character to the field at i,j
	{
		Action a = new LabelAction(Field.string(i, j), GF);
		Pos.node().toggleaction(a);
		update(i, j);
	}

	public Node newnode() {
		Node n = new Node(++number);
		TreeNode newpos = new TreeNode(n);
		Pos.addchild(newpos); // note the move
		n.main(Pos);
		Pos = newpos; // update current position pointerAction a;
		setlast();
		return n;
	}

	public void set(int i, int j, int c)
	// set a new stone, if the board position is empty
	// and we are on the last node.
	{
		if (Pos.haschildren()) return;
		setc(i, j, c);
	}

	public void setc(int i, int j, int c) {
		synchronized (Pos) {
			Action a;
			if (P.color(i, j) == 0) // empty?
			{
				Node n = Pos.node();
				if (GF.getParameter("puresgf", true) &&
						(n.contains("B") || n.contains("W")))
					n = newnode();
				n.addchange(new Change(i, j, 0));
				if (c > 0) {
					a = new Action("AB", Field.string(i, j));
				} else {
					a = new Action("AW", Field.string(i, j));
				}
				n.expandaction(a); // note the move action
				P.color(i, j, c);
				update(i, j);
			}
		}
	}

	int captured = 0,
			capturei,
			capturej;

	public void capture(int i, int j, Node n)
	// capture neighboring groups without liberties
	// capture own group on suicide
	{
		int c = -P.color(i, j);
		captured = 0;
		if (i > 0)
			capturegroup(i - 1, j, c, n);
		else
			capturegroup(S - 1, j, c, n);
		if (j > 0)
			capturegroup(i, j - 1, c, n);
		else
			capturegroup(i, S - 1, c, n);
		if (i < S - 1)
			capturegroup(i + 1, j, c, n);
		else
			capturegroup(0, j, c, n);
		if (j < S - 1)
			capturegroup(i, j + 1, c, n);
		else
			capturegroup(i, 0, c, n);
		if (P.color(i, j) == -c) {
			capturegroup(i, j, -c, n);
		}
		if (captured == 1 && P.count(i, j) != 1)
			captured = 0;
		if (!GF.getParameter("korule", true)) captured = 0;
	}

	public void capturegroup(int i, int j, int c, Node n)
	// Used by capture to determine the state of the groupt at (i,j)
	// Remove it, if it has no liberties and note the removals
	// as actions in the current node.
	{
		int ii, jj;
		Action a;
		if (P.color(i, j) != c) return;
		if (!P.markgrouptest(i, j, 0)) // liberties?
		{
			for (ii = 0; ii < S; ii++)
				for (jj = 0; jj < S; jj++) {
					if (P.marked(ii, jj)) {
						n.addchange(new Change(ii, jj, P.color(ii, jj), P.number(ii, jj)));
						if (P.color(ii, jj) > 0) {
							Pb++;
							n.Pb++;
						} else {
							Pw++;
							n.Pw++;
						}
						P.color(ii, jj, 0);
						update(ii, jj); // redraw the field (offscreen)
						captured++;
						capturei = ii;
						capturej = jj;
					}
				}
		}
	}

	public void variation(int i, int j) {
		if (Pos.parentPos() == null) return;
		if (P.color(i, j) == 0) // empty?
		{
			int c = P.color();
			goback();
			P.color(-c);
			set(i, j);
			if (!GF.getParameter("variationnumbers", false)) {
				P.number(i, j, 1);
				number = 2;
				Pos.node().number(2);
			}
			update(i, j);
		}
	}

	public void variation1(int i, int j) {
		if (Pos.parentPos() == null) return;
		if (P.color(i, j) == 0) // empty?
		{
			if (!Pos.haschildren()) {
				// TODO set a new move?
				return;
			}
			goforward();
			if (P.color(i, j) != 0)
				return;
			variation(i, j);
		}
	}

	public String formtime(int t) {
		int s, m, h = t / 3600;
		if (h >= 1) {
			t = t - 3600 * h;
			m = t / 60;
			s = t - 60 * m;
			return "" + h + ":" + twodigits(m) + ":" + twodigits(s);
		} else {
			m = t / 60;
			s = t - 60 * m;
			return "" + m + ":" + twodigits(s);
		}
	}

	public String twodigits(int n) {
		if (n < 10)
			return "0" + n;
		else
			return "" + n;
	}

	public String lookuptime(String type) {
		int t;
		if (Pos.parentPos() != null) {
			String s = Pos.parentPos().node().getaction(type);
			if (!s.equals("")) {
				try {
					t = Integer.parseInt(s);
					return formtime(t);
				} catch (Exception e) {
					return "";
				}
			} else
				return "";
		}
		return "";
	}

	public void showinformation()
	// update the label to display the next move and who's turn it is
	// and disable variation buttons
	// update the navigation buttons
	// update the comment
	{
		Node n = Pos.node();
		number = n.number();
		NodeName = n.getaction("N");
		String ms = "";
		if (n.main()) {
			if (!Pos.haschildren())
				ms = "** ";
			else
				ms = "* ";
		}
		switch (State) {
			case 3:
				LText = ms + GF.resourceString("Set_black_stones");
				break;
			case 4:
				LText = ms + GF.resourceString("Set_white_stones");
				break;
			case 5:
				LText = ms + GF.resourceString("Mark_fields");
				break;
			case 6:
				LText = ms + GF.resourceString("Place_letters");
				break;
			case 7:
				LText = ms + GF.resourceString("Delete_stones");
				break;
			case 8:
				LText = ms + GF.resourceString("Remove_prisoners");
				break;
			case 9:
				LText = ms + GF.resourceString("Set_special_marker");
				break;
			case 10:
				LText = ms + GF.resourceString("Text__") + TextMarker;
				break;
			default :
				if (P.color() > 0) {
					String s = lookuptime("BL");
					if (!s.equals(""))
						LText = ms + GF.resourceString("Next_move__Black_") + number + " (" + s + ")";
					else
						LText = ms + GF.resourceString("Next_move__Black_") + number;
				} else {
					String s = lookuptime("WL");
					if (!s.equals(""))
						LText = ms + GF.resourceString("Next_move__White_") + number + " (" + s + ")";
					else
						LText = ms + GF.resourceString("Next_move__White_") + number;
				}
		}
		//LText = LText + " (" + siblings() + " " + GF.resourceString("Siblings") + ", " +
		//		children() + " " + GF.resourceString("Children") + ")";
		if (NodeName.equals("")) {
			GF.setLabel(LText);
			DisplayNodeName = false;
		} else {
			GF.setLabel(NodeName);
			DisplayNodeName = true;
		}
		GF.setState(3, !n.main());
		GF.setState(4, !n.main());
		GF.setState(7, !n.main() || Pos.haschildren());
		if (State == 1 || State == 2) {
			if (P.color() == 1)
				State = 1;
			else
				State = 2;
		}
		GF.setState(1, Pos.parentPos() != null &&
				Pos.parentPos().firstChild() != Pos);
		GF.setState(2, Pos.parentPos() != null &&
				Pos.parentPos().lastChild() != Pos);
		GF.setState(5, Pos.haschildren());
		GF.setState(6, Pos.parentPos() != null);
		if (State != 9)
			GF.setState(State);
		else
			GF.setMarkState(SpecialMarker);
		int i, j;
		// delete all marks and variations
		for (i = 0; i < S; i++)
			for (j = 0; j < S; j++) {
				if (P.tree(i, j) != null) {
					P.tree(i, j, null);
					update(i, j);
				}
				if (P.marker(i, j) != Field.NONE) {
					P.marker(i, j, Field.NONE);
					update(i, j);
				}
				if (P.letter(i, j) != 0) {
					P.letter(i, j, 0);
					update(i, j);
				}
				if (P.haslabel(i, j)) {
					P.clearlabel(i, j);
					update(i, j);
				}
			}
		ListElement la = Pos.node().actions();
		Action a;
		String s;
		String sc = "";
		int let = 1;
		while (la != null) // setup the marks and letters
		{
			a = (Action) la.content();
			if (a.type().equals("C")) {
				sc = (String) a.arguments().content();
			} else if (a.type().equals("SQ") || a.type().equals("SL")) {
				ListElement larg = a.arguments();
				while (larg != null) {
					s = (String) larg.content();
					i = Field.i(s);
					j = Field.j(s);
					if (valid(i, j)) {
						P.marker(i, j, Field.SQUARE);
						update(i, j);
					}
					larg = larg.next();
				}
			} else if (a.type().equals("MA") || a.type().equals("M")
					|| a.type().equals("TW") || a.type().equals("TB")) {
				ListElement larg = a.arguments();
				while (larg != null) {
					s = (String) larg.content();
					i = Field.i(s);
					j = Field.j(s);
					if (valid(i, j)) {
						P.marker(i, j, Field.CROSS);
						update(i, j);
					}
					larg = larg.next();
				}
			} else if (a.type().equals("TR")) {
				ListElement larg = a.arguments();
				while (larg != null) {
					s = (String) larg.content();
					i = Field.i(s);
					j = Field.j(s);
					if (valid(i, j)) {
						P.marker(i, j, Field.TRIANGLE);
						update(i, j);
					}
					larg = larg.next();
				}
			} else if (a.type().equals("CR")) {
				ListElement larg = a.arguments();
				while (larg != null) {
					s = (String) larg.content();
					i = Field.i(s);
					j = Field.j(s);
					if (valid(i, j)) {
						P.marker(i, j, Field.CIRCLE);
						update(i, j);
					}
					larg = larg.next();
				}
			} else if (a.type().equals("L")) {
				ListElement larg = a.arguments();
				while (larg != null) {
					s = (String) larg.content();
					i = Field.i(s);
					j = Field.j(s);
					if (valid(i, j)) {
						P.letter(i, j, let);
						let++;
						update(i, j);
					}
					larg = larg.next();
				}
			} else if (a.type().equals("LB")) {
				ListElement larg = a.arguments();
				while (larg != null) {
					s = (String) larg.content();
					i = Field.i(s);
					j = Field.j(s);
					if (valid(i, j) && s.length() >= 4 &&
							s.charAt(2) == ':') {
						P.setlabel(i, j, s.substring(3));
						update(i, j);
					}
					larg = larg.next();
				}
			}
			la = la.next();
		}
		TreeNode p;
		ListElement l = null;
		if (VCurrent) {
			p = Pos.parentPos();
			if (p != null) l = p.firstChild().listelement();
		} else if (Pos.haschildren() && Pos.firstChild() != Pos.lastChild()) {
			l = Pos.firstChild().listelement();
		}
		while (l != null) {
			p = (TreeNode) l.content();
			if (p != Pos) {
				la = p.node().actions();
				while (la != null) {
					a = (Action) la.content();
					if (a.type().equals("W") || a.type().equals("B")) {
						s = (String) a.arguments().content();
						i = Field.i(s);
						j = Field.j(s);
						if (valid(i, j)) {
							P.tree(i, j, p);
							update(i, j);
						}
						break;
					}
					la = la.next();
				}
			}
			l = l.next();
		}
		if (!GF.getComment().equals(sc)) {
			GF.setComment(sc);
		}
		//if (Range >= 0 && !KeepRange) clearrange();
	}

	public int siblings() {
		ListElement l = Pos.listelement();
		if (l == null) return 0;
		while (l.previous() != null) l = l.previous();
		int count = 0;
		while (l.next() != null) {
			l = l.next();
			count++;
		}
		return count;
	}

	public int children() {
		if (!Pos.haschildren()) return 0;
		TreeNode p = Pos.firstChild();
		if (p == null) return 0;
		ListElement l = p.listelement();
		if (l == null) return 0;
		while (l.previous() != null) l = l.previous();
		int count = 1;
		while (l.next() != null) {
			l = l.next();
			count++;
		}
		return count;
	}

	public void clearsend() {
		if (sendi >= 0) {
			int i = sendi;
			sendi = -1;
			update(i, sendj);
		}
	}

	public void getinformation()
	// update the comment, when leaving the position
	{
		ListElement la = Pos.node().actions();
		Action a;
		clearsend();
		while (la != null) {
			a = (Action) la.content();
			if (a.type().equals("C")) {
				if (GF.getComment().equals(""))
					Pos.node().removeaction(la);
				else
					a.arguments().content((Object) GF.getComment());
				return;
			}
			la = la.next();
		}
		String s = GF.getComment();
		if (!s.equals("")) {
			Pos.addaction(new Action("C", s));
		}
	}

	public void update(int origI, int origJ) {
		int i = getI(origI, origJ);
		int j = getJ(origI, origJ);
		update_(i, j);
		if (GF.useVirtualBoard()) {
			int double_vbw = 2 * virtualBoardWidth;
			if (i < double_vbw) update_(i + S, j);
			if (j < double_vbw) update_(i, j + S);
			if (i < double_vbw && j < double_vbw) update_(i + S, j + S);
			if (double_vbw > S) { // extrawide virtual board (for small board game)
				int doubleS = 2 * S;
				if (i < double_vbw - S) {
					update_(i + doubleS, j);
					update_(i + doubleS, j + S);
				}
				if (j < double_vbw - S) {
					update_(i, j + doubleS);
					update_(i + S, j + doubleS);
				}
				if (i < double_vbw - S && j < double_vbw - S) update_(i + doubleS, j + doubleS);
			}
		}
	}

	public void update_(int i, int j)
	// update the field (i,j) in the offscreen image Active
	// in dependance of the board position P.
	// display the last move mark, if applicable.
	{
		if (ActiveImage == null) return;
		if (i < 0 || j < 0) return;
		int origi = getOrigI(i, j);
		int origj = getOrigJ(i, j);
		Graphics g = ActiveImage.getGraphics();
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		int xi = O + OTU + i * D;
		int xj = O + OTU + j * D;
		synchronized (this) {
			g.drawImage(Empty, xi, xj, xi + D, xj + D, xi, xj, xi + D, xj + D, this);
			if (GF.getParameter("shadows", true) && GF.getParameter("beauty", true)
					&& GF.getParameter("beautystones", true)) {
				if (P.color(origi, origj) != 0) {
					g.drawImage(EmptyShadow, xi - OP / 2, xj + OP / 2, xi + D - OP / 2, xj + D + OP / 2,
							xi - OP / 2, xj + OP / 2, xi + D - OP / 2, xj + D + OP / 2, this);
				} else {
					g.drawImage(Empty, xi - OP / 2, xj + OP / 2, xi + D - OP / 2, xj + D + OP / 2,
							xi - OP / 2, xj + OP / 2, xi + D - OP / 2, xj + D + OP / 2, this);
				}
				g.setClip(xi - OP / 2, xj + OP / 2, D, D);
				update1(g, i - 1, j);
				update1(g, i, j + 1);
				update1(g, i - 1, j + 1);
				g.setClip(xi, xj, D, D);
				switch (rotate % 4) {
					case 0:
						if (P.color(origi, normalize(origj - 1)) != 0) {
							g.drawImage(EmptyShadow, xi, xj, xi + D - OP / 2, xj + OP / 2,
									xi, xj, xi + D - OP / 2, xj + OP / 2, this);
						}
						break;
					case 1:
						if (P.color(normalize(origi + 1), origj) != 0) {
							g.drawImage(EmptyShadow, xi, xj, xi + D - OP / 2, xj + OP / 2,
									xi, xj, xi + D - OP / 2, xj + OP / 2, this);
						}
						break;
					case 2:
						if (P.color(origi, normalize(origj + 1)) != 0) {
							g.drawImage(EmptyShadow, xi, xj, xi + D - OP / 2, xj + OP / 2,
									xi, xj, xi + D - OP / 2, xj + OP / 2, this);
						}
						break;
					case 3:
						if (P.color(normalize(origi - 1), origj) != 0) {
							g.drawImage(EmptyShadow, xi, xj, xi + D - OP / 2, xj + OP / 2,
									xi, xj, xi + D - OP / 2, xj + OP / 2, this);
						}
						break;
				}
			}
		}
		g.setClip(xi, xj, D, D);
		update1(g, i, j);
		g.dispose();
	}

	void update1(Graphics g, int i, int j) {
		int max = S;
		if (GF.useVirtualBoard()) max += 2 * virtualBoardWidth;
		if (i < 0 || i >= max || j < 0 || j >= max) return;
		int origi = getOrigI(i, j);
		int origj = getOrigJ(i, j);
		char c[] = new char[1];
		int xi = O + OTU + i * D;
		int xj = O + OTU + j * D;
		if (P.color(origi, origj) > 0 || (P.color(origi, origj) < 0 && GF.blackOnly())) {
			if (BlackStone != null) {
				g.drawImage(BlackStone, xi - 1, xj - 1, this);
			} else {
				g.setColor(GF.blackColor());
				g.fillOval(xi + 1, xj + 1, D - 2, D - 2);
				g.setColor(GF.blackSparkleColor());
				g.drawArc(xi + D / 2, xj + D / 4, D / 4, D / 4, 40, 50);
			}
		} else if (P.color(origi, origj) < 0) {
			if (WhiteStone != null) {
				if (i >= virtualBoardWidth && i < S + virtualBoardWidth && j >= virtualBoardWidth && j < S + virtualBoardWidth)
					g.drawImage(WhiteStone, xi - 1, xj - 1, this);
				else
					g.drawImage(vbWhiteStone, xi - 1, xj - 1, this);
			} else {
				g.setColor(GF.whiteColor());
				g.fillOval(xi + 1, xj + 1, D - 2, D - 2);
				g.setColor(GF.whiteSparkleColor());
				g.drawArc(xi + D / 2, xj + D / 4, D / 4, D / 4, 40, 50);
			}
		}
		if (P.marker(origi, origj) != Field.NONE) {
			//if (GF.bwColor()) {
			if (P.color(origi, origj) >= 0)
				g.setColor(Color.white);
			else
				g.setColor(Color.black);
			//} else
			//    g.setColor(GF.markerColor(P.color(origi, origj)));
			int h = D / 4;
			switch (P.marker(origi, origj)) {
				case Field.CIRCLE:
					((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g.drawOval(xi + D / 2 - h, xj + D / 2 - h, 2 * h, 2 * h);
					break;
				case Field.CROSS:
					g.drawLine(xi + D / 2 - h, xj + D / 2 - h, xi + D / 2 + h, xj + D / 2 + h);
					g.drawLine(xi + D / 2 + h, xj + D / 2 - h, xi + D / 2 - h, xj + D / 2 + h);
					break;
				case Field.TRIANGLE:
					g.drawLine(xi + D / 2, xj + D / 2 - h, xi + D / 2 - h, xj + D / 2 + h);
					g.drawLine(xi + D / 2, xj + D / 2 - h, xi + D / 2 + h, xj + D / 2 + h);
					g.drawLine(xi + D / 2 - h, xj + D / 2 + h, xi + D / 2 + h, xj + D / 2 + h);
					break;
				default :
					g.drawRect(xi + D / 2 - h, xj + D / 2 - h, 2 * h, 2 * h);
			}
		}
		if (P.letter(origi, origj) != 0) {
			//if (GF.bwColor()) {
			if (P.color(origi, origj) >= 0)
				g.setColor(Color.white);
			else
				g.setColor(Color.black);
			//} else
			//    g.setColor(GF.labelColor(P.color(origi, origj)));
			c[0] = (char) ('a' + P.letter(origi, origj) - 1);
			String hs = new String(c);
			int w = largeFontmetrics.stringWidth(hs) / 2;
			int h = largeFontmetrics.getAscent() / 2 - 2;
			g.setFont(largeFont);
			g.drawString(hs, xi + D / 2 - w, xj + D / 2 + h);
		} else if (P.haslabel(origi, origj)) {
			//if (GF.bwColor()) {
			if (P.color(origi, origj) >= 0)
				g.setColor(Color.white);
			else
				g.setColor(Color.black);
			//} else
			//    g.setColor(GF.labelColor(P.color(origi, origj)));
			String hs = P.label(origi, origj);
			int w = largeFontmetrics.stringWidth(hs) / 2;
			int h = largeFontmetrics.getAscent() / 2 - 2;
			g.setFont(largeFont);
			g.drawString(hs, xi + D / 2 - w, xj + D / 2 + h);
		} else if (P.tree(origi, origj) != null && !VHide) {
			drawVariation(g, origi, origj, xi, xj);
		}
		if (sendi == origi && sendj == origj) {
			if (GF.bwColor()) {
				if (P.color(origi, origj) > 0)
					g.setColor(Color.white);
				else
					g.setColor(Color.black);
			} else
				g.setColor(Color.gray);
			g.drawLine(xi + D / 2 - 1, xj + D / 2, xi + D / 2 + 1, xj + D / 2);
			g.drawLine(xi + D / 2, xj + D / 2 - 1, xi + D / 2, xj + D / 2 + 1);
		}
		if (lasti == origi && lastj == origj && showlast) {
			//if (GF.lastNumber() || Range > 0) {
			if (GF.lastNumber() || Range != 0) {
				if (P.color(origi, origj) > 0)
					g.setColor(Color.white);
				else
					g.setColor(Color.black);
				if (P.number(origi, origj) > 0) {
					//String hs = "" + P.number(origi, origj);
					int numToShow = getMoveNumberToShow(origi, origj);
					if (numToShow > 0) {
						String hs = String.valueOf(numToShow);
						int w = fontmetrics.stringWidth(hs) / 2;
						int h = fontmetrics.getAscent() / 2 - 1;
						g.setFont(font);
						g.drawString(hs, xi + D / 2 - w, xj + D / 2 + h);
					} else {
						drawLastMoveMark(g, origi, origj, xi, xj);
					}
				}
			} else {
				drawLastMoveMark(g, origi, origj, xi, xj);
			}
		} else
		if (P.color(origi, origj) != 0 && Range > 0 && lasti >= 0)
		{
			if (P.color(origi, origj) > 0)
				g.setColor(Color.white);
			else
				g.setColor(Color.black);
			if (P.number(origi, origj) > 0) {
				//String hs = "" + P.number(origi, origj);
				int numToShow = getMoveNumberToShow(origi, origj);
				if (numToShow > 0) {
					String hs = String.valueOf(numToShow);
					int w = fontmetrics.stringWidth(hs) / 2;
					int h = fontmetrics.getAscent() / 2 - 1;
					g.setFont(font);
					g.drawString(hs, xi + D / 2 - w, xj + D / 2 + h);
				}
			}
		}
	}

	private void drawVariation(Graphics g, int i, int j, int x, int y) {
		if (GF.bwColor()) {
			if (P.color(i, j) > 0)
				g.setColor(Color.white);
			else
				g.setColor(Color.black);
		} else
			g.setColor(Color.green);

		int length = (D / 6 + 1) * 2;
		g.fillRect(x + D / 2 - length / 2, y + D / 2 - 1, length, 2);
		g.fillRect(x + D / 2 - 1, y + D / 2 - length / 2, 2, length);
	}

	private void drawLastMoveMark(Graphics g, int i, int j, int x, int y) {
		if (GF.bwColor()) {
			if (P.color(i, j) > 0)
				g.setColor(Color.white);
			else
				g.setColor(Color.black);
		} else
			g.setColor(Color.red);

		int length = (D / 6 + 1) * 2;
		g.fillRect(x + D / 2 - length / 2, y + D / 2 - 1, length, 2);
		g.fillRect(x + D / 2 - 1, y + D / 2 - length / 2, 2, length);
	}

	public void copy()
	// copy the offscreen board to the screen
	{
		if (ActiveImage == null) return;
		try {
			getGraphics().drawImage(ActiveImage, 0, 0, this);
		} catch (Exception e) {
		}
	}

	public void undonode()
	// Undo everything that has been changed in the node.
	// (This will not correct the last move marker!)
	{
		Node n = Pos.node();
		ListElement p = n.lastchange();
		while (p != null) {
			Change c = (Change) p.content();
			P.color(c.I, c.J, c.C);
			P.number(c.I, c.J, c.N);
			update(c.I, c.J);
			p = p.previous();
		}
		n.clearchanges();
		Pw -= n.Pw;
		Pb -= n.Pb;
	}

	public void setaction(Node n, Action a, int c)
	// interpret a set move action, update the last move marker,
	// c being the color of the move.
	{
		String s = (String) (a.arguments().content());
		int i = Field.i(s);
		int j = Field.j(s);
		if (!valid(i, j)) return;
		lastNonPassMove = c;
		currentMoveNumber = n.number() - 1;
		n.addchange(new Change(i, j, P.color(i, j), P.number(i, j)));
		P.color(i, j, c);
		P.number(i, j, currentMoveNumber);
		showlast = false;
		update(lasti, lastj);
		showlast = true;
		lasti = i;
		lastj = j;
		update(i, j);
		P.color(-c);
		capture(i, j, n);
	}

	public void placeaction(Node n, Action a, int c)
	// interpret a set move action, update the last move marker,
	// c being the color of the move.
	{
		int i, j;
		ListElement larg = a.arguments();
		while (larg != null) {
			String s = (String) larg.content();
			i = Field.i(s);
			j = Field.j(s);
			if (valid(i, j)) {
				n.addchange(new Change(i, j, P.color(i, j), P.number(i, j)));
				P.color(i, j, c);
				update(i, j);
			}
			larg = larg.next();
		}
	}

	public void emptyaction(Node n, Action a)
	// interpret a remove stone action
	{
		int i, j, r = 1;
		ListElement larg = a.arguments();
		while (larg != null) {
			String s = (String) larg.content();
			i = Field.i(s);
			j = Field.j(s);
			if (valid(i, j)) {
				n.addchange(new Change(i, j, P.color(i, j), P.number(i, j)));
				if (P.color(i, j) < 0) {
					n.Pw++;
					Pw++;
				} else if (P.color(i, j) > 0) {
					n.Pb++;
					Pb++;
				}
				P.color(i, j, 0);
				update(i, j);
			}
			larg = larg.next();
		}
	}

	public void setnode()
	// interpret all actions of a node
	{
		Node n = Pos.node();
		ListElement p = n.actions();
		if (p == null) return;
		Action a;
		String s;
		int i, j;
		while (p != null) {
			a = (Action) (p.content());
			if (a.type().equals("SZ")) {
				if (Pos.parentPos() == null)
// only at first node
				{
					try {
						int ss = Integer.parseInt(a.argument().trim());
						if (ss != S) {
							S = ss;
							P = new Position(gameType, S);
							makeimages();
							updateall();
							copy();
						}
					} catch (NumberFormatException e) {
					}
				}
			}
			p = p.next();
		}
		n.clearchanges();
		n.Pw = n.Pb = 0;
		p = n.actions();
		while (p != null) {
			a = (Action) (p.content());
			if (a.type().equals("B")) {
				setaction(n, a, 1);
			} else if (a.type().equals("W")) {
				setaction(n, a, -1);
			}
			if (a.type().equals("AB")) {
				placeaction(n, a, 1);
			}
			if (a.type().equals("AW")) {
				placeaction(n, a, -1);
			} else if (a.type().equals("AE")) {
				emptyaction(n, a);
			}
			p = p.next();
		}
	}

	public void setlast()
	// update the last move marker applying all
	// set move actions in the node
	{
		Node n = Pos.node();
		ListElement l = n.actions();
		Action a;
		String s;
		int i = lasti, j = lastj;
		lasti = -1;
		lastj = -1;
		update(i, j);
		while (l != null) {
			a = (Action) (l.content());
			if (a.type().equals("B") || a.type().equals("W")) {
				s = (String) a.arguments().content();
				i = Field.i(s);
				j = Field.j(s);
				if (valid(i, j)) {
					lasti = i;
					lastj = j;
					update(lasti, lastj);
					P.color(-P.color(i, j));
				}
			}
			l = l.next();
		}
		number = n.number();
	}

	public void undo()
	// take back the last move, ask if necessary
	{    // System.out.println("undo");
		if (Pos.haschildren() ||
				(Pos.parent() != null &&
						Pos.parent().lastchild() != Pos.parent().firstchild() &&
						Pos == Pos.parent().firstchild())) {
			if (GF.askUndo()) doundo(Pos);
		} else
			doundo(Pos);
	}

	public void doundo(TreeNode pos1) {
		if (pos1 != Pos) return;
		if (Pos.parentPos() == null) {
			undonode();
			Pos.removeall();
			Pos.node().removeactions();
			showinformation();
			copy();
			return;
		}
		TreeNode pos = Pos;
		goback();
		if (pos == Pos.firstchild())
			Pos.removeall();
		else
			Pos.remove(pos);
		goforward();
		showinformation();
		copy();
	}

	public void goback()
	// go one move back
	{
		State = 1;
		if (Pos.parentPos() == null) return;
		undonode();
		Pos = Pos.parentPos();
		setlast();
	}

	public void goforward()
	// go one move forward
	{
		if (!Pos.haschildren()) return;
		Pos = Pos.firstChild();
		setnode();
		setlast();
	}

	public void gotoMove(int move) {
		if (move < 0) {
			allforward();
			return;
		}
		if (number < move) {
			while (number <= move && Pos.firstChild() != null) {
				goforward();
			}
		} else if (number > move) {
			while (number > move && Pos.parentPos() != null) {
				goback();
			}
		}
	}

	public void tovarleft() {
		ListElement l = Pos.listelement();
		if (l == null) return;
		if (l.previous() == null) return;
		TreeNode newpos = (TreeNode) l.previous().content();
		goback();
		Pos = newpos;
		setnode();
	}

	public void tovarright() {
		ListElement l = Pos.listelement();
		if (l == null) return;
		if (l.next() == null) return;
		TreeNode newpos = (TreeNode) l.next().content();
		goback();
		Pos = newpos;
		setnode();
	}

	public boolean hasvariation() {
		ListElement l = Pos.listelement();
		if (l == null) return false;
		if (l.next() == null) return false;
		return true;
	}

	public static TreeNode getNext(TreeNode p) {
		ListElement l = p.listelement();
		if (l == null) return null;
		if (l.next() == null) return null;
		return (TreeNode) l.next().content();
	}

	public void territory(int i, int j) {
		mark(i, j);
		copy();
	}

	public void setpass() {
		TreeNode p = T.top();
		while (p.haschildren()) p = p.firstChild();
		Node n = new Node(number);
		p.addchild(new TreeNode(n));
		n.main(p);
		GF.yourMove(Pos != p);
		if (Pos == p) {
			getinformation();
			int c = P.color();
			goforward();
			P.color(-c);
			showinformation();
			GF.addComment(GF.resourceString("Pass"));
		}
		MainColor = -MainColor;
		captured = 0;
	}

	public void resume()
	// Resume playing after marking
	{
		getinformation();
		State = 1;
		showinformation();
	}

	Node newtree() {
		number = 1;
		Pw = Pb = 0;
		Node n = new Node(number);
		T = new SGFTree(n);
		Trees.setElementAt(T, CurrentTree);
		resettree();
		return n;
	}

	void resettree() {
		Pos = T.top();
		P = new Position(gameType, S);
		lasti = lastj = -1;
		Pb = Pw = 0;
		updateall();
		copy();
	}

	public boolean deltree() {
		newtree();
		return true;
	}

	public void active(boolean f) {
		Active = f;
	}

	public int getboardsize() {
		return S;
	}

	public boolean canfinish() {
		return Pos.isLastMain();
	}

	public int maincolor() {
		return MainColor;
	}

	public boolean ismain() {
		return Pos.isLastMain();
	}

	Node firstnode() {
		return T.top().node();
	}

	boolean valid(int i, int j) {
		return (i >= 0 && i < S && j >= 0 && j < S);
	}

	public void clearrange() {
		//if (Range == -1) return;
		//Range = -1;
		Range = 0;
		updateall();
		copy();
	}

	// *****************************************
	// Methods to be called from outside sources
	// *****************************************

	// ****** navigational things **************

	// methods to move in the game tree, including
	// update of the visible board:

	public synchronized void back()
	// one move up
	{
		State = 1;
		getinformation();
		goback();
		showinformation();
		copy();
	}

	public synchronized void forward()
	// one move down
	{
		State = 1;
		getinformation();
		goforward();
		showinformation();
		copy();
	}

	public synchronized void fastback()
	// 10 moves up
	{
		getinformation();
		for (int i = 0; i < 10; i++)
			goback();
		showinformation();
		copy();
	}

	public synchronized void fastforward()
	// 10 moves down
	{
		getinformation();
		for (int i = 0; i < 10; i++)
			goforward();
		showinformation();
		copy();
	}

	public synchronized void allback()
	// to top of tree
	{
		getinformation();
		while (Pos.parentPos() != null) goback();
		showinformation();
		copy();
	}

	public synchronized void allforward()
	// to end of variation
	{
		getinformation();
		while (Pos.haschildren()) goforward();
		showinformation();
		copy();
	}

	private static String getComment(TreeNode pos) {
		ListElement la = pos.node().actions();
		Action a;
		String sc = "";
		while (la != null) // setup the marks and letters
		{
			a = (Action) la.content();
			if (a.type().equals("C")) {
				sc = (String) a.arguments().content();
			}
			la = la.next();
		}
		return sc;
	}

	/* to next comment
		*/
	public synchronized void commentForward() {
		getinformation();
		while (Pos.haschildren()) {
			goforward();
			String comment = getComment(Pos);
			if (comment != null && comment.length() > 0)
				break;
			if (Pos.parentPos().firstChild() != Pos.parentPos().lastChild())
				break;
		}
		showinformation();
		copy();
	}

	/* to previous comment
		*/
	public synchronized void commentBack() {
		getinformation();
		while (Pos != null && Pos.parentPos() != null) {
			goback();
			String comment = getComment(Pos);
			if (comment != null && comment.length() > 0)
				break;
			if (Pos == null || Pos.parentPos() == null || Pos.parentPos().firstChild() != Pos.parentPos().lastChild())
				break;
		}
		showinformation();
		copy();
	}

	public synchronized void varleft()
	// one variation to the left
	{
		State = 1;
		getinformation();
		ListElement l = Pos.listelement();
		if (l == null) return;
		if (l.previous() == null) return;
		TreeNode newpos = (TreeNode) l.previous().content();
		goback();
		Pos = newpos;
		setnode();
		showinformation();
		copy();
	}

	public synchronized void varright()
	// one variation to the right
	{
		State = 1;
		getinformation();
		ListElement l = Pos.listelement();
		if (l == null) return;
		if (l.next() == null) return;
		TreeNode newpos = (TreeNode) l.next().content();
		goback();
		Pos = newpos;
		setnode();
		showinformation();
		copy();
	}

	public synchronized void varmain()
	// to the main variation
	{
		State = 1;
		getinformation();
		while (Pos.parentPos() != null &&
				!Pos.node().main()) {
			goback();
		}
		if (Pos.haschildren()) goforward();
		showinformation();
		copy();
	}

	public synchronized void varmaindown()
	// to end of main variation
	{
		State = 1;
		getinformation();
		while (Pos.parentPos() != null &&
				!Pos.node().main()) {
			goback();
		}
		while (Pos.haschildren()) {
			goforward();
		}
		showinformation();
		copy();
	}

	public synchronized void varup()
	// to the start of the variation
	{
		State = 1;
		getinformation();
		if (Pos.parentPos() != null) goback();
		while (Pos.parentPos() != null &&
				Pos.parentPos().firstChild() == Pos.parentPos().lastChild()
				&& !Pos.node().main()) {
			goback();
		}
		showinformation();
		copy();
	}

	public synchronized void gotonext()
	// goto next named node
	{
		State = 1;
		getinformation();
		goforward();
		while (Pos.node().getaction("N").equals("")) {
			if (!Pos.haschildren()) break;
			goforward();
		}
		showinformation();
		copy();
	}

	public synchronized void gotoprevious()
	// gotoprevious named node
	{
		State = 1;
		getinformation();
		goback();
		while (Pos.node().getaction("N").equals("")) {
			if (Pos.parentPos() == null) break;
			goback();
		}
		showinformation();
		copy();
	}

	public synchronized void gotonextmain()
	// goto next game tree
	{
		if (CurrentTree + 1 >= Trees.size()) return;
		State = 1;
		getinformation();
		T.top().setaction("AP", "Daoqi:" + GF.version(), true);
		T.top().setaction("SZ", "" + S, true);
		T.top().setaction("GM", Defaults.SGF_GM_DAOQI, true);
		T.top().setaction("FF",
				GF.getParameter("puresgf", false) ? "4" : "1", true);
		CurrentTree++;
		T = (SGFTree) Trees.elementAt(CurrentTree);
		resettree();
		setnode();
		showinformation();
		copy();
	}

	public synchronized void gotopreviousmain()
	// goto previous game tree
	{
		if (CurrentTree == 0) return;
		State = 1;
		getinformation();
		T.top().setaction("AP", "Daoqi:" + GF.version(), true);
		T.top().setaction("SZ", "" + S, true);
		T.top().setaction("GM", Defaults.SGF_GM_DAOQI, true);
		T.top().setaction("FF",
				GF.getParameter("puresgf", false) ? "4" : "1", true);
		CurrentTree--;
		T = (SGFTree) Trees.elementAt(CurrentTree);
		resettree();
		setnode();
		showinformation();
		copy();
	}

	public synchronized void addnewgame() {
		State = 1;
		getinformation();
		T.top().setaction("AP", "Daoqi:" + GF.version(), true);
		T.top().setaction("SZ", "" + S, true);
		T.top().setaction("GM", Defaults.SGF_GM_DAOQI, true);
		T.top().setaction("FF",
				GF.getParameter("puresgf", false) ? "4" : "1", true);
		Node n = new Node(number);
		T = new SGFTree(n);
		CurrentTree++;
		if (CurrentTree >= Trees.size())
			Trees.addElement(T);
		else
			Trees.insertElementAt(T, CurrentTree);
		resettree();
		setnode();
		showinformation();
		copy();
	}

	public synchronized void removegame() {
		if (Trees.size() == 1) return;
		Trees.removeElementAt(CurrentTree);
		if (CurrentTree >= Trees.size()) CurrentTree--;
		T = (SGFTree) Trees.elementAt(CurrentTree);
		resettree();
		setnode();
		showinformation();
		copy();
	}

	// ***** change the node at end of main tree ********
	// usually called by another board or server

	public synchronized void black(int i, int j)
	// white move at i,j at the end of the main tree
	{
		if (i < 0 || j < 0 || i >= S || j >= S) return;
		TreeNode p = T.top();
		while (p.haschildren()) p = p.firstChild();
		Action a = new Action("B", Field.string(i, j));
		Node n = new Node(p.node().number() + 1);
		n.addaction(a);
		p.addchild(new TreeNode(n));
		n.main(p);
		GF.yourMove(Pos != p);
		if (Pos == p) forward();
		MainColor = -1;
	}

	public synchronized void white(int i, int j)
	// black move at i,j at the end of the main tree
	{
		if (i < 0 || j < 0 || i >= S || j >= S) return;
		TreeNode p = T.top();
		while (p.haschildren()) p = p.firstChild();
		Action a = new Action("W", Field.string(i, j));
		Node n = new Node(p.node().number() + 1);
		n.addaction(a);
		p.addchild(new TreeNode(n));
		n.main(p);
		GF.yourMove(Pos != p);
		if (Pos == p) forward();
		MainColor = 1;
	}

	public synchronized void setblack(int i, int j)
	// set a white stone at i,j at the end of the main tree
	{
		if (i < 0 || j < 0 || i >= S || j >= S) return;
		TreeNode p = T.top();
		while (p.haschildren()) p = (TreeNode) p.firstChild();
		Action a = new Action("AB", Field.string(i, j));
		Node n;
		if (p == T.top()) {
			TreeNode newpos;
			p.addchild(newpos = new TreeNode(1));
			if (Pos == p) Pos = newpos;
			p = newpos;
			p.main(true);
		}
		n = p.node();
		n.expandaction(a);
		if (Pos == p) {
			n.addchange(new Change(i, j, P.color(i, j), P.number(i, j)));
			P.color(i, j, 1);
			update(i, j);
			copy();
		}
		MainColor = -1;
	}

	public synchronized void setwhite(int i, int j)
	// set a white stone at i,j at the end of the main tree
	{
		if (i < 0 || j < 0 || i >= S || j >= S) return;
		TreeNode p = T.top();
		while (p.haschildren()) p = (TreeNode) p.firstChild();
		Action a = new Action("AW", Field.string(i, j));
		Node n;
		if (p == T.top()) {
			TreeNode newpos;
			p.addchild(newpos = new TreeNode(1));
			if (Pos == p) Pos = newpos;
			p = newpos;
			p.main(true);
		}
		n = p.node();
		n.expandaction(a);
		if (Pos == p) {
			n.addchange(new Change(i, j, P.color(i, j), P.number(i, j)));
			P.color(i, j, -1);
			update(i, j);
			copy();
		}
		MainColor = 1;
	}

	public synchronized void pass()
	// pass at current node
	// notify BoardInterface
	{
		if (Pos.haschildren()) return;
		if (GF.blocked() && Pos.node().main()) return;
		getinformation();
		P.color(-P.color());
		Node n = new Node(number);
		Pos.addchild(new TreeNode(n));
		n.main(Pos);
		goforward();
		setlast();
		showinformation();
		copy();
		GF.addComment(GF.resourceString("Pass"));
		captured = 0;
	}

	public synchronized void remove(int i0, int j0)
	// completely remove a group there
	{
		int s = State;
		varmaindown();
		State = s;
		if (P.color(i0, j0) == 0) return;
		Action a;
		P.markgroup(i0, j0);
		int i, j;
		int c = P.color(i0, j0);
		Node n = Pos.node();
		if (GF.getParameter("puresgf", true) &&
				(n.contains("B") || n.contains("W")))
			n = newnode();
		for (i = 0; i < S; i++)
			for (j = 0; j < S; j++) {
				if (P.marked(i, j)) {
					a = new Action("AE", Field.string(i, j));
					n.addchange(new Change(i, j, P.color(i, j), P.number(i, j)));
					n.expandaction(a);
					if (P.color(i, j) > 0) {
						n.Pb++;
						Pb++;
					} else {
						n.Pw++;
						Pw++;
					}
					P.color(i, j, 0);
					update(i, j);
				}
			}
		copy();
	}

	// ************ change the current node *****************

	public void clearmarks()
	// clear all marks in the current node
	{
		getinformation();
		undonode();
		ListElement la = Pos.node().actions(), lan;
		Action a;
		while (la != null) {
			a = (Action) la.content();
			lan = la.next();
			if (a.type().equals("M") || a.type().equals("L")
					|| a.type().equals("MA") || a.type().equals("SQ")
					|| a.type().equals("SL") || a.type().equals("CR")
					|| a.type().equals("TR") || a.type().equals("LB")) {
				Pos.node().removeaction(la);
			}
			la = lan;
		}
		setnode();
		showinformation();
		copy();
	}

	public void clearremovals()
	// undo all removals in the current node
	{
		if (Pos.haschildren()) return;
		getinformation();
		undonode();
		ListElement la = Pos.node().actions(), lan;
		Action a;
		while (la != null) {
			a = (Action) la.content();
			lan = la.next();
			if (a.type().equals("AB") || a.type().equals("AW")
					|| a.type().equals("AE")) {
				Pos.node().removeaction(la);
			}
			la = lan;
		}
		setnode();
		showinformation();
		copy();
	}

	// *************** change the game tree ***********

	public void insertnode()
	// insert an empty node
	{
		if (Pos.haschildren() && !GF.askInsert()) return;
		Node n = new Node(Pos.node().number());
		Pos.insertchild(new TreeNode(n));
		n.main(Pos);
		getinformation();
		Pos = Pos.lastChild();
		setlast();
		showinformation();
		copy();
	}

	public void insertvariation()
	// insert an empty variation to the current
	{
		if (Pos.parentPos() == null) return;
		getinformation();
		int c = P.color();
		back();
		Node n = new Node(2);
		Pos.addchild(new TreeNode(n));
		n.main(Pos);
		Pos = Pos.lastChild();
		setlast();
		P.color(-c);
		showinformation();
		copy();
	}

	public void undo(int n)
	// undo the n last moves, notify BoardInterface
	{
		varmaindown();
		for (int i = 0; i < n; i++) {
			goback();
			Pos.removeall();
			showinformation();
			copy();
		}
		GF.addComment(GF.resourceString("Undo"));
	}

	// ********** set board state ******************

	public void setblack()
	// set black mode
	{
		getinformation();
		State = 3;
		showinformation();
	}

	public void setwhite()
	// set white mode
	{
		getinformation();
		State = 4;
		showinformation();
	}

	public void black()
	// black to play
	{
		getinformation();
		State = 1;
		P.color(1);
		showinformation();
	}

	public void white()
	// white to play
	{
		getinformation();
		State = 2;
		P.color(-1);
		showinformation();
	}

	public void mark()
	// marking
	{
		getinformation();
		State = 5;
		showinformation();
	}

	public void specialmark(int i)
	// marking
	{
		getinformation();
		State = 9;
		SpecialMarker = i;
		showinformation();
	}

	public void textmark(String s)
	// marking
	{
		getinformation();
		State = 10;
		TextMarker = s;
		showinformation();
	}

	public void letter()
	// letter
	{
		getinformation();
		State = 6;
		showinformation();
	}

	public void deletestones()
	// hide stones
	{
		getinformation();
		State = 7;
		showinformation();
	}

	public boolean score()
	// board state is removing groups
	{
		if (Pos.haschildren()) return false;
		getinformation();
		State = 8;
		Removing = true;
		showinformation();
		if (Pos.node().main())
			return true;
		else
			return false;
	}

	synchronized public void setsize(int s)
	// set the board size
	// clears the board !!!
	{
		if (s < 1 || s > 40) return;
		S = s;
		P = new Position(gameType, S);
		number = 1;
		Node n = new Node(number);
		n.main(true);
		lasti = lastj = -1;
		T = new SGFTree(n);
		Trees.setElementAt(T, CurrentTree);
		Pos = T.top();
		makeimages();
		showinformation();
		copy();
	}

	// ******** set board information **********

	void setname(String s)
	// set the name of the node
	{
		Pos.setaction("N", s, true);
		showinformation();
	}

	public void setinformation(String black, String blackrank,
	                           String white, String whiterank,
	                           String komi, String handicap)
	// set various things like names, rank etc.
	{
		T.top().setaction("PB", black, true);
		T.top().setaction("BR", blackrank, true);
		T.top().setaction("PW", white, true);
		T.top().setaction("WR", whiterank, true);
		T.top().setaction("KM", komi, true);
		T.top().setaction("HA", handicap, true);
		T.top().setaction("GN", white + "W-" + black+"B", true);
		T.top().setaction("DT", new Date().toString());
	}

	// ************ get board information ******

	String getname()
	// get node name
	{
		return T.top().getaction("N");
	}

	public String getKomi()
	// get Komi string
	{
		return T.top().getaction("KM");
	}

	public String extraInformation()
	// get a mixture from handicap, komi and prisoners
	{
		StringBuffer b = new StringBuffer(GF.resourceString("_("));
		Node n = T.top().node();
		if (n.contains("HA")) {
			b.append(GF.resourceString("Ha_"));
			b.append(n.getaction("HA"));
		}
		if (n.contains("KM")) {
			b.append(GF.resourceString("__Ko"));
			b.append(n.getaction("KM"));
		}
		b.append(GF.resourceString("__B"));
		b.append("" + Pw);
		b.append(GF.resourceString("__W"));
		b.append("" + Pb);
		b.append(GF.resourceString("_)"));
		return b.toString();
	}

	// ***************** several other things ******

	public void print(Frame f)
	// print the board
	{
		Position p = new Position(P);
		// TODO handle logic change of 'range' in PrintBoard!
		PrintBoard PB = new PrintBoard(p, Range, f);
	}

	public void hidenumber() {
		//Range = -1;
		//KeepRange = false;
		Range = 0;
		updateall();
		copy();
	}

	public void lastrange(int n)
	// set the range for stone numbers
	{
		Range = n;
		//if (Range < 0) Range = 0;
		//KeepRange = true;
		updateall();
		copy();
	}

	/**
	 * @param s accepts these formats
	 *          "-1" show number on last move
	 *          "0" don't show number
	 *          "51=1" show number from 51st move, start number=1
	 */
	public boolean setShowNumber(String s) {
		if (s == null || s.trim().length() == 0) {
			System.out.println("setShowNumber(): invalid argument '" + s + "'");
			return false;
		}
		s = s.trim();
		int idx = s.indexOf(":");
		try {
			if (idx > 0) {
				Range = Integer.parseInt(s.substring(0, idx));
				if (Range > 0) {
					startNumber = Integer.parseInt(s.substring(idx + 1));
				} else {
					startNumber = 0;
				}
			} else {
				Range = Integer.parseInt(s);
				startNumber = 0;
			}
			return true;
		} catch (Exception e) {
			System.out.println("setShowNumber() exception: " + e.getMessage());
			return false;
		}
	}

	public void addcomment(String s)
	// add a string to the comments, notifies comment area
	{
		TreeNode p = T.top();
		while (p.haschildren()) p = p.firstChild();
		if (Pos == p) getinformation();
		ListElement la = p.node().actions();
		Action a;
		String Added = "";
		ListElement larg;
		outer:
		while (true) {
			while (la != null) {
				a = (Action) la.content();
				if (a.type().equals("C")) {
					larg = a.arguments();
					if (((String) larg.content()).equals("")) {
						larg.content(s);
						Added = s;
					} else {
						larg.content((String) larg.content() + "\n" + s);
						Added = "\n" + s;
					}
					break outer;
				}
				la = la.next();
			}
			p.addaction(new Action("C", s));
			break;
		}
		if (Pos == p) {
			GF.appendComment(Added);
			showinformation();
		}
	}

	public String done()
	// count territory and return result string
	// notifies BoardInterface
	{
		if (Pos.haschildren()) return null;
		clearmarks();
		getinformation();
		//GameResultTemp result = calcResult();
		GameResultInput input = prepareResultInput();
		GameResult result = Rule.getRule(GF.getCountRule()).determineResult(input);
		showinformation();
		copy();
		if (Pos.node().main()) {    // TODO: return different result if using different rule?
//GF.result(result.tb,result.tw);
		}
		return result.toLongString();
	}

	private GameResultInput prepareResultInput() {
		GameResultInput input = new GameResultInput();
		input.setTotal(S * S);
		if (currentMoveNumber > 0) {
			input.setTotalMoves(currentMoveNumber);
			input.setSharedLiberties(sharedLiberties);
			input.setBlackGroups(P.getBlackGroups());
			input.setWhiteGroups(P.getWhiteGroups());
			//input.setHandicaps();
			int bterritory = 0;
			int wterritory = 0;
			int blackStones = 0;
			int whiteStones = 0;
			P.getterritory();
			for (int i = 0; i < S; i++) {
				for (int j = 0; j < S; j++) {
					//System.out.print(P.territory(i,j));
					if (P.territory(i, j) == 1 || P.territory(i, j) == -1) {
						markterritory(i, j, P.territory(i, j));
						if (P.territory(i, j) > 0)
							bterritory++;
						else if (P.territory(i, j) < 0) wterritory++;
					} else {
						if (P.color(i, j) > 0)
							blackStones++;
						else if (P.color(i, j) < 0) whiteStones++;
					}
				}
				//System.out.println("");
			}

			input.setBlackStones(bterritory + blackStones);
			input.setWhiteStones(wterritory + whiteStones);
			input.setBlackMokus(bterritory + Pw);
			input.setWhiteMokus(wterritory + Pb);
			input.setNonOccupiedCount(input.getTotal() - input.getBlackStones() - input.getWhiteStones());
		}

		//System.out.println(input);
		return input;
	}

	/**
	 * maka a local count
	 */
	public GameResult docount() {
		clearmarks();
		getinformation();
		GameResultInput input = prepareResultInput();
		GameResult result = Rule.getRule(GF.getCountRule()).determineResult(input);
		showinformation();
		copy();
		return result;
	}

	public void load(BufferedReader in) throws IOException
	// load a game from the stream
	{
		Vector v = SGFTree.load(in, GF);
		synchronized (this) {
			if (v.size() == 0) return;
			showlast = false;
			update(lasti, lastj);
			showlast = true;
			lasti = lastj = -1;
			newtree();
			Trees = v;
			T = (SGFTree) v.elementAt(0);
			CurrentTree = 0;
			resettree();
			setnode();
			showinformation();
			copy();
		}
	}

	public void loadXml(XmlReader xml)
			throws XmlReaderException
	// load a game from the stream
	{
		Vector v = SGFTree.load(xml, GF);
		synchronized (this) {
			if (v.size() == 0) return;
			showlast = false;
			update(lasti, lastj);
			showlast = true;
			lasti = lastj = -1;
			newtree();
			Trees = v;
			T = (SGFTree) v.elementAt(0);
			CurrentTree = 0;
			resettree();
			setnode();
			showinformation();
			copy();
		}
	}

	public void save(PrintWriter o)
	// saves the file to the specified print stream
	// in SGF
	{
		getinformation();
		T.top().setaction("AP", "Daoqi:" + GF.version(), true);
		T.top().setaction("SZ", "" + S, true);
		T.top().setaction("GM", Defaults.SGF_GM_DAOQI, true);
		T.top().setaction("FF",
				GF.getParameter("puresgf", false) ? "4" : "1", true);
		for (int i = 0; i < Trees.size(); i++)
			((SGFTree) Trees.elementAt(i)).print(o);
	}

	public void savePos(PrintWriter o)
	// saves the file to the specified print stream
	// in SGF
	{
		getinformation();
		Node n = new Node(0);
		positionToNode(n);
		o.println("(");
		n.print(o);
		o.println(")");
	}

	public void saveXML(PrintWriter o, String encoding)
	// save the file in Jago's XML format
	{
		getinformation();
		T.top().setaction("AP", "Daoqi:" + GF.version(), true);
		T.top().setaction("SZ", "" + S, true);
		T.top().setaction("GM", Defaults.SGF_GM_DAOQI, true);
		T.top().setaction("FF",
				GF.getParameter("puresgf", false) ? "4" : "1", true);
		XmlWriter xml = new XmlWriter(o);
		xml.printEncoding(encoding);
		xml.printXls("go.xsl");
		xml.printDoctype("Go", "go.dtd");
		xml.startTagNewLine("Go");
		for (int i = 0; i < Trees.size(); i++) {
			((SGFTree) Trees.elementAt(i)).printXML(xml);
		}
		xml.endTagNewLine("Go");
	}

	public void saveXMLPos(PrintWriter o, String encoding)
	// save the file in Jago's XML format
	{
		getinformation();
		T.top().setaction("AP", "Daoqi:" + GF.version(), true);
		T.top().setaction("SZ", "" + S, true);
		T.top().setaction("GM", Defaults.SGF_GM_DAOQI, true);
		T.top().setaction("FF",
				GF.getParameter("puresgf", false) ? "4" : "1", true);
		XmlWriter xml = new XmlWriter(o);
		xml.printEncoding(encoding);
		xml.printXls("go.xsl");
		xml.printDoctype("Go", "go.dtd");
		xml.startTagNewLine("Go");
		Node n = new Node(0);
		positionToNode(n);
		SGFTree t = new SGFTree(n);
		t.printXML(xml);
		xml.endTagNewLine("Go");
	}

	public void asciisave(PrintWriter o)
	// an ASCII image of the board.
	{
		int i, j;
		o.println(T.top().getaction("GN"));
		o.print("      ");
		for (i = 0; i < S; i++) {
			char a;
			if (i <= 7)
				a = (char) ('A' + i);
			else
				a = (char) ('A' + i + 1);
			o.print(" " + a);
		}
		o.println();
		o.print("      ");
		for (i = 0; i < S; i++)
			o.print("--");
		o.println("-");
		for (i = 0; i < S; i++) {
			o.print("  ");
			if (S - i < 10)
				o.print(" " + (S - i));
			else
				o.print(S - i);
			o.print(" |");
			for (j = 0; j < S; j++) {
				switch (P.color(j, i)) {
					case 1:
						o.print(" #");
						break;
					case -1:
						o.print(" O");
						break;
					case 0:
						if (P.haslabel(j, i))
							o.print(" " + P.label(j, i));
						else if (P.letter(j, i) > 0)
							o.print(" " + (char) (P.letter(j, i) + 'a' - 1));
						else if (P.marker(j, i) > 0)
							o.print(" X");
						else if (ishand(i) && ishand(j))
							o.print(" ,");
						else
							o.print(" .");
						break;
				}
			}
			o.print(" | ");
			if (S - i < 10)
				o.print(" " + (S - i));
			else
				o.print(S - i);
			o.println();
		}
		o.print("      ");
		for (i = 0; i < S; i++)
			o.print("--");
		o.println("-");
		o.print("      ");
		for (i = 0; i < S; i++) {
			char a;
			if (i <= 7)
				a = (char) ('A' + i);
			else
				a = (char) ('A' + i + 1);
			o.print(" " + a);
		}
		o.println();
	}

	public void positionToNode(Node n)
	// copy the current position to a node.
	{
		n.setaction("AP", "Daoqi:" + GF.version(), true);
		n.setaction("SZ", "" + S, true);
		n.setaction("GM", Defaults.SGF_GM_DAOQI, true);
		n.setaction("FF",
				GF.getParameter("puresgf", false) ? "4" : "1", true);
		n.copyAction(T.top().node(), "GN");
		n.copyAction(T.top().node(), "DT");
		n.copyAction(T.top().node(), "PB");
		n.copyAction(T.top().node(), "BR");
		n.copyAction(T.top().node(), "PW");
		n.copyAction(T.top().node(), "WR");
		n.copyAction(T.top().node(), "PW");
		n.copyAction(T.top().node(), "US");
		n.copyAction(T.top().node(), "CP");
		int i, j;
		for (i = 0; i < S; i++) {
			for (j = 0; j < S; j++) {
				String field = Field.string(i, j);
				switch (P.color(i, j)) {
					case 1:
						n.expandaction(new Action("AB", field));
						break;
					case -1:
						n.expandaction(new Action("AW", field));
						break;
				}
				if (P.marker(i, j) > 0) {
					switch (P.marker(i, j)) {
						case Field.SQUARE:
							n.expandaction(new Action("SQ", field));
							break;
						case Field.TRIANGLE:
							n.expandaction(new Action("TR", field));
							break;
						case Field.CIRCLE:
							n.expandaction(new Action("CR", field));
							break;
						default:
							n.expandaction(new MarkAction(field, GF));
					}
				} else if (P.haslabel(i, j))
					n.expandaction(new Action("LB", field + ":" + P.label(i, j)));
				else if (P.letter(i, j) > 0)
					n.expandaction(new Action("LB", field + ":" + P.letter(i, j)));
			}
		}
	}

	boolean ishand(int i) {
		if (S > 13) {
			return (i == 3 || i == S - 4 || i == S / 2);
		} else if (S > 9) {
			return (i == 3 || i == S - 4);
		} else
			return false;
	}

	public void handicap(int n)
	// set number of handicap points
	{
		int h = (S < 13) ? 3 : 4;
		if (n > 5) {
			setblack(h - 1, S / 2);
			setblack(S - h, S / 2);
		}
		if (n > 7) {
			setblack(S / 2, h - 1);
			setblack(S / 2, S - h);
		}
		switch (n) {
			case 9:
			case 7:
			case 5:
				setblack(S / 2, S / 2);
			case 8:
			case 6:
			case 4:
				setblack(S - h, S - h);
			case 3:
				setblack(h - 1, h - 1);
			case 2:
				setblack(h - 1, S - h);
			case 1:
				setblack(S - h, h - 1);
		}
		MainColor = -1;
	}

	public void updateall()
	// update all of the board
	{
		if (ActiveImage == null) return;
		synchronized (this) {
			ActiveImage.getGraphics().drawImage(Empty, 0, 0, this);
		}
		int i, j;
		for (i = 0; i < S; i++)
			for (j = 0; j < S; j++)
				update(i, j);
		showinformation();
	}

	public void updateboard()
	// redraw the board and its background
	{
		BlackStone = WhiteStone = vbWhiteStone = null;
		EmptyShadow = null;
		setfonts();
		makeimages();
		updateall();
		copy();
	}

	/**
	 * Search the string as substring of a comment,
	 * go to that node and report success. On failure
	 * this routine will go up to the root node.
	 */
	public boolean search(String s) {
		State = 1;
		getinformation();
		TreeNode pos = Pos;
		boolean found = true;
		outer:
		while (Pos.node().getaction("C").indexOf(s) < 0 || Pos == pos) {
			if (!Pos.haschildren()) {
				while (!hasvariation()) {
					if (Pos.parent() == null) {
						found = false;
						break outer;
					} else
						goback();
				}
				tovarright();
			} else
				goforward();
		}
		showinformation();
		copy();
		return found;
	}

	Image getBoardImage() {
		return ActiveImage;
	}

	Dimension getBoardImageSize() {
		return new Dimension(ActiveImage.getWidth(this), ActiveImage.getHeight(this));
	}

	//*****************************************************
	// procedures that might be overloaded for more control
	// (Callback to server etc.)
	//*****************************************************

	public void movemouse(int i, int j)
	// set a move at i,j
	{
		if (Pos.haschildren()) return;
		if (captured == 1 && capturei == i && capturej == j &&
				GF.getParameter("preventko", true))
			return;
		try {
			if (!Global.isApplet())
				JagoSound.play("click", "click", false);
		} catch (Throwable e) {
			// ignore.
		}
		set(i, j); // try to set a new move
	}

	void setmouse(int i, int j, int c)
	// set a stone at i,j with specified color
	{
		set(i, j, c);
		undonode();
		setnode();
		showinformation();
	}

	void setmousec(int i, int j, int c)
	// set a stone at i,j with specified color
	{
		setc(i, j, c);
		undonode();
		setnode();
		showinformation();
	}

	void deletemouse(int i, int j)
	// delete a stone at i,j
	{
		if (Pos.haschildren()) return;
		deletemousec(i, j);
	}

	void deletemousec(int i, int j)
	// delete a stone at i,j
	{
		delete(i, j);
		undonode();
		setnode();
		showinformation();
	}

	void removemouse(int i, int j)
	// remove a group at i,j
	{
		if (Pos.haschildren()) return;
		removegroup(i, j);
		undonode();
		setnode();
		showinformation();
	}

	void setVariationStyle(boolean hide, boolean current) {
		undonode();
		VHide = hide;
		VCurrent = current;
		setnode();
		updateall();
		copy();
	}

	int getI(int origI, int origJ) {
		int i = 0;
		switch (rotate % 4) {
			case 1: // rotate 90 degree
				i = origJ + virtualBoardWidth + offsety;
				break;
			case 2: // rotate 180 degree
				i = S - 1 - origI + virtualBoardWidth - offsetx;
				break;
			case 3: // rotate 270 degree
				i = S - 1 - origJ + virtualBoardWidth - offsety;
				break;
			default:
				i = origI + virtualBoardWidth + offsetx;
				break;
		}
		return normalize(i);
	}

	int getJ(int origI, int origJ) {
		int j = 0;
		switch (rotate % 4) {
			case 1: // rotate 90 degree
				j = S - 1 - origI + virtualBoardWidth - offsetx;
				break;
			case 2: // rotate 180 degree
				j = S - 1 - origJ + virtualBoardWidth - offsety;
				break;
			case 3: // rotate 270 degree
				j = origI + virtualBoardWidth + offsetx;
				break;
			default:
				j = origJ + virtualBoardWidth + offsety;
				break;
		}
		return normalize(j);
	}

	int getOrigI(int i, int j) {
		int origI = 0;
		switch (rotate % 4) {
			case 1: // rotate 90 degree
				origI = S - 1 - j + virtualBoardWidth - offsetx;
				break;
			case 2: // rotate 180 degree
				origI = S - 1 - i + virtualBoardWidth - offsetx;
				break;
			case 3: // rotate 270 degree
				origI = j - virtualBoardWidth - offsetx;
				break;
			default:
				origI = i - virtualBoardWidth - offsetx;
				break;
		}
		return normalize(origI);
	}

	int getOrigJ(int i, int j) {
		int origJ = 0;
		switch (rotate % 4) {
			case 1: // rotate 90 degree
				origJ = i - virtualBoardWidth - offsety;
				break;
			case 2: // rotate 180 degree
				origJ = S - 1 - j + virtualBoardWidth - offsety;
				break;
			case 3: // rotate 270 degree
				origJ = S - 1 - i + virtualBoardWidth - offsety;
				break;
			default:
				origJ = j - virtualBoardWidth - offsety;
				break;
		}
		return normalize(origJ);
	}

	public int normalize(int i) {
		while (i < 0) i += S;
		while (i >= S) i -= S;
		return i;
	}

	public int boardSize() {
		return S;
	}

	public void setSharedLiberties(int sharedLiberties) {
		this.sharedLiberties = sharedLiberties;
	}

	public void setGameResult(GameResult result) {
		T.top().setaction("RE", result.toString());
	}

	public void rotateLeft() {
		rotate = (rotate + 1) % 4;
		updateboard();
	}

	public void rotateRight() {
		rotate = (rotate + 3) % 4;
		updateboard();
	}

	private String getVLabel(int idx) {
		if (rotate % 2 == 0) {
			return String.valueOf(idx + 1);
		} else {
			if (idx > 7)
				idx++;
			char a[] = new char[1];
			if (idx > 'Z' - 'A')
				a[0] = (char) ('a' + idx - ('Z' - 'A') - 1);
			else
				a[0] = (char) ('A' + idx);
			return new String(a);
		}
	}

	private String getHLabel(int idx) {
		if (rotate % 2 == 0) {
			if (idx > 7)
				idx++;
			char a[] = new char[1];
			if (idx > 'Z' - 'A')
				a[0] = (char) ('a' + idx - ('Z' - 'A') - 1);
			else
				a[0] = (char) ('A' + idx);
			return new String(a);
		} else {
			return String.valueOf(idx + 1);
		}
	}

	private String getLabel(int i, int j) {
		return Field.coordinate(i, S - 1 - j, S);
	}

	private void drag(int di, int dj) {
		switch (rotate % 4) {
			case 0:
				offsetx = normalize(offsetx + di);
				offsety = normalize(offsety + dj);
				break;
			case 1:
				offsetx = normalize(offsetx - dj);
				offsety = normalize(offsety + di);
				break;
			case 2:
				offsetx = normalize(offsetx - di);
				offsety = normalize(offsety - dj);
				break;
			case 3:
				offsetx = normalize(offsetx + dj);
				offsety = normalize(offsety - di);
				break;
		}
	}

	private void recenter(int i, int j) {
		switch (rotate % 4) {
			case 1:
				offsetx = normalize(S / 2 - i);
				offsety = normalize(S / 2 - j);
				break;
			case 2:
				offsetx = normalize(S / 2 - i);
				offsety = normalize(S / 2 - j);
				break;
			case 3:
				offsetx = normalize(S / 2 - i);
				offsety = normalize(S / 2 - j);
				break;
			default:
				offsetx = normalize(S / 2 - i);
				offsety = normalize(S / 2 - j);
				break;
		}
	}

	public void setVirtualBoardWidth(int virtualBoardWidth) {
		if (gameType == Global.WEIQI) return;
		this.virtualBoardWidth = virtualBoardWidth;
	}

	public int getNextMoveNumber() {
		return number;
	}

	public Move getMove(int moveNumber) {
		moveNumber += 2; // the incoming number starts from 0, but the number in node starts from 2
		if (moveNumber < 1) {
			return null;
		}
		TreeNode treeNode = T.top();
		Dump.println("T.top()=" + treeNode);
		int n = treeNode.node().number();
		while (treeNode != null && n < moveNumber) {
			treeNode = treeNode.firstChild();
			n ++;
		}
		if (treeNode == null)
			return null;
		Move move = new Move();
		move.setX(-1);
		move.setY(-1);
		Node node = treeNode.node();
		if (node.actions() != null) {
			Action action = (Action) node.actions().content();
			if ("b".equalsIgnoreCase(action.type())) {
				move.setColor(1);
				move.setX(Field.i(action.argument()));
				move.setY(Field.j(action.argument()));
			} else if ("w".equalsIgnoreCase(action.type())) {
				move.setColor(0);
				move.setX(Field.i(action.argument()));
				move.setY(Field.j(action.argument()));
			}
		}
		return move;
	}

	// setpass() doesn't increment number!
	public void changeMove(int moveNumber, int color, int i, int j) {
		moveNumber += 2;
		if (moveNumber < 1) {
			return;
		}
		TreeNode treeNode = T.top();
		Dump.println("T.top()=" + treeNode);
		int n = treeNode.node().number();
		while (treeNode != null && n < moveNumber) {
			treeNode = treeNode.firstChild();
			n ++;
		}
		if (treeNode == null)
			return;
		Node node = treeNode.node();
		node.removeactions();
		if (color > 0) {
			node.addaction(new Action("B", Field.string(i, j)));
		} else {
			node.addaction(new Action("W", Field.string(i, j)));
		}
		showinformation();
		copy();
	}
}