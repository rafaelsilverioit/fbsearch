package filters;

import java.util.List;
import persistence.entities.Post;

/**
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public interface PostFilter {
	public List<Post> filter(List<Post> posts);
}