package jagoclient.igs.channel;

import jagoclient.Dump;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2006-1-24
 * Time: 13:07:22
 * To change this template use File | Settings | File Templates.
 */
public class Channel {
// ------------------------------ FIELDS ------------------------------

	protected int no;
	protected String title;
	protected String owner;
	protected String membersRaw; // intermediate data, lazily parsed
	protected List members;
	protected String channelRaw; // intermediate data, lazily parsed

// --------------------------- CONSTRUCTORS ---------------------------

	public Channel(String raw) {
		no = getChannelNo(raw);
		this.channelRaw = raw;
	}

	public Channel(int no) {
		this.no = no;
	}

// --------------------- GETTER / SETTER METHODS ---------------------

	public List getMembers() {
		if (membersRaw != null) {
			membersRaw = membersRaw.trim();
			String[] parts = membersRaw.split("\\s+");
			members = new ArrayList();
			for (int i = 1; i < parts.length; i++) {
				members.add(parts[i]);
			}
			membersRaw = null;
		}
		return members;
	}

	public void setMembers(List members) {
		this.members = members;
	}

	public String getMembersRaw() {
		return membersRaw;
	}

	public void setMembersRaw(String membersRaw) {
		this.membersRaw = membersRaw;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getOwner() {
		parseChannelRaw();
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * channelRaw is like
	 * #1 Title: Professional Channel -- Open
	 * #25 Title: Untitled -- Open
	 * #33 Title: (malf) Doggie on Desk -- Open
	 * <p/>
	 * Test it on
	 * http://www.fileformat.info/tool/regex.htm
	 */
	private void parseChannelRaw() {
		if (channelRaw != null) {
			Dump.println(channelRaw);
			Pattern pattern = Pattern.compile("#\\d+\\s+Title: (\\((\\w+)\\))?([^-]+)-.*");
			Matcher matcher = pattern.matcher(channelRaw);
			if (matcher.matches()) {
				owner = matcher.group(2);
				title = matcher.group(3);
			}
			channelRaw = null;
		}
	}

	public String getTitle() {
		parseChannelRaw();
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

// ------------------------ CANONICAL METHODS ------------------------

	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("Channel");
		sb.append("{no=").append(no);
		sb.append(", title='").append(getTitle()).append('\'');
		sb.append(", owner='").append(getOwner()).append('\'');
		sb.append(", members='").append(getMembers()).append('\'');
		sb.append('}');
		return sb.toString();
	}

	public static int getChannelNo(String raw) {
		if (raw == null)
			throw new IllegalArgumentException("raw channel is null");
		raw = raw.trim();
		return Integer.parseInt(raw.substring(1, raw.indexOf(" ")));
	}
}
