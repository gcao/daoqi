/* Decompiled by IdeaJad 2169.2 ~ tagtraum industries incorporated ~ http://www.tagtraum.com/ */

import java.awt.*;

class MultButton extends Canvas {

	Image imagel3, imagel4, imagestop, imager4;
	Image image1, image2, image3, image4;
	Image image5, image6, image7, image8;
	int clickCount, type1, type2, type3;
	String str1, str2, str3, str4;
	String str5, str6, str7, str8;
	int type4, type5, type6, type7;
	int type8, orgx0, orgy0, ww;
	int hh, widthstr, heightstr, widthimag;
	Dimension r;
	int heightimag, widthbutton, heightbutton, downumber;
	Font f1;
	FontMetrics fm;
	wqp11 master;
	Color bcolor;

	MultButton(wqp11 wqp11_1, String s, String s1, String s2, String s3, String s4, String s5, 
			String s6, String s7, int i, int j, int k, int l, int i1, 
			int j1, int k1, int l1) {
		clickCount = 1;
		master = wqp11_1;
		image1 = wqp11_1.imagel1;
		image2 = wqp11_1.imager1;
		image3 = wqp11_1.imagel2;
		image4 = wqp11_1.imager2;
		image5 = wqp11_1.imagel3;
		image6 = wqp11_1.imager3;
		image7 = wqp11_1.imageopen;
		image8 = wqp11_1.imagecross;
		imagel3 = wqp11_1.imagel3;
		imagel4 = wqp11_1.imagel4;
		str1 = s;
		str2 = s1;
		str3 = s2;
		str4 = s3;
		str5 = s4;
		str6 = s5;
		str7 = s6;
		str8 = s7;
		type1 = i;
		type2 = j;
		type3 = k;
		type4 = l;
		type5 = i1;
		type6 = j1;
		type7 = k1;
		type8 = l1;
		bcolor = wqp11_1.bcolorB;
		setForeground(wqp11_1.fcolorB);
		f1 = new Font("\u4EFF\u5B8B_GB2312", 0, 12);
		fm = getFontMetrics(f1);
		widthstr = Math.max(Math.max(Math.max(Math.max(Math.max(Math.max(Math.max(fm.stringWidth(s), fm.stringWidth(s1)), fm.stringWidth(s2)), fm.stringWidth(s3)), fm.stringWidth(s4)), fm.stringWidth(s5)), fm.stringWidth(s6)), fm.stringWidth(s7));
		heightstr = fm.getHeight();
		widthimag = 20;
		heightimag = 20;
		widthbutton = (wqp11_1.w2 - wqp11_1.sb) / 2;
		heightbutton = wqp11_1.h5 / 4;
		ww = widthbutton * 2;
		hh = heightbutton * 4;
	}

	public void paint(Graphics g) {
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g.setFont(f1);
		r = getSize();
		orgx0 = (r.width - 2 * widthbutton) / 2;
		orgy0 = (r.height - 4 * heightbutton) / 2;
		g.setColor(master.getBackground());
		g.fillRect(0, 0, r.width, r.height);
		drawup(g, orgx0, orgy0, image1, str1, type1);
		drawup(g, orgx0 + widthbutton, orgy0, image2, str2, type2);
		drawup(g, orgx0, orgy0 + heightbutton, image3, str3, type3);
		drawup(g, orgx0 + widthbutton, orgy0 + heightbutton, image4, str4, type4);
		drawup(g, orgx0, orgy0 + 2 * heightbutton, image5, str5, type5);
		drawup(g, orgx0 + widthbutton, orgy0 + 2 * heightbutton, image6, str6, type6);
		drawup(g, orgx0, orgy0 + 3 * heightbutton, image7, str7, type7);
		drawup(g, orgx0 + widthbutton, orgy0 + 3 * heightbutton, image8, str8, type8);
	}

	public void ttpaint() {
		Graphics g = getGraphics();
		paint(g);
		g.dispose();
	}

	public void drawup8() {
		Graphics g = getGraphics();
		drawup(g, orgx0 + widthbutton, orgy0 + 3 * heightbutton, image8, str8, type8);
		g.dispose();
	}

	protected void drawup(Graphics g, int i, int j, Image image, String s, int k) {
		int l = fm.stringWidth(s);
		int i1 = i + (widthbutton - l) / 2;
		int j1 = j + (heightbutton - (heightbutton - heightstr) / 2);
		int k1 = i + (widthbutton - 20) / 2;
		int l1 = j + (heightbutton - 20) / 2;
		int i2 = i + (widthbutton - l - 20) / 2 + 5;
		int j2 = j + (heightbutton - (heightbutton - heightstr) / 2);
		int k2 = i + l + (widthbutton - l - 20) / 2 + 5;
		int l2 = j + (heightbutton - 20) / 2;
		int i3 = i + (widthbutton - l - 20) / 2;
		int j3 = j + (heightbutton - 20) / 2;
		int k3 = i + 20 + (widthbutton - l - 20) / 2 + 5;
		int l3 = j + (heightbutton - (heightbutton - heightstr) / 2);
		g.setFont(f1);
		g.setColor(bcolor);
		g.fillRect(i + 2, j + 2, widthbutton - 4, heightbutton - 4);
		g.setColor(Color.gray);
		g.drawRect(i + 1, j + 1, widthbutton - 3, heightbutton - 3);
		g.setColor(Color.black);
		g.drawRect(i, j, widthbutton - 1, heightbutton - 1);
		g.setColor(Color.white);
		g.drawLine(i + 1, j + 1, (i + widthbutton) - 2, j + 1);
		g.drawLine(i, j, (i + widthbutton) - 1, j);
		g.drawLine(i + 1, j + 1, i + 1, (j + heightbutton) - 2);
		g.drawLine(i, j, i, (j + heightbutton) - 1);
		if (k == 0) {
			g.setColor(getForeground());
			g.drawString(s, i1, j1);
			return;
		}
		if (k == 1) {
			g.drawImage(image, k1, l1, this);
			return;
		}
		if (k == 2) {
			g.setColor(getForeground());
			g.drawString(s, i2, j2);
			g.drawImage(image, k2, l2, this);
			return;
		}
		if (k == 3) {
			g.drawImage(image, i3, j3, this);
			g.setColor(getForeground());
			g.drawString(s, k3, l3);
		}
	}

	protected void drawdown(Graphics g, int i, int j, Image image, String s, int k) {
		int l = fm.stringWidth(s);
		int i1 = 2 + i + (widthbutton - l) / 2;
		int j1 = 2 + j + (heightbutton - (heightbutton - heightstr) / 2);
		int k1 = 2 + i + (widthbutton - 20) / 2;
		int l1 = 2 + j + (heightbutton - 20) / 2;
		int i2 = 2 + i + (widthbutton - l - 20) / 2 + 5;
		int j2 = 2 + j + (heightbutton - (heightbutton - heightstr) / 2);
		int k2 = 2 + i + l + (widthbutton - l - 20) / 2 + 5;
		int l2 = 2 + j + (heightbutton - 20) / 2;
		int i3 = 2 + i + (widthbutton - l - 20) / 2;
		int j3 = 2 + j + (heightbutton - 20) / 2;
		int k3 = 2 + i + 20 + (widthbutton - l - 20) / 2 + 5;
		int l3 = 2 + j + (heightbutton - (heightbutton - heightstr) / 2);
		g.setColor(bcolor);
		g.fillRect(i + 2, j + 2, widthbutton - 4, heightbutton - 4);
		g.setColor(Color.white);
		g.drawRect(i + 1, j + 1, widthbutton - 3, heightbutton - 3);
		g.setColor(Color.white);
		g.drawRect(i, j, widthbutton - 1, heightbutton - 1);
		g.setColor(Color.black);
		g.drawLine(i, j, (i + widthbutton) - 1, j);
		g.drawLine(i, j, i, (j + heightbutton) - 1);
		g.setColor(Color.gray);
		g.drawLine(i + 1, j + 1, (i + widthbutton) - 2, j + 1);
		g.drawLine(i + 1, j + 1, i + 1, (j + heightbutton) - 2);
		g.setFont(f1);
		if (k == 0) {
			g.setColor(getForeground());
			g.drawString(s, i1, j1);
			return;
		}
		if (k == 1) {
			g.drawImage(image, k1, l1, this);
			return;
		}
		if (k == 2) {
			g.setColor(getForeground());
			g.drawString(s, i2, j2);
			g.drawImage(image, k2, l2, this);
			return;
		}
		if (k == 3) {
			g.setColor(getForeground());
			g.drawString(s, k3, l3);
			g.drawImage(image, i3, j3, this);
		}
	}

	public boolean handleEvent(Event event) {
		switch (event.id) {
		case 501: // Event.MOUSE_DOWN
			Graphics g = getGraphics();
			if (event.x >= orgx0 && event.x < widthbutton + orgx0 && event.y < heightbutton + orgy0 && event.y > orgy0) {
				drawdown(g, orgx0, orgy0, image1, str1, type1);
				Event event1 = new Event(this, 1001, "down" + str1);
				deliverEvent(event1);
				g.dispose();
				downumber = 1;
				return true;
			}
			if (event.x >= widthbutton + orgx0 && event.x < 2 * widthbutton + orgx0 && event.y >= orgy0 && event.y < heightbutton + orgy0) {
				drawdown(g, orgx0 + widthbutton, orgy0, image2, str2, type2);
				Event event2 = new Event(this, 1001, "down" + str2);
				deliverEvent(event2);
				g.dispose();
				downumber = 2;
				return true;
			}
			if (event.x >= orgx0 && event.x < widthbutton + orgx0 && event.y >= heightbutton + orgy0 && event.y < 2 * heightbutton + orgy0) {
				drawdown(g, orgx0, orgy0 + heightbutton, image3, str3, type3);
				Event event3 = new Event(this, 1001, "down" + str3);
				deliverEvent(event3);
				g.dispose();
				downumber = 3;
			}
			else
			if (event.x >= widthbutton + orgx0 && event.x < 2 * widthbutton + orgx0 && event.y >= heightbutton + orgy0 && event.y < 2 * heightbutton + orgy0) {
				drawdown(g, orgx0 + widthbutton, orgy0 + heightbutton, image4, str4, type4);
				Event event4 = new Event(this, 1001, "down" + str4);
				deliverEvent(event4);
				g.dispose();
				downumber = 4;
			}
			else
			if (event.x >= orgx0 && event.x < widthbutton + orgx0 && event.y >= 2 * heightbutton + orgy0 && event.y < 3 * heightbutton + orgy0) {
				drawdown(g, orgx0, orgy0 + 2 * heightbutton, image5, str5, type5);
				downumber = 5;
			}
			else
			if (event.x >= widthbutton + orgx0 && event.x < 2 * widthbutton + orgx0 && event.y >= 2 * heightbutton + orgy0 && event.y < 3 * heightbutton + orgy0) {
				drawdown(g, orgx0 + widthbutton, orgy0 + 2 * heightbutton, image6, str6, type6);
				downumber = 6;
			}
			else
			if (event.x >= orgx0 && event.x < widthbutton + orgx0 && event.y >= 3 * heightbutton + orgy0 && event.y < 4 * heightbutton + orgy0) {
				drawdown(g, orgx0, orgy0 + 3 * heightbutton, image7, str7, type7);
				downumber = 7;
			}
			else
			if (event.x >= widthbutton + orgx0 && event.x < 2 * widthbutton + orgx0 && event.y >= 3 * heightbutton + orgy0 && event.y < 4 * heightbutton + orgy0) {
				Event event5 = new Event(this, 1001, "down" + str8);
				deliverEvent(event5);
				drawdown(g, orgx0 + widthbutton, orgy0 + 3 * heightbutton, image8, str8, type8);
				downumber = 8;
			}
			g.dispose();
			return true;

		case 502: // Event.MOUSE_UP
			Graphics g1 = getGraphics();
			if (downumber == 1) {
				drawup(g1, orgx0, orgy0, image1, str1, type1);
				Event event6 = new Event(this, 1001, str1);
				deliverEvent(event6);
				g1.dispose();
				downumber = 0;
				return true;
			}
			if (downumber == 2) {
				drawup(g1, orgx0 + widthbutton, orgy0, image2, str2, type2);
				Event event7 = new Event(this, 1001, str2);
				deliverEvent(event7);
				g1.dispose();
				downumber = 0;
				return true;
			}
			if (downumber == 3) {
				drawup(g1, orgx0, orgy0 + heightbutton, image3, str3, type3);
				Event event8 = new Event(this, 1001, str3);
				deliverEvent(event8);
				g1.dispose();
				downumber = 0;
				return true;
			}
			if (downumber == 4) {
				drawup(g1, orgx0 + widthbutton, orgy0 + heightbutton, image4, str4, type4);
				Event event9 = new Event(this, 1001, str4);
				deliverEvent(event9);
				g1.dispose();
				downumber = 0;
				return true;
			}
			if (downumber == 5) {
				drawup(g1, orgx0, orgy0 + 2 * heightbutton, image5, str5, type5);
				Event event10 = new Event(this, 1001, str5);
				deliverEvent(event10);
				g1.dispose();
				downumber = 0;
				return true;
			}
			if (downumber == 6) {
				drawup(g1, orgx0 + widthbutton, orgy0 + 2 * heightbutton, image6, str6, type6);
				Event event11 = new Event(this, 1001, str6);
				deliverEvent(event11);
				g1.dispose();
				downumber = 0;
				return true;
			}
			if (downumber == 7) {
				drawup(g1, orgx0, orgy0 + 3 * heightbutton, image7, str7, type7);
				Event event12 = new Event(this, 1001, str7);
				deliverEvent(event12);
				g1.dispose();
				downumber = 0;
				return true;
			}
			if (downumber == 8) {
				Event event13 = new Event(this, 1001, str8);
				deliverEvent(event13);
				g1.dispose();
				downumber = 0;
				return true;
			}

		case 506: // Event.MOUSE_DRAG
			if (event.x < widthbutton + orgx0 && event.x > orgx0 && event.y < heightbutton + orgy0 && event.y > orgy0) {
				if (master.drag2 == 0)
					master.drag2 = event.x;
				else
				if ((master.drag2 - event.x) * (master.drag2 - event.x) > 324) {
					Event event14 = new Event(this, 1001, "drag1");
					deliverEvent(event14);
				}
				return true;
			}
			if (event.x >= widthbutton + orgx0 && event.x < 2 * widthbutton + orgx0 && event.y < heightbutton + orgy0 && event.y > orgy0) {
				if (master.drag2 == 0)
					master.drag2 = event.x;
				else
				if ((master.drag2 - event.x) * (master.drag2 - event.x) > 324) {
					Event event15 = new Event(this, 1001, "drag2");
					deliverEvent(event15);
				}
				return true;
			}
			if (event.x >= orgx0 && event.x < widthbutton + orgx0 && event.y >= heightbutton + orgy0 && event.y < 2 * heightbutton + orgy0) {
				if (master.drag2 == 0)
					master.drag2 = event.x;
				else
				if ((master.drag2 - event.x) * (master.drag2 - event.x) > 324) {
					Event event16 = new Event(this, 1001, "drag3");
					deliverEvent(event16);
				}
				return true;
			}
			if (event.x >= widthbutton + orgx0 && event.x < 2 * widthbutton + orgx0 && event.y >= heightbutton + orgy0 && event.y < 2 * heightbutton + orgy0) {
				if (master.drag2 == 0)
					master.drag2 = event.x;
				else
				if ((master.drag2 - event.x) * (master.drag2 - event.x) > 324) {
					Event event17 = new Event(this, 1001, "drag4");
					deliverEvent(event17);
				}
				return true;
			}

		default:
			return false;
		}
	}

	public void setImage(int i, int j) {
		if (j == 8 && i == 0)
			image8 = imager4;
		if (j == 8 && i == 1)
			image8 = imagestop;
		if (j == 5 && i == 0)
			image5 = imagel3;
		if (j == 5 && i == 1)
			image5 = imagel4;
	}

	public void setLabel(String s, int i) {
		if (i == 1)
			str1 = s;
		else
		if (i == 2)
			str2 = s;
		else
		if (i == 3)
			str3 = s;
		else
		if (i == 4)
			str4 = s;
		else
		if (i == 5)
			str5 = s;
		else
		if (i == 6)
			str6 = s;
		else
		if (i == 7)
			str7 = s;
		else
		if (i == 8)
			str8 = s;
		Graphics g = getGraphics();
		paint(g);
		g.dispose();
	}

	public String getLabel(int i) {
		if (i == 1)
			return str1;
		if (i == 2)
			return str2;
		if (i == 3)
			return str3;
		if (i == 4)
			return str4;
		if (i == 5)
			return str5;
		if (i == 6)
			return str6;
		if (i == 7)
			return str7;
		if (i == 8)
			return str8;
		else
			return "null";
	}

	public void update(Graphics g) {
		paint(g);
	}
}