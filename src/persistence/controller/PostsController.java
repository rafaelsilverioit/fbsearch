package persistence.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import clusters.DistanceCluster;
import persistence.dao.PostsDAO;
import persistence.dao.PostsExtraInfoDAO;
import persistence.dao.PostsHashtagsDAO;
import persistence.dao.PostsUrlsDAO;
import persistence.dao.PostsAlertsDAO;
import persistence.dao.PostsSimilarDAO;
//import persistence.dao.PostsUserMentionsDAO;
import persistence.dao.PostsUsersDAO;
import persistence.entities.PostExtraInfo;
import persistence.entities.SimplifiedPost;
import persistence.entities.PostHashtags;
import persistence.entities.PostUrl;
import persistence.entities.PostAlertGroup;
//import persistence.entities.PostUserMention;
import persistence.entities.PostUser;

/**
 * Controller DAO
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class PostsController {
	private final PostsDAO postsDAO;
	private final PostsHashtagsDAO postsHashtagsDAO;
	private final PostsUrlsDAO postsUrlsDAO;
	private final PostsUsersDAO postsUsersDAO;
	// private final PostsUserMentionsDAO postsUserMentionsDAO;
	private final PostsExtraInfoDAO postsExtraInfoDAO;
	private final PostsAlertsDAO postsAlertsDAO;
	private final PostsSimilarDAO postsSimilarDAO;
	private final DistanceCluster postsCluster;

	public PostsController() {
		postsDAO = new PostsDAO();
		postsHashtagsDAO = new PostsHashtagsDAO();
		postsUrlsDAO = new PostsUrlsDAO();
		postsUsersDAO = new PostsUsersDAO();
		// postsUserMentionsDAO = new PostsUserMentionsDAO();
		postsExtraInfoDAO = new PostsExtraInfoDAO();

		postsAlertsDAO = new PostsAlertsDAO();
		postsSimilarDAO = new PostsSimilarDAO();
		postsCluster = new DistanceCluster();
	}

	public boolean persist(SimplifiedPost post) {
		boolean state = false;

		try {
			state = postsDAO.insert(post);

			// if insertion failed then close persist
			if (!state) {
				return false;
			}

			for (PostHashtags hashtags : post.getHashtagsFromPost()) {
				postsHashtagsDAO.insert(hashtags);
			}

			for (PostUrl urls : post.getUrlsFromPost()) {
				postsUrlsDAO.insert(urls);
			}

			PostUser user = post.getUserFromPost();
			postsUsersDAO.insert(user);

			// for (PostUserMention userMention :
			// post.getUserMentionsFromPost()) {
			// postsUserMentionsDAO.insert(userMention);
			// }

			if (post.hasExtraInfo()) { // if post is tagged as spam, it dont
										// have extra info
				for (PostExtraInfo extraInfo : post.getPostExtraInfo()) {
					postsExtraInfoDAO.insert(post.getPostId(), extraInfo);
				}
			}

			if (post.getPostType() == 'A') {
				// look up for an existing group
				String group = postsCluster.clustering(post);

				// add alert into tables: Alerts and SimilarPosts
				if (group == null) {
					PostAlertGroup alertGroup = new PostAlertGroup(post);
					postsAlertsDAO.insert(alertGroup);
					postsSimilarDAO.insert(alertGroup);
				} else {
					// TODO: update lastDate, postsQuantity in PostAlertGroup
					postsAlertsDAO.update(group, post.getCreatedTime()/*
																	 * .getTime()
																	 */);
					postsSimilarDAO.insert(post.getPostId(), group);
				}

				Logger.getLogger("StreamSearch").log(Level.INFO,
						"ALERT: [{0}, {1}]",
						new Object[] { post.getUserId(), post.getMessage() });
			}
		} catch (Exception ex) {
			return false;
		}

		return state;
	}

	public boolean hasPost(SimplifiedPost post) {
		return postsDAO.hasPost(post);
	}
}
