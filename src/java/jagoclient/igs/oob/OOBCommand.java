package jagoclient.igs.oob;

import jagoclient.*;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2004-12-16
 * Time: 10:34:45
 */
public class OOBCommand {
// ------------------------------ FIELDS ------------------------------

	public static final String PREFIX = "1lB8OO0";
	public static final String SEPARATOR = ":";
	public static final int MOVE = 1;
	public static final int IGNORE_NEXT_PASS = 2;
	public static final int MOVE_QUESTION = 3;
	public static final int MOVE_ANSWER = 4;
	public static final int COUNT_RULE_SUGGEST = 10;
	public static final int COUNT_RULE_ACCEPT = 11;
	public static final int COUNT_RULE_DECLINE = 12;
	public static final int RESULT_SUGGEST = 20;
	public static final int RESULT_ACCEPT = 21;
	public static final int RESULT_DECLINE = 22;
	public static final int IGNORE_ME = 100;
	public static OOBCommand IGNORE_ME_COMMAND;

	protected int commandType;
	protected int gameNo;

// -------------------------- STATIC METHODS --------------------------

	static {
		IGNORE_ME_COMMAND = new OOBCommand();
		IGNORE_ME_COMMAND.commandType = IGNORE_ME;
	}

	protected OOBCommand() {
	}

	public static OOBCommand parse(String s) {
		if (s == null) {
			return null;
		}
		if (s.indexOf(PREFIX) >= 0) {
			s = s.substring(s.indexOf(PREFIX) + PREFIX.length());
		}
		s = s.trim();
		s = Global.decrypt(s);
		String[] parts = s.split(SEPARATOR);
		OOBCommand oobCmd = null;
		int index = 0;
		int commandType = Integer.parseInt(parts[index++]);
		int gameNo = Integer.parseInt(parts[index++]);
		Dump.println("OOBCommand.parse():gameNo=" + gameNo);
		switch (commandType) {
			case MOVE:
				int i = Integer.parseInt(parts[index++]);
				int j = Integer.parseInt(parts[index++]);
				Move move = new Move();
				move.setX(i);
				move.setY(j);
				oobCmd = new OOBMove(move);
				oobCmd.setGameNo(gameNo);
				break;
			case IGNORE_NEXT_PASS:
				oobCmd = new OOBIgnoreNextPass(gameNo);
				break;
			case MOVE_QUESTION:
				oobCmd = new OOBMoveQuestion(gameNo, parts[index++], Integer.parseInt(parts[index++]));
				break;
			case MOVE_ANSWER:
				oobCmd = new OOBMoveAnswer(gameNo, Integer.parseInt(parts[index++]),
						Integer.parseInt(parts[index++]), Integer.parseInt(parts[index++]),
						Integer.parseInt(parts[index++]));
				break;
			case IGNORE_ME:
				oobCmd = IGNORE_ME_COMMAND;
				break;
			case COUNT_RULE_SUGGEST:
			case COUNT_RULE_ACCEPT:
			case COUNT_RULE_DECLINE:
				oobCmd = new OOBCountRule();
				oobCmd.commandType = commandType;
				((OOBCountRule) oobCmd).setCountRule(Integer.parseInt(parts[index++]));
				break;
			case RESULT_SUGGEST:
			case RESULT_ACCEPT:
			case RESULT_DECLINE:
				oobCmd = new OOBResult();
				oobCmd.commandType = commandType;
				GameResult result = new GameResult();
				int ruleType = Integer.parseInt(parts[index++]);
				result.setRuleInUse(Rule.getRule(ruleType));
				result.setWinner(Integer.parseInt(parts[index++]));
				result.setWonBy(Float.parseFloat(parts[index++]));
				result.setBlackTotal(Float.parseFloat(parts[index++]));
				result.setWhiteTotal(Float.parseFloat(parts[index++]));
				((OOBResult) oobCmd).setResult(result);
				break;
			default:
				oobCmd = new OOBCommand();
				oobCmd.commandType = commandType;
				oobCmd.gameNo = gameNo;
		}
		return oobCmd;
	}

// --------------------- GETTER / SETTER METHODS ---------------------

	/**
	 * @return Returns the commandType.
	 */
	public int getCommandType() {
		return commandType;
	}

	public void setCommandType(int commandType) {
		this.commandType = commandType;
	}

	/**
	 * @return Returns the gameNo.
	 */
	public int getGameNo() {
		return gameNo;
	}

	/**
	 * @param gameNo The gameNo to set.
	 */
	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

// ------------------------ CANONICAL METHODS ------------------------

	public String toString() {
		String s = commandType + SEPARATOR + gameNo;
		Dump.println("OOBCommand: "+s);
		return PREFIX + Global.encrypt(s);
	}
}
