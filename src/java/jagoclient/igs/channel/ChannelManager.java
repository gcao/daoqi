package jagoclient.igs.channel;

import java.util.Hashtable;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2006-1-24
 * Time: 18:40:56
 * To change this template use File | Settings | File Templates.
 */
public class ChannelManager implements ChannelsDistributorListener {
// ------------------------------ FIELDS ------------------------------

	protected Hashtable channelMap = new Hashtable();
	private int justAddedChannel;
	private int activeChannelNo;

// --------------------- GETTER / SETTER METHODS ---------------------

	public int getActiveChannelNo() {
		return activeChannelNo;
	}

	public void setActiveChannelNo(int activeChannelNo) {
		this.activeChannelNo = activeChannelNo;
	}

// ------------------------ INTERFACE METHODS ------------------------

// --------------------- Interface ChannelsDistributorListener ---------------------

	public void begin() {
		channelMap.clear();
	}

	public void end() {
	}

	/**
	 * @param c is in below format:
	 *          #33 Title: (malf) Doggie on Desk -- Open  = (channel title and status)
	 *          #33     BigDog       fool    escaper      = (players belong to this channel)
	 */
	public void send(String c) {
		if (c.indexOf("Title: ") >= 0) {
			Channel channel = new Channel(c);
			addChannel(channel);
			justAddedChannel = channel.no;
		} else {
			Channel channel = getChannel(justAddedChannel);
			if (channel != null)
				channel.setMembersRaw(c);
		}
	}

// -------------------------- OTHER METHODS --------------------------

	public void addChannel(Channel channel) {
		channelMap.put(new Integer(channel.getNo()), channel);
	}

	public void clear() {
		channelMap.clear();
	}

	public Channel getActiveChannel() {
		return getChannel(getActiveChannelNo());
	}

	public Channel getChannel(int no) {
		return (Channel) channelMap.get(new Integer(no));
	}

	public Iterator getChannelIterator() {
		return channelMap.values().iterator();
	}
}
