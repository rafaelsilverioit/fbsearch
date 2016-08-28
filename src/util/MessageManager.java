package util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.restfb.types.Post;

import filters.SpamUrlsFilter;

/**
 * Some methods to process facebook messages
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class MessageManager {

	/**
	 * Extract URLs from post using REGEX
	 * 
	 * @param post
	 *            - a single post to be analyzed
	 * @return url - A JSONArray with all urls extracted
	 */
	@SuppressWarnings("unchecked")
	public static JSONArray getUrlsFromPost(Post post) {
		JSONArray urlsArray = new JSONArray();
		JSONObject jObj;
		if (post.getMessage() != null) {
			try {
				Set<String> tokens = new HashSet<>();

				// extract URLs
				String regex = "\\(?\\b(https?://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
				Pattern pa = Pattern.compile(regex);
				Matcher m = pa.matcher(post.getMessage());

				while (m.find()) {
					tokens.add(m.group());
				}

				for (String sta : tokens) {
					jObj = new JSONObject();
				//	Runtime.getRuntime().exec(new String[]{"wkhtmltoimage", "--disable-javascript",
				//			"--crop-h 13000", "--crop-w 10000", "--width 1280", "--height 768 ",
				//			sta + " " + post.getId() + "_" + sta + ".png"});
					jObj.put("url", SpamUrlsFilter.class.newInstance()
							.cleanProtocolUrl(sta));
					urlsArray.add(jObj);
				}

				return urlsArray;

			} catch (Exception ex) {

			}
		}

		return urlsArray;
	}

	/**
	 * Extract hashtags from post
	 * 
	 * @param post
	 *            - A single post to be analyzed
	 * @return hashtags - A JSONArray with all hashtags extracted
	 */
	@SuppressWarnings("unchecked")
	public static JSONArray getHashtagsFromPost(Post post) {
		JSONArray hashtagsArray = new JSONArray();
		JSONObject jO;

		if (post.getMessage() != null) {
			try {
				Set<String> tokens = new HashSet<>();

				// extract Hashtags
				String regex = "(#)((?:[A-Za-z0-9-_]*))";
				Pattern pa = Pattern.compile(regex);
				Matcher m = pa.matcher(post.getMessage());

				while (m.find()) {
					tokens.add(m.group());
				}

				for (String sta : tokens) {
					jO = new JSONObject();
					jO.put("text", sta);
					hashtagsArray.add(jO);
				}

				return hashtagsArray;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return hashtagsArray;
	}

	/**
	 * Remove RT, urls, mentions
	 * 
	 * @param text
	 *            original post message
	 * @return clean post message
	 */
	public static String clean(String text) {
		// regex to find URL, RT and Mentions
		String regex = "\\(?\\b(https?://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]|RT:?|^@\\w+|\\s@\\w+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);

		return m.replaceAll("");
	}
}