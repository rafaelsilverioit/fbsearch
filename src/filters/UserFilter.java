package filters;

import java.util.List;

import persistence.entities.Post;

/**
 * Get profile of users who score of alert is between threshold and intermediary
 * value. Thus, we can verify if alert is relevant.
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class UserFilter implements PostFilter {

	@Override
	public List<Post> filter(List<Post> posts) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}