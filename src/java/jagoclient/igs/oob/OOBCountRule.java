package jagoclient.igs.oob;

import jagoclient.Global;
import jagoclient.Dump;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2004-12-21
 * Time: 13:36:40
 *
 * @see jagoclient.Rule
 */
public class OOBCountRule extends OOBCommand {
// ------------------------------ FIELDS ------------------------------

	private int countRule;

// --------------------- GETTER / SETTER METHODS ---------------------

	public int getCountRule() {
		return countRule;
	}

	public void setCountRule(int countRule) {
		this.countRule = countRule;
	}

// -------------------------- OTHER METHODS --------------------------

	public String toString() {
		String s = commandType + SEPARATOR + gameNo + SEPARATOR + countRule;
		Dump.println("OOBCountRule: "+s);
		return PREFIX + Global.encrypt(s);
	}
}
