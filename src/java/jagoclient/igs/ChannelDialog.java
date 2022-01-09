package jagoclient.igs;

import jagoclient.Global;
import jagoclient.gui.*;
import jagoclient.igs.channel.Channel;
import jagoclient.igs.channel.ChannelsDistributorListener;
import jagoclient.igs.who.MatchQuestion;
import jagoclient.igs.who.TellQuestion;
import rene.viewer.Lister;
import rene.viewer.SystemLister;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintWriter;
import java.util.List;

/**
 * Contains a text area and a text field for anwers.
 *
 * @see ChannelDistributor
 */
public class ChannelDialog extends CloseFrame
		implements CloseListener, KeyListener, ChannelsDistributorListener {
// ------------------------------ FIELDS ------------------------------

	PrintWriter igsWriter;
	TextArea display;
	Lister memberLister;
	ConnectionFrame connectionFrame;
	ChannelDistributor channelDistributor;
	int channelNo;
	HistoryTextField inputField;

// --------------------------- CONSTRUCTORS ---------------------------

	public ChannelDialog(ConnectionFrame cf, PrintWriter out, int n, ChannelDistributor mdis, String title) {
		super(title);
		connectionFrame = cf;
		channelDistributor = mdis;
		channelNo = n;
		connectionFrame.addCloseListener(this);
		setLayout(new BorderLayout());
		//MenuBar M = new MenuBar();
		//Menu help = new MyMenu(Global.resourceString("Help"));
		//help.add(new MenuItem(Global.resourceString("Channels")));
		//M.setHelpMenu(help);
		add("North", new MyLabel(title));
		Panel pm = new MyPanel();
		pm.setLayout(new BorderLayout());
		pm.add("Center", display = new MyTextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY));
		memberLister = Global.getParameter("systemlister", false) ? new SystemLister() : new Lister();
		memberLister.setFont(Global.Monospaced);
		memberLister.setBackground(Global.gray);
		memberLister.setText("            ");
		memberLister.hideHorizontalScrollbar();
		PopupMenu playerPop = new PopupMenu();
		addPopup(playerPop, Global.resourceString("Tell"), "Tell");
		addPopup(playerPop, Global.resourceString("Match"), "Match");
		memberLister.setPopupMenu(playerPop);
		pm.add("East", memberLister);
		pm.add("South", inputField = new HistoryTextField(this, "Answer"));
		add("Center", pm);
		Panel pb = new MyPanel();
		pb.add(new ButtonAction(this, Global.resourceString("Channels")));
		pb.add(new ButtonAction(this, Global.resourceString("Refresh")));
		pb.add(new ButtonAction(this, Global.resourceString("Close")));
		add("South", new Panel3D(pb));
		igsWriter = out;
		//seticon("iwho.gif");
		Global.setwindow(this, "channeldialog", 500, 400);
		validate();
		setVisible(true);
		connectionFrame.channelsDistributor.addListener(this);
		connectionFrame.channelsDistributor.refresh();
	}

	public void addPopup(PopupMenu pop, String label, String action) {
		MenuItem mi = new MenuItemAction(this, label, action);
		pop.add(mi);
	}

	private void populateMembers(Channel channel) {
		memberLister.setText("");
		if (channel != null) {
			memberLister.setText("");
			List members = channel.getMembers();
			// fix myself not showing when open the channel dialog.
			if (members != null && !members.contains(connectionFrame.username))
				memberLister.appendLine(connectionFrame.username);
			for (int i = 0; members != null && i < members.size(); i++) {
				memberLister.appendLine((String) members.get(i));
			}
		}
		memberLister.doUpdate(false);
	}

// ------------------------ INTERFACE METHODS ------------------------

// --------------------- Interface ChannelsDistributorListener ---------------------

	public void begin() {
		memberLister.setText("");
	}

	public void end() {
		populateMembers(connectionFrame.channelManager.getActiveChannel());
	}

	public void send(String c) {
	}

// --------------------- Interface CloseListener ---------------------

	public void isClosed() {
		doclose();
	}

// --------------------- Interface DoActionListener ---------------------

	public void doAction(String o) {
		if (Global.resourceString("Channels").equals(o)) {
			connectionFrame.openChannelsWindow();
			//new Help("channels");
		} else if (Global.resourceString("Refresh").equals(o)) {
			connectionFrame.channelsDistributor.refresh();
		} else if ("Tell".equals(o)) {
			String user = memberLister.getSelectedItem().trim();
			if (user.equals("")) return;
			new TellQuestion(this, connectionFrame, user);
		} else if ("Match".equals(o)) {
			String user = memberLister.getSelectedItem().trim();
			if (user.equals("")) return;
			new MatchQuestion(this, connectionFrame, user);
		} else if ("Answer".equals(o)) {
			if (!inputField.getText().equals("")) {
				igsWriter.println("; " + inputField.getText());
				display.append("---> " + inputField.getText() + "\n");
				inputField.setText("");
			}
		} else super.doAction(o);
	}

// --------------------- Interface KeyListener ---------------------

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE && close()) doclose();
	}

	public void keyReleased(KeyEvent e) {
		String s = Global.getFunctionKey(e.getKeyCode());
		if (s.equals("")) return;
		inputField.setText(s);
		inputField.requestFocus();
	}

// -------------------------- OTHER METHODS --------------------------

	public void append(String s) {
		display.append(s + "\n");
	}

	public boolean close() {
		return true;
	}

	public void doclose() {
		channelDistributor.unchain();
		connectionFrame.channelsDistributor.removeListener(this);
		// quit from channel
		igsWriter.println("; \\-1");
		channelDistributor.channelDialog = null;
		Global.notewindow(this, "channeldialog");
		super.doclose();
	}
}
