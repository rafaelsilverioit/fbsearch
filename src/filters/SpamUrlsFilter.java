package filters;

import java.util.List;

import persistence.entities.SimplifiedPost;
import persistence.entities.Post;

/**
 * Remove posts that contains urls from blacklisted sites
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class SpamUrlsFilter implements PostFilter {
	private final List<String> spamUrls;

	public SpamUrlsFilter(List<String> spamUrls) {
		this.spamUrls = spamUrls;
	}

	public SpamUrlsFilter() {
		this.spamUrls = null;
	}

	@Override
	public List<Post> filter(List<Post> posts) {
		for (Post post : posts) {
			SimplifiedPost simplePost = (SimplifiedPost) post;

			// Apply filter if post has URL and does not have (S)pam tag
			if (simplePost.getUrlQuantity() > 0
					&& simplePost.getPostType() != 'S') {
				String url = simplePost.getUrlsFromPost().get(0).getUrl();

				try {
					String cleanUrl = cleanProtocolUrl(url);

					if (hasSpamUrl(cleanUrl)) {
						// it has a black listed url
						simplePost.setPostType('S'); // (S)pam tag
					} else {
						// simplePost.setPostType('A'); // (A)lert tag
					}
				} catch (Exception e) {
					System.out.println("Error: " + e.getMessage());
				}
			}
		}
		return posts;
	}

	/**
	 * Verify if url starts with a black listed domain
	 * 
	 * @param url
	 *            - analyzed url
	 * @return boolean - it has a black listed domain or not
	 */
	private boolean hasSpamUrl(String url) {
		for (String spamUrl : spamUrls) {
			if (url.startsWith(spamUrl)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Remove http, https or www from a url
	 * 
	 * @param url
	 *            - long url
	 * @return url - cleaned url
	 */
	public String cleanProtocolUrl(String url) {
		if (url.startsWith("http://www."))
			return url.substring(11);
		if (url.startsWith("https://www."))
			return url.substring(12);
		if (url.startsWith("http://"))
			return url.substring(7);
		if (url.startsWith("https://"))
			return url.substring(8);

		return url;
	}
}