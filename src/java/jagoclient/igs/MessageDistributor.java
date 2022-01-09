package jagoclient.igs;

import jagoclient.Global;
import jagoclient.sound.JagoSound;
import rene.util.parser.StringParser;

import java.awt.*;
import java.io.PrintWriter;

/**
 * This is used to parse messages (code 24) from the server. It will
 * open a MessageDialog, unless this is checked off. The reply method
 * of the ConnectionFrame is checked for auto replies.
 */

public class MessageDistributor extends Distributor {
	ConnectionFrame CF;
	PrintWriter Out;
	MessageDialog MD;
	String LastUser;

	public MessageDistributor
			(ConnectionFrame cf, IgsStream in, PrintWriter out) {
		super(in, 24, 0, false);
		CF = cf;
		Out = out;
		MD = null;
	}

	/**
	 * got a message
	 * IGS format: *username*: xxx
	 * modified NNGS format: username --> xxx
	 */
	public void send(String C) {
		if (C.equals("")) return;
		StringParser p = new StringParser(C);
		String user;
		if (CF.getServerType() != Global.NNGS_DAOQI) { // IGS or old NNGS
			if (!p.skip("*")) return;
			user = p.upto('*');
			if (!p.skip("*:")) return;
		} else { // NNGS based Daoqi server
			user = p.upto(' ');
			p.skipblanks();
			p.skip("-->");
		}
		p.skipblanks();
		if (p.error()) return;
		CF.append(C, Color.red.darker());
		String a = CF.reply();
		if (!a.equals("")) // autoreply on
		{
			if (LastUser == null || !LastUser.equals(user)) {
				CF.append(Global.resourceString("Auto_reply_sent_to_") + user);
				Out.println("tell " + user + " " + a);
				LastUser = user;
			}
		}
		// no autoreply
		else if (Global.blocks(C) != 0) return;
		else if (CF.wantsmessages() || Global.posfilter(C)) {
			if (MD != null) {
				MD.append(user, p.upto((char) 0));
				JagoSound.play("wip", "wip", true);
			} else {
				MD = new MessageDialog(CF, user, p.upto((char) 0), Out, this);
				if (Global.blocks(C) == 0) JagoSound.play("message", "wip", true);
			}
		} else {
			JagoSound.play("wip", "wip", true);
		}
	}

	public void remove() {
		MD = null;
	}
}
