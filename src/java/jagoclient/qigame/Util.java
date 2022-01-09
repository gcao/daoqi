package jagoclient.qigame;

import jagoclient.Global;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2005-4-12
 * Time: 13:22:38
 */
public class Util {
	public static String getQigameUsername() {
		return Global.getParameter("qigame.username", "");
	}

	public static void setQigameUsername(String username) {
		Global.setParameter("qigame.username", username);
	}

	public static String getQigamePassword() {
		String password = Global.getParameter("qigame.password", "");
		if (password != null && password.length() > 0) {
			password = Global.decrypt(password);
		}
		return password;
	}

	public static void setQigamePassword(String password) {
		Global.setParameter("qigame.password", Global.encrypt(password));
	}
}
