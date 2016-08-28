package clusters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import persistence.dao.PostsAlertsDAO;
import persistence.entities.Post;
import persistence.entities.PostAlertGroup;
import util.MessageManager;

/**
 * Cluster alerts based on edit distance
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class DistanceCluster implements PostCluster {
	private static final float THRESHOLD = 0.1F; // 10% message size is
													// proximity threshold
	private Map<String, String> activeGroups;

	public DistanceCluster() {
		this.loadActiveGroups();
	}

	@Override
	public Map<String, List<Post>> clustering(List<Post> posts) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * Analyze if post can be put into an existing group
	 * 
	 * @param post
	 * @return null - not grouped, group_id - grouped
	 */
	@Override
	public String clustering(Post post) {
		String text = MessageManager.clean(post.getMessage());

		int thresholdProximity = (int) (THRESHOLD * text.length());

		// find out similat posts in groups
		for (Map.Entry<String, String> entrySet : activeGroups.entrySet()) {
			int distance = StringUtils.getLevenshteinDistance(text,
					entrySet.getValue());

			if (distance < thresholdProximity) {
				return entrySet.getKey();
			}
		}

		// add post as new group
		activeGroups.put(post.getPostId(), text);

		// did not find an existing group
		return null;
	}

	/**
	 * Load from database active groups TODO: load only last 100 alerts or from
	 * initial date
	 */
	private void loadActiveGroups() {
		activeGroups = new HashMap<>();

		try {
			PostsAlertsDAO alertsDAO = new PostsAlertsDAO();
			List<PostAlertGroup> alerts = alertsDAO.getRelevantAlertMessages();

			for (PostAlertGroup alert : alerts) {
				String cleanText = MessageManager.clean(alert.getText());
				activeGroups.put(alert.getGroupId(), cleanText);
			}
		} catch (Exception e) {
			System.err.println("ERROR: " + e);
		}
	}

}
