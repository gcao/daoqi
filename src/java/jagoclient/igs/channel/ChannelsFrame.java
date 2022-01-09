package jagoclient.igs.channel;

import jagoclient.Global;
import jagoclient.gui.*;
import jagoclient.igs.ChannelDistributor;
import jagoclient.igs.ConnectionFrame;
import jagoclient.igs.Distributor;
import jagoclient.igs.IgsStream;
import jagoclient.igs.who.MatchQuestion;
import jagoclient.igs.who.TellQuestion;
import rene.util.list.ListClass;
import rene.util.list.ListElement;
import rene.viewer.Lister;
import rene.viewer.SystemLister;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2006-1-24
 * Time: 12:26:28
 * To change this template use File | Settings | File Templates.
 */
public class ChannelsFrame extends CloseFrame
		implements ChannelsDistributorListener, CloseListener {
// ------------------------------ FIELDS ------------------------------

	IgsStream igsStream;
	PrintWriter igsWriter;
	Lister channelLister;
	Lister playerLister;
	ListClass channelList;
	ConnectionFrame connectionFrame;
	ChannelsDistributor channelsDistributor;
	TextField titleField;

// --------------------------- CONSTRUCTORS ---------------------------

	public ChannelsFrame(ConnectionFrame connectionFrame, PrintWriter out, IgsStream in) {
		super(Global.resourceString("Channels"));
		connectionFrame.addCloseListener(this);
		igsStream = in;
		igsWriter = out;
		setLayout(new BorderLayout());
		channelLister = Global.getParameter("systemlister", false) ? new SystemLister() : new Lister();
		channelLister.setFont(Global.Monospaced);
		channelLister.setBackground(Global.gray);
		channelLister.setText("");
		channelLister.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				String s = channelLister.getSelectedItem();
				int channelNo = Channel.getChannelNo(s);
				Channel channel = ChannelsFrame.this.connectionFrame.channelManager.getChannel(channelNo);
				if (e.getClickCount() == 2) { // double click
					igsStream.out("; \\" + channelNo);
					doclose();
					openChannelDialog(channelNo, channel.getTitle());
				} else {
					playerLister.setText("");
					java.util.List members = channel.getMembers();
					if (members == null || members.size() == 0) {
						playerLister.setVisible(false);
					} else {
						for (int i = 0; i < members.size(); i++) {
							String member = (String) members.get(i);
							if (channel.getOwner() != null && channel.getOwner().equals(member)) {
								playerLister.appendLine(member, Color.blue);
							} else {
								playerLister.appendLine(member);
							}
						}
						playerLister.setVisible(true);
					}
					validate();
					setVisible(true);
				}
			}
		});
		add("Center", channelLister);
		playerLister = Global.getParameter("systemlister", false) ? new SystemLister() : new Lister();
		playerLister.setFont(Global.Monospaced);
		playerLister.setBackground(Global.gray);
		playerLister.setText("            ");
		playerLister.hideHorizontalScrollbar();
		playerLister.setVisible(false);
		PopupMenu playerPop = new PopupMenu();
		addpop(playerPop, Global.resourceString("Tell"), "Tell");
		addpop(playerPop, Global.resourceString("Match"), "Match");
		playerLister.setPopupMenu(playerPop);
		add("East", playerLister);
		Panel p = new MyPanel();
		p.add(new ButtonAction(this, Global.resourceString("Create_Channel")));
		p.add(titleField = new TextField("", 20));
		p.add(new MyLabel("  "));
		p.add(new ButtonAction(this, Global.resourceString("Refresh")));
		p.add(new ButtonAction(this, Global.resourceString("Close")));
		add("South", new Panel3D(p));
		this.connectionFrame = connectionFrame;
		//PopupMenu pop = new PopupMenu();
		//addPopup(pop, Global.resourceString("Join"), "Join");
		//channelLister.setPopupMenu(pop);
		channelList = new ListClass();
	}

	public void doclose() {
		super.doclose();
		connectionFrame.channelsDistributor.removeListener(this);
		connectionFrame.channelsFrame = null;
	}

	private void openChannelDialog(int channelNo, String channelTitle) {
		// open ChannelDialog
		Distributor dis = igsStream.findDistributor(32);
		if (dis == null) {
			dis = new ChannelDistributor(ChannelsFrame.this.connectionFrame, igsStream, igsWriter, channelNo, channelTitle);
		} else if (dis.game() != channelNo) {
			dis.unchain();
			((ChannelDistributor) dis).channelDialog.doclose();
			igsWriter.println("; \\" + channelNo);
			dis = new ChannelDistributor(ChannelsFrame.this.connectionFrame, igsStream, igsWriter, channelNo, channelTitle);
		} else {
			((ChannelDistributor) dis).channelDialog.requestFocus();
		}
		// set channel title accordingly
	}

	public void addpop(PopupMenu pop, String label, String action) {
		MenuItem mi = new MenuItemAction(this, label, action);
		pop.add(mi);
	}

// --------------------- GETTER / SETTER METHODS ---------------------

	public ChannelsDistributor getChannelsDistributor() {
		return channelsDistributor;
	}

// ------------------------ INTERFACE METHODS ------------------------

// --------------------- Interface ChannelsDistributorListener ---------------------

	public void begin() {
		clear();
	}

	/**
	 * When the distributor has all channels, it calls end
	 */
	public synchronized void end() {
		ListElement channel = channelList.first();
		while (channel != null) {
			channel = channel.next();
		}
		channel = channelList.first();
		while (channel != null) {
			int channelNo = Channel.getChannelNo((String) channel.content());
			if (channelNo == connectionFrame.channelManager.getActiveChannelNo()) {
				channelLister.appendLine((String) channel.content(), Color.BLUE);
			} else {
				channelLister.appendLine((String) channel.content());
			}
			channel = channel.next();
		}
		channelLister.doUpdate(false);
	}

	public synchronized void send(String s) {
		if (s.indexOf("Title:") >= 0)
			channelList.append(new ListElement(s));
	}

// --------------------- Interface CloseListener ---------------------

	public void isClosed() {
		if (Global.getParameter("menuclose", true))
			setMenuBar(null);
		setVisible(false);
		dispose();
	}

// --------------------- Interface DoActionListener ---------------------

	public void doAction(String o) {
		if (Global.resourceString("Refresh").equals(o)) {
			refresh();
		} else if ("Tell".equals(o)) {
			String user = playerLister.getSelectedItem().trim();
			if (user.equals("")) return;
			new TellQuestion(this, connectionFrame, user);
		} else if ("Match".equals(o)) {
			String user = playerLister.getSelectedItem().trim();
			if (user.equals("")) return;
			new MatchQuestion(this, connectionFrame, user);
		} else if (Global.resourceString("Create_Channel").equals(o)) {
			// close old ChannelDialog
			Distributor dis = igsStream.findDistributor(32);
			if (dis != null) {
				dis.unchain();
				((ChannelDistributor) dis).channelDialog.doclose();
			}
			// find a unused channel no
			int channelNo = (int) (Math.random() * 100);
			for (int i = 0; i < 10; i++) {
				if (connectionFrame.channelManager.getChannel(channelNo) == null)
					break;
				channelNo = (int) (Math.random() * 100);
			}
			if (connectionFrame.channelManager.getChannel(channelNo) != null)
				return;
			// enter the channel
			//   ; \23
			igsStream.out("; \\" + channelNo);
			// set channelTitle
			//   channel 23 channelTitle XXX
			String channelTitle = titleField.getText().trim();
			igsStream.out("channel " + channelNo + " title " + channelTitle);
			// close ChannelsFrame and open ChannelDialog
			doclose();
			openChannelDialog(channelNo, channelTitle);
		} else {
			super.doAction(o);
		}
	}

// -------------------------- OTHER METHODS --------------------------

	public void addPopup(PopupMenu pop, String label, String action) {
		MenuItem mi = new MenuItemAction(this, label, action);
		pop.add(mi);
	}

	public synchronized void clear() {
		channelList = new ListClass();
		channelLister.setText("");
		playerLister.setText("");
	}

	public synchronized void refresh() {
		connectionFrame.channelsDistributor.refresh();
	}
}
