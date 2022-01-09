package jagoclient.igs;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2004-12-21
 * Time: 13:36:40
 * @see jagoclient.Rule
 */
public class OOBCountRule extends OOBCommand {
    private int countRule;

    public int getCountRule() {
        return countRule;
    }

    public void setCountRule(int countRule) {
        this.countRule = countRule;
    }

    public String toString() {
        return super.toString() + SEPARATOR + countRule;
    }
}
