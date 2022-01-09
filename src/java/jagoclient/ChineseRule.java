package jagoclient;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2004-12-22
 * Time: 13:28:37
 */
public class ChineseRule extends Rule {
    public ChineseRule() {
        ruleType = Rule.CHINESE_RULE;
        komi = (float)2.75;
        unit = Rule.STONE;
        balanceMoves = false;
        balanceGroups = false;
    }

    public GameResult determineResult(GameResultInput input) {
        GameResult result = new GameResult();
        result.setRuleInUse(this);

        float b = input.getBlackStones();
        float w = input.getWhiteStones();
        b += 0.5 * input.getSharedLiberties();
        w += 0.5 * input.getSharedLiberties();
        int danguan = input.getNonOccupiedCount() - input.getSharedLiberties();
        if (input.getTotalMoves()%2 == 1) {
            b += (int)(danguan/2);
            w += (int)((danguan+1)/2);
        } else {
            b += (int)((danguan+1)/2);
            w += (int)(danguan/2);
        }

        result.setBlackTotal(b);
        result.setWhiteTotal(w);

        float wonBy = b - this.getKomi() - (float)(input.getTotal()/2.);
        if (wonBy > 0) {
            result.setWonBy(wonBy);
            result.setWinner(1);
        } else {
            result.setWonBy(Math.abs(wonBy));
            result.setWinner(-1);
        }

        return result;
    }

    public String toString() {
        return Global.resourceString("Chinese_Rule");
    }

    public String toShortString() {
        return Global.resourceString("Chinese_Rule_Short");
    }
}
