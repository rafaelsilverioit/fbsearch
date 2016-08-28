package search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import persistence.entities.SimplifiedPost;
import util.MessageManager;

import com.restfb.Connection;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookException;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.Post;

import org.json.simple.JSONObject;

/**
 * Contains the methods used to fetch posts from Facebook.
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class FacebookSearch {
	private final FacebookClient facebookClient;

	private SimplifiedPost simplePost;

	private String fileUserId;
	private String filePageId;
	private String fileGroupId;

	public FacebookSearch(FacebookClient fbClient) {
		this.facebookClient = fbClient;
	}

	/**
	 * Fetch information from a single page
	 * 
	 * @param pageId
	 *            - Page IDentification
	 * @param sinceDate
	 *            - Fetch posts since this date
	 * @return posts - List of com.restfb.types.Posts
	 */
	public List<Post> fetchPagePosts(String pageId, Date sinceDate) {
		Connection<Post> feed = facebookClient.fetchConnection(
				pageId + "/feed", Post.class,
				Parameter.with("since", sinceDate));
		return feed.getData();
	}

	public List<NamedFacebookType> fetchFriendList() {
		Connection<NamedFacebookType> friend = 
				facebookClient.fetchConnection("/me/friends", NamedFacebookType.class);
		return friend.getData();
	}
	/**
	 * Fetch information from a single group
	 * 
	 * @param groupId
	 *            - Group IDentification
	 * @param sinceDate
	 *            - Fetch posts since this date
	 * @return posts - List of com.restfb.types.Posts
	 */
	public List<Post> fetchGroupPosts(String groupId, Date sinceDate) {
		Connection<Post> feed = facebookClient.fetchConnection(groupId
				+ "/feed", Post.class, Parameter.with("since", sinceDate));
		return feed.getData();
	}

	/**
	 * Fetch information from a single user
	 * 
	 * @param userId
	 *            - User IDentification
	 * @param sinceDate
	 *            - Fetch posts since this date
	 * @return posts - List of com.restfb.types.Posts
	 */
	public List<Post> fetchUserPosts(String userId, Date sinceDate) {
		Connection<Post> feed = facebookClient.fetchConnection(userId
				+ "/statuses", Post.class, Parameter.with("since", sinceDate));
		return feed.getData();
	}

	/**
	 * Fetch posts from users
	 * 
	 * @return posts - an list of posts
	 */
	public List<JSONObject> getPostsFromUsers(List<String> userId, Date lastDate)
			throws Exception {
		// This will store posts ids for preventing repeated posts
		ArrayList<String> postId = new ArrayList<>();

		// This will receives all posts from restfb api
		List<Post> postUser;

		// This will store posts in JSON format
		List<JSONObject> postsUser = new ArrayList<JSONObject>();

		// This is for fetch posts from all users ids
		for (String user : userId) {
			// This is fetching posts from restfb
			try {
				postUser = fetchUserPosts(user, lastDate);
			} catch (FacebookException fe) {
				Logger.getLogger(FacebookSearchMain.class.getName()).log(
						Level.WARNING, "User dead: " + user + " - REMOVE IT!",
						fe);
				continue;
			}

			// This is checking if there's at least 1 post
			if (postUser.size() > 0) {
				// For each post id fetch the information and store in a
				// JSONArray
				for (Post post : postUser) {
					// Check if the post was already logged
					if (!postId.contains(post.getId())) {
						// If the first time we see the post, then
						// save it's ID
						postId.add(post.getId());

						// Send information to SimplifiedPost object
						simplePost = new SimplifiedPost(post,
								MessageManager.getUrlsFromPost(post),
								MessageManager.getHashtagsFromPost(post));

						// Translate posts to List<JSONObject>
						postsUser = simplePost.getPostsInJSON();
					}
				}
			}
		}
		return postsUser;
	}

	/**
	 * Fetch posts from groups
	 * 
	 * @return posts - an list of posts
	 */
	public List<JSONObject> getPostsFromGroups(List<String> groupId,
			Date lastDate) throws Exception {
		ArrayList<String> postId = new ArrayList<>();

		List<Post> postGroup;

		List<JSONObject> postsGroup = new ArrayList<JSONObject>();

		for (String group : groupId) {
			try {
				postGroup = fetchGroupPosts(group, lastDate);
			} catch (FacebookException fe) {
				Logger.getLogger(FacebookSearchMain.class.getName()).log(
						Level.WARNING,
						"Group dead: " + group + " - REMOVE IT!", fe);
				continue;
			}

			for (Post post : postGroup) {
				if (!(postId.contains(post.getId()))) {
					postId.add(post.getId());

					simplePost = new SimplifiedPost(post,
							MessageManager.getUrlsFromPost(post),
							MessageManager.getHashtagsFromPost(post));

					postsGroup = simplePost.getPostsInJSON();
				}
			}
		}
		return postsGroup;
	}

	/**
	 * Fetch posts from pages
	 * 
	 * @param lastDate
	 * 
	 * @return posts - an list of posts
	 */
	public List<JSONObject> getPostsFromPages(List<String> pageId, Date lastDate)
			throws Exception {
		ArrayList<String> postId = new ArrayList<>();

		List<Post> postPage;

		List<JSONObject> postsPages = new ArrayList<JSONObject>();

		for (String page : pageId) {
			try {
				postPage = fetchPagePosts(page, lastDate);
			} catch (FacebookException fe) {
				Logger.getLogger(FacebookSearchMain.class.getName()).log(
						Level.WARNING, "Page dead: " + page + " - REMOVE IT!",
						fe);
				continue;
			}

			for (Post post : postPage) {
				if (pageId.contains(post.getFrom().getId())) {
					if (!(postId.contains(post.getId()))) {
						postId.add(post.getId());

						simplePost = new SimplifiedPost(post,
								MessageManager.getUrlsFromPost(post),
								MessageManager.getHashtagsFromPost(post));

						postsPages = simplePost.getPostsInJSON();
					}
				}
			}
		}
		return postsPages;
	}

	public String getFileUserId() {
		return this.fileUserId;
	}

	public void setFileUserId(String fileUserId) {
		this.fileUserId = fileUserId;
	}

	public String getFileGroupId() {
		return this.fileGroupId;
	}

	public void setFileGroupId(String fileGroupId) {
		this.fileGroupId = fileGroupId;
	}

	public String getFilePageId() {
		return this.filePageId;
	}

	public void setFilePageId(String filePageId) {
		this.filePageId = filePageId;
	}
}