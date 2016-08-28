package clusters;

import java.util.List;
import java.util.Map;

import persistence.entities.Post;

/**
 * Group or add posts in a cluster. Each cluster is identified by a String.
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public interface PostCluster {
	Map<String, List<Post>> clustering(List<Post> posts);

	String clustering(Post post);
}
