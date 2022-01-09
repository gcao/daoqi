/* Decompiled by IdeaJad 2169.2 ~ tagtraum industries incorporated ~ http://www.tagtraum.com/ */

import java.awt.*;

class ImagButton extends Canvas {

	Image image;
	Dimension r;
	String str;
	int orgx1, orgx0, orgx20, orgx21;
	int orgx31, orgx30, orgy1, orgy0;
	int orgy20, orgy21, orgy31, orgy30;
	int type, width1, height1, imagwidth;
	Font f1;
	FontMetrics fm;
	int imagheight, ww, hh;
	Cursor entercur;
	boolean visiable;
	Color temp;

	ImagButton(Image image1, String s, int i, Font font) {
		f1 = font;
		str = s;
		image = image1;
		type = i;
		visiable = true;
		entercur = new Cursor(0);
		fm = getFontMetrics(f1);
		width1 = fm.stringWidth(str);
		height1 = fm.getHeight();
		imagwidth = image.getWidth(this);
		imagwidth = 20;
		imagheight = 20;
		if (type == 1) {
			ww = imagwidth + 10;
			hh = imagheight + 10;
			return;
		}
		else {
			ww = width1 + 10;
			hh = height1 + 10;
			return;
		}
	}

	public void paint(Graphics g) {
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		r = getSize();
		fm = getFontMetrics(f1);
		width1 = fm.stringWidth(str);
		height1 = fm.getHeight();
		g.setFont(f1);
		imagwidth = 20;
		imagheight = 20;
		orgx0 = (r.width - width1) / 2;
		orgy0 = height1 + (r.height - height1) / 2;
		if (str.equals("a") || str.equals("b") || str.equals("c") || str.equals("d") || str.equals("e"))
			orgy0 = height1;
		orgx1 = (r.width - imagwidth) / 2;
		orgy1 = (r.height - imagheight) / 2;
		orgx20 = (r.width - width1 - imagwidth) / 2;
		orgy20 = r.height - (r.height - height1) / 2 - 2;
		orgx21 = width1 + (r.width - width1 - imagwidth) / 2;
		orgy21 = (r.height - imagheight) / 2;
		orgx30 = (r.width - width1 - imagwidth) / 2;
		orgy30 = (r.height - imagheight) / 2;
		orgx31 = imagwidth + (r.width - width1 - imagwidth) / 2 + 4;
		orgy31 = r.height - (r.height - height1) / 2 - 2;
		g.setColor(getBackground());
		g.fillRect(2, 2, r.width - 4, r.height - 4);
		if (type != 4) {
			g.setColor(Color.gray);
			g.drawRect(1, 1, r.width - 3, r.height - 3);
			g.setColor(Color.black);
			g.drawRect(0, 0, r.width - 1, r.height - 1);
			g.setColor(Color.white);
			g.drawLine(1, 1, r.width - 2, 1);
			g.drawLine(0, 0, r.width - 1, 0);
			g.drawLine(1, 1, 1, r.height - 2);
			g.drawLine(0, 0, 0, r.height - 1);
		}
		if (type == 0 || type == 4) {
			g.setColor(getForeground());
			g.drawString(str, orgx0, orgy0);
			return;
		}
		if (type == 1) {
			g.drawImage(image, orgx1, orgy1, this);
			return;
		}
		if (type == 2) {
			g.setColor(getForeground());
			g.drawString(str, orgx20, orgy20);
			g.drawImage(image, orgx21, orgy21, this);
			return;
		}
		if (type == 3) {
			g.drawImage(image, orgx30, orgy30, this);
			g.setColor(getForeground());
			g.drawString(str, orgx31, orgy31);
		}
	}

	public boolean handleEvent(Event event) {
		switch (event.id) {
		case 501: // Event.MOUSE_DOWN
			Graphics g = getGraphics();
			g.setFont(f1);
			g.setColor(getBackground());
			g.fillRect(2, 2, r.width - 4, r.height - 4);
			if (type != 4) {
				g.setColor(Color.white);
				g.drawRect(1, 1, r.width - 3, r.height - 3);
				g.setColor(Color.white);
				g.drawRect(0, 0, r.width - 1, r.height - 1);
				g.setColor(Color.black);
				g.drawLine(1, 1, r.width - 2, 1);
				g.drawLine(1, 1, 1, r.height - 2);
				g.setColor(Color.gray);
				g.drawLine(0, 0, r.width - 1, 0);
				g.drawLine(0, 0, 0, r.height - 1);
				g.setColor(Color.black);
			}
			if (type == 0 || type == 4) {
				g.setColor(getForeground());
				g.drawString(str, orgx0 + 2, orgy0 + 2);
			}
			else
			if (type == 1)
				g.drawImage(image, orgx1 + 2, orgy1 + 2, this);
			else
			if (type == 2) {
				g.setColor(getForeground());
				g.drawString(str, orgx20 + 2, orgy20 + 2);
				g.drawImage(image, orgx21 + 2, orgy21 + 2, this);
			}
			else
			if (type == 3) {
				g.drawImage(image, orgx30 + 2, orgy30 + 2, this);
				g.setColor(getForeground());
				g.drawString(str, orgx31 + 2, orgy31 + 2);
			}
			Event event1 = new Event(this, 1001, "down" + str);
			deliverEvent(event1);
			g.dispose();
			return true;

		case 502: // Event.MOUSE_UP
			Graphics g1 = getGraphics();
			g1.setFont(f1);
			Event event2 = new Event(this, 1001, str);
			deliverEvent(event2);
			g1.setColor(getBackground());
			g1.fillRect(2, 2, r.width - 4, r.height - 4);
			if (type != 4) {
				g1.setColor(Color.gray);
				g1.drawRect(1, 1, r.width - 3, r.height - 3);
				g1.setColor(Color.black);
				g1.drawRect(0, 0, r.width - 1, r.height - 1);
				g1.setColor(Color.white);
				g1.drawLine(1, 1, r.width - 2, 1);
				g1.drawLine(0, 0, r.width - 1, 0);
				g1.drawLine(1, 1, 1, r.height - 2);
				g1.drawLine(0, 0, 0, r.height - 1);
				g1.setColor(Color.black);
			}
			if (type == 0 || type == 4) {
				g1.setColor(getForeground());
				g1.drawString(str, orgx0, orgy0);
			}
			else
			if (type == 1)
				g1.drawImage(image, orgx1, orgy1, this);
			else
			if (type == 2) {
				g1.setColor(getForeground());
				g1.drawString(str, orgx20, orgy20);
				g1.drawImage(image, orgx21, orgy21, this);
			}
			else
			if (type == 3) {
				g1.drawImage(image, orgx30, orgy30, this);
				g1.setColor(getForeground());
				g1.drawString(str, orgx31, orgy31);
			}
			g1.dispose();
			return true;

		case 504: // Event.MOUSE_EVENT
			if (type == 4) {
				temp = getForeground();
				setForeground(Color.blue);
				repaint();
			}
			setCursor(entercur);
			return true;

		case 505: // Event.MOUSE_EXIT
			if (type == 4) {
				setForeground(temp);
				repaint();
			}
			setCursor(new Cursor(0));
			return true;

		case 503: // Event.MOUSE_MOVE
		default:
			return false;
		}
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void setLable(String s) {
		str = s;
	}
}