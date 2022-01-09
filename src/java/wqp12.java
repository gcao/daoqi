/* Decompiled by IdeaJad 2169.2 ~ tagtraum industries incorporated ~ http://www.tagtraum.com/ */

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.*;
import java.net.URL;
import java.net.MalformedURLException;

public class wqp12 extends Applet {

	wqp11 wqp;
	Thread animate2;
	Toolkit toolkit;
	URL codeBase;
	String qptype, bcolor, fcolor, filename;
	String codebase, str1, bstring;
	AudioClip au_da, au_drip;
	AppletContext cont;
	Image image;
	int framewidth, frameheight;

	public void init() {
		super.init();
		cont = getAppletContext();
		filename = getParameter("filename");
		codebase = getCodeBase().toString();
		str1 = "";
		if (filename.startsWith("http")) {
			str1 = filename;
		} else {
			str1 = codebase + filename;
		}
		qptype = getParameter("qptype");
		bcolor = getParameter("bcolor");
		fcolor = getParameter("fcolor");
		au_da = getAudioClip(getCodeBase(), "dada3.au");
		au_drip = getAudioClip(getCodeBase(), "di.au");
		toolkit = getToolkit();
		codeBase = getCodeBase();
		try {
			image = getImage(new URL("http://www.weiqi361.com/images/weiqi36188x31.gif"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		Dimension dimension = getSize();
		framewidth = dimension.width;
		frameheight = dimension.height;
		wqp = new wqp11(this, str1);
		wqp.setSize(framewidth, frameheight);
		setLayout(new BorderLayout(0, 0));
		add("Center", wqp);
		setBackground(wqp.bcolorZ);
		wqp.setVisible(true);
		if (wqp.dqp)
			wqp.goPlate.readsgfFile(str1);
	}

	public void update(Graphics g) {
	}

	public wqp12() {
		qptype = "dcp";
	}
}