package jagoclient;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2005-6-1
 * Time: 13:25:50
 */
public class Projection {
    public int getRotate() {
        return rotate;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }

    public boolean isMirrorHorizontally() {
        return mirrorHorizontally;
    }

    public void setMirrorHorizontally(boolean mirrorHorizontally) {
        this.mirrorHorizontally = mirrorHorizontally;
    }

    public boolean isMirrorVertically() {
        return mirrorVertically;
    }

    public void setMirrorVertically(boolean mirrorVertically) {
        this.mirrorVertically = mirrorVertically;
    }

    public int getOffsetx() {
        return offsetx;
    }

    public void setOffsetx(int offsetx) {
        this.offsetx = offsetx;
    }

    public int getOffsety() {
        return offsety;
    }

    public void setOffsety(int offsety) {
        this.offsety = offsety;
    }

    public int getVirtualBoardWidth() {
        return virtualBoardWidth;
    }

    public void setVirtualBoardWidth(int virtualBoardWidth) {
        this.virtualBoardWidth = virtualBoardWidth;
    }

    public Point toIdealBoardPoint(Point screenBoardPoint) {
        return null;
    }

    public Point toScreenBoardPoint(Point idealBoardPoint) {
        return null;
    }

    int rotate;
    boolean mirrorHorizontally;
    boolean mirrorVertically;
    int offsetx;
    int offsety;
    int virtualBoardWidth;
}
