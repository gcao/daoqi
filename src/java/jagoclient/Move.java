package jagoclient;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2004-12-16
 * Time: 10:25:41
 */
public class Move {
    int moveNumber;
    int color;
    int x;
    int y;

    public int getMoveNumber() {
        return moveNumber;
    }

    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int i) {
        this.x = i;
    }

    public int getY() {
        return y;
    }

    public void setY(int j) {
        this.y = j;
    }
}
