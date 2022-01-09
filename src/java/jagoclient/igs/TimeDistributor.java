package jagoclient.igs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeDistributor extends Distributor {
    ConnectionFrame F;
    int blackTime, blackMoves;
    int whiteTime, whiteMoves;
    static final Pattern TIME_LINE_PATTERN = Pattern.compile("\\s*(Black|White)\\(.*\\)\\s+:\\s+(\\d+):(\\d+)(\\s+\\(B\\)\\s+(\\d+))?");

    public TimeDistributor(ConnectionFrame f, IgsStream in) {
        super(in, 26, -1, false);
        F = f;
        In = in;
    }

    public void send(String cmd) {
        if (cmd.startsWith("Game")) {
            // Game : 116
            G = Integer.parseInt(cmd.substring(cmd.indexOf(": ")+1).trim());
        } else if (cmd.startsWith("Black")) {
            // Black(dqdq2) : 0:39
            parseTimeLine(cmd);
            // black line is the last line
            IgsGoFrame goFrame = F.getGoFrame(G);
            if (goFrame != null) {
                goFrame.BlackTime = blackTime;
                goFrame.BlackMoves = blackMoves;
                goFrame.BlackRun = 0;
                goFrame.WhiteTime = whiteTime;
                goFrame.WhiteMoves = whiteMoves;
                goFrame.WhiteRun = 0;
                goFrame.CurrentTime = System.currentTimeMillis();
                goFrame.settitle1();
            }
        } else if (cmd.startsWith("White")) {
            // White(dqdq1) : 0:45 (B) 24
            parseTimeLine(cmd);
        }
    }

    private void parseTimeLine(String line) {
        Matcher matcher = TIME_LINE_PATTERN.matcher(line);
        if (matcher.matches()) {
            int time = 0;
            time += 60 * Integer.parseInt(matcher.group(2));
            time += Integer.parseInt(matcher.group(3));
            int moves = 0;
            if (matcher.group(4) != null) {
                String s = matcher.group(4).trim();
                moves = Integer.parseInt(s.substring(s.indexOf(' ')).trim());
            }
            if (matcher.group(1).equals("Black")) {
                blackTime = time;
                blackMoves = moves;
            } else {
                whiteTime = time;
                whiteMoves = moves;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        //Matcher matcher = TIME_LINE_PATTERN.matcher("Black(dqdq2) : 0:39");
        Matcher matcher = TIME_LINE_PATTERN.matcher("White(dqdq1) : 0:45 (B) 24");
        System.out.println(matcher.matches());
        System.out.println(matcher.groupCount());
        for (int i = 0; i < matcher.groupCount(); i++) {
            System.out.println(i + ": " + matcher.group(i));
        }
    }
}
