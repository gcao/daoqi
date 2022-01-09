/* Decompiled by IdeaJad 2169.2 ~ tagtraum industries incorporated ~ http://www.tagtraum.com/ */

import java.awt.*;

class Board extends Panel {

	String str1, str2;
	int state, dzn, ddx, ddy;
	Dimension r;
	wqp11 master;
	Font f1;
	int bwidth, bheight;
	ImagButton but1, but2, but3, but4;
	ImagButton but5, but6;

	Board(wqp11 wqp11_1) {
		master = wqp11_1;
		bwidth = (wqp11_1.w2 - 2 * wqp11_1.sb) / 6;
		bheight = wqp11_1.h4;
		str1 = "";
		str2 = "";
		state = 0;
		f1 = wqp11_1.f1;
		java.awt.Image image = wqp11_1.imagel1;
		Font font = new Font("\u4EFF\u5B8B_GB2312", 0, 12);
		Font font1 = new Font("times", 1, 16);
		but1 = new ImagButton(image, "a", 0, font1);
		but2 = new ImagButton(image, "b", 0, font1);
		but3 = new ImagButton(image, "c", 0, font1);
		but4 = new ImagButton(image, "d", 0, font1);
		but5 = new ImagButton(image, "e", 0, font1);
		but6 = new ImagButton(image, "\u8FD4", 0, font);
		but1.setForeground(wqp11_1.fcolorB);
		but1.setBackground(wqp11_1.bcolorB);
		but2.setForeground(wqp11_1.fcolorB);
		but2.setBackground(wqp11_1.bcolorB);
		but3.setForeground(wqp11_1.fcolorB);
		but3.setBackground(wqp11_1.bcolorB);
		but4.setForeground(wqp11_1.fcolorB);
		but4.setBackground(wqp11_1.bcolorB);
		but5.setForeground(wqp11_1.fcolorB);
		but5.setBackground(wqp11_1.bcolorB);
		but6.setForeground(wqp11_1.fcolorB);
		but6.setBackground(wqp11_1.bcolorB);
		but1.setSize(bwidth - 1, bheight - 1);
		but2.setSize(bwidth - 1, bheight - 1);
		but3.setSize(bwidth - 1, bheight - 1);
		but4.setSize(bwidth - 1, bheight - 1);
		but5.setSize(bwidth - 1, bheight - 1);
		but6.setSize(bwidth - 1, bheight - 1);
		but1.setVisible(false);
		but2.setVisible(false);
		but3.setVisible(false);
		but4.setVisible(false);
		but5.setVisible(false);
		but6.setVisible(false);
		add(but1);
		add(but2);
		add(but3);
		add(but4);
		add(but5);
		add(but6);
		dzn = 0;
	}

	public void paint(Graphics g) {
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		r = getSize();
		ddx = (r.width - 6 * bwidth) / 2;
		ddy = (r.height - bheight) / 2;
		int i = bheight / 4;
		but1.setLocation(ddx, ddy);
		but2.setLocation(ddx + bwidth, ddy);
		but3.setLocation(ddx + 2 * bwidth, ddy);
		but4.setLocation(ddx + 3 * bwidth, ddy);
		but5.setLocation(ddx + 4 * bwidth, ddy);
		but6.setLocation(ddx + 5 * bwidth, ddy);
		if (state == 0)
			if (!master.goPlate.bianhzt) {
				but1.setVisible(false);
				but2.setVisible(false);
				but3.setVisible(false);
				but4.setVisible(false);
				but5.setVisible(false);
				but6.setVisible(false);
				g.setColor(master.bcolorZ);
				g.fillRect(0, 0, r.width, r.height);
				FontMetrics fontmetrics = getFontMetrics(f1);
				g.setFont(f1);
				g.setColor(master.fcolorT);
				g.drawString(str1, (r.width - fontmetrics.stringWidth(str1)) / 2, r.height - 2 * i);
				return;
			}
			else {
				g.setColor(master.bcolorZ);
				g.fillRect(0, 0, r.width, r.height);
				g.setColor(master.fcolorT);
				g.drawString(master.goPlate.stringbh, ddx + bwidth, r.height - 2 * i);
				but1.setVisible(false);
				but2.setVisible(false);
				but3.setVisible(false);
				but4.setVisible(false);
				but5.setVisible(false);
				but6.setVisible(true);
				return;
			}
		g.setColor(master.bcolorZ);
		g.fillRect(0, 0, r.width, r.height);
		if (dzn == 2) {
			but1.setVisible(true);
			but2.setVisible(false);
			but3.setVisible(false);
			but4.setVisible(false);
			but5.setVisible(false);
			but6.setVisible(false);
			return;
		}
		if (dzn == 3) {
			but1.setVisible(true);
			but2.setVisible(true);
			but3.setVisible(false);
			but4.setVisible(false);
			but5.setVisible(false);
			but6.setVisible(false);
			return;
		}
		if (dzn == 4) {
			but1.setVisible(true);
			but2.setVisible(true);
			but3.setVisible(true);
			but4.setVisible(false);
			but5.setVisible(false);
			but6.setVisible(false);
			return;
		}
		if (dzn == 5) {
			but1.setVisible(true);
			but2.setVisible(true);
			but3.setVisible(true);
			but4.setVisible(true);
			but5.setVisible(false);
			but6.setVisible(false);
			return;
		}
		if (dzn >= 6) {
			but1.setVisible(true);
			but2.setVisible(true);
			but3.setVisible(true);
			but4.setVisible(true);
			but5.setVisible(true);
			but6.setVisible(false);
		}
	}

	public boolean action(Event event, Object obj) {
		if ("a".equals(obj)) {
			master.goPlate.gonode(master.goPlate.DZnode[1]);
			master.goPlate.goend();
			repaint();
			return true;
		}
		if ("b".equals(obj)) {
			master.goPlate.gonode(master.goPlate.DZnode[2]);
			master.goPlate.goend();
			repaint();
			return true;
		}
		if ("c".equals(obj)) {
			master.goPlate.gonode(master.goPlate.DZnode[3]);
			master.goPlate.goend();
			repaint();
			return true;
		}
		if ("d".equals(obj)) {
			master.goPlate.gonode(master.goPlate.DZnode[4]);
			master.goPlate.goend();
			repaint();
			return true;
		}
		if ("e".equals(obj)) {
			master.goPlate.gonode(master.goPlate.DZnode[5]);
			master.goPlate.goend();
			repaint();
			return true;
		}
		if ("\u8FD4".equals(obj)) {
			if (master.multButton.getLabel(5) == "\u6062\u590D")
				master.goPlate.huif();
			master.goPlate.resetDZ();
			if (master.goPlate.bianhn > 0)
				master.goPlate.bianhn--;
			master.goPlate.bianhzt = false;
			master.goPlate.stringbh = "";
			master.goPlate.gobackDZ();
			if (master.bhqssz)
				master.goPlate.stepNutrue = 1;
			else
				master.goPlate.stepNutrue = 0;
			but6.setVisible(false);
			repaint();
			if (master.goPlate.stepNutrue == 1)
				master.goPlate.repaint1();
			else
				master.goPlate.putcom(master.goPlate.step);
			return true;
		}
		else {
			return false;
		}
	}

	public void update(Graphics g) {
		paint(g);
	}
}