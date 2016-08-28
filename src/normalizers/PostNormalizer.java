package normalizers;

import java.util.List;

import persistence.entities.Post;

/**
 * Interface for normalize posts
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public interface PostNormalizer {
	public List<Post> normalizer(List<Post> posts);
}
