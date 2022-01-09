package jagoclient;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2005-12-4
 * Time: 14:14:07
 * To change this template use File | Settings | File Templates.
 */
public interface Defaults {
// ------------------------------ FIELDS ------------------------------

    public static final String SGF_GM_DAOQI = "10";
    public static final int VIRTUAL_BOARD_WIDTH = 3;
	public static final int DEFAULT_PORT = 6969;
	public static final String DEFAULT_PORT_S = "6969";
	public static final String IGS_HOST = "igs.joyjoy.net";
	public static final String IGS_PORT_S = "6969";
	public static final int TOTAL_TIME = 40; // 40 minutes
	public static final int EXTRA_TIME = 20; // 20 minutes for every 25 moves
    public static final boolean MOSAIC_BOARD = false;
    public static final Color BOARD_LINE_COLOR = Color.black;
    public static final Color VIRTUAL_BOARD_LINE_COLOR = Color.black;
}
