package util;

import java.util.List;

import com.restfb.Connection;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookException;
import com.restfb.types.Post;

/**
 * Token verification
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class Token {
	private static FacebookClient facebookClient;

	private static List<Post> fetchPagePosts(String pageId) {
		Connection<Post> feed = facebookClient.fetchConnection(
				pageId + "/feed", Post.class, Parameter.with("limit", 2));
		return feed.getData();
	}

	/**
	 * Check if an access token is still valid
	 * 
	 * @param fbClient
	 *            - facebook client object
	 * @return boolean - True or False
	 */
	public static boolean isValid(FacebookClient fbClient) {
		try {
			facebookClient = fbClient;
		} catch (FacebookException e) {
			e.printStackTrace();
		}

		// using Facebook official page to verify
		return fetchPagePosts("123739327653840").isEmpty() ? false : true;
	}
}
