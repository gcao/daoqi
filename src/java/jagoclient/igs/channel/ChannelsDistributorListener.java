package jagoclient.igs.channel;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2006-1-30
 * Time: 10:23:07
 * To change this template use File | Settings | File Templates.
 */
public interface ChannelsDistributorListener {
	public void begin();

	public void end();

	public void send(String c);
}
