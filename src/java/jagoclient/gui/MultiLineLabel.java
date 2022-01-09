package jagoclient.gui;

import java.awt.*;
import java.util.*;


public class MultiLineLabel extends Canvas {
    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;
    private String text;
    private String lines[];
    private int num_lines;
    private int line_height;
    private int line_ascent;
    private int line_widths[];
    private int max_width;
    private int alignment;
    private boolean border;
    private int topBottomMargin;
    private int leftRightMargin;
    private int x = 0;
    private int y = 0;
    Dimension offDimension;
    Image offImage;
    Graphics offGraphics;
    Color borderColor = new Color(0).black;

    public MultiLineLabel(String s, int i, boolean b) {
        // s the label
        // i alignement MultiLineLabel.CENTER, MultiLineLabel.RIGHT,
        //   MultiLineLabel.LEFT
        //   default MultiLineLabel.LEFT
        // b border present or not
        setAlignment(i);
        setText(s);
        setBorder(b);
    }

    public MultiLineLabel(String string, int i) {
        this(string, i, false);
    }

    public MultiLineLabel(String string) {
        this(string, 0);
    }

    public MultiLineLabel() {
        this("", 0);
    }

    public void addNotify() {
        super.addNotify();
        calc();
    }

    public void setX(int i) {
        x = i;
    }

    public void setY(int i) {
        y = i;
    }


    public int getLeftRightMargin() {
        return leftRightMargin;
    }

    public void setLeftRightMargin(int i) {
        // make sense only if alignment is MultiLineLabel.LEFT!
        if (i >= 0) leftRightMargin = i;
    }

    public int getAlignment() {
        return alignment;
    }

    public void setAlignment(int i) {
        switch (alignment) {
            case 0:
            case 1:
            case 2:
                alignment = i;
                break;
            default:
                throw new IllegalArgumentException();
        }
        repaint();
    }

    public int getTopBottomMargin() {
        return topBottomMargin;
    }

    public void setTopBottomMargin(int i) {
        if (i >= 0) topBottomMargin = i;
    }

    public void setFont(Font font) {
        super.setFont(font);
        calc();
        repaint();
    }

    public Dimension getMinimumSize() {
        Dimension d = new Dimension
                (max_width + leftRightMargin * 2,
                        num_lines * line_height + topBottomMargin * 2);
        if (d.width == 0) d.width = 10;
        if (d.height == 0) d.height = 10;
        return d;
    }

    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    public boolean getBorder() {
        return border;
    }

    public void setBorder(boolean flag) {
        border = flag;
    }

    public void setText(String s) {
        // parse the string , "\n" is a the line separator
        StringTokenizer st =
                new StringTokenizer(s, "\n");
        num_lines = st.countTokens();
        lines = new String[num_lines];
        line_widths = new int[num_lines];
        for (int i = 0; i < num_lines; i++)
            lines[i] = st.nextToken();
        calc();
        repaint();
        text = new String(s);
    }

    public String getText() {
        return text;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color c) {
        borderColor = c;
    }

    private void calc() {
        // calc dimension and extract maximum width
        Font f = getFont();
        if (f != null) {
            FontMetrics fm = getFontMetrics(f);
            if (fm != null) {
                line_height = fm.getHeight();
                line_ascent = fm.getAscent();
                max_width = 0;
                for (int i = 0; i < num_lines; i++) {
                    line_widths[i] =
                            fm.stringWidth(lines[i]);
                    if (line_widths[i] > max_width)
                        max_width = line_widths[i];
                }
            }
        }
    }

    public void update(Graphics g) {
        super.paint(g);
        Dimension d = getSize();
        if ((offGraphics == null) ||
                (d.width != offDimension.width) ||
                (d.height != offDimension.height)
                ) {
            offDimension = d;
            offImage = createImage(d.width, d.height);
            offGraphics = offImage.getGraphics();
            ((Graphics2D)offGraphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        }
        offGraphics.setColor(getBackground());
        offGraphics.fillRect
                (x, y, getSize().width - 1,
                        getSize().height - 1);
        if (border) {
            offGraphics.setColor(borderColor);
            offGraphics.drawRect
                    (x, y, getSize().width - 1, getSize().height - 1);
        }
        int j = line_ascent +
                (d.height - num_lines * line_height) / 2;
        for (int k = 0; k < num_lines;) {
            int i;
            switch (alignment) {
                case 0:
                    i = 0;
                    break;
                case 2:
                    i = d.width - line_widths[k];
                    break;
                default:
                    i = (d.width - line_widths[k]) / 2;
                    break;
            }
            i += leftRightMargin;
            offGraphics.setColor(getForeground());
            offGraphics.drawString(lines[k], i + x, j + y);
            k++;
            j += line_height;
        }
        g.drawImage(offImage, 0, 0, this);
    }

    public void paint(Graphics g) {
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        update(g);
    }

    public static void main(String args[]) {
        Frame f = new Frame("Test MultiLineLabel");
        f.setSize(200, 200);
        f.setLayout(new FlowLayout());
        f.setVisible(true);

        MultiLineLabel mll1 = new MultiLineLabel
                ("This a test!\nsecond line\nthird line",
                        MultiLineLabel.LEFT, true);
        // mll1.setBorderColor(new Color(0).blue);
        mll1.setLeftRightMargin(15);
        mll1.setTopBottomMargin(15);
        f.add(mll1);

        Button b = new Button("Dummy");
        f.add(b);

        MultiLineLabel mll2 = new MultiLineLabel
                ("123\n4\n567", MultiLineLabel.RIGHT, false);
        mll2.setForeground(new Color(0).yellow);
        mll2.setBackground(new Color(0).black);
        f.add(mll2);

        f.validate();
    }
}
