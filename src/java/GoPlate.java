/* Decompiled by IdeaJad 2169.2 ~ tagtraum industries incorporated ~ http://www.tagtraum.com/ */

import java.awt.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

class GoPlate extends Canvas {

	int cell_n, delta, half, org_x;
	int org_y, black, step, fast;
	int goCell[][], jgz[][];
	Dimension r;
	int stepNutrue, awn, abn, ABWN;
	public int recordl;
	byte ABi[], ABj[], AWi[], AWj[];
	int zhuanp, tzn, dem, pointtype;
	boolean hlxs, bdefault1, bdefault2, bdefault3;
	boolean boolcom, dzdraw, autorun, bianhzt;
	int htzs, btzs, first, ask1;
	String putstring, putstringold, GN, DT;
	String PC, PB, BR, PW;
	String WR, KM, RE, US;
	String SE, HA, EV, SO;
	String RO, TM, VW, SZ;
	String REstring, stringbh;
	byte tzi[], tzj[], stepi[], stepj[];
	ConTroler conTroler;
	wqp11 master;
	int ask2, autonumber, bianhn, nodel;
	File testfile;
	int bianhd[], nodef[], nodez[], nodex[];
	int nodecurrent, bnode, lastnode, TZn;
	byte nodei[], nodej[], TZi[], TZj[];
	int noded[], nodeTZn[], nodeTZp[], xtnode[];
	String nodeCOM[], nodeLB[], nodeSQ[], nodeTR[];
	String nodeCR[], nodeMA[], nodeRG[];
	int TZlast, xtnoden, DZn, dd1;
	int DZnode[];
	int dd2, i1, i2, j1;
	boolean first1;
	int j2, oi1, oi2, oj1;
	int oj2, wid, hei, halfa;
	int bottom, baizistep;

	GoPlate(ConTroler controler, wqp11 wqp11_1) {
		cell_n = 19;
		goCell = new int[cell_n][cell_n];
		recordl = 600;
		ABWN = 150;
		ABi = new byte[ABWN];
		ABj = new byte[ABWN];
		AWi = new byte[ABWN];
		AWj = new byte[ABWN];
		jgz = new int[cell_n][cell_n];
		dem = 2;
		pointtype = 2;
		hlxs = true;
		bdefault1 = true;
		bdefault2 = true;
		bdefault3 = true;
		boolcom = true;
		dzdraw = true;
		putstringold = "";
		GN = "";
		DT = "";
		PC = "";
		PB = "";
		BR = "";
		PW = "";
		WR = "";
		KM = "";
		RE = "";
		US = "";
		SE = "";
		HA = "";
		EV = "";
		SO = "";
		RO = "";
		TM = "";
		VW = "";
		SZ = "";
		REstring = "";
		tzi = new byte[100];
		tzj = new byte[100];
		autorun = false;
		autonumber = 4;
		stepi = new byte[recordl];
		stepj = new byte[recordl];
		bianhd = new int[20];
		bianhzt = false;
		nodel = 2000;
		bnode = -1;
		lastnode = -1;
		nodei = new byte[nodel];
		nodej = new byte[nodel];
		nodef = new int[nodel];
		nodez = new int[nodel];
		nodex = new int[nodel];
		noded = new int[nodel];
		nodeCOM = new String[nodel];
		nodeLB = new String[nodel];
		nodeSQ = new String[nodel];
		nodeTR = new String[nodel];
		nodeCR = new String[nodel];
		nodeMA = new String[nodel];
		nodeRG = new String[nodel];
		nodeTZn = new int[nodel];
		nodeTZp = new int[nodel];
		TZi = new byte[300];
		TZj = new byte[300];
		xtnode = new int[30];
		DZnode = new int[20];
		stringbh = "";
		baizistep = 1000;
		conTroler = controler;
		master = wqp11_1;
		testfile = new File("C:/kg/projects-b/gogame", "testfile.wqp");
		setBackground(wqp11_1.bcolorP);
		setForeground(wqp11_1.fcolorP);
		black = 1;
		org_x = 0;
		org_y = 0;
		for (int i = 0; i < cell_n; i++) {
			for (int j = 0; j < cell_n; j++)
				goCell[i][j] = 0;

		}

		if (wqp11_1.master.qptype.length() > 3) {
			if (wqp11_1.master.qptype.charAt(1) == 'p' || wqp11_1.master.qptype.charAt(1) == 'P')
				bdefault1 = true;
			else
				bdefault1 = false;
			if (wqp11_1.master.qptype.charAt(2) == 'c' || wqp11_1.master.qptype.charAt(2) == 'C')
				bdefault2 = true;
			else
				bdefault2 = false;
			if (wqp11_1.master.qptype.charAt(3) == 'x' || wqp11_1.master.qptype.charAt(2) == 'X')
				bdefault3 = true;
			else
				bdefault3 = false;
		}
		if (bdefault1)
			dem = 2;
		else
			dem = 3;
		if (bdefault2)
			pointtype = 2;
		else
			pointtype = 1;
		hlxs = bdefault3;
		first1 = true;
		oj1 = 0;
		oj2 = cell_n - 1;
		oi1 = 0;
		oi2 = cell_n - 1;
		wid = 69;
		hei = 75;
		halfa = 2;
	}

	public void drawMark(int i, int j, int k) {
		if (i < cell_n && i >= oi1 && i <= oi2 && j >= oj1 && j <= oj2) {
			if (goCell[i][j] == 0)
				drawEmbg(i, j);
			int l = i;
			int k1 = j;
			int l1 = l - oi1;
			int k2 = k1 - oj1;
			int l2 = org_x + halfa + delta * l1;
			int i3 = org_y + halfa + delta * k2;
			Graphics g = getGraphics();
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			int j3 = delta / 3;
			if (k == 0) {
				if (goCell[i][j] == 1)
					g.setColor(new Color(200, 200, 120));
				else
					g.setColor(new Color(100, 100, 50));
				int ai[] = new int[4];
				int ai3[] = new int[4];
				ai[0] = l2 + j3;
				ai3[0] = i3 + j3;
				ai[1] = (l2 + delta) - j3;
				ai3[1] = i3 + j3;
				ai[2] = (l2 + delta) - j3;
				ai3[2] = (i3 + delta) - j3;
				ai[3] = l2 + j3;
				ai3[3] = (i3 + delta) - j3;
				g.fillPolygon(ai, ai3, 4);
			} else if (k == 1) {
				j3 = delta / 3;
				if (goCell[i][j] == 1)
					g.setColor(new Color(200, 200, 120));
				else
					g.setColor(new Color(100, 100, 50));
				g.fillOval(l2 + j3, i3 + j3, delta - 2 * j3, delta - 2 * j3);
			} else if (k == 2) {
				j3 = (5 * delta) / 16;
				if (goCell[i][j] == 1)
					g.setColor(new Color(200, 200, 120));
				else if (goCell[i][j] == 0)
					g.setColor(new Color(150, 150, 60));
				else
					g.setColor(new Color(100, 100, 50));
				int ai1[] = new int[3];
				int ai4[] = new int[3];
				ai1[0] = l2 + delta / 2;
				ai4[0] = i3 + delta / 4;
				ai1[1] = l2 + delta / 4;
				ai4[1] = (i3 + delta) - delta / 4;
				ai1[2] = (l2 + delta) - delta / 4;
				ai4[2] = (i3 + delta) - delta / 4;
				g.fillPolygon(ai1, ai4, 3);
			} else if (k == 3) {
				if (goCell[i][j] == 1)
					g.setColor(new Color(200, 200, 120));
				else
					g.setColor(new Color(100, 100, 50));
				g.drawLine(l2 + j3, i3 + j3, (l2 + delta) - j3, (i3 + delta) - j3);
				g.drawLine(l2 + j3, (i3 + delta) - j3, (l2 + delta) - j3, i3 + j3);
				g.drawLine(l2 + j3, i3 + j3 + 1, (l2 + delta) - j3 - 1, (i3 + delta) - j3);
				g.drawLine(l2 + j3, (i3 + delta) - j3 - 1, (l2 + delta) - j3 - 1, i3 + j3);
				g.drawLine(l2 + j3 + 1, i3 + j3, (l2 + delta) - j3, (i3 + delta) - j3 - 1);
				g.drawLine(l2 + j3 + 1, (i3 + delta) - j3, (l2 + delta) - j3, i3 + j3 + 1);
			} else if (k == 4) {
				int k3 = delta / 4;
				if (goCell[i][j] == 1)
					g.setColor(new Color(200, 200, 120));
				else
					g.setColor(new Color(100, 100, 50));
				int ai2[] = new int[4];
				int ai5[] = new int[4];
				ai2[0] = l2 + delta / 2;
				ai5[0] = i3 + k3;
				ai2[1] = l2 + k3;
				ai5[1] = i3 + delta / 2;
				ai2[2] = (l2 + delta) - delta / 2;
				ai5[2] = (i3 + delta) - k3;
				ai2[3] = (l2 + delta) - k3;
				ai5[3] = (i3 + delta) - delta / 2;
				g.fillPolygon(ai2, ai5, 4);
			}
			g.dispose();
		}
	}

	public void drawPoint(int i, int j) {
		if (i < cell_n && stepNutrue != 1 && i >= oi1 && i <= oi2 && j >= oj1 && j <= oj2) {
			int k = i;
			int l = j;
			int k1 = k - oi1;
			int l1 = l - oj1;
			int k2 = org_x + halfa + delta * k1;
			int l2 = org_y + halfa + delta * l1;
			Graphics g = getGraphics();
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			if (pointtype == 0) {
				if (goCell[i][j] == 1)
					g.setColor(new Color(180, 180, 100));
				else
					g.setColor(new Color(90, 90, 50));
				g.drawOval(k2 + 1, l2 + 1, delta - 2, delta - 2);
				g.drawOval(k2 + 2, l2 + 2, delta - 4, delta - 4);
				g.drawOval(k2 + 3, l2 + 3, delta - 6, delta - 6);
			} else if (pointtype == 1) {
				if (goCell[i][j] == 1)
					g.setColor(new Color(250, 0, 0));
				else
					g.setColor(new Color(250, 0, 0));
				g.fillOval((k2 + delta / 2) - 3, (l2 + delta / 2) - 3, 6, 6);
			} else if (pointtype == 2) {
				if (goCell[i][j] == 1)
					g.setColor(new Color(250, 250, 250));
				else
					g.setColor(new Color(0, 0, 0));
				g.drawLine(k2 + delta / 4, l2 + delta / 4, (k2 + delta) - delta / 4, (l2 + delta) - delta / 4);
				g.drawLine(k2 + delta / 4, (l2 + delta) - delta / 4, (k2 + delta) - delta / 4, l2 + delta / 4);
			}
			g.dispose();
		}
	}

	public void paint(Graphics g) {
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		r = getSize();
		i1 = oi1;
		i2 = oi2;
		j1 = oj1;
		j2 = oj2;
		int i = (i2 - i1) + 1;
		int j = (j2 - j1) + 1;
		if (hlxs)
			i++;
		if (hlxs)
			j++;
		int k = 2 * halfa;
		int l = (r.width - k) / i;
		int k1 = (r.height - k) / j;
		delta = Math.min(l, k1);
		half = (int) Math.floor(delta / 2);
		if (r.width > delta * i) {
			org_x = (r.width - delta * i - k) / 2;
			g.setColor(master.getBackground());
			g.fillRect(0, 0, org_x, r.height);
			g.fillRect(org_x + k + delta * i + 1, 0, org_x + k, r.height);
		}
		if (r.height > delta * j) {
			org_y = (r.height - delta * j - k) / 2;
			g.setColor(master.getBackground());
			bottom = (r.height - org_y) + k + delta * j;
			g.fillRect(0, org_y + k + delta * j + 1, r.width, bottom);
			g.fillRect(0, 0, r.width, org_y);
		}
		g.setColor(Color.white);
		g.drawLine(org_x, org_y, org_x + delta * i + k, org_y);
		g.drawLine(org_x, org_y, org_x, org_y + delta * j + k);
		g.setColor(Color.black);
		g.drawLine(org_x + 1, org_y + delta * j + k, org_x + delta * i + k, org_y + delta * j + k);
		g.drawLine(org_x + delta * i + k, org_y + 1, org_x + delta * i + k, org_y + delta * j + k);
		if (hlxs)
			i--;
		if (hlxs)
			j--;
		for (int l1 = oi1; l1 < oi2 + 1; l1++) {
			for (int k2 = oj1; k2 < oj2 + 1; k2++)
				if (goCell[l1][k2] == 1)
					drawBlack(l1, k2);
				else if (goCell[l1][k2] == -1)
					drawWhite(l1, k2);
				else if (goCell[l1][k2] == 0)
					drawEmpty(l1, k2);

		}

		if (first1)
			drawhl();
		first1 = true;
		if (step > 0 && fast == 0 && master.filein == 1 && boolcom) {
			drawPoint(stepi[step], stepj[step]);
			putcom(step);
			return;
		}
		if (master.filein == 1)
			putcom(0);
	}

	public void drawhl() {
		if (hlxs) {
			char c = (char) (65 + i1);
			if (i1 > 7)
				c = (char) (65 + i1 + 1);
			for (int i = i1; i < i2 + 1; i++) {
				drawChs(i - i1, (j2 - j1) + 1, String.valueOf(c), 0);
				if (c == 'H')
					c++;
				c++;
			}

			for (int j = j1; j < j2 + 1; j++)
				drawChs((i2 - i1) + 1, j - j1, Integer.toString(cell_n - j), 1);

			drawChs((i2 - i1) + 1, (j2 - j1) + 1, "  ", 2);
		}
	}

	public void reset() {
		Graphics g = getGraphics();
		r = size();
		g.setColor(master.getBackground());
		g.fillRect(0, 0, r.width, r.height);
		g.dispose();
	}

	public void update(Graphics g) {
	}

	public void repaint1() {
		Graphics g = getGraphics();
		paint(g);
		g.dispose();
	}

	public boolean handleEvent(Event event) {
		switch (event.id) {
			case 501: // Event.MOUSE_DOWN
				return true;

			case 502: // Event.MOUSE_UP
				if (master.filein == 0)
					return true;
				int i = (int) Math.floor((event.x - org_x - 5) / delta);
				int j = (int) Math.floor((event.y - org_y - 5) / delta);
				if (i > oi2 - oi1) {
					if (hlxs && oj2 - j < DZn) {
						bianhd[bianhn] = step;
						bianhn++;
						boolcom = false;
						gonode(DZnode[oj2 - j]);
						boolcom = true;
						master.board.repaint();
						gonextDZ();
						if (stepNutrue == 1)
							repaint1();
					}
					return true;
				}
				if (j > oj2 - oj1)
					return true;
				int k = oi1;
				int l = oj1;
				int k1 = i + k;
				int l1 = j + l;
				if (master.zhaoz) {
					boolean flag = false;
					boolean flag2 = false;
					boolean flag3 = false;
					int i4 = nodecurrent;
					if (nodei[i4] == k1 && nodej[i4] == l1)
						flag3 = true;
					while (nodef[i4] >= 0) {
						i4 = nodef[i4];
						if (nodei[i4] == k1 && nodej[i4] == l1)
							flag2 = true;
					}
					for (int j4 = nodecurrent; nodez[j4] >= 0;) {
						j4 = nodez[j4];
						if (nodei[j4] == k1 && nodej[j4] == l1)
							flag = true;
					}

					if (!flag3)
						if (flag) {
							resetDZ();
							boolcom = false;
							while (goCell[k1][l1] == 0 && nodez[nodecurrent] > 0)
								gonext();
							goback();
							boolcom = true;
							master.zhaoz = false;
							gonext();
						} else if (flag2) {
							resetDZ();
							boolcom = false;
							while ((k1 != nodei[nodecurrent] || l1 != nodej[nodecurrent]) && nodef[nodecurrent] >= 0)
								goback();
							boolcom = true;
							master.zhaoz = false;
						}
					if (step > 0 && fast == 0 && boolcom)
						putcom(step);
					master.multButton.drawup8();
					master.zhaoz = false;
				} else if (bianhzt || goselect(k1, l1) < 0) {
					boolean flag1 = false;
					if (goCell[k1][l1] == 0) {
						if (black == 1) {
							goCell[k1][l1] = 1;
							qtz(k1, l1);
							if (tzn == 0 && qftz(k1, l1)) {
								flag1 = true;
								goCell[k1][l1] = 0;
							} else
							if (tzn == 1 && qftz(k1, l1) && nodeTZn[nodecurrent] == 1 && k1 == TZi[nodeTZp[nodecurrent]] && l1 == TZj[nodeTZp[nodecurrent]])
							{
								flag1 = true;
								goCell[k1][l1] = 0;
							} else {
								black = -1;
								drawDZ(nodecurrent, 0);
								drawCell(stepi[step], stepj[step]);
								stepi[step + 1] = (byte) k1;
								stepj[step + 1] = (byte) l1;
								step++;
								drawCell(k1, l1);
								drawPoint(k1, l1);
								bnode++;
								nodei[bnode] = (byte) k1;
								nodej[bnode] = (byte) l1;
								nodef[bnode] = nodecurrent;
								nodez[bnode] = -1;
								noded[bnode] = -1;
								nodex[bnode] = -1;
								if (nodez[nodecurrent] < 0 || master.multButton.getLabel(5) == "\u6062\u590D")
								{
									nodez[nodecurrent] = bnode;
								} else {
									int k2;
									for (k2 = nodez[nodecurrent]; noded[k2] > 0; k2 = noded[k2]) ;
									noded[k2] = bnode;
									nodex[bnode] = k2;
								}
								if (nodecurrent <= lastnode && nodex[bnode] <= lastnode) {
									xtnode[xtnoden] = bnode;
									xtnoden++;
								}
								nodecurrent = bnode;
								if (tzn > 0) {
									nodeTZn[nodecurrent] = tzn;
									nodeTZp[nodecurrent] = TZn;
									for (int l2 = 0; l2 < tzn; l2++) {
										TZi[TZn] = tzi[l2];
										TZj[TZn] = tzj[l2];
										TZn++;
									}

									int k3 = nodeTZn[nodecurrent];
									int k4 = nodeTZp[nodecurrent];
									for (int i5 = 0; i5 < k3; i5++) {
										if (black == 1)
											htzs++;
										else
											btzs++;
										goCell[TZi[k4 + i5]][TZj[k4 + i5]] = 0;
										if (step > 0 && fast == 0)
											drawCell(TZi[k4 + i5], TZj[k4 + i5]);
									}

								}
							}
						} else if (black == -1) {
							goCell[k1][l1] = -1;
							qtz(k1, l1);
							if (tzn == 0 && qftz(k1, l1)) {
								flag1 = true;
								goCell[k1][l1] = 0;
							} else
							if (tzn == 1 && qftz(k1, l1) && nodeTZn[nodecurrent] == 1 && k1 == TZi[nodeTZp[nodecurrent]] && l1 == TZj[nodeTZp[nodecurrent]])
							{
								flag1 = true;
								goCell[k1][l1] = 0;
							} else {
								black = 1;
								drawDZ(nodecurrent, 0);
								drawCell(stepi[step], stepj[step]);
								stepi[step + 1] = (byte) k1;
								stepj[step + 1] = (byte) l1;
								step++;
								drawCell(k1, l1);
								drawPoint(k1, l1);
								bnode++;
								nodei[bnode] = (byte) k1;
								nodej[bnode] = (byte) l1;
								nodef[bnode] = nodecurrent;
								nodez[bnode] = -1;
								noded[bnode] = -1;
								nodex[bnode] = -1;
								if (nodez[nodecurrent] < 0 || master.multButton.getLabel(5) == "\u6062\u590D")
								{
									nodez[nodecurrent] = bnode;
								} else {
									int i3;
									for (i3 = nodez[nodecurrent]; noded[i3] > 0; i3 = noded[i3]) ;
									noded[i3] = bnode;
									nodex[bnode] = i3;
								}
								if (nodecurrent <= lastnode) {
									xtnode[xtnoden] = bnode;
									xtnoden++;
								}
								nodecurrent = bnode;
								if (tzn > 0) {
									nodeTZn[nodecurrent] = tzn;
									nodeTZp[nodecurrent] = TZn;
									for (int j3 = 0; j3 < tzn; j3++) {
										TZi[TZn] = tzi[j3];
										TZj[TZn] = tzj[j3];
										TZn++;
									}

									int l3 = nodeTZn[nodecurrent];
									int l4 = nodeTZp[nodecurrent];
									for (int j5 = 0; j5 < l3; j5++) {
										if (black == 1)
											htzs++;
										else
											btzs++;
										goCell[TZi[l4 + j5]][TZj[l4 + j5]] = 0;
										if (step > 0 && fast == 0)
											drawCell(TZi[l4 + j5], TZj[l4 + j5]);
									}

								}
							}
						}
					} else {
						flag1 = true;
					}
					if (black == 0) {
						drawEmpty(k1, l1);
						goCell[k1][l1] = 0;
					}
					if (!flag1 && master.multButton.getLabel(5) != "\u6062\u590D") {
						master.multButton.setImage(1, 5);
						master.multButton.setLabel("\u6062\u590D", 5);
						if (ask2 == 0)
							ask2 = 1;
						baizistep = step;
					}
					if (step > 0 && fast == 0 && !flag1 && boolcom) {
						putcom(step);
						if (dd2 > 1 || nodeCOM[nodecurrent] != "")
							master.belldrip();
						else
							master.bellda();
					}
				}
				return true;
		}
		return false;
	}

	public void huif() {
		master.multButton.setImage(0, 5);
		master.multButton.setLabel("\u6570\u5B57", 5);
		for (int i = 0; i < xtnoden; i++)
			if (nodex[xtnode[i]] > 0)
				noded[nodex[xtnode[i]]] = -1;
			else if (nodef[xtnode[i]] > 0)
				nodez[nodef[xtnode[i]]] = -1;

		while (nodecurrent > lastnode)
			goback();
		dd1 = drawDZ(nodecurrent, 0);
		putcom(step);
		bnode = lastnode;
		TZn = TZlast;
		baizistep = 1000;
	}

	public boolean brz(int i, int j) {
		boolean flag = false;
		if (i == 0)
			flag = true;
		else if (goCell[i - 1][j] * black == -1)
			flag = true;
		else
			return false;
		if (j == 0)
			flag = true;
		else if (goCell[i][j - 1] * black == -1)
			flag = true;
		else
			return false;
		if (i == cell_n - 1)
			flag = true;
		else if (goCell[i + 1][j] * black == -1)
			flag = true;
		else
			return false;
		if (j == cell_n - 1)
			flag = true;
		else if (goCell[i][j + 1] * black == -1)
			flag = true;
		else
			return false;
		return flag;
	}

	public void qtz(int i, int j) {
		tzn = 0;
		for (int k = 0; k < cell_n; k++) {
			for (int l = 0; l < cell_n; l++)
				jgz[k][l] = 0;

		}

		for (int k1 = 0; k1 < 100; k1++) {
			tzi[k1] = 0;
			tzj[k1] = 0;
		}

		if (i > 0 && goCell[i - 1][j] * black == -1)
			qtzp(i - 1, j);
		if (j > 0 && goCell[i][j - 1] * black == -1 && jgz[i][j - 1] == 0)
			qtzp(i, j - 1);
		if (i < cell_n - 1 && goCell[i + 1][j] * black == -1 && jgz[i + 1][j] == 0)
			qtzp(i + 1, j);
		if (j < cell_n - 1 && goCell[i][j + 1] * black == -1 && jgz[i][j + 1] == 0)
			qtzp(i, j + 1);
	}

	public void qtzp(int i, int j) {
		int k = 0;
		int l = 0;
		int k1 = 1;
		byte abyte0[] = new byte[200];
		byte abyte1[] = new byte[200];
		byte abyte2[] = new byte[200];
		byte abyte3[] = new byte[200];
		byte byte0 = (byte) i;
		byte byte1 = (byte) j;
		jgz[byte0][byte1] = 1;
		int l1 = 0;
		abyte0[k] = byte0;
		abyte1[k] = byte1;
		k++;
		while (k1 > 0) {
			if (++l1 > 400) {
				l = 0;
				break;
			}
			if (jgz[byte0][byte1] == 1) {
				if (byte0 == 0) {
					jgz[byte0][byte1] = 2;
					continue;
				}
				if (jgz[byte0 - 1][byte1] > 0) {
					jgz[byte0][byte1] = 2;
					continue;
				}
				if (goCell[byte0 - 1][byte1] * black == -1) {
					jgz[byte0][byte1] = 2;
					byte0--;
					abyte0[k] = byte0;
					abyte1[k] = byte1;
					k++;
					jgz[byte0][byte1] = 1;
					k1++;
					continue;
				}
				if (goCell[byte0 - 1][byte1] * black == 1) {
					jgz[byte0][byte1] = 2;
					continue;
				}
				if (goCell[byte0 - 1][byte1] != 0)
					continue;
				for (int k2 = 0; k2 < k; k2++) {
					byte0 = abyte0[k2];
					byte1 = abyte1[k2];
					jgz[byte0][byte1] = 0;
				}

				l = 0;
				break;
			}
			if (jgz[byte0][byte1] == 2) {
				if (byte1 == 0) {
					jgz[byte0][byte1] = 3;
					continue;
				}
				if (jgz[byte0][byte1 - 1] > 0) {
					jgz[byte0][byte1] = 3;
					continue;
				}
				if (goCell[byte0][byte1 - 1] * black == -1) {
					jgz[byte0][byte1] = 3;
					byte1--;
					abyte0[k] = byte0;
					abyte1[k] = byte1;
					k++;
					jgz[byte0][byte1] = 1;
					k1++;
					continue;
				}
				if (goCell[byte0][byte1 - 1] * black == 1) {
					jgz[byte0][byte1] = 3;
					continue;
				}
				if (goCell[byte0][byte1 - 1] != 0)
					continue;
				for (int l2 = 0; l2 < k; l2++) {
					byte0 = abyte0[l2];
					byte1 = abyte1[l2];
					jgz[byte0][byte1] = 0;
				}

				l = 0;
				break;
			}
			if (jgz[byte0][byte1] == 3) {
				if (byte0 == cell_n - 1) {
					jgz[byte0][byte1] = 4;
					continue;
				}
				if (jgz[byte0 + 1][byte1] > 0) {
					jgz[byte0][byte1] = 4;
					continue;
				}
				if (goCell[byte0 + 1][byte1] * black == -1) {
					jgz[byte0][byte1] = 4;
					byte0++;
					abyte0[k] = byte0;
					abyte1[k] = byte1;
					k++;
					jgz[byte0][byte1] = 1;
					k1++;
					continue;
				}
				if (goCell[byte0 + 1][byte1] * black == 1) {
					jgz[byte0][byte1] = 4;
					continue;
				}
				if (goCell[byte0 + 1][byte1] != 0)
					continue;
				for (int i3 = 0; i3 < k; i3++) {
					byte0 = abyte0[i3];
					byte1 = abyte1[i3];
					jgz[byte0][byte1] = 0;
				}

				l = 0;
				break;
			}
			if (jgz[byte0][byte1] == 4) {
				if (byte1 == cell_n - 1) {
					jgz[byte0][byte1] = 5;
					continue;
				}
				if (jgz[byte0][byte1 + 1] > 0) {
					jgz[byte0][byte1] = 5;
					continue;
				}
				if (goCell[byte0][byte1 + 1] * black == -1) {
					jgz[byte0][byte1] = 5;
					byte1++;
					abyte0[k] = byte0;
					abyte1[k] = byte1;
					k++;
					jgz[byte0][byte1] = 1;
					k1++;
					continue;
				}
				if (goCell[byte0][byte1 + 1] * black == 1) {
					jgz[byte0][byte1] = 5;
					continue;
				}
				if (goCell[byte0][byte1 + 1] != 0)
					continue;
				for (int j3 = 0; j3 < k; j3++) {
					byte0 = abyte0[j3];
					byte1 = abyte1[j3];
					jgz[byte0][byte1] = 0;
				}

				l = 0;
				break;
			}
			if (jgz[byte0][byte1] == 5) {
				abyte2[l] = byte0;
				abyte3[l] = byte1;
				l++;
				k1--;
				if (k > 1) {
					k--;
					byte0 = abyte0[k - 1];
					byte1 = abyte1[k - 1];
				}
			}
		}
		if (l > 0) {
			for (int k3 = 0; k3 < l; k3++) {
				tzi[tzn] = abyte2[k3];
				tzj[tzn] = abyte3[k3];
				tzn++;
			}

		}
	}

	public boolean qftz(int i, int j) {
		black = -black;
		for (int k = 0; k < cell_n; k++) {
			for (int l = 0; l < cell_n; l++)
				jgz[k][l] = 0;

		}

		int k1 = 0;
		int l1 = 0;
		int k2 = 1;
		int ai[] = new int[200];
		int ai1[] = new int[200];
		int ai2[] = new int[200];
		int ai3[] = new int[200];
		int l2 = i;
		int i3 = j;
		jgz[l2][i3] = 1;
		int j3 = 0;
		ai[k1] = l2;
		ai1[k1] = i3;
		k1++;
		while (k2 > 0) {
			if (++j3 > 400) {
				l1 = 0;
				break;
			}
			if (jgz[l2][i3] == 1) {
				if (l2 == 0) {
					jgz[l2][i3] = 2;
					continue;
				}
				if (jgz[l2 - 1][i3] > 0) {
					jgz[l2][i3] = 2;
					continue;
				}
				if (goCell[l2 - 1][i3] * black == -1) {
					jgz[l2][i3] = 2;
					l2--;
					ai[k1] = l2;
					ai1[k1] = i3;
					k1++;
					jgz[l2][i3] = 1;
					k2++;
					continue;
				}
				if (goCell[l2 - 1][i3] * black == 1) {
					jgz[l2][i3] = 2;
					continue;
				}
				if (goCell[l2 - 1][i3] != 0)
					continue;
				for (int k3 = 0; k3 < k1; k3++) {
					l2 = ai[k3];
					i3 = ai1[k3];
					jgz[l2][i3] = 0;
				}

				l1 = -1;
				break;
			}
			if (jgz[l2][i3] == 2) {
				if (i3 == 0) {
					jgz[l2][i3] = 3;
					continue;
				}
				if (jgz[l2][i3 - 1] > 0) {
					jgz[l2][i3] = 3;
					continue;
				}
				if (goCell[l2][i3 - 1] * black == -1) {
					jgz[l2][i3] = 3;
					i3--;
					ai[k1] = l2;
					ai1[k1] = i3;
					k1++;
					jgz[l2][i3] = 1;
					k2++;
					continue;
				}
				if (goCell[l2][i3 - 1] * black == 1) {
					jgz[l2][i3] = 3;
					continue;
				}
				if (goCell[l2][i3 - 1] != 0)
					continue;
				for (int l3 = 0; l3 < k1; l3++) {
					l2 = ai[l3];
					i3 = ai1[l3];
					jgz[l2][i3] = 0;
				}

				l1 = -1;
				break;
			}
			if (jgz[l2][i3] == 3) {
				if (l2 == cell_n - 1) {
					jgz[l2][i3] = 4;
					continue;
				}
				if (jgz[l2 + 1][i3] > 0) {
					jgz[l2][i3] = 4;
					continue;
				}
				if (goCell[l2 + 1][i3] * black == -1) {
					jgz[l2][i3] = 4;
					l2++;
					ai[k1] = l2;
					ai1[k1] = i3;
					k1++;
					jgz[l2][i3] = 1;
					k2++;
					continue;
				}
				if (goCell[l2 + 1][i3] * black == 1) {
					jgz[l2][i3] = 4;
					continue;
				}
				if (goCell[l2 + 1][i3] != 0)
					continue;
				for (int i4 = 0; i4 < k1; i4++) {
					l2 = ai[i4];
					i3 = ai1[i4];
					jgz[l2][i3] = 0;
				}

				l1 = -1;
				break;
			}
			if (jgz[l2][i3] == 4) {
				if (i3 == cell_n - 1) {
					jgz[l2][i3] = 5;
					continue;
				}
				if (jgz[l2][i3 + 1] > 0) {
					jgz[l2][i3] = 5;
					continue;
				}
				if (goCell[l2][i3 + 1] * black == -1) {
					jgz[l2][i3] = 5;
					i3++;
					ai[k1] = l2;
					ai1[k1] = i3;
					k1++;
					jgz[l2][i3] = 1;
					k2++;
					continue;
				}
				if (goCell[l2][i3 + 1] * black == 1) {
					jgz[l2][i3] = 5;
					continue;
				}
				if (goCell[l2][i3 + 1] != 0)
					continue;
				for (int j4 = 0; j4 < k1; j4++) {
					l2 = ai[j4];
					i3 = ai1[j4];
					jgz[l2][i3] = 0;
				}

				l1 = -1;
				break;
			}
			if (jgz[l2][i3] == 5) {
				ai2[l1] = l2;
				ai3[l1] = i3;
				l1++;
				k2--;
				if (k1 > 1) {
					k1--;
					l2 = ai[k1 - 1];
					i3 = ai1[k1 - 1];
				}
			}
		}
		black = -black;
		boolean flag = false;
		if (l1 > 0)
			flag = true;
		return flag;
	}

	public void gonext() {
		master.firstdown = true;
		master.firstup = true;
		dd1 = 0;
		dd2 = 0;
		if (step >= 0 && fast == 0)
			resetLB();
		if (nodez[nodecurrent] >= 0 && step < recordl - 1) {
			if (step > 0)
				drawCell(nodei[nodecurrent], nodej[nodecurrent]);
			dd1 = drawDZ(nodecurrent, 0);
			nodecurrent = nodez[nodecurrent];
			step++;
			stepi[step] = nodei[nodecurrent];
			stepj[step] = nodej[nodecurrent];
			if (nodei[nodecurrent] != cell_n)
				if (black == 1) {
					goCell[nodei[nodecurrent]][nodej[nodecurrent]] = 1;
					if (fast == 0) {
						drawBlack(nodei[nodecurrent], nodej[nodecurrent]);
						drawPoint(nodei[nodecurrent], nodej[nodecurrent]);
					}
				} else if (black == -1) {
					goCell[nodei[nodecurrent]][nodej[nodecurrent]] = -1;
					if (fast == 0) {
						drawWhite(nodei[nodecurrent], nodej[nodecurrent]);
						drawPoint(nodei[nodecurrent], nodej[nodecurrent]);
					}
				}
			if (nodei[nodecurrent] == cell_n)
				nodeTZn[nodecurrent] = 0;
			if (nodeTZn[nodecurrent] == -1) {
				qtz(nodei[nodecurrent], nodej[nodecurrent]);
				nodeTZn[nodecurrent] = tzn;
				nodeTZp[nodecurrent] = TZn;
				for (int i = 0; i < tzn; i++) {
					TZi[TZn] = tzi[i];
					TZj[TZn] = tzj[i];
					TZn++;
				}

			}
			if (nodeTZn[nodecurrent] > 0) {
				int j = nodeTZn[nodecurrent];
				int k = nodeTZp[nodecurrent];
				for (int l = 0; l < j; l++) {
					if (black == 1)
						btzs++;
					else
						htzs++;
					goCell[TZi[k + l]][TZj[k + l]] = 0;
					if (step > 0 && fast == 0)
						drawCell(TZi[k + l], TZj[k + l]);
				}

			}
			black = -black;
			if (step > 0 && fast == 0 && boolcom) {
				if (nodeCOM[nodecurrent] != "" || dd2 > 1)
					master.belldrip();
				else
					master.bellda();
				putcom(step);
				return;
			}
		} else {
			if (step > 0 && fast == 0 && boolcom)
				putcom(step);
			master.multButton.getLabel(5);
			if (master.conTroler.autorun) {
				master.conTroler.autorun = false;
				master.multButton.setImage(0, 8);
				master.multButton.setLabel("\u540E\u9000", 1);
				master.multButton.setLabel("\u524D\u8FDB", 2);
				master.multButton.setLabel("\u81EA\u52A8", 8);
			}
		}
	}

	public int goselect(int i, int j) {
		dd1 = 0;
		dd2 = 0;
		int k = -1;
		if (nodez[nodecurrent] > 0)
			if (nodei[nodez[nodecurrent]] == i && nodej[nodez[nodecurrent]] == j) {
				k = nodez[nodecurrent];
			} else {
				for (int l = nodez[nodecurrent]; noded[l] > 0;) {
					l = noded[l];
					if (nodei[l] == i && nodej[l] == j)
						k = l;
				}

			}
		if (k > 0)
			gonode(k);
		return k;
	}

	public void gonode(int i) {
		if (i > 0) {
			resetLB();
			if (nodez[nodecurrent] >= 0) {
				if (nodecurrent > 0)
					drawCell(nodei[nodecurrent], nodej[nodecurrent]);
				dd1 = drawDZ(nodecurrent, 0);
				nodecurrent = i;
				step++;
				stepi[step] = nodei[nodecurrent];
				stepj[step] = nodej[nodecurrent];
				if (nodex[nodecurrent] > 0) {
					bianhd[bianhn] = step - 1;
					bianhn++;
					bianhzt = true;
					master.bhqssz = stepNutrue == 1;
					stepNutrue = 1;
					master.board.state = 1;
					if (nodex[nodex[nodecurrent]] == -1)
						stringbh = "\u53D8\u5316a";
					else if (nodex[nodex[nodex[nodecurrent]]] == -1)
						stringbh = "\u53D8\u5316b";
					else if (nodex[nodex[nodex[nodex[nodecurrent]]]] == -1)
						stringbh = "\u53D8\u5316c";
					else if (nodex[nodex[nodex[nodex[nodex[nodecurrent]]]]] == -1)
						stringbh = "\u53D8\u5316d";
					else if (nodex[nodex[nodex[nodex[nodex[nodex[nodecurrent]]]]]] == -1)
						stringbh = "\u53D8\u5316e";
					if (master.bhqssz)
						repaint1();
					master.board.repaint();
				}
				if (nodei[nodecurrent] != cell_n)
					if (black == 1) {
						goCell[nodei[nodecurrent]][nodej[nodecurrent]] = 1;
						if (fast == 0) {
							drawBlack(nodei[nodecurrent], nodej[nodecurrent]);
							drawPoint(nodei[nodecurrent], nodej[nodecurrent]);
						}
					} else if (black == -1) {
						goCell[nodei[nodecurrent]][nodej[nodecurrent]] = -1;
						if (fast == 0) {
							drawWhite(nodei[nodecurrent], nodej[nodecurrent]);
							drawPoint(nodei[nodecurrent], nodej[nodecurrent]);
						}
					}
				if (nodei[nodecurrent] == cell_n)
					nodeTZn[nodecurrent] = 0;
				if (nodeTZn[nodecurrent] == -1) {
					qtz(nodei[nodecurrent], nodej[nodecurrent]);
					nodeTZn[nodecurrent] = tzn;
					nodeTZp[nodecurrent] = TZn;
					for (int j = 0; j < tzn; j++) {
						TZi[TZn] = tzi[j];
						TZj[TZn] = tzj[j];
						TZn++;
					}

				}
				if (nodeTZn[nodecurrent] > 0) {
					int k = nodeTZn[nodecurrent];
					int l = nodeTZp[nodecurrent];
					for (int k1 = 0; k1 < k; k1++) {
						if (black == 1)
							btzs++;
						else
							htzs++;
						goCell[TZi[l + k1]][TZj[l + k1]] = 0;
						if (step > 0 && fast == 0)
							drawCell(TZi[l + k1], TZj[l + k1]);
					}

				}
				black = -black;
				if (step > 0 && fast == 0 && boolcom) {
					putcom(step);
					if (dd2 > 1 || nodeCOM[nodecurrent] != "") {
						master.belldrip();
						return;
					}
					master.bellda();
				}
			}
		}
	}

	public void goend() {
		boolcom = false;
		while (nodez[nodecurrent] != -1)
			gonext();
		boolcom = true;
		putcom(step);
	}

	public void gonextDZ() {
		gonext();
		boolcom = false;
		while (nodez[nodecurrent] != -1 && noded[nodez[nodecurrent]] == -1)
			gonext();
		boolcom = true;
		putcom(step);
	}

	public void gobackDZ() {
		if (step > 0) {
			goback();
			boolcom = false;
			while (nodef[nodecurrent] != -1 && noded[nodez[nodecurrent]] == -1)
				goback();
			boolcom = true;
			putcom(step);
		}
	}

	public void goback() {
		master.firstdown = true;
		master.firstup = true;
		if (baizistep == step && master.multButton.getLabel(5) == "\u6062\u590D") {
			huif();
			return;
		}
		if (step > 0 && fast == 0 && master.multButton.getLabel(5) != "\u6062\u590D")
			resetLB();
		if (step > 0 && nodef[nodecurrent] >= 0) {
			if (nodei[nodecurrent] < cell_n)
				goCell[nodei[nodecurrent]][nodej[nodecurrent]] = 0;
			if (fast == 0 && nodei[nodecurrent] < cell_n)
				drawCell(nodei[nodecurrent], nodej[nodecurrent]);
			if (fast == 0)
				dd1 = drawDZ(nodecurrent, 0);
			black = -black;
			if (nodeTZn[nodecurrent] > 0) {
				for (int i = 0; i < nodeTZn[nodecurrent]; i++) {
					if (black == -1) {
						goCell[TZi[nodeTZp[nodecurrent] + i]][TZj[nodeTZp[nodecurrent] + i]] = 1;
						htzs--;
					} else if (black == 1) {
						goCell[TZi[nodeTZp[nodecurrent] + i]][TZj[nodeTZp[nodecurrent] + i]] = -1;
						btzs--;
					}
					if (fast == 0)
						drawCell(TZi[nodeTZp[nodecurrent] + i], TZj[nodeTZp[nodecurrent] + i]);
				}

			}
			nodecurrent = nodef[nodecurrent];
			step--;
		}
		if (fast == 0 && boolcom)
			putcom(step);
	}

	public void readsgfFile(String s) {
		master.textArea1.setText("\r\n\r\n    \u6B63\u5728\u4E0B\u8F7D\u68CB\u8C31\r\n\r\n      " + s + "\r\n\r\n      \u8BF7\u7B49\u5019\u3002\u3002\u3002");
		int i = 0;
		byte abyte0[] = new byte[20000];
		for (int j = 0; j < 20000; j++)
			abyte0[j] = 0;

		try {
			URL url = new URL(s);
			URLConnection urlconnection = url.openConnection();
			java.io.InputStream inputstream = urlconnection.getInputStream();
			DataInputStream datainputstream = new DataInputStream(inputstream);
			byte byte0 = 0;
			byte0 = datainputstream.readByte();
			for (abyte0[0] = byte0; byte0 != -1; abyte0[i] = byte0) {
				byte0 = datainputstream.readByte();
				i++;
			}

			datainputstream.close();
		}
		catch (MalformedURLException _ex) {
		}
		catch (IOException _ex) {
		}
		if (i > 0) {
			String s1 = new String(abyte0, 0, i + 1);
			readString(s1);
			putstringold = "";
			for (int k = 0; k < cell_n; k++) {
				for (int l = 0; l < cell_n; l++)
					goCell[k][l] = 0;

			}

			for (int k1 = 0; k1 < abn; k1++)
				goCell[ABi[k1]][ABj[k1]] = 1;

			for (int l1 = 0; l1 < awn; l1++)
				goCell[AWi[l1]][AWj[l1]] = -1;

			if (BR != "" && (BR.charAt(1) == 'p' || BR.charAt(1) == 'P'))
				BR = BR.charAt(0) + "\u6BB5";
			if (WR != "" && (WR.charAt(1) == 'p' || WR.charAt(1) == 'P'))
				WR = WR.charAt(0) + "\u6BB5";
			if (BR.length() > 2 && (BR.charAt(2) == 'p' || BR.charAt(2) == 'P' || BR.charAt(2) == 'd'))
				BR = BR.charAt(0) + "\u6BB5";
			if (WR.length() > 2 && (WR.charAt(2) == 'p' || WR.charAt(2) == 'P' || WR.charAt(2) == 'd'))
				WR = WR.charAt(0) + "\u6BB5";
			if (VW.length() == 5) {
				oi1 = trans(VW.charAt(0));
				oj1 = trans(VW.charAt(1));
				oi2 = trans(VW.charAt(3));
				oj2 = trans(VW.charAt(4));
				if (oi1 > oi2) {
					int k2 = oi1;
					oi1 = oi2;
					oi2 = k2;
				}
				if (oj1 > oj2) {
					int l2 = oj1;
					oj1 = oj2;
					oj2 = l2;
				}
				while (oi2 - oi1 < 2)
					if (oi2 < cell_n - 1)
						oi2++;
					else
						oi1--;
				while (oj2 - oj1 < 2)
					if (oj2 < cell_n - 1)
						oj2++;
					else
						oj1--;
			} else {
				oi1 = 0;
				oj1 = 0;
				oi2 = cell_n - 1;
				oj2 = cell_n - 1;
			}
			fast = 0;
			master.filein = 1;
			boolcom = true;
			lastnode = bnode;
			step = 0;
			btzs = 0;
			htzs = 0;
			nodecurrent = 0;
			black = first;
			TZn = 0;
			fast = 1;
			bianhzt = false;
			if (master.bhqssz)
				stepNutrue = 1;
			else
				stepNutrue = 0;
			bianhn = 0;
			while (nodez[nodecurrent] >= 0)
				gonext();
			TZlast = TZn;
			if (master.bools)
				while (nodef[nodecurrent] >= 0)
					goback();
			fast = 0;
			reset();
			repaint1();
			putcom(step);
			return;
		} else {
			master.textArea1.setText("\r\n\r\n" + s + "\r\n\r\n       \u6B64\u8C31\u672A\u51C6\u5907\u597D\uFF0C\r\n   " +
					"    \u4E0B\u8F7D\u5931\u8D25\u3002\r\n\r\n   \u8BF7\u9000\u51FA\u91CD" +
					"\u65B0\u9009\u62E9\u68CB\u8C31\uFF01"
			);
			return;
		}
	}

	public void readString(String s) {
		for (; !s.endsWith(")"); s = s.substring(0, s.length() - 1)) ;
		for (; !s.startsWith("("); s = s.substring(1, s.length())) ;
		String s1 = "";
		boolean flag = false;
		for (int i = 0; i < s.length(); i++) {
			if (!flag) {
				if (s.charAt(i) != '\n' && s.charAt(i) != '\r' && s.charAt(i) != ' ' && s.charAt(i) != '\t')
					s1 = s1 + s.charAt(i);
			} else {
				s1 = s1 + s.charAt(i);
			}
			if (s.charAt(i) == '[')
				flag = true;
			if (s.charAt(i) == ']')
				flag = false;
		}

		s = s1;
		if (s.startsWith("(;B[") || s.startsWith("(;W"))
			s = "(;SZ[19]" + s.substring(1, s.length());
		first = 0;
		fast = 1;
		cell_n = 19;
		GN = "";
		DT = "";
		PC = "";
		PB = "";
		BR = "";
		PW = "";
		WR = "";
		KM = "";
		RE = "";
		US = "";
		SE = "";
		HA = "";
		EV = "";
		SO = "";
		RO = "";
		TM = "";
		VW = "";
		SZ = "";
		for (int j = 0; j < nodel; j++) {
			nodeCOM[j] = "";
			nodeLB[j] = "";
			nodeSQ[j] = "";
			nodeTR[j] = "";
			nodeCR[j] = "";
			nodeMA[j] = "";
			nodeRG[j] = "";
			nodeTZn[j] = -1;
			nodef[j] = -1;
			nodez[j] = -1;
			nodex[j] = -1;
			noded[j] = -1;
			nodeTZn[j] = -1;
			nodeTZp[j] = -1;
		}

		REstring = "";
		String s2 = "";
		String s3 = "";
		byte byte0 = -1;
		awn = 0;
		abn = 0;
		bnode = -1;
		int ai[] = new int[50];
		int k = 0;
		int ai1[] = new int[50];
		int l = 0;
		byte0 = 0;
		boolean flag1 = false;
		char c = 'a';
		byte byte1 = 97;
		byte byte2 = 97;
		int k1 = 0;
		c = s.charAt(k1);
		for (k1++; k1 < s.length() + 1;) {
			char c1;
			if (k1 < s.length())
				c1 = s.charAt(k1);
			else
				c1 = '\n';
			char c2;
			if (++k1 < s.length())
				c2 = s.charAt(k1);
			else
				c2 = '\n';
			if (!flag1) {
				if (c == '(' && c1 == ';') {
					bnode++;
					if (bnode != 0)
						nodez[bnode - 1] = bnode;
					nodef[bnode] = bnode - 1;
					nodex[bnode] = -1;
					noded[bnode] = -1;
					ai[k] = bnode - 1;
					k++;
					ai1[l] = bnode;
					l++;
					s2 = "";
					k1++;
					c1 = c2;
				} else if (c == ')' && c1 == '(' && c2 == ';') {
					bnode++;
					if (bnode != 0)
						nodez[bnode - 1] = -1;
					nodef[bnode] = ai[k - 1];
					nodex[bnode] = ai1[l - 1];
					noded[ai1[l - 1]] = bnode;
					ai1[l - 1] = bnode;
					noded[bnode] = -1;
					s2 = "";
					if (++k1 < s.length())
						c1 = s.charAt(k1);
					else
						c1 = '\n';
					k1++;
				} else if (c == ')' && c1 == ')') {
					noded[ai1[l - 1]] = -1;
					l--;
					k--;
				} else if (c == ')' && c1 == '\n') {
					l--;
					k--;
					nodez[bnode] = -1;
				} else if (c == ';') {
					bnode++;
					if (bnode != 0)
						nodez[bnode - 1] = bnode;
					if (bnode != 0)
						nodef[bnode] = bnode - 1;
					nodex[bnode] = -1;
					noded[bnode] = -1;
					s2 = "";
				} else if (c == '[') {
					flag1 = true;
					s3 = "";
				} else if (Character.isUpperCase(c))
					s2 = s2 + c;
			} else if (c != ']') {
				if (s2.equals("C"))
					s3 = s3 + c;
				else if (c != '\n' && c != '\r')
					s3 = s3 + c;
			} else {
				if (s2.equals("GN"))
					GN = s3;
				else if (s2.equals("DT"))
					DT = s3;
				else if (s2.equals("PC"))
					PC = s3;
				else if (s2.equals("PB"))
					PB = s3;
				else if (s2.equals("BR"))
					BR = s3;
				else if (s2.equals("PW"))
					PW = s3;
				else if (s2.equals("WR"))
					WR = s3;
				else if (s2.equals("KM"))
					KM = s3;
				else if (s2.equals("RE"))
					RE = s3;
				else if (s2.equals("US"))
					US = s3;
				else if (s2.equals("SE"))
					SE = s3;
				else if (s2.equals("HA"))
					HA = s3;
				else if (s2.equals("EV"))
					EV = s3;
				else if (s2.equals("SO"))
					SO = s3;
				else if (s2.equals("RO"))
					RO = s3;
				else if (s2.equals("TM"))
					TM = s3;
				else if (s2.equals("VW"))
					VW = s3;
				else if (s2.equals("SZ")) {
					SZ = s3;
					if (SZ != "")
						try {
							cell_n = Integer.parseInt(SZ);
						}
						catch (NumberFormatException _ex) {
						}
				} else if (s2.equals("C"))
					nodeCOM[bnode] = nodeCOM[bnode] + s3;
				else if (s2.equals("AB")) {
					if (s3.length() > 0) {
						ABi[abn] = trans(s3.charAt(0));
						ABj[abn] = trans(s3.charAt(1));
						abn++;
					}
				} else if (s2.equals("AW")) {
					if (s3.length() > 0) {
						AWi[awn] = trans(s3.charAt(0));
						AWj[awn] = trans(s3.charAt(1));
						awn++;
					}
				} else if (s2.equals("B") || s2.equals("W")) {
					if (first == 0)
						if (s2.equals("B"))
							first = 1;
						else
							first = -1;
					byte byte3 = (byte) cell_n;
					byte byte4 = (byte) cell_n;
					nodeCOM[bnode] = "\u672C\u624B\u662F\u7A7A\u624B\u3002";
					if (s3.length() == 2) {
						byte3 = trans(s3.charAt(0));
						byte4 = trans(s3.charAt(1));
						if (byte3 < cell_n)
							nodeCOM[bnode] = "";
					}
					nodei[bnode] = byte3;
					nodej[bnode] = byte4;
				} else if (s2.equals("LB")) {
					if (nodeLB[bnode] == null)
						nodeLB[bnode] = s3;
					else
						nodeLB[bnode] = nodeLB[bnode] + s3;
				} else if (s2.equals("SQ")) {
					if (nodeSQ[bnode] == null)
						nodeSQ[bnode] = s3;
					else
						nodeSQ[bnode] = nodeSQ[bnode] + s3;
				} else if (s2.equals("TR")) {
					if (nodeTR[bnode] == null)
						nodeTR[bnode] = s3;
					else
						nodeTR[bnode] = nodeTR[bnode] + s3;
				} else if (s2.equals("CR")) {
					if (nodeCR[bnode] == null)
						nodeCR[bnode] = s3;
					else
						nodeCR[bnode] = nodeCR[bnode] + s3;
				} else if (s2.equals("MA")) {
					if (nodeMA[bnode] == null)
						nodeMA[bnode] = s3;
					else
						nodeMA[bnode] = nodeMA[bnode] + s3;
				} else if (s2.equals("RG"))
					if (nodeRG[bnode] == null)
						nodeRG[bnode] = s3;
					else
						nodeRG[bnode] = nodeRG[bnode] + s3;
				if (c1 == '[') {
					c1 = s.charAt(k1);
					s3 = "";
					k1++;
					c = c1;
				} else {
					s2 = "";
					flag1 = false;
				}
			}
			c = c1;
		}

	}

	public byte trans(char c) {
		byte byte0 = 0;
		if (c == 'a')
			byte0 = 0;
		if (c == 'b')
			byte0 = 1;
		if (c == 'c')
			byte0 = 2;
		if (c == 'd')
			byte0 = 3;
		if (c == 'e')
			byte0 = 4;
		if (c == 'f')
			byte0 = 5;
		if (c == 'g')
			byte0 = 6;
		if (c == 'h')
			byte0 = 7;
		if (c == 'i')
			byte0 = 8;
		if (c == 'j')
			byte0 = 9;
		if (c == 'k')
			byte0 = 10;
		if (c == 'l')
			byte0 = 11;
		if (c == 'm')
			byte0 = 12;
		if (c == 'n')
			byte0 = 13;
		if (c == 'o')
			byte0 = 14;
		if (c == 'p')
			byte0 = 15;
		if (c == 'q')
			byte0 = 16;
		if (c == 'r')
			byte0 = 17;
		if (c == 's')
			byte0 = 18;
		if (c == 't')
			byte0 = (byte) cell_n;
		return byte0;
	}

	public void resetLB() {
		String s = nodeLB[nodecurrent];
		if (s != null) {
			for (int i = 0; i < s.length(); i += 4)
				drawCell(trans(s.charAt(i)), trans(s.charAt(i + 1)));

		}
		s = nodeSQ[nodecurrent];
		if (s != null) {
			for (int j = 0; j < s.length(); j += 2)
				drawCell(trans(s.charAt(j)), trans(s.charAt(j + 1)));

		}
		s = nodeTR[nodecurrent];
		if (s != null) {
			for (int k = 0; k < s.length(); k += 2)
				drawCell(trans(s.charAt(k)), trans(s.charAt(k + 1)));

		}
		s = nodeCR[nodecurrent];
		if (s != null) {
			for (int l = 0; l < s.length(); l += 2)
				drawCell(trans(s.charAt(l)), trans(s.charAt(l + 1)));

		}
		s = nodeMA[nodecurrent];
		if (s != null) {
			for (int k1 = 0; k1 < s.length(); k1 += 2)
				drawCell(trans(s.charAt(k1)), trans(s.charAt(k1 + 1)));

		}
		s = nodeRG[nodecurrent];
		if (s != null) {
			for (int l1 = 0; l1 < s.length(); l1 += 2)
				drawCell(trans(s.charAt(l1)), trans(s.charAt(l1 + 1)));

		}
	}

	public void putcom(int i) {
		dd2 = drawDZ(nodecurrent, 1);
		String s = nodeLB[nodecurrent];
		if (s != "") {
			for (int j = 0; j < s.length(); j += 4)
				drawCom(trans(s.charAt(j)), trans(s.charAt(j + 1)), String.valueOf(s.charAt(j + 3)), 0);

		}
		String s1 = "\u9ED1\u65B9\uFF1A" + PB + " " + BR;
		String s2 = "\u767D\u65B9\uFF1A" + PW + " " + WR;
		if (PB != "")
			putstr(s1, 0);
		else
			putstr("", 0);
		if (PW != "")
			putstr(s2, 1);
		if (PB == "" && GN + EV != "" && step > 0)
			putstr(GN + EV, 1);
		if (step == 0) {
			String s3 = "\u6BD4\u8D5B\u65E5\u671F\uFF1A" + DT;
			String s5 = GN + EV;
			String s7 = "\u6BD4\u8D5B\u5730\u70B9\uFF1A" + PC;
			String s9 = RE;
			if (RE.length() > 2) {
				if (RE.charAt(1) == '+' || RE.charAt(2) == '+')
					if (RE.charAt(0) == 'B') {
						if (RE.charAt(2) == 'R')
							s9 = "\u9ED1\u4E2D\u76D8\u80DC\u3002";
						else
							s9 = "\u9ED1\u80DC" + RE.substring(2, RE.length()) + "\u76EE";
					} else if (RE.charAt(2) == 'R')
						s9 = "\u767D\u4E2D\u76D8\u80DC\u3002";
					else
						s9 = "\u767D\u80DC" + RE.substring(2, RE.length()) + "\u76EE";
			} else if (RE.length() == 1)
				if (RE.charAt(0) == 'B')
					s9 = "\u9ED1\u80DC";
				else if (RE.charAt(0) == 'W')
					s9 = "\u767D\u80DC";
			REstring = "\u6BD4\u8D5B\u7ED3\u679C\uFF1A" + s9;
			if (s5 != "")
				putstr(s5, 1);
			if (s3.length() > 5)
				putstr(s3, 1);
			if (s7.length() > 5)
				putstr(s7, 1);
			if (RE != "")
				putstr(REstring, 1);
			int l2 = 0;
			for (int i3 = 1; i3 < lastnode + 1; i3++)
				if (nodeCOM[i3].length() > 0)
					l2++;

			String s10 = "";
			if (l2 > 0)
				s10 = "\u5171\u6709" + Integer.toString(l2) + "\u624B\u89E3\u8BF4";
			else
				s10 = "\u65E0\u89E3\u8BF4";
			if (l2 > 0)
				putstr(s10, 1);
			if (l2 == 6)
				master.textArea1.setText(nodeCOM[0] + nodeCOM[1]);
		}
		String s4 = "\u767D";
		if (black == -1)
			s4 = "\u9ED1";
		String s6 = s4 + "\u7B2C" + i + "\u624B ";
		if (htzs + btzs > 0 && master.board.state == 0)
			s6 = s6 + "\u6B7B\u5B50:\u25CF" + htzs + ",\u25CB" + btzs;
		if (step == 0)
			s6 = "";
		if (master.board.state == 0) {
			master.board.str1 = s6;
			master.board.repaint();
		}
		if (bianhzt) {
			master.board.str1 = stringbh;
			master.board.repaint();
		}
		if (nodeCOM[nodecurrent] != "") {
			putstr("", 1);
			putstr(nodeCOM[nodecurrent], 1);
		}
		if (DZn > 1)
			putstr("", 2);
		else if (nodez[nodecurrent] < 0) {
			if (bianhzt)
				putstr(" *\u8BF7\u7528\u201C\u8FD4\u56DE\u201D\u6216\u201C\u524D\u6CE8\u201D" +
						"\u56DE\u5230\u539F\u8C31"
						, 2);
			else if (RE == "") {
				putstr("    \u5171" + step + "\u624B\u3002", 2);
			} else {
				String s8 = RE;
				if (RE.length() > 2) {
					if (RE.charAt(1) == '+' || RE.charAt(2) == '+')
						if (RE.charAt(0) == 'B') {
							if (RE.charAt(2) == 'R')
								s8 = "\u9ED1\u4E2D\u76D8\u80DC\u3002";
							else
								s8 = "\u9ED1\u80DC" + RE.substring(2, RE.length()) + "\u76EE";
						} else if (RE.charAt(2) == 'R')
							s8 = "\u767D\u4E2D\u76D8\u80DC\u3002";
						else
							s8 = "\u767D\u80DC" + RE.substring(2, RE.length()) + "\u76EE";
				} else if (RE.length() == 1)
					if (RE.charAt(0) == 'B')
						s8 = "\u9ED1\u80DC";
					else if (RE.charAt(0) == 'W')
						s8 = "\u767D\u80DC";
				putstr("\n\r\u7ED3\u675F\uFF0C\u5171" + step + "\u624B\u3002" + s8, 2);
			}
		} else if (!bianhzt || nodex[nodecurrent] == -1 && noded[nodecurrent] == -1)
			putstr("", 2);
		else
			putstr("", 2);
		if (step > 0)
			drawPoint(nodei[nodecurrent], nodej[nodecurrent]);
		boolean flag;
		for (flag = false; bianhn > 0 && step <= bianhd[bianhn - 1]; flag = true)
			bianhn--;

		if (flag)
			repaint1();
		s = nodeSQ[nodecurrent];
		if (s != "") {
			for (int k = 0; k < s.length(); k += 2)
				drawMark(trans(s.charAt(k)), trans(s.charAt(k + 1)), 0);

		}
		s = nodeCR[nodecurrent];
		if (s != "") {
			for (int l = 0; l < s.length(); l += 2)
				drawMark(trans(s.charAt(l)), trans(s.charAt(l + 1)), 1);

		}
		s = nodeTR[nodecurrent];
		if (s != "") {
			for (int k1 = 0; k1 < s.length(); k1 += 2)
				drawMark(trans(s.charAt(k1)), trans(s.charAt(k1 + 1)), 2);

		}
		s = nodeMA[nodecurrent];
		if (s != "") {
			for (int l1 = 0; l1 < s.length(); l1 += 2)
				drawMark(trans(s.charAt(l1)), trans(s.charAt(l1 + 1)), 3);

		}
		s = nodeRG[nodecurrent];
		if (s != "") {
			for (int k2 = 0; k2 < s.length(); k2 += 2)
				drawMark(trans(s.charAt(k2)), trans(s.charAt(k2 + 1)), 4);

		}
	}

	public void putstr(String s, int i) {
		if (i == 0) {
			putstring = s;
			return;
		}
		if (i == 1)
			if (putstring == "") {
				putstring = s;
				return;
			} else {
				putstring = putstring + "\r\n" + s;
				return;
			}
		if (i == 2) {
			putstring = putstring + "\r\n" + s;
			if (!putstring.equals(putstringold) || step <= 0) {
				master.textArea1.setText(putstring);
				putstringold = putstring;
			}
			int j = master.textArea1.getCaretPosition();
			if (j > 180)
				master.textArea1.setCaretPosition(0);
			master.textArea1.requestFocus();
		}
	}

	public void drawCell(int i, int j) {
		if (i < cell_n) {
			if (goCell[i][j] == 1) {
				drawBlack(i, j);
				return;
			}
			if (goCell[i][j] == -1) {
				drawWhite(i, j);
				return;
			}
			if (goCell[i][j] == 0)
				drawEmpty(i, j);
		}
	}

	public int drawDZ(int i, int j) {
		int k = i;
		DZn = 0;
		if (nodez[k] != -1) {
			k = nodez[k];
			DZnode[DZn] = k;
			for (DZn++; noded[k] != -1; DZn++) {
				k = noded[k];
				DZnode[DZn] = k;
			}

		}
		if (fast == 0 && boolcom)
			if (DZn > 1) {
				master.board.dzn = DZn;
				master.board.state = 1;
				master.board.repaint();
			} else {
				if (dzdraw && DZn == 1 && bianhzt)
					if (j == 0)
						drawCell(nodei[DZnode[0]], nodej[DZnode[0]]);
					else
						drawMark(nodei[DZnode[0]], nodej[DZnode[0]], 2);
				master.board.dzn = DZn;
				master.board.state = 0;
				master.board.repaint();
			}
		return DZn;
	}

	public void resetDZ() {
		for (int i = 0; i < DZn; i++)
			drawCell(nodei[DZnode[i]], nodej[DZnode[i]]);

		if (bianhzt && nodez[nodecurrent] > 0)
			drawCell(nodei[nodez[nodecurrent]], nodej[nodez[nodecurrent]]);
	}

	public void drawBlack(int i, int j) {
		if (i >= oi1 && i <= oi2 && j >= oj1 && j <= oj2) {
			int k = i;
			int l = j;
			int k1 = k - oi1;
			int l1 = l - oj1;
			int k2 = org_x + halfa + delta * k1;
			int l2 = org_y + halfa + delta * l1;
			if (fast == 0) {
				Graphics g = getGraphics();
				((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setColor(getBackground());
				g.fillRect(k2, l2, delta, delta);
				g.setColor(Color.black);
				g.fillOval(k2 + 1, l2 + 1, delta - 2, delta - 2);
				if (dem == 3) {
					g.setColor(Color.gray);
					g.drawArc(k2 + 1, l2 + 1, delta - 2, delta - 2, 0, 270);
					g.setColor(Color.gray);
					g.drawArc(k2 + 4, l2 + 4, delta - 8, delta - 8, 100, 60);
					g.setColor(Color.gray);
					g.drawArc(k2 + 5, l2 + 5, delta - 10, delta - 10, 100, 60);
					g.setColor(Color.lightGray);
					g.drawArc(k2 + 5, l2 + 5, delta - 8, delta - 8, 130, 10);
				}
				g.setColor(Color.white);
				Font font = new Font("times", 0, delta / 2 - 1);
				FontMetrics fontmetrics = getFontMetrics(font);
				g.setFont(font);
				int i3 = getNu(i, j);
				if (bianhn > 0 && bianhzt)
					i3 -= bianhd[bianhn - 1];
				String s = Integer.toString(i3);
				if (i3 > 0 && stepNutrue == 1)
					g.drawString(s, k2 + (delta - fontmetrics.stringWidth(s)) / 2, (l2 + delta) - delta / 3);
				g.dispose();
			}
		}
	}

	public int getNu(int i, int j) {
		int k = 0;
		for (int l = 0; l < step + 1; l++)
			if (stepi[l] == (byte) i && stepj[l] == (byte) j)
				k = l;

		return k;
	}

	public void drawWhite(int i, int j) {
		if (i >= oi1 && i <= oi2 && j >= oj1 && j <= oj2) {
			int k = i;
			int l = j;
			int k1 = k - oi1;
			int l1 = l - oj1;
			int k2 = org_x + halfa + delta * k1;
			int l2 = org_y + halfa + delta * l1;
			if (fast == 0) {
				Graphics g = getGraphics();
				((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setColor(getBackground());
				g.fillRect(k2, l2, delta, delta);
				g.setColor(new Color(235, 235, 235));
				g.fillOval(k2 + 1, l2 + 1, delta - 2, delta - 2);
				if (dem == 2) {
					g.setColor(new Color(10, 10, 10));
					g.fillOval(k2 + 1, l2 + 1, delta - 2, delta - 2);
					g.setColor(new Color(235, 235, 235));
					g.fillOval(k2 + 2, l2 + 2, delta - 4, delta - 4);
					/*
					int r = (delta - 2) / 2;
					int x = k2 + 1 + r;
					int y = l2 + 1 + r;
					circle(x, y, r+1, g);
					*/
					/*
					g.setColor(new Color(10, 10, 10));
					g.drawOval(k2 + 1, l2 + 1, delta - 2, delta - 2);
					*/
				} else if (dem == 3) {
					g.setColor(Color.gray);
					g.drawOval(k2 + 1, l2 + 1, delta - 2, delta - 2);
					g.setColor(Color.lightGray);
					g.drawArc(k2 + 1, l2 + 1, delta - 2, delta - 2, 0, 270);
					g.setColor(Color.white);
					g.drawArc(k2 + 4, l2 + 4, delta - 8, delta - 8, 100, 60);
					g.setColor(Color.white);
					g.drawArc(k2 + 5, l2 + 5, delta - 10, delta - 10, 100, 60);
				}
				g.setColor(Color.black);
				Font font = new Font("times", 0, delta / 2 - 1);
				FontMetrics fontmetrics = getFontMetrics(font);
				g.setFont(font);
				int i3 = getNu(i, j);
				if (bianhn > 0 && bianhzt)
					i3 -= bianhd[bianhn - 1];
				String s = Integer.toString(i3);
				if (i3 > 0 && stepNutrue == 1)
					g.drawString(s, k2 + (delta - fontmetrics.stringWidth(s)) / 2, (l2 + delta) - delta / 3);
				g.dispose();
			}
		}
	}

	public void drawCom(int i, int j, String s, int k) {
		if (i >= oi1 && i <= oi2 && j >= oj1 && j <= oj2) {
			int l = i;
			int k1 = j;
			int l1 = l - oi1;
			int k2 = k1 - oj1;
			int l2 = org_x + halfa + delta * l1;
			int i3 = org_y + halfa + delta * k2;
			Graphics g = getGraphics();
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			int j3 = delta / 6;
			if (k == 0) {
				drawEmbg(i, j);
				Font font = new Font("times", 1, delta / 2 + 1);
				FontMetrics fontmetrics = getFontMetrics(font);
				g.setColor(master.fcolorP);
				g.setFont(font);
				g.drawString(s, l2 + (delta - fontmetrics.stringWidth(s)) / 2, (i3 + delta) - delta / 3);
			} else if (k == 1) {
				g.setColor(new Color(100, 130, 100));
				g.fillOval(l2 + j3, i3 + j3, delta - 2 * j3, delta - 2 * j3);
				g.setColor(new Color(255, 255, 255));
				g.drawOval(l2 + j3, i3 + j3, delta - 2 * j3, delta - 2 * j3);
				Font font1 = new Font("Aral", 1, delta - 3 * j3);
				FontMetrics fontmetrics1 = getFontMetrics(font1);
				g.setFont(font1);
				g.drawString(s, l2 + (delta - fontmetrics1.stringWidth(s)) / 2, i3 + fontmetrics1.getHeight() + j3 / 2);
			} else if (k == 2) {
				g.setColor(new Color(100, 130, 100));
				g.fillOval(l2 + j3, i3 + j3, delta - 2 * j3, delta - 2 * j3);
				g.setColor(new Color(255, 255, 255));
				g.drawOval(l2 + j3, i3 + j3, delta - 2 * j3, delta - 2 * j3);
			} else if (k == -1) {
				g.setColor(new Color(255, 255, 180));
				g.fillOval(l2 + j3, i3 + j3, delta - 2 * j3, delta - 2 * j3);
				g.setColor(new Color(0, 0, 0));
				g.drawOval(l2 + j3, i3 + j3, delta - 2 * j3, delta - 2 * j3);
				Font font2 = new Font("Aral", 1, delta - 3 * j3);
				FontMetrics fontmetrics2 = getFontMetrics(font2);
				g.setFont(font2);
				g.drawString(s, l2 + (delta - fontmetrics2.stringWidth(s)) / 2, i3 + fontmetrics2.getHeight() + j3 / 2);
			} else if (k == -2) {
				g.setColor(new Color(255, 255, 180));
				g.fillOval(l2 + j3, i3 + j3, delta - 2 * j3, delta - 2 * j3);
				g.setColor(new Color(0, 0, 0));
				g.drawOval(l2 + j3, i3 + j3, delta - 2 * j3, delta - 2 * j3);
			}
			g.dispose();
		}
	}

	public void drawChs(int i, int j, String s, int k) {
		int l = org_x + halfa + delta * i;
		int k1 = org_y + halfa + delta * j;
		Graphics g = getGraphics();
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(getBackground());
		g.fillRect(l, k1, delta, delta);
		Font font = new Font("\u4EFF\u5B8B_GB2312", 1, 14);
		FontMetrics fontmetrics = getFontMetrics(font);
		Font font1 = new Font("\u4EFF\u5B8B_GB2312", 0, 11);
		FontMetrics fontmetrics1 = getFontMetrics(font1);
		g.setColor(master.fcolorH);
		g.setFont(font);
		if (k == 0)
			g.drawString(s, l + (delta - fontmetrics.stringWidth(s)) / 2, k1 + fontmetrics.getHeight());
		else if (k == 1)
			g.drawString(s, l + 3 + (fontmetrics.stringWidth("12") - fontmetrics.stringWidth(s)) / 2, (k1 + (fontmetrics.getHeight() + delta) / 2) - 1);
		else if (k == 2) {
			g.setFont(font1);
			g.drawString(s, (l + delta) - fontmetrics1.stringWidth(s), (k1 + delta) - 2);
		}
		g.dispose();
	}

	public void drawEmpty(int i, int j) {
		if (i >= oi1 && i <= oi2 && j >= oj1 && j <= oj2) {
			int k = i;
			int l = j;
			int k1 = k - oi1;
			int l1 = l - oj1;
			int k2 = org_x + halfa + delta * k1;
			int l2 = org_y + halfa + delta * l1;
			int i3 = k2 + half;
			int j3 = l2 + half;
			int k3 = k2 + delta;
			int l3 = l2 + delta;
			Graphics g = getGraphics();
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(getBackground());
			g.fillRect(k2, l2, delta, delta);
			g.setColor(getForeground());
			if (k == 0 && l == 0) {
				g.drawLine(i3, j3, k3, j3);
				g.drawLine(i3, j3, i3, l3);
			} else if (k == 0 && l == cell_n - 1) {
				g.drawLine(i3, j3, k3, j3);
				g.drawLine(i3, l2, i3, j3);
			} else if (k == cell_n - 1 && l == 0) {
				g.drawLine(k2, j3, i3, j3);
				g.drawLine(i3, j3, i3, l3);
			} else if (k == cell_n - 1 && l == cell_n - 1) {
				g.drawLine(k2, j3, i3, j3);
				g.drawLine(i3, l2, i3, j3);
			} else if (k == 0) {
				g.drawLine(i3, j3, k3, j3);
				g.drawLine(i3, l2, i3, l3);
			} else if (k == cell_n - 1) {
				g.drawLine(k2, j3, i3, j3);
				g.drawLine(i3, l2, i3, l3);
			} else if (l == 0) {
				g.drawLine(k2, j3, k3, j3);
				g.drawLine(i3, j3, i3, l3);
			} else if (l == cell_n - 1) {
				g.drawLine(k2, j3, k3, j3);
				g.drawLine(i3, l2, i3, j3);
			} else {
				g.drawLine(k2, j3, k3, j3);
				g.drawLine(i3, l2, i3, l3);
			}
			if ((k == 3 || k == 15 || k == 9) && (l == 3 || l == 15 || l == 9) && cell_n == 19)
				g.fillOval(i3 - 2, j3 - 2, 5, 5);
			else if ((k == 3 || k == cell_n - 4) && (l == 3 || l == cell_n - 4) && cell_n < 19 && cell_n > 9)
				g.fillOval(i3 - 2, j3 - 2, 5, 5);
			else if ((k == 2 || k == cell_n - 3) && (l == 2 || l == cell_n - 3) && cell_n < 10 && cell_n > 6)
				g.fillOval(i3 - 2, j3 - 2, 5, 5);
			g.dispose();
		}
	}

	public void drawEmbg(int i, int j) {
		if (i >= oi1 && i <= oi2 && j >= oj1 && j <= oj2) {
			int k = i;
			int l = j;
			int k1 = k - oi1;
			int l1 = l - oj1;
			int k2 = org_x + halfa + delta * k1;
			int l2 = org_y + halfa + delta * l1;
			int i3 = k2 + half;
			int j3 = l2 + half;
			int k3 = k2 + delta;
			int l3 = l2 + delta;
			int i4 = delta / 3;
			Graphics g = getGraphics();
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(getBackground());
			g.fillRect(k2, l2, delta, delta);
			g.setColor(getForeground());
			if (k == 0 && l == 0) {
				g.drawLine(i3 + i4, j3, k3, j3);
				g.drawLine(i3, j3 + i4, i3, l3);
			} else if (k == 0 && l == cell_n - 1) {
				g.drawLine(i3 + i4, j3, k3, j3);
				g.drawLine(i3, l2, i3, j3 - i4);
			} else if (k == cell_n - 1 && l == 0) {
				g.drawLine(k2, j3, i3 - i4, j3);
				g.drawLine(i3, j3 + i4, i3, l3);
			} else if (k == cell_n - 1 && l == cell_n - 1) {
				g.drawLine(k2, j3, i3 - i4, j3);
				g.drawLine(i3, l2, i3, j3 - i4);
			} else if (k == 0) {
				g.drawLine(i3 + i4, j3, k3, j3);
				g.drawLine(i3, l2, i3, j3 - i4);
				g.drawLine(i3, j3 + i4, i3, l3);
			} else if (k == cell_n - 1) {
				g.drawLine(k2, j3, i3 - i4, j3);
				g.drawLine(i3, l2, i3, j3 - i4);
				g.drawLine(i3, j3 + i4, i3, l3);
			} else if (l == 0) {
				g.drawLine(k2, j3, i3 - i4, j3);
				g.drawLine(i3 + i4, j3, k3, j3);
				g.drawLine(i3, j3 + i4, i3, l3);
			} else if (l == cell_n - 1) {
				g.drawLine(k2, j3, i3 - i4, j3);
				g.drawLine(i3 + i4, j3, k3, j3);
				g.drawLine(i3, l2, i3, j3 - i4);
			} else {
				g.drawLine(k2, j3, i3 - i4, j3);
				g.drawLine(i3 + i4, j3, k3, j3);
				g.drawLine(i3, l2, i3, j3 - i4);
				g.drawLine(i3, j3 + i4, i3, l3);
			}
			g.dispose();
		}
	}

	public void drawBg(int i, int j) {
		Graphics g = getGraphics();
		g.setColor(getBackground());
		g.fillRect(i, j, delta, delta);
		g.dispose();
	}

	/*
	public void plotpoints(int x0, int y0, int x, int y, Graphics g) {
		g.drawLine(x0 + x, y0 + y, x0 + x, y0 + y);
		g.drawLine(x0 + y, y0 + x, x0 + y, y0 + x);
		g.drawLine(x0 + y, y0 - x, x0 + y, y0 - x);
		g.drawLine(x0 + x, y0 - y, x0 + x, y0 - y);
		g.drawLine(x0 - x, y0 - y, x0 - x, y0 - y);
		g.drawLine(x0 - y, y0 - x, x0 - y, y0 - x);
		g.drawLine(x0 - y, y0 + x, x0 - y, y0 + x);
		g.drawLine(x0 - x, y0 + y, x0 - x, y0 + y);
	}

	// Circle is just Bresenham's algorithm for a scan converted circle
	public void circle(int x0, int y0, int r, Graphics g) {
		int x, y;
		float d;
		x = 0;
		y = r;
		d = 5 / 4 - r;
		plotpoints(x0, y0, x, y, g);

		while (y > x) {
			if (d < 0) {
				d = d + 2 * x + 3;
				x++;
			} else {
				d = d + 2 * (x - y) + 5;
				x++;
				y--;
			}
			plotpoints(x0, y0, x, y, g);
		}
	}
	*/
}