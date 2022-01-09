/*
 * Created on 2004-12-16
 */
package jagoclient.igs;

import jagoclient.Global;
import jagoclient.Rule;
import jagoclient.dialogs.SingleLineMessage;

/**
 * @author Guoliang Cao
 */
public class OOBCommandHandler {
	private ConnectionFrame connectionFrame;

	public OOBCommandHandler(ConnectionFrame connectionFrame) {
		this.connectionFrame = connectionFrame;
	}

	public void handle(OOBCommand oobCmd) {
		//System.out.println(oobCmd);
		IgsGoFrame goFrame = connectionFrame.getGoFrame(oobCmd.getGameNo());
		if (goFrame == null) {
			goFrame = connectionFrame.getGoFrame(connectionFrame.getGameInPlay());
		}
		switch (oobCmd.getCommandType()) {
			case OOBCommand.MOVE:
				goFrame.setOppositeMove(((OOBMove) oobCmd).getMove());
				break;
			case OOBCommand.IGNORE_NEXT_PASS:
				goFrame.setIgnoreNextPass(true);
				break;
			case OOBCommand.COUNT_RULE_SUGGEST:
				new CountRuleDialog(connectionFrame, ((OOBCountRule) oobCmd).getCountRule());
				break;
			case OOBCommand.COUNT_RULE_ACCEPT:
				goFrame.setCountRule(((OOBCountRule) oobCmd).getCountRule());
				new SingleLineMessage(goFrame, Global.resourceString("Your_Opponent_Agreed_To_Use_Rule")
						+ Rule.getRule(((OOBCountRule) oobCmd).getCountRule()), 300, 100, true);
				break;
			case OOBCommand.COUNT_RULE_DECLINE:
				new SingleLineMessage(goFrame, Global.resourceString("Your_Opponent_Declined_To_Use_Rule")
						+ Rule.getRule(((OOBCountRule) oobCmd).getCountRule()), 300, 100, true);
				break;
		}
	}
}