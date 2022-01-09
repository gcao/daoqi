package jagoclient;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2004-12-21
 * Time: 16:32:47
 */
public abstract class Rule {
	public static final int CHINESE_RULE = 1;
	public static final int JI_HUO_ZI_RULE = 2;
	public static final int JAPANESE_RULE = 3;

	private static final Rule CHINESE_RULE_OBJ = new ChineseRule();
	private static final Rule JI_HUO_ZI_RULE_OBJ = new JiHuoZiRule();
	private static final Rule JAPANESE_RULE_OBJ = new JapaneseRule();

	public static final int STONE = 0; // zi
	public static final int MOKU = 1; // mu

    protected int ruleType;
    protected float komi;
    protected int unit;
    protected boolean balanceMoves;
    /**
     * true means Huan Qi Tou
     */
    protected boolean balanceGroups;

    public int getRuleType() {
        return ruleType;
    }

    public float getKomi() {
        return komi;
    }

    public int getUnit() {
        return unit;
    }

    public boolean balanceMoves() {
        return balanceMoves;
    }

    public boolean balanceGroups() {
        return balanceGroups;
    }

    public abstract GameResult determineResult(GameResultInput input);

    public abstract String toShortString();

    public static String getUnitName(int unitType) {
        if (unitType == STONE) {
            return Global.resourceString("Stone");
        } else if (unitType == MOKU) {
            return Global.resourceString("Moku");
        } else {
            throw new IllegalArgumentException("getUnitName(): "+unitType);
        }
    }

    public static Rule getRule(int ruleType) {
        Rule rule = null;
        switch (ruleType) {
            case CHINESE_RULE:
                rule = CHINESE_RULE_OBJ;
                break;
            case JI_HUO_ZI_RULE:
                rule = JI_HUO_ZI_RULE_OBJ;
                break;
            case JAPANESE_RULE:
                rule = JAPANESE_RULE_OBJ;
                break;
            default:
                throw new IllegalArgumentException("getRule(): "+ruleType);
        }
        return rule;
    }

    public static String normalizeWonBy(float wonBy) {
        StringBuffer sb = new StringBuffer();
        int integerPart = (int)wonBy;
        if (integerPart > 0)
            sb.append(integerPart).append(" ");

        int i = (int)(4 * (wonBy - integerPart));
        switch(i) {
            case 1:
                sb.append("1/4");
                break;
            case 2:
                sb.append("1/2");
                break;
            case 3:
                sb.append("3/4");
                break;
        }

        return sb.toString();
    }
}
