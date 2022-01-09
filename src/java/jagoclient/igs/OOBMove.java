package jagoclient.igs;

import jagoclient.Move;

/**
 * Created by IntelliJ IDEA. User: Guoliang Cao Date: 2004-12-16 Time: 10:40:37
 */
public class OOBMove extends OOBCommand {
    private Move move;

    public OOBMove(Move move) {
        this.commandType = OOBCommand.MOVE;
        this.move = move;
    }

    public String toString() {
        return PREFIX + commandType + SEPARATOR + gameNo + SEPARATOR + move.getX()
                + SEPARATOR + move.getY();
    }
    /**
     * @return Returns the move.
     */
    public Move getMove() {
        return move;
    }
}