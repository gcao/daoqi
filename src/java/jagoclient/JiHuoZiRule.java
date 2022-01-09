package jagoclient;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2004-12-22
 * Time: 13:36:42
 */
public class JiHuoZiRule extends Rule {
    public JiHuoZiRule() {
        ruleType = Rule.JI_HUO_ZI_RULE;
        komi = (float)2.75;
        unit = Rule.STONE;
        balanceMoves = true;
        balanceGroups = true;
    }

    public GameResult determineResult(GameResultInput input) {
        GameResult result = new GameResult();
        result.setRuleInUse(this);

        float b = input.getBlackStones();
        float w = input.getWhiteStones();
        b += 0.5 * input.getNonOccupiedCount();
        w += 0.5 * input.getNonOccupiedCount();
        int groupDiff = input.getBlackGroups() - input.getWhiteGroups();
        b -= groupDiff;
        w += groupDiff;

        result.setBlackTotal(b);
        result.setWhiteTotal(w);

        float wonBy = b - this.getKomi() - input.getTotal()/2;
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
        return Global.resourceString("Ji_Huo_Zi_Rule");
    }

    public String toShortString() {
        return Global.resourceString("Ji_Huo_Zi_Rule_Short");
    }
}
