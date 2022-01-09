package jagoclient.igs.oob;

import jagoclient.Global;
import jagoclient.Move;
import jagoclient.Dump;

/**
 * Created by IntelliJ IDEA. User: Guoliang Cao Date: 2004-12-16 Time: 10:40:37
 */
public class OOBMove extends OOBCommand {
// ------------------------------ FIELDS ------------------------------

	private Move move;

// --------------------------- CONSTRUCTORS ---------------------------

	public OOBMove(Move move) {
		this.commandType = OOBCommand.MOVE;
		this.move = move;
	}

// --------------------- GETTER / SETTER METHODS ---------------------

	/**
	 * @return Returns the move.
	 */
	public Move getMove() {
		return move;
	}

// -------------------------- OTHER METHODS --------------------------

	public String toString() {
		String s = commandType + SEPARATOR + gameNo + SEPARATOR + move.getX()
				+ SEPARATOR + move.getY();
		Dump.println("OOBMove: "+s);
		return PREFIX + Global.encrypt(s);
	}
}