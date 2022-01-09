package jagoclient.igs.oob;

import jagoclient.Global;
import jagoclient.Dump;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2006-1-23
 * Time: 14:21:24
 * To change this template use File | Settings | File Templates.
 */
public class OOBMoveAnswer extends OOBCommand {
// ------------------------------ FIELDS ------------------------------

	protected int moveNumber;
	protected int color;
	protected int x;
	protected int y;

// --------------------- GETTER / SETTER METHODS ---------------------

	public OOBMoveAnswer(int gameNo, int moveNumber, int color, int x, int y) {
		this.commandType = OOBCommand.MOVE_ANSWER;
		this.gameNo = gameNo;
		this.moveNumber = moveNumber;
		this.color = color;
		this.x = x;
		this.y = y;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getMoveNumber() {
		return moveNumber;
	}

	public void setMoveNumber(int moveNumber) {
		this.moveNumber = moveNumber;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

// -------------------------- OTHER METHODS --------------------------

	public String toString() {
		String s = commandType + SEPARATOR + gameNo + SEPARATOR + moveNumber + SEPARATOR + color + SEPARATOR + x + SEPARATOR + y;
		Dump.println("OOBMoveAnswer: "+s);
		return PREFIX + Global.encrypt(s);
	}
}
