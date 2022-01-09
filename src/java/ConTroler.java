/* Decompiled by IdeaJad 2169.2 ~ tagtraum industries incorporated ~ http://www.tagtraum.com/ */

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

class ConTroler extends Canvas {

	boolean autorun, down;
	int autonumber, black, step, orgx;
	int orgy, widthimag, heightimag;
	wqp11 master;

	ConTroler(int i, wqp11 wqp11_1) {
		autorun = false;
		autonumber = 4;
		black = 1;
		widthimag = 161;
		heightimag = 61;
		down = false;
		master = wqp11_1;
		black = i;
		setForeground(new Color(100, 50, 50));
	}

	public void paint(Graphics g) {
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		widthimag = master.imagelogo.getWidth(this);
		heightimag = master.imagelogo.getHeight(this);
		Dimension dimension = getSize();
		if (widthimag > 0) {
			orgx = (dimension.width - widthimag) / 2;
			orgy = (dimension.height - heightimag) / 2;
		}
		else {
			orgx = 0;
			orgy = 0;
		}
		if (down) {
			g.drawImage(master.imagelogo, orgx + 3, orgy + 3, this);
			return;
		}
		else {
			g.drawImage(master.imagelogo, orgx, orgy, this);
			return;
		}
	}

	public boolean handleEvent(Event event) {
		switch (event.id) {
		case 501: // Event.MOUSE_DOWN
			if (event.x > orgx && event.x < orgx + widthimag && event.y > orgy && event.y < orgy + heightimag) {
				down = true;
				repaint();
				return true;
			}

		case 502: // Event.MOUSE_UP
			down = false;
			if (event.x > orgx && event.x < orgx + widthimag && event.y > orgy && event.y < orgy + heightimag) {
				repaint();
				try {
					URL url = new URL("http://weiqi.cn.tom.com/");
					master.master.cont.showDocument(url, "_blank");
				}
				catch (MalformedURLException _ex) { }
				return true;
			}

		case 504: // Event.MOUSE_EVENT
			master.master.setCursor(new Cursor(12));
			return true;

		case 505: // Event.MOUSE_EXIT
			master.master.setCursor(new Cursor(0));
			return true;

		case 503: // Event.MOUSE_MOVE
		default:
			return false;
		}
	}
}