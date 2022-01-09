import java.awt.*;
import java.util.Random;

public class drawoval extends java.applet.Applet {
	private int[] x1;
	private int[] y1;
	private int[] sx1;
	private int[] sy1;
	private int[] x2;
	private int[] y2;
	private int[] sx2;
	private int[] sy2;
	private static final int nb_points = 50;
	private Random ran;
	private Image offscreenImg;
	private Graphics offscreenG;
	private int MAX = 250;
	private int TIMETOCHANGE = 20;
	private int temps = 0;
	private Color [] tabofcolor;
	private Object syncobj = new Object();

	int give_positive(int a) {
		if (a < 0) a = a * -1;
		return (a);
	}

	public drawoval() {
	}

	public void init() {
		tabofcolor = new Color[nb_points + 1];

		System.out.println(" init ");
		for (int i = 0; i <= nb_points; i++) {
			tabofcolor[i] = new Color((i * 128) / nb_points, 0, 255 - (i * 255) / nb_points);
		}
		// double buffering
		offscreenImg = createImage(400, 400);
		offscreenG = offscreenImg.getGraphics();
		((Graphics2D)offscreenG).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

		ran = new Random();
		x1 = new int[nb_points + 1];
		y1 = new int[nb_points + 1];
		sx1 = new int[nb_points + 1];
		sy1 = new int[nb_points + 1];
		x2 = new int[nb_points + 1];
		y2 = new int[nb_points + 1];
		sx2 = new int[nb_points + 1];
		sy2 = new int[nb_points + 1];
		initDots();
	}

	public void paint(Graphics g) {
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		int i;
		for (i = 0; i <= nb_points; i++) {
			plot(offscreenG, x1[i], y1[i], x2[i], y2[i], i);
		}
		moveDots();
		if (temps > TIMETOCHANGE) {
			sx1[nb_points] = ran.nextInt() % 10;
			sy1[nb_points] = ran.nextInt() % 10;
			sx2[nb_points] = ran.nextInt() % 10;
			sy2[nb_points] = ran.nextInt() % 10;
			temps = 0;
		} else temps = temps + 1;
		// only temporisation
		synchronized (syncobj) {
			try {
				syncobj.wait(50);
			} catch (InterruptedException e) {
			}
		}
		repaint();
	}

	public void update(Graphics g) {
		g.drawImage(offscreenImg, 0, 0, this);
		offscreenG.setColor(Color.white);
		offscreenG.fillRect(0, 0, 400, 400);
		paint(g);
	}

	public void plot(Graphics g, int x1, int y1, int x2, int y2, int i) {
		g.setColor(tabofcolor[i]);
		g.fillOval(give_positive((x1 - x2) / 2) + x1,
				give_positive((y1 - y2) / 2) + y1,
				give_positive(x2 - x1) / 2,
				give_positive(y2 - x1) / 2);
	}

	public void initDots() {
		int i;
		x1[nb_points] = give_positive(ran.nextInt() % 100);
		y1[nb_points] = give_positive(ran.nextInt() % 100);
		sx1[nb_points] = 4;
		sy1[nb_points] = 7;

		x2[nb_points] = give_positive(ran.nextInt() % 100);
		y2[nb_points] = give_positive(ran.nextInt() % 100);
		sx2[nb_points] = 5;
		sy2[nb_points] = 3;
		for (i = 1; i < nb_points; i++) {
			x1[i] = x1[nb_points];
			x2[i] = x2[nb_points];
			y1[i] = y1[nb_points];
			y2[i] = y2[nb_points];
			sx1[i] = sx1[nb_points];
			sy1[i] = sy1[nb_points];
			sx2[i] = sx2[nb_points];
			sy2[i] = sy2[nb_points];
		}

	}

	public void moveDots() {
		int i;

		i = nb_points;

		x1[i] += sx1[i];
		y1[i] += sy1[i];
		if (x1[i] > MAX) {
			sx1[i] = -sx1[i];
			x1[i] += sx1[i];
		}
		if (x1[i] < 0) {
			sx1[i] = -sx1[i];
			x1[i] += sx1[i];
		}
		if (y1[i] > MAX) {
			sy1[i] = -sy1[i];
			y1[i] += sy1[i];
		}
		if (y1[i] < 0) {
			sy1[i] = -sy1[i];
			y1[i] += sy1[i];
		}

		x2[i] += sx2[i];
		y2[i] += sy2[i];
		if (x2[i] > MAX) {
			sx2[i] = -sx2[i];
			x2[i] += sx2[i];
		}
		if (x2[i] < 0) {
			sx2[i] = -sx2[i];
			x2[i] += sx2[i];
		}
		if (y2[i] > MAX) {
			sy2[i] = -sy2[i];
			y2[i] += sy2[i];
		}
		if (y2[i] < 0) {
			sy2[i] = -sy2[i];
			y2[i] += sy2[i];
		}

		for (i = 1; i < nb_points + 1; i++) {
			x1[i - 1] = x1[i];
			x2[i - 1] = x2[i];
			y1[i - 1] = y1[i];
			y2[i - 1] = y2[i];
			sx1[i - 1] = sx1[i];
			sy1[i - 1] = sy1[i];
			sx2[i - 1] = sx2[i];
			sy2[i - 1] = sy2[i];
			sx1[i - 1] = sx1[i - 1] + 1;
			sx2[i - 1] = sx2[i - 1] + 1;
			sy1[i - 1] = sy1[i - 1] + 1;
			sy2[i - 1] = sy2[i - 1] + 1;
		}
	}

	public void start() {
	}

	public void stop() {
	}

	public static void main(String args[]) {
		Frame f = new Frame("Demo oval charles vidal");
		drawoval nf4 = new drawoval();
		f.add("Center", nf4);
		f.resize(400, 400);
		nf4.init();
		f.show();
		nf4.start();
	}
}