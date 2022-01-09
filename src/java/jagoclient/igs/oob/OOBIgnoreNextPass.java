package jagoclient.igs.oob;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2006-1-23
 * Time: 10:38:55
 * To change this template use File | Settings | File Templates.
 */
public class OOBIgnoreNextPass extends OOBCommand {
	public OOBIgnoreNextPass(int gameNo) {
		this.commandType = OOBCommand.IGNORE_NEXT_PASS;
		this.gameNo = gameNo;
	}
}
