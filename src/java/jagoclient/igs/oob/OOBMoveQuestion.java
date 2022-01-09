package jagoclient.igs.oob;

import jagoclient.Global;
import jagoclient.Dump;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2006-1-23
 * Time: 11:57:46
 * To change this template use File | Settings | File Templates.
 */
public class OOBMoveQuestion extends OOBCommand {
// ------------------------------ FIELDS ------------------------------

	protected int moveNumber;
	protected String observer;

// --------------------------- CONSTRUCTORS ---------------------------

	public OOBMoveQuestion(int gameNo, String observer, int moveNumber) {
		this.observer = observer;
		this.commandType = OOBCommand.MOVE_QUESTION;
		this.gameNo = gameNo;
		this.moveNumber = moveNumber;
	}

// --------------------- GETTER / SETTER METHODS ---------------------

	public int getMoveNumber() {
		return moveNumber;
	}

	public String getObserver() {
		return observer;
	}

// -------------------------- OTHER METHODS --------------------------

	public String toString() {
		String s = commandType + SEPARATOR + gameNo + SEPARATOR + observer + SEPARATOR + moveNumber;
		Dump.println("OOBMoveQuestion: "+s);
		return PREFIX + Global.encrypt(s);
	}
}
