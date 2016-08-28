package persistence.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import util.DateManager;

/**
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class SimplifiedPost implements Post {
	// Post ID
	private String postID;
	// User (ID) who did the post
	private String userID;
	// Post's owner
	private PostUser userFromPost;
	// Username from user who did the post
	// private String username;
	// Date of post
	private Date createdAt;
	// When the post was updated
	private Date updatedAt;
	// Post message
	private String text;
	// Post description
	private String description;
	// Post url (e.g.: fb.com/postId)
	private String postURL;
	// Likes count
	private Integer likesCount;
	// Comments count
	private int commentsCount;
	// Shares count
	private int sharesCount;
	// Quantity of urls from post
	private int urlQuantity;
	// Quantity of hashtags from post
	private int hashtagsQuantity;
	// Quantity of user mentions from post
	// private int userMentionsQuantity;
	// Post type - A (alert), S (spam), U (undetermined)
	private char postType;
	// URL from posts
	private List<PostUrl> urlsFromPost;
	// List of hashtags from posts
	private List<PostHashtags> hashtagsFromPost;
	// List of user mentions from posts
	// private List<PostUserMention> userMentionsFromPost;
	// Add extra info to posts (optional)
	private List<PostExtraInfo> postExtraInfo;
	// Proccess JSON posts
	private JSONObject postJSON;

	/**
	 * This class is based on Post JSON Object, then a post in JSON format is
	 * required!
	 * 
	 * @param postJSON
	 *            - Post Json Object.
	 */
	public SimplifiedPost(JSONObject postJSON) {
		JSONPost json = new JSONPost();

		this.postJSON = postJSON;
		this.postID = json.getPostID(postJSON);
		this.userID = json.getUserID(postJSON);
		this.userFromPost = json.getUser(postJSON);
		this.createdAt = json.getDateTime(postJSON);
		this.updatedAt = json.getUpdatedTime(postJSON);
		this.text = json.getTextMessage(postJSON);
		this.description = json.getDescription(postJSON);
		this.postURL = json.getPostUrl(postJSON);
		this.likesCount = json.getLikesCount(postJSON);
		this.commentsCount = json.getCommentsCount(postJSON);
		this.sharesCount = json.getSharesCount(postJSON);
		this.urlQuantity = json.getQuantityOfURLsFromJSON(postJSON);
		this.hashtagsQuantity = json.getQuantityOfHashtags(postJSON);
		/*
		 * this.userMentionsQuantity = json
		 * .getQuantityOfUsersMentionsFromJSON(postJSON);
		 */
		this.postType = 'U';
		this.postExtraInfo = null;
		this.urlsFromPost = json.getUrls(postJSON);
		this.hashtagsFromPost = json.getHashtags(postJSON);
		/* this.userMentionsFromPost = json.getUserMentions(postJSON); */
	}

	/**
	 * 
	 * @param post
	 *            - posts fetched from restfb
	 */
	@SuppressWarnings("unchecked")
	public SimplifiedPost(com.restfb.types.Post post, JSONArray urlsFromPost,
			JSONArray hashtagsFromPost) {

		JSONPost json = new JSONPost();
		PostUser user = new PostUser();

		this.postID = post.getId();
		this.userID = post.getFrom().getId();

		user.setName(post.getFrom().getName());
		user.setUrl("facebook.com/" + post.getFrom().getId());
		user.setUserId(post.getFrom().getId());

		this.userFromPost = user;
		JSONObject jObj = new JSONObject();

		jObj.put("created_at", post.getCreatedTime());
		jObj.put("updated_at", post.getUpdatedTime());

		this.createdAt = json.getDateTime(jObj);
		this.updatedAt = json.getUpdatedTime(jObj);

		this.text = post.getMessage();
		this.description = post.getDescription();
		this.postURL = post.getLink();
		// 0 because it throws "NullPointerException" error
		this.likesCount = 0;// Integer.parseInt(post.getLikesCount().toString());
		this.commentsCount = Integer.parseInt(post.getCommentsCount()
				.toString());
		this.sharesCount = Integer.parseInt(post.getSharesCount().toString());
		this.urlQuantity = urlsFromPost.size();
		this.hashtagsQuantity = hashtagsFromPost.size();
		this.postType = 'U';
		this.postExtraInfo = null;
		this.urlsFromPost = urlsFromPost;
		this.hashtagsFromPost = hashtagsFromPost;
	}

	public SimplifiedPost() {

	}

	@SuppressWarnings({ "unchecked", "unused" })
	public List<JSONObject> getPostsInJSON() {
		List<JSONObject> postsInJSON = new ArrayList<JSONObject>();

		JSONObject obj = new JSONObject();

		obj.put("post_id", this.getPostId());
		obj.put("user_id", this.getUserId());
		obj.put("user", this.getUserFromPost().getName());
		obj.put("user_url", this.getUserFromPost().getUrl());
		// obj.put("To : " + post.getTo());
		obj.put("created_at", this.getCreatedTime());
		obj.put("updated_at", this.getUpdatedTime());

		Object aux = (getMessage() != null) ? obj.put("message",
				this.getMessage()) : obj.put("message", "null");
		aux = (!(getDescription() == null)) ? obj.put("description",
				this.getDescription()) : obj.put("description",
				this.getMessage());
		aux = (!(getLink() == null)) ? obj.put("post_url", this.getLink())
				: obj.put("post_url", "facebook.com/" + this.getPostId());

		obj.put("likes_count", this.getLikesCount());
		obj.put("comments_count", this.getCommentsCount());
		obj.put("shares_count", this.getSharesCount());
		obj.put("hashtags", this.getHashtagsFromPost());
		obj.put("urls", this.getUrlsFromPost());

		postsInJSON.add(obj);

		return postsInJSON;
	}

	@Override
	public String getPostId() {
		return postID;
	}

	public void setPostId(String post_id) {
		this.postID = post_id;
	}

	@Override
	public String getUserId() {
		return userID;
	}

	public void setUserId(String user_id) {
		this.userID = user_id;
	}

	@Override
	public PostUser getUserFromPost() {
		return userFromPost;
	}

	public void setUserFromPost(PostUser user) {
		this.userFromPost = user;
	}

	@Override
	public String getTo() {
		return null;
	}

	public void setTo() {

	}

	@Override
	public Date getCreatedTime() {
		return createdAt;
	}

	public void setCreatedTime(Date created_at) {
		this.createdAt = created_at;
	}

	@Override
	public Date getUpdatedTime() {
		return updatedAt;
	}

	public void setUpdatedTime(Date updated_at) {
		this.updatedAt = updated_at;
	}

	@Override
	public String getMessage() {
		return text;
	}

	public void setMessage(String message) {
		this.text = message;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getLink() {
		return postURL;
	}

	public void setLink(String post_url) {
		this.postURL = post_url;
	}

	@Override
	public int getLikesCount() {
		return likesCount;
	}

	public void setLikesCount(Integer likes_count) {
		this.likesCount = likes_count;
	}

	@Override
	public int getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(Integer comments_count) {
		this.commentsCount = comments_count;
	}

	@Override
	public int getSharesCount() {
		return sharesCount;
	}

	public void setSharesCount(Integer shares_count) {
		this.sharesCount = shares_count;
	}

	public int getUrlQuantity() {
		return urlQuantity;
	}

	public void setUrlQuantity(int url_quantity) {
		this.urlQuantity = url_quantity;
	}

	public int getHashtagsQuantity() {
		return hashtagsQuantity;
	}

	public void setHashtagsQuantity(int hashtags_quantity) {
		this.hashtagsQuantity = hashtags_quantity;
	}

	/*
	 * public int getUserMentionsQuantity() { return userMentionsQuantity; }
	 * 
	 * public void setUserMentionsQuantity(int user_mentions_quantity) {
	 * this.userMentionsQuantity = user_mentions_quantity; }
	 */

	public char getPostType() {
		return postType;
	}

	public void setPostType(char postType) {
		this.postType = postType;
	}

	public JSONObject getPostJSON() {
		return postJSON;
	}

	public void setPostJSON(JSONObject postJSON) {
		this.postJSON = postJSON;
	}

	public List<PostUrl> getUrlsFromPost() {
		return urlsFromPost;
	}

	public void setUrlsFromPost(List<PostUrl> arrayUrlsFromPost) {
		this.urlsFromPost = arrayUrlsFromPost;
	}

	public List<PostHashtags> getHashtagsFromPost() {
		return hashtagsFromPost;
	}

	public void setHashtagsFromPost(List<PostHashtags> arrayHashtagsFromPost) {
		this.hashtagsFromPost = arrayHashtagsFromPost;
	}

	/*
	 * public List<PostUserMention> getUserMentionsFromPost() { return
	 * userMentionsFromPost; }
	 * 
	 * public void setUserMentionsFromPost( List<PostUserMention>
	 * arrayUserMentionsFromPost) { this.userMentionsFromPost =
	 * arrayUserMentionsFromPost; }
	 */

	public List<PostExtraInfo> getPostExtraInfo() {
		return postExtraInfo;
	}

	public void setPostExtraInfo(List<PostExtraInfo> postExtraInfo) {
		this.postExtraInfo = postExtraInfo;
	}

	public void addExtraInfo(PostExtraInfo extraInfo) {
		if (this.postExtraInfo == null) {
			this.postExtraInfo = new ArrayList<>();
		}
		this.postExtraInfo.add(extraInfo);
	}

	public boolean hasExtraInfo() {
		return this.postExtraInfo != null;
	}

	/**
	 * Get printable informations from post
	 * 
	 * @return printable post.
	 */
	public String getForPrintPostInformation() {
		return " post_id: "
				+ this.postID
				+ " user_id: "
				+ this.userID
				+
				// " user: " + this.userFromPost +
				" create_at: "
				+ DateManager.formatterDatePost
						.format(this.createdAt.getTime())
				+ " updated_at: "
				+ DateManager.formatterDatePost
						.format(this.updatedAt.getTime()) + " message: "
				+ this.text + " description: " + this.description
				+ " post_url: " + this.postURL + " likes_count: "
				+ this.likesCount + " comments_count: " + this.commentsCount
				+ " shares_count: " + this.sharesCount + " \n\tuser: "
				+ this.userFromPost.getForPrintUserInformation()
				+ this.getForPrintPostUrlsInformation()
				+ this.getForPrintPostHashtagsInformation();
		// + this.getForPrintPostUserMentionsInformation();
	}

	/**
	 * Get printable informations about urls from post
	 * 
	 * @return - Printable urls
	 */
	public String getForPrintPostUrlsInformation() {
		String urlsFromPost = "";
		for (PostUrl url : this.urlsFromPost) {
			urlsFromPost = urlsFromPost + "\n\turls: " + url.getUrl();
		}

		return urlsFromPost;
	}

	/**
	 * Get printable informations from post hashtags
	 * 
	 * @return - Printable hashtags
	 */
	public String getForPrintPostHashtagsInformation() {
		String hashtagsFromPost = "";
		for (PostHashtags hashtag : this.hashtagsFromPost) {
			hashtagsFromPost = hashtagsFromPost + "\n\thashtag: "
					+ hashtag.getText();
		}

		return hashtagsFromPost;
	}

	/**
	 * Get printable informations from post user mentions
	 * 
	 * @return - Printable user mentions
	 * 
	 *         public String getForPrintPostUserMentionsInformation() { String
	 *         userMentionsFromPost = ""; for (PostUserMention userMention :
	 *         this.userMentionsFromPost) { userMentionsFromPost =
	 *         userMentionsFromPost + "\n\tUser id: " +
	 *         userMention.getUser_id(); userMentionsFromPost =
	 *         userMentionsFromPost + "\n\tName : " + userMention.getName(); }
	 * 
	 *         return userMentionsFromPost; }
	 */
}
