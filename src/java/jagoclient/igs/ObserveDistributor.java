package jagoclient.igs;

import jagoclient.Dump;
import jagoclient.igs.oob.OOBMoveQuestion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This distributor takes moves from the IgsStream and sends them
 * to a GoObserver, which is opened elswhere.
 */

public class ObserveDistributor extends Distributor {
	GoObserver P;
	boolean Blocked;

	public ObserveDistributor(IgsStream in, GoObserver p, int n) {
		super(in, 15, n, false);
		P = p;
		Blocked = true;
	}

	public void send(String c) {
		//"  1(W): Pass"
		Pattern pattern = Pattern.compile("\\W*(\\d+)\\([B|W]\\): Pass");
		Matcher matcher = pattern.matcher(c);
		if (matcher.matches()) {
			// send a query to the black player for the specific move
			int moveNumber = Integer.parseInt(matcher.group(1));
			/*
			if (moveNumber > P.getGoFrame().B.getNextMoveNumber()) {
				//P.receive(c);
				return;
			}
			*/
			In.out("tell " + P.getGoFrame().BlackName + " " + new OOBMoveQuestion(P.getGoFrame().GameNumber,
					P.getGoFrame().getConnectionFrame().getUsername(), moveNumber));
		}
		P.receive(c);
	}

	public void remove() {
		P.remove();
	}

	public boolean blocked() {
		if (Playing) return false;
		else return Blocked;
	}

	public void refresh() {
		P.refresh();
	}

	public void allsended() {
		P.sended();
	}

	public boolean started() {
		return P.started();
	}

	public void set(int i, int j) {
		Dump.println("Observe Distributor got move at " + i + "," + j);
		P.set(i, j);
	}

	public void pass() {
		Dump.println("Observe Distributor got a pass");
		P.pass();
	}

	public boolean newmove() {
		return P.newmove();
	}
}

