package jagoclient;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2004-12-22
 * Time: 13:37:38
 */
public class JapaneseRule extends Rule {
    public JapaneseRule() {
        ruleType = Rule.JAPANESE_RULE;
        komi = (float)6.5;
        unit = Rule.MOKU;
        balanceMoves = false;
        balanceGroups = false;
    }

    public GameResult determineResult(GameResultInput input) {
        GameResult result = new GameResult();
        result.setRuleInUse(this);

        float b = input.getBlackMokus();
        float w = input.getWhiteMokus();

        result.setBlackTotal(b);
        result.setWhiteTotal(w);

        float wonBy = b - w - this.getKomi();
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
        return Global.resourceString("Japanese_Rule");
    }

    public String toShortString() {
        return Global.resourceString("Japanese_Rule_Short");
    }
}
