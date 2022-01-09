package jagoclient.board;

import jagoclient.Global;
import jagoclient.StopThread;

import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;

/**
 * This is used to create an image of the empty bard at startup or when
 * a board is needed (request from Board via WoodPaint).
 *
 * @see jagoclient.board.WoodPaint
 */

public class EmptyVirtualRealBoardPaint extends StopThread {
    static Board board;
    static int width, height, virtualBoardWidth;
    static int ox, oy, d;
    static Color realBoardColor;
    static Color virtualBoardColor;
    static boolean shadow;
    static boolean mosaic;
    static Image staticImage = null;
    static Image staticShadowImage = null;

    EmptyVirtualRealBoardPaint(Board b, int w, int h, int vbw, Color rbc, Color vbc, boolean shadows, int ox, int oy, int d, boolean m) {
        board = b;
        width = w;
        height = h;
        virtualBoardWidth = vbw;
        realBoardColor = rbc;
        virtualBoardColor = vbc;
        shadow = shadows;
        mosaic = m;
        EmptyVirtualRealBoardPaint.ox = ox;
        EmptyVirtualRealBoardPaint.oy = oy;
        EmptyVirtualRealBoardPaint.d = d;
        start();
    }

    public void run() {
        try {
            setPriority(getPriority() - 1);
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            sleep(100);
        } catch (Exception e) {
        }
        createwood(this);
        if (!stopped()) board.updateboard();
    }

    /**
     * Create an image of the wooden board. The component is used
     * to create the image.
     */
    static public void createwood(StopThread EPT) {
        if (width == 0 || height == 0) return;
        staticImage = staticShadowImage = null;
        int p[] = new int[width * height];
        int ps[] = null;
        if (shadow) ps = new int[width * height];
        int i, j;
        double f = 9e-1;
        int rbBlue = realBoardColor.getBlue(), rbGreen = realBoardColor.getGreen(), rbRed = realBoardColor.getRed();
        int vbBlue = virtualBoardColor.getBlue(), vbGreen = virtualBoardColor.getGreen(), vbRed = virtualBoardColor.getRed();
        int blue, green, red;
        double r, g, b;
        double x, y, dist;
        boolean fine = Global.getParameter("fineboard", true);
        int rbStartx = virtualBoardWidth, rbEndx = width - virtualBoardWidth;
        int rbStarty = virtualBoardWidth, rbEndy = height - virtualBoardWidth;
        for (i = 0; i < height; i++)
            for (j = 0; j < width; j++) {
                if (mosaic) {
                    boolean b1 = i >= rbStartx && i < rbEndx;
                    boolean b2 = j >= rbStartx && j < rbEndx;
                    if (b1 && b2 || !(b1 || b2)) {
                        blue = rbBlue;
                        green = rbGreen;
                        red = rbRed;
                    } else {
                        blue = vbBlue;
                        green = vbGreen;
                        red = vbRed;
                    }
                } else {
                    if (i >= rbStartx && i < rbEndx && j >= rbStarty && j < rbEndy) {
                        blue = rbBlue;
                        green = rbGreen;
                        red = rbRed;
                    } else {
                        blue = vbBlue;
                        green = vbGreen;
                        red = vbRed;
                    }
                }

                if (fine)
                    f = ((Math.sin(18 * (double) j / width) + 1) / 2
                            + (Math.sin(3 * (double) j / width) + 1) / 10
                            + 0.2 * Math.cos(5 * (double) i / height) +
                            +0.1 * Math.sin(11 * (double) i / height))
                            * 20 + 0.5;
                else
                    f = ((Math.sin(14 * (double) j / width) + 1) / 2
                            + 0.2 * Math.cos(3 * (double) i / height) +
                            +0.1 * Math.sin(11 * (double) i / height))
                            * 10 + 0.5;
                f = f - Math.floor(f);
                if (f < 2e-1)
                    f = 1 - f / 2;
                else if (f < 4e-1)
                    f = 1 - (4e-1 - f) / 2;
                else
                    f = 1;
                if (i == width - 1 || (i == width - 2 && j < width - 2) || j == 0
                        || (j == 1 && i > 1))
                    f = f / 2;
                if (i == 0 || (i == 1 && j > 1) || j >= width - 1
                        || (j == width - 2 && i < height - 1)) {
                    r = 128 + red * f / 2;
                    g = 128 + green * f / 2;
                    b = 128 + blue * f / 2;
                } else {
                    r = red * f;
                    g = green * f;
                    b = blue * f;
                }
                p[width * i + j] = (255 << 24) | ((int) (r) << 16) | ((int) (g) << 8) | (int) (b);
                if (shadow) {
                    f = 1;
                    y = Math.abs((i - (ox + d / 2 + (i - ox) / d * (double) d)));
                    x = Math.abs((j - (oy + d / 2 + (j - oy) / d * (double) d)));
                    dist = Math.sqrt(x * x + y * y) / d * 2;
                    if (dist < 1.0) f = 0.9 * dist;
                    if (j <= width - d / 2) {
                        ps[width * i + j] = (255 << 24) | ((int) (r * f) << 16) | ((int) (g * f) << 8) | (int) (b * f);
                    } else {
                        ps[width * i + j] = p[width * i + j];
                    }
                }
                if (EPT.stopped()) return;
            }
        if (shadow) {
            staticShadowImage = board.createImage(new MemoryImageSource(width, height, ColorModel.getRGBdefault(),
                    ps, 0, width));
        }
        staticImage = board.createImage(new MemoryImageSource(width, height, ColorModel.getRGBdefault(),
                p, 0, width));

    }

    static boolean haveImage(int w, int h, int vbw, Color c, Color vbc, int ox, int oy, int d, boolean m) {
        if (staticImage == null) return false;
        return w == width && h == height && vbw == virtualBoardWidth
                && ox == EmptyVirtualRealBoardPaint.ox && oy == EmptyVirtualRealBoardPaint.oy
                && d == EmptyVirtualRealBoardPaint.d && c.getRGB() == realBoardColor.getRGB()
                && vbc.getRGB() == virtualBoardColor.getRGB() && mosaic == m;
    }
}
