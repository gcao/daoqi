import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Created by IntelliJ IDEA.
 * User: Guoliang Cao
 * Date: 2006-3-3
 * Time: 18:07:34
 * To change this template use File | Settings | File Templates.
 */
public class Test {
	public static void main(String[] args) throws Exception {
		Pattern pattern = Pattern.compile("<(br|p)>", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher("<br><BR><p>abc");
		System.out.println(matcher.replaceAll(""));
	}
}
