/*
 * Created on 2004-12-16
 */
package jagoclient.igs.oob;

import jagoclient.*;
import jagoclient.dialogs.SimpleMessage;
import jagoclient.igs.ConnectionFrame;
import jagoclient.igs.CountRuleDialog;
import jagoclient.igs.IgsGoFrame;
import jagoclient.igs.ResultSuggestDialog;

/**
 * @author Guoliang Cao
 */
public class OOBCommandHandler {
	private ConnectionFrame connectionFrame;

	public OOBCommandHandler(ConnectionFrame connectionFrame) {
		this.connectionFrame = connectionFrame;
	}

	public void handle(OOBCommand oobCmd) {
		Dump.println(oobCmd.toString());
		IgsGoFrame goFrame = connectionFrame.getGoFrame(oobCmd.getGameNo());
		if (goFrame == null) {
			goFrame = connectionFrame.getGoFrame(connectionFrame.getGameInPlay());
		}
		switch (oobCmd.getCommandType()) {
			case OOBCommand.MOVE:
				Move move = ((OOBMove) oobCmd).getMove();
				if (goFrame.getMyRole() == IgsGoFrame.PLAYER) {
					goFrame.setOppositeMove(move);
				} else {
					Dump.println("I'm observer.");
					if (goFrame.B.maincolor() > 0)
						goFrame.black(move.getX(), move.getY());
					else
						goFrame.white(move.getX(), move.getY());
				}
				break;
			case OOBCommand.IGNORE_NEXT_PASS:
				connectionFrame.getGoFrame(oobCmd.gameNo).setIgnoreNextPass(true);
				break;
			case OOBCommand.MOVE_QUESTION: {
				OOBMoveQuestion moveQuestion = (OOBMoveQuestion) oobCmd;
				Dump.println(moveQuestion.getObserver() + " ask for move " + moveQuestion.getMoveNumber());
				// get the move
				move = goFrame.B.getMove(moveQuestion.getMoveNumber());
				OOBMoveAnswer moveAnswer = new OOBMoveAnswer(moveQuestion.getGameNo(), moveQuestion.getMoveNumber(),
						move.getColor(), move.getX(), move.getY());
				connectionFrame.getIgsStream().out("tell " + moveQuestion.getObserver() + " " + moveAnswer);
				break;
			}
			case OOBCommand.MOVE_ANSWER: {
				Dump.println("received OOBMoveAnswer");
				OOBMoveAnswer moveAnswer = (OOBMoveAnswer) oobCmd;
				if (moveAnswer.getX() < 0) {
					// do nothing
				} else {
					goFrame.B.changeMove(moveAnswer.getMoveNumber(), moveAnswer.getColor(), moveAnswer.getX(), moveAnswer.getY());
				}
				Dump.println("processed OOBMoveAnswer");
				goFrame.B.allback();
				goFrame.B.allforward();
				break;
			}
			case OOBCommand.COUNT_RULE_SUGGEST:
				new CountRuleDialog(connectionFrame, ((OOBCountRule) oobCmd).getCountRule());
				break;
			case OOBCommand.COUNT_RULE_ACCEPT: {
				goFrame.setCountRule(((OOBCountRule) oobCmd).getCountRule());
				int ruleType = ((OOBCountRule) oobCmd).getCountRule();
				String ruleName = Rule.getRule(ruleType).toString();
				new SimpleMessage(goFrame, Global.resourceString("Your_Opponent_Agreed_To_Use_Rule")
						+ ruleName, 300, 100, true);
				break;
			}
			case OOBCommand.COUNT_RULE_DECLINE: {
				int ruleType = ((OOBCountRule) oobCmd).getCountRule();
				String ruleName = Rule.getRule(ruleType).toString();
				new SimpleMessage(goFrame, Global.resourceString("Your_Opponent_Declined_To_Use_Rule")
						+ ruleName, 300, 100, true);
				break;
			}
			case OOBCommand.RESULT_SUGGEST:
				new ResultSuggestDialog(connectionFrame, ((OOBResult) oobCmd).getResult());
				break;
			case OOBCommand.RESULT_ACCEPT: {
				GameResult result = ((OOBResult) oobCmd).getResult();
                goFrame.finishGame(result);
                new SimpleMessage(goFrame, Global.resourceString("Your_Opponent_Accepted_Result") + "\n\n"
						+ result.toLongString(), 300, 100, true);
				break;
			}
			case OOBCommand.RESULT_DECLINE: {
				GameResult result = ((OOBResult) oobCmd).getResult();
				new SimpleMessage(goFrame, Global.resourceString("Your_Opponent_Declined_Result") + "\n\n"
						+ result.toLongString(), 300, 100, true);
				break;
			}
		}
	}
}