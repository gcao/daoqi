package jagoclient;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2004-12-22
 * Time: 13:56:01
 */
public class GameResultInput {
    int total;
    int totalMoves;

    // stones
    int blackStones;
    int whiteStones;

    // Mu Shu
    int blackMokus;
    int whiteMokus;

    // living groups
    int blackGroups;
    int whiteGroups;

    // non occupied points
    int nonOccupiedCount;

    // Shuang Huo Gong Qi
    int sharedLiberties;

    int handicaps;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalMoves() {
        return totalMoves;
    }

    public void setTotalMoves(int totalMoves) {
        this.totalMoves = totalMoves;
    }

    public int getBlackStones() {
        return blackStones;
    }

    public void setBlackStones(int blackStones) {
        this.blackStones = blackStones;
    }

    public int getWhiteStones() {
        return whiteStones;
    }

    public void setWhiteStones(int whiteStones) {
        this.whiteStones = whiteStones;
    }

    public int getBlackMokus() {
        return blackMokus;
    }

    public void setBlackMokus(int blackMokus) {
        this.blackMokus = blackMokus;
    }

    public int getWhiteMokus() {
        return whiteMokus;
    }

    public void setWhiteMokus(int whiteMokus) {
        this.whiteMokus = whiteMokus;
    }

    public int getBlackGroups() {
        return blackGroups;
    }

    public void setBlackGroups(int blackGroups) {
        this.blackGroups = blackGroups;
    }

    public int getWhiteGroups() {
        return whiteGroups;
    }

    public void setWhiteGroups(int whiteGroups) {
        this.whiteGroups = whiteGroups;
    }

    public int getNonOccupiedCount() {
        return nonOccupiedCount;
    }

    public void setNonOccupiedCount(int nonOccupiedCount) {
        this.nonOccupiedCount = nonOccupiedCount;
    }

    public int getSharedLiberties() {
        return sharedLiberties;
    }

    public void setSharedLiberties(int sharedLiberties) {
        this.sharedLiberties = sharedLiberties;
    }

    public int getHandicaps() {
        return handicaps;
    }

    public void setHandicaps(int handicaps) {
        this.handicaps = handicaps;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<GameResultInput>\n");
        sb.append("handicaps: ").append(handicaps).append("\n");
        sb.append("totalMoves: ").append(totalMoves).append("\n");
        sb.append("sharedLiberties: ").append(sharedLiberties).append("\n");
        sb.append("nonOccupiedCount: ").append(nonOccupiedCount).append("\n");
        sb.append("blackGroups: ").append(blackGroups).append("\n");
        sb.append("blackMokus: ").append(blackMokus).append("\n");
        sb.append("blackStones: ").append(blackStones).append("\n");
        sb.append("whiteGroups: ").append(whiteGroups).append("\n");
        sb.append("whiteMokus: ").append(whiteMokus).append("\n");
        sb.append("whiteStones: ").append(whiteStones).append("\n");
        sb.append("</GameResultInput>\n");
        return sb.toString();
    }
}
