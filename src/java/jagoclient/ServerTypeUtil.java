package jagoclient;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2005-11-20
 * Time: 15:00:10
 * To change this template use File | Settings | File Templates.
 */
public class ServerTypeUtil {
// -------------------------- STATIC METHODS --------------------------

	public static Choice createServerTypeChoice() {
		Choice serverTypeChoice = new Choice();
		serverTypeChoice.setFont(Global.SansSerif);
		serverTypeChoice.add(Global.resourceString("IGS"));
		//serverTypeChoice.add(Global.resourceString("NNGS_Based_Daoqi_Server"));
		return serverTypeChoice;
	}

	public static void setServerType(Choice serverTypeChoice, int serverType) {
		switch (serverType) {
			case Global.IGS :
				serverTypeChoice.select(Global.resourceString("IGS"));
				break;
			case Global.NNGS_DAOQI :
				serverTypeChoice.select(Global.resourceString("NNGS_Based_Daoqi_Server"));
				break;
		}
	}

	public static int getServerType(Choice serverTypeChoice) {
		int serverType = Global.NNGS_DAOQI;
		switch (serverTypeChoice.getSelectedIndex()) {
			case 0 :
				serverType = Global.IGS;
				break;
			case 1 :
				serverType = Global.NNGS_DAOQI;
				break;
		}
		return serverType;
	}
}
