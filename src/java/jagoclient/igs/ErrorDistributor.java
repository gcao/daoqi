package jagoclient.igs;

import jagoclient.Global;
import jagoclient.Move;
import jagoclient.dialogs.Message;

import java.awt.*;
import java.io.PrintWriter;

/**
 * This distributor receives and handles error messages from the server.
 * It will always open a new dialog box (a Message) to display the error.
 *
 * @see MessageDialog
 */

public class ErrorDistributor extends Distributor {
	ConnectionFrame CF;
	PrintWriter Out;
	String S;

	public ErrorDistributor
			(ConnectionFrame cf, IgsStream in, PrintWriter out) {
		super(in, 5, 0, false);
		CF = cf;
		Out = out;
		S = new String("");
	}

	public void send(String C) {
		if (C.indexOf("Cannot play a dead stone") >= 0
				|| C.indexOf("Cannot place a stone in an occupied place") >= 0) {
			// send a message to the other player (see OOBMove) through say channel. (OOBCOMMAND=out of bound command)
			// send "pass" to the server
			Move lastMove = CF.getLastMove();
			// set local move!
			IgsGoFrame goFrame = CF.getGoFrame(CF.getGameInPlay());
			goFrame.setSelfMove(lastMove);
			// set remote move
			CF.oobPlay(lastMove);
			goFrame.setIgnoreNextPass(true);
			// fool IGS
			CF.pass();
			return;
		}
		if (S.equals("")) S = S + C;
		else S = S + "\n" + C;
	}

	public void allsended() {
		if (Global.blocks(S) == 0 && CF.wantserrors())
			new Message(CF, Global.resourceString("Error:\n") + S);
		CF.append("Error\n" + S, Color.red.darker());
		S = "";
	}
}

