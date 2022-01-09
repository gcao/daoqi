package jagoclient.igs.oob;

import jagoclient.Dump;
import jagoclient.GameResult;
import jagoclient.Global;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2004-12-29
 * Time: 15:07:47
 */
public class OOBResult extends OOBCommand {
// ------------------------------ FIELDS ------------------------------

	GameResult result;

// --------------------- GETTER / SETTER METHODS ---------------------

	public GameResult getResult() {
		return result;
	}

	public void setResult(GameResult result) {
		this.result = result;
	}

// -------------------------- OTHER METHODS --------------------------

	public String toString() {
		String s = commandType + SEPARATOR + gameNo + SEPARATOR + result.getRuleInUse().getRuleType() + SEPARATOR + result.getWinner()
				+ SEPARATOR + result.getWonBy() + SEPARATOR + result.getBlackTotal() + SEPARATOR + result.getWhiteTotal();
		Dump.println("OOBResult: " + s);
		return PREFIX + Global.encrypt(s);
	}
}
