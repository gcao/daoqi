package jagoclient;

import jagoclient.board.GoFrame;
import jagoclient.board.LocalGoFrame;
import jagoclient.sound.JagoSound;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;

/**
Similar to Go. The main() method starts a GoFrame.
@see jagoclient.Go
@see jagoclient.board.GoFrame
 */
public class LocalGo {

    static GoFrame GF;

    public static void main(String[] args) {
        boolean homefound = false;
        String localgame = "";
        int na = 0;
        int move = -1;
        while (args.length > na) {
            if (args.length - na >= 2 && args[na].startsWith("-l")) {
                if (args.length > na + 1) {
                    String lang = args[na + 1];
                    if ("chinese".equalsIgnoreCase(lang)) {
                        Locale.setDefault(Locale.CHINESE);
                    } else if ("english".equalsIgnoreCase(lang)) {
                        Locale.setDefault(Locale.ENGLISH);
                    }
                }
                na += 2;
            } else if (args.length - na >= 2 && args[na].startsWith("-h")) {
                Global.home(args[na + 1]);
                na += 2;
                homefound = true;
            } else if (args[na].startsWith("-d")) {
                Dump.open("dump.dat");
                na++;
            } else {
                localgame = args[na];
                na++;
                if (args.length > na) {
                    try {
                        move = Integer.parseInt(args[na]);
                        na++;
                    } catch (Exception e) {
                    }
                }
                break;
            }
        }
        Global.setApplet(false);
        if (!homefound) {
            Global.home(System.getProperty("user.home"));
        }
        Global.readparameter("go.cfg");
        Global.createfonts();
        Global.frame(new Frame());
        JagoSound.play("high", "", true);
        if (!localgame.equals("")) {
            open(localgame, move);
        } else {
            GF = new LocalGoFrame(new Frame(), Global.resourceString("Local_Viewer"));
        }
        Global.setcomponent(GF);
    }

    static void openlocal(String file, int move) {
        GF = new LocalGoFrame(new Frame(), Global.resourceString("Local_Viewer"));
        GF.load(file, move);
    }

    static void open(String s, int move) {
        if (s != null && s.trim().length() > 0) {
            s = s.trim();
            if (s.toLowerCase().indexOf("http:") == 0 || s.toLowerCase().indexOf("https:") == 0) {
                openurl(s, move);
            } else {
                openlocal(s, move);
            }
        }
    }

    static void openurl(String url, int move) {
        GF = new LocalGoFrame(new Frame(), Global.resourceString("Local_Viewer"));
        try {
            GF.load(new URL(url).openStream(), move);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}