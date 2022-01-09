package jagoclient.igs;

import jagoclient.Global;

import java.io.PrintWriter;

/**
 * This distributor opens a ChannelDialog for channel n. IgsStream
 * sorts out the channels and calls the correct distributor.
 *
 * @see ChannelDialog
 */
public class ChannelDistributor extends Distributor {
// ------------------------------ FIELDS ------------------------------
	ConnectionFrame connectionFrame;
	PrintWriter igsWriter;
	public ChannelDialog channelDialog;

// --------------------------- CONSTRUCTORS ---------------------------

	public ChannelDistributor(ConnectionFrame connectionFrame, IgsStream igsStream, PrintWriter igsWriter, int channelNo, String channelTitle) {
		super(igsStream, 32, channelNo, false);
		this.connectionFrame = connectionFrame;
		this.connectionFrame.channelManager.setActiveChannelNo(channelNo);
		this.igsWriter = igsWriter;
		String title = Global.resourceString("Channel") + channelNo + " - " + channelTitle;
		this.channelDialog = new ChannelDialog(this.connectionFrame, this.igsWriter, game(), this, title);
		this.connectionFrame.channelsDistributor.addListener(channelDialog);
	}

// -------------------------- OTHER METHODS --------------------------

	public void send(String C) {
		//if (CD == null) {
		//	CD = new ChannelDialog(CF, Out, game(), this);
		//}
		channelDialog.append(C);
	}
}

