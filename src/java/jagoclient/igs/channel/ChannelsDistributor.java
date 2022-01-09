package jagoclient.igs.channel;

import jagoclient.igs.ConnectionFrame;
import jagoclient.igs.Distributor;
import jagoclient.igs.IgsStream;

import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2006-1-24
 * Time: 13:17:33
 * To change this template use File | Settings | File Templates.
 */
public class ChannelsDistributor extends Distributor {
// ------------------------------ FIELDS ------------------------------

	/**
	 * use a different id than 9(which is shared in IGS protocol)
	 */
	public static final int ID = 109;
	ConnectionFrame connectionFrame;
	List listeners = new Vector();

// --------------------------- CONSTRUCTORS ---------------------------

	public ChannelsDistributor(ConnectionFrame connectionFrame, IgsStream in) {
		super(in, ID, 0, false);
		this.connectionFrame = connectionFrame;
	}

// -------------------------- OTHER METHODS --------------------------

	public synchronized void addListener(ChannelsDistributorListener listener) {
		listeners.add(listener);
	}

	public synchronized void allsended() {
		for (int i = 0; i < listeners.size(); i++) {
			ChannelsDistributorListener listener = (ChannelsDistributorListener) listeners.get(i);
			listener.end();
		}
	}

	public synchronized void refresh() {
		getIgsStream().out("channels");
		for (int i = 0; i < listeners.size(); i++) {
			ChannelsDistributorListener listener = (ChannelsDistributorListener) listeners.get(i);
			listener.begin();
		}
	}

	public synchronized void removeListener(ChannelsDistributorListener listener) {
		listeners.remove(listener);
	}

	public synchronized void send(String c) {
		for (int i = 0; i < listeners.size(); i++) {
			ChannelsDistributorListener listener = (ChannelsDistributorListener) listeners.get(i);
			listener.send(c);
		}
	}
}
