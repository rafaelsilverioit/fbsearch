package filters;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import persistence.FileManager;
import persistence.entities.SimplifiedPost;
import persistence.entities.Post;
import persistence.entities.PostClassificationData;
import persistence.entities.PostExtraInfo;
import persistence.entities.PostHashtags;
import persistence.entities.PostUrl;
import persistence.entities.PostUser;
import search.FacebookLoggerFactory;

//import persistence.entities.PostUserMention;

/**
 * This filter prioritize messages using a weight system. Each group (hashtags,
 * profiles, keywords, rnp, tools, urls) is associated a weight to increase the
 * relevance of an analyzed message.
 * <br>
 * WEIGHT (WT) means:
 * 
 * <br>WT 1 - normal term, it is not computed
 * <br>WT 2 - important terms 
 * <br>WT 3 - very important terms 
 * <br>WT 4 - High probability of threat
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class WeightFilter implements PostFilter {
	private static final Logger logger = FacebookLoggerFactory.getInstance(
			"StreamSearchFilter", "posts_stream_filter.log");

	private final int threshold;

	private final Set<String> hashtags;
	private final Set<String> profiles;
	private final Set<String> keywords;
	private final Set<String> rnpterms;
	private final Set<String> infosec;
	private final Set<String> tools;
	private final Set<String> urls;

	Map<String, Set<String>> classMap = new HashMap<>();
	Map<String, Integer> weightMap = new HashMap<>();

	private final Pattern urlPattern;

	// not optimized code - only testing possibilities
	public WeightFilter() {
		this.loadConfig("confs/weight_filter.conf");

		this.threshold = weightMap.get("threshold");

		this.hashtags = classMap.get("hashtags");
		this.profiles = classMap.get("profiles");
		this.keywords = classMap.get("keywords");
		this.rnpterms = classMap.get("rnp");
		this.tools = classMap.get("tools");
		this.infosec = classMap.get("infosec");
		this.urls = classMap.get("urls");

		// create pattern
		Iterator<String> iUrls = urls.iterator();
		String pattern = (String) iUrls.next();
		while (iUrls.hasNext()) {
			pattern += "|" + iUrls.next();
		}

		this.urlPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
	}

	@Override
	public List<Post> filter(List<Post> posts) {
		int nKeywords, nHashtags, nInfosec, nProfiles, nRnpTerms, nTools, nUrls, total;// nMentions;

		for (Post post : posts) {
			SimplifiedPost simplePost = (SimplifiedPost) post;

			if (simplePost.getPostType() == 'S')
				continue; // skip if contains spam tag

			// count keywords, hashtags, profiles, usermentions and urls
			// patterns
			int[] countTerms = countTerms(simplePost.getMessage());
			nKeywords = countTerms[0];
			nInfosec = countTerms[1];
			nRnpTerms = countTerms[2];
			nTools = countTerms[3];
			nHashtags = countHashtags(simplePost.getHashtagsFromPost());
			nProfiles = countProfiles(simplePost.getUserFromPost());
			// nMentions = countMentions(simplePost.getUserMentionsFromPost());
			nUrls = countUrlPattern(simplePost);

			total = 0;
			if (nInfosec > 0
					|| hasHashtagsAsInfosec(simplePost.getHashtagsFromPost())) {
				total = nRnpTerms * weightMap.get("rnp") + nHashtags
						* weightMap.get("hashtags") + nInfosec
						* weightMap.get("infosec") + nTools
						* weightMap.get("tools") + nKeywords
						* weightMap.get("keywords");
			}

			total += nUrls * weightMap.get("urls");

			// if it has only profiles and mentions, probably is not important
			if (total > 0) {
				total += nProfiles * weightMap.get("profiles");// + nMentions;
			}

			// select alerts by threshold
			if (total > threshold) {
				simplePost.setPostType('A');
			}

			// add classification to Facebook
			PostExtraInfo extraInfo = new PostClassificationData(new Float(
					total), "WeightFilter");
			simplePost.addExtraInfo(extraInfo);

			// log facebook and points
			String weightPost = "[" + post.getPostId() + ", "
					+ post.getMessage() + "] ---" + nKeywords + "," + nInfosec
					+ "," + nRnpTerms + "n" + nTools + "," + nHashtags + ","
					+ nProfiles + "," + nUrls + " T:" + total; // removed
																// nMentions

			logger.log(Level.INFO, weightPost);
		}

		return posts;
	}

	/**
	 * Count terms by classes
	 * 
	 * @param text
	 *            - text to be analyzed
	 * @return array - array of number terms (keywords, infosec, rnp, tools)
	 */
	public int[] countTerms(String text) {
		Set<String> tokens = this.extractUniqueTokens(text);

		int countKeyword = 0, countRnp = 0, countInfosec = 0, countTools = 0;
		for (String token : tokens) {
			if (this.keywords.contains(token))
				countKeyword++;
			else if (this.infosec.contains(token))
				countInfosec++;
			else if (this.rnpterms.contains(token))
				countRnp++;
			else if (this.tools.contains(token))
				countTools++;
		}

		int[] counts = { countKeyword, countInfosec, countRnp, countTools };
		return counts;
	}

	/**
	 * Count relevant hashtags in a post
	 * 
	 * @param hashtagsFromPost
	 * @return
	 */
	private int countHashtags(List<PostHashtags> hashtagsFromPost) {
		int countHashtags = 0;

		for (PostHashtags hashtag : hashtagsFromPost) {
			String lowerHashtag = hashtag.getText().toLowerCase();

			if (this.hashtags.contains(lowerHashtag))
				countHashtags++;
			else if (this.rnpterms.contains(lowerHashtag))
				countHashtags++;
			else if (this.infosec.contains(lowerHashtag))
				countHashtags++;
			else if (this.tools.contains(lowerHashtag))
				countHashtags++;
		}

		return countHashtags;
	}

	/**
	 * Verify if a user is in relevant profiles
	 * 
	 * @param userFromPost
	 * @return 1 for relevant user or 0 for irrelevant user
	 */
	private int countProfiles(PostUser userFromPost) {
		if (this.profiles.contains(userFromPost.getUserId())) {
			return 1;
		}

		return 0;
	}

	/**
	 * Count mentions to relevant profiles
	 * 
	 * @param userMentions
	 * @return number of mentioned profiles
	 *
	 * private int countMentions(List<PostUserMention> userMentions) { int
	 * countMentions = 0;
	 * 
	 * for (PostUserMention userMention : userMentions) { if
	 * (this.profiles.contains(userMention.getUser_id())) { countMentions++; } }
	 * 
	 * return countMentions; }
	 */

	/**
	 * Verify if a post has a relevant domain
	 * 
	 * @param post
	 * @return
	 */
	private int countUrlPattern(Post post) {
		SimplifiedPost simplePost = (SimplifiedPost) post;
		Matcher urlMatcher = urlPattern.matcher(simplePost.getMessage());

		if (urlMatcher.find()) {
			return 1;
		}

		for (PostUrl postUrl : simplePost.getUrlsFromPost()) {
			urlMatcher = urlPattern.matcher(postUrl.getUrl());
			if (urlMatcher.find()) {
				return 1;
			}
		}

		return 0;
	}

	private void loadConfig(String fileName) {
		List<String> linesConf = FileManager
				.readCleanStringLineFromFile(fileName);

		String currentClass = "";
		for (String line : linesConf) {
			if (line.charAt(0) == '[') {
				String[] pieces = line.replaceAll("\\[(.*?)\\]", "$1").split(
						",");
				weightMap.put(pieces[0], Integer.parseInt(pieces[1]));
				classMap.put(pieces[0], new HashSet<String>());
				currentClass = pieces[0];
			} else {
				// add ter in existing map
				classMap.get(currentClass).add(line);
			}
		}
	}

	/**
	 * Verify if a hashtags is a infosec or tool term
	 * 
	 * @param hashtagsFromPost
	 *            - list of hashtags
	 * @return boolean
	 */
	private boolean hasHashtagsAsInfosec(List<PostHashtags> hashtagsFromPost) {
		for (PostHashtags hashtag : hashtagsFromPost) {
			String lowerHashtag = hashtag.getText().toLowerCase();

			if (this.infosec.contains(lowerHashtag))
				return true;
			else if (this.tools.contains(lowerHashtag))
				return true;
		}

		return false;
	}

	/**
	 * Extract tokens (words, urls, hashtags) from post
	 * 
	 * @param text
	 *            - message from post
	 * @return - set of unique tokens
	 */
	private Set<String> extractUniqueTokens(String text) {
		text = text.toLowerCase();
		Set<String> tokens = new HashSet<>();

		// extract URLs
		String regex = "\\(?\\b(https?://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);

		while (m.find()) {
			tokens.add(m.group());
		}

		// remove URLs
		text = m.replaceAll("");

		// extract other tokens
		StringTokenizer st = new StringTokenizer(text.toLowerCase(), " ,.!?-:");

		while (st.hasMoreTokens()) {
			tokens.add(st.nextToken());
		}

		return tokens;
	}

	@Override
	public String toString() {
		String str = "";

		for (Set<String> conj : classMap.values()) {
			str = "\n-----\n";
			for (String value : conj) {
				str += value + ",";
			}
		}

		return str;
	}
}
