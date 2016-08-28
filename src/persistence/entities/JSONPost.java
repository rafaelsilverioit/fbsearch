package persistence.entities;

import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import util.DateManager;
import util.StringManager;

/**
 * Parse information from JSON to Posts, Users, Hashtags.. Objects
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class JSONPost {

	public JSONPost() {

	}

	/**
	 * Get Post ID
	 * 
	 * @param postJSON
	 * @return post ID - ID number from post.
	 */
	public String getPostID(JSONObject postJSON) {
		return postJSON.get("post_id").toString();
	}

	/**
	 * Get User ID
	 * 
	 * @param postJSON
	 * @return user ID - ID number from post author.
	 */
	public String getUserID(JSONObject postJSON) {
		return postJSON.get("user_id").toString();
	}

	/**
	 * Get username
	 * 
	 * @param postJSON
	 * @return username - username from the post's author
	 */
	public PostUser getUser(JSONObject postJSON) {
		PostUser username = (PostUser) postJSON.get("user");
		return username;
	}

	/**
	 * Get post date.
	 * 
	 * @param postJSON
	 * @return post date - post creation date
	 */
	public Date getDateTime(JSONObject postJSON) {
		String postDate = postJSON.get("created_at").toString();
		return DateManager.formatedPostStringToDate(postDate).getTime();
	}

	/**
	 * Get post updated time
	 * 
	 * @param postJSON
	 * @return updated time
	 */
	public Date getUpdatedTime(JSONObject postJSON) {
		String updatedTime = postJSON.get("updated_at").toString();
		return DateManager.formatedPostStringToDate(updatedTime).getTime();
	}

	/**
	 * Get post message
	 * 
	 * @param postJSON
	 * @return post message - post text message.
	 */
	public String getTextMessage(JSONObject postJSON) {
		String postMessage = postJSON.get("message").toString();
		postMessage = StringManager
				.removeTroubleCharactersFromString(postMessage);
		return postMessage;
	}

	/**
	 * Get posts description
	 * 
	 * @param postJSON
	 * @return description - post description message.
	 */
	public String getDescription(JSONObject postJSON) {
		String description = postJSON.get("description").toString();
		description = StringManager
				.removeTroubleCharactersFromString(description);
		return description;
	}

	/**
	 * Get post url - e.g.: http://fb.com/postID
	 * 
	 * @param postJSON
	 * @return url
	 */
	public String getPostUrl(JSONObject postJSON) {
		String postUrl = postJSON.get("post_url").toString();
		postUrl = StringManager.removeTroubleCharactersFromString(postUrl);
		return postUrl;
	}

	/**
	 * Get likes total count
	 * 
	 * @param postJSON
	 * @return likes count
	 */
	public Integer getLikesCount(JSONObject postJSON) {
		Integer likesCount = (Integer) postJSON.get("likes_count");
		return likesCount;
	}

	/**
	 * Get comments total count
	 * 
	 * @param postJSON
	 * @return comments count
	 */
	public Integer getCommentsCount(JSONObject postJSON) {
		Integer commentsCount = Integer.parseInt(postJSON.get("comments_count")
				.toString());
		return commentsCount;
	}

	/**
	 * Get shares total count
	 * 
	 * @param postJSON
	 * @return shares count
	 */
	public Integer getSharesCount(JSONObject postJSON) {
		Integer sharesCount = Integer.parseInt(postJSON.get("shares_count")
				.toString());
		return sharesCount;
	}

	/**
	 * Get quantity of hashtags present in the post
	 * 
	 * @return - Quantity of hashtags in the post message
	 */
	public int getQuantityOfHashtags(JSONObject postJSON) {
		JSONObject user = postJSON;
		if (user.get("hashtags") != null) {
			JSONArray arrayHashtags = (JSONArray) user.get("hashtags");
			return arrayHashtags.size();
		}
		return 0;
	}

	/**
	 * Get quantity of the URLs present in the post
	 * 
	 * @return Quantity of URLs from post message
	 */
	public int getQuantityOfURLsFromJSON(JSONObject postJSON) {
		JSONObject user = postJSON;
		if (user.get("urls") != null) {
			JSONArray arrayURLs = (JSONArray) user.get("urls");
			return arrayURLs.size();
		}

		return 0;
	}

	/**
	 * Get quantity of users mentioned in the post
	 * 
	 * @return Quantity of users mentioned in the post
	 */
	/*
	 * public int getQuantityOfUsersMentionsFromJSON(JSONObject postJSON) {
	 * JSONObject user = (JSONObject) postJSON; //.get("entities"); //
	 * System.out.println(user.keySet()); if (user.get("user_mentions") != null)
	 * { JSONArray arrayMentions = (JSONArray) user.get("user_mentions"); return
	 * arrayMentions.length(); }
	 * 
	 * return 0; }
	 */

	/**
	 * Get urls from post
	 * 
	 * @param postJSON
	 * @return
	 * @throws ParseException
	 */
	public ArrayList<PostUrl> getUrls(JSONObject postJSON) {
		ArrayList<PostUrl> arrayUrlsFromPost = new ArrayList<>();
		// JSONObject entitiesJSON = postJSON; //.get("entities");
		JSONArray jA = (JSONArray) postJSON.get("urls");
		// if (entitiesJSON.get("urls") != null) {
		if (jA.size() > 0) {
			// JSONArray arrayURLs = (JSONArray) entitiesJSON.get("urls");
			for (Object arrayURL : jA) {
				// JSONObject objJSON = (JSONObject) arrayURL;
				JSONObject objJSON = (JSONObject) arrayURL;
				PostUrl postUrl = new PostUrl();
				postUrl.setPost_id(getPostID(postJSON));
				postUrl.setUrl(objJSON.get("url").toString());
				arrayUrlsFromPost.add(postUrl);

			}
		}

		return arrayUrlsFromPost;
	}

	/**
	 * Get hashtags from post
	 * 
	 * @param postJSON
	 * @return - ArrayList of hashtags
	 */
	public ArrayList<PostHashtags> getHashtags(JSONObject postJSON) {
		ArrayList<PostHashtags> arrayHashtagsFromPost = new ArrayList<>();
		JSONArray jA = (JSONArray) postJSON.get("hashtags");
		if (jA.size() > 0) {
			for (Object objJSON : jA) {
				JSONObject hashtagJSON = (JSONObject) objJSON;
				PostHashtags postHashtag = new PostHashtags();
				postHashtag.setPost_id(getPostID(postJSON));
				String hashtag_text = hashtagJSON.get("text").toString();
				hashtag_text = StringManager
						.removeTroubleCharactersFromString(hashtag_text);
				postHashtag.setText(hashtagJSON.get("text").toString());
				arrayHashtagsFromPost.add(postHashtag);
			}
		}

		return arrayHashtagsFromPost;
	}

	/**
	 * Get user mentions from post
	 */
	/*
	 * public ArrayList<PostUserMention> getUserMentions(JSONObject postJSON) {
	 * ArrayList<PostUserMention> arrayUserMentionsFromPost = new ArrayList<>();
	 * if(postJSON.has("user_mentions")){ JSONObject entitiesJSON =
	 * postJSON;//.get("entities"); if (entitiesJSON.get("user_mentions") !=
	 * null) { // System.out.println(entitiesJSON.get("user_mentions");
	 * JSONArray arrayUserMentions =
	 * entitiesJSON.getJSONArray("user_mentions");//new
	 * JSONArray(entitiesJSON.get("user_mentions")); //(JSONArray) entitiesJSON
	 * //.get("user_mentions"); for (Object objJSON : arrayUserMentions) {
	 * JSONObject userMentionJSON = new JSONObject(objJSON); PostUserMention
	 * postUserMention = new PostUserMention();
	 * postUserMention.setPost_id(getPostID(postJSON));
	 * postUserMention.setUser_id(getUserID(postJSON)); String name =
	 * userMentionJSON.get("name").toString(); name =
	 * StringManager.removeTroubleCharactersFromString(name);
	 * postUserMention.setName(name);
	 * arrayUserMentionsFromPost.add(postUserMention); } } }
	 * 
	 * return arrayUserMentionsFromPost; }
	 */
}