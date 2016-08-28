package filters;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import persistence.entities.SimplifiedPost;
import persistence.entities.Post;

/**
 * Remove posts that contains common spam words
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class SpamWordsFilter implements PostFilter {
	private final String[] spamWords;
	private final String[] alertWords;

	/**
	 * Initialize the spam words list.
	 * 
	 * @param spamWords
	 *            - list of spam words.
	 */
	public SpamWordsFilter(List<String> spamWords, List<String> alertWords) {
		spamWords.sort(null); // sort to use binary search
		alertWords.sort(null);

		this.spamWords = spamWords.toArray(new String[] {});
		this.alertWords = alertWords.toArray(new String[] {});
	}

	@Override
	public List<Post> filter(List<Post> posts) {
		for (Post post : posts) {
			SimplifiedPost simplePost = (SimplifiedPost) post;

			// post does not have (S)pam tag and post has a spam word
			if (simplePost.getPostType() != 'S'
					&& hasKeyword(simplePost.getMessage())) {
				simplePost.setPostType('S');
			} else if (simplePost.getPostType() != 'A'
					&& hasAlertWord(simplePost.getMessage())) {
				simplePost.setPostType('A');
			}

			// if description is not null, then just verify
			// else set the same message body
			if (!(simplePost.getDescription() == "null")) {
				if (simplePost.getPostType() != 'S'
						&& hasKeyword(simplePost.getDescription())) {
					simplePost.setPostType('S');
				} else if (simplePost.getPostType() != 'A'
						&& hasAlertWord(simplePost.getDescription())) {
					simplePost.setPostType('A');
				}
			} else {
				simplePost.setDescription(simplePost.getMessage());

				if (simplePost.getPostType() != 'S'
						&& hasKeyword(simplePost.getDescription())) {
					simplePost.setPostType('S');
				} else if (simplePost.getPostType() != 'A'
						&& hasAlertWord(simplePost.getDescription())) {
					simplePost.setPostType('A');
				}
			}
		}
		return posts;
	}

	/**
	 * Verify if a message contains an alert word
	 * 
	 * @param text
	 *            - analyzed text
	 * @return boolean - has an alert word or not
	 */
	public boolean hasAlertWord(String text) {
		StringTokenizer st = new StringTokenizer(text.toLowerCase(), " :,.!?-#");

		while (st.hasMoreTokens()) {
			if (Arrays.binarySearch(alertWords, st.nextToken()) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Verify if a message contains a spam word
	 * 
	 * @param text
	 *            - analyzed text
	 * @return boolean - has a spam word or not
	 */
	public boolean hasKeyword(String text) {
		StringTokenizer st = new StringTokenizer(text.toLowerCase(), " :,.!?-#");

		while (st.hasMoreTokens()) {
			if (Arrays.binarySearch(spamWords, st.nextToken()) >= 0) {
				return true;
			}
		}
		return false;
	}
}