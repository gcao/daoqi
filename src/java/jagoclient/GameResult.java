package jagoclient;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2004-12-22
 * Time: 14:06:59
 */
public class GameResult {
    private Rule ruleInUse;
    private int winner;
    private float wonBy;
    private boolean wonByResign;
    private boolean wonByTime;
    private float blackTotal;
    private float whiteTotal;

    public Rule getRuleInUse() {
        return ruleInUse;
    }

    public void setRuleInUse(Rule ruleInUse) {
        this.ruleInUse = ruleInUse;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public float getWonBy() {
        return wonBy;
    }

    public void setWonBy(float wonBy) {
        if (wonBy < 0) {
            throw new IllegalArgumentException("setWonBy(): " + wonBy);
        }
        this.wonBy = wonBy;
    }

    public boolean isWonByResign() {
        return wonByResign;
    }

    public boolean isWonByTime() {
        return wonByTime;
    }

    public void setWonByResign(boolean wonByResign) {
        this.wonByResign = wonByResign;
    }

    public void setWonByTime(boolean wonByTime) {
        this.wonByTime = wonByTime;
    }

    public float getBlackTotal() {
        return blackTotal;
    }

    public void setBlackTotal(float blackTotal) {
        this.blackTotal = blackTotal;
    }

    public float getWhiteTotal() {
        return whiteTotal;
    }

    public void setWhiteTotal(float whiteTotal) {
        this.whiteTotal = whiteTotal;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (winner == 0) {
            sb.append("draw");
            sb.append(" (").append(ruleInUse.toShortString()).append(")");
        } else {
            sb.append(winner == 1 ? "B+" : "W+");
            if (wonByResign) {
                sb.append("Resign");
            } else if (wonByTime) {
                sb.append("Time");
            } else {
                sb.append(Rule.normalizeWonBy(wonBy));
                sb.append(" (").append(ruleInUse.toShortString()).append(")");
            }
        }
        return sb.toString();
    }

    public String toLongString() {
        StringBuffer sb = new StringBuffer();
        if (winner == 0) {
            sb.append(Global.resourceString("")).append(ruleInUse.toString());
            sb.append("\n");
            sb.append(Global.resourceString("Draw"));
            sb.append("\n");
            sb.append(Global.resourceString("Black")).append(" ").append(blackTotal);
            sb.append(Global.resourceString("White")).append(" ").append(whiteTotal);
        } else {
            if (wonByResign) {
                if (winner == 1) sb.append(Global.resourceString("Black"));
                else if (winner == -1) sb.append(Global.resourceString("White"));
                sb.append(Global.resourceString("Won_By_Resign"));
            } else if (wonByTime) {
                if (winner == 1) sb.append(Global.resourceString("Black"));
                else if (winner == -1) sb.append(Global.resourceString("White"));
                sb.append(Global.resourceString("Won_By_Time"));
            } else {
                sb.append(Global.resourceString("")).append(ruleInUse.toString());
                sb.append("\n");
                if (winner == 1) sb.append(Global.resourceString("Black"));
                else if (winner == -1) sb.append(Global.resourceString("White"));
                sb.append(Global.resourceString("Won")).append(" ");
                sb.append(Rule.normalizeWonBy(wonBy)).append(" ");
                sb.append(Rule.getUnitName(ruleInUse.getUnit()));
                sb.append("\n");
                sb.append(Global.resourceString("Black")).append(" ").append(blackTotal).append("\n");
                sb.append(Global.resourceString("White")).append(" ").append(whiteTotal);
            }
        }
        return sb.toString();
    }
}
