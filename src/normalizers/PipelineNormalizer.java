package normalizers;

import java.util.ArrayList;
import java.util.List;

import persistence.entities.Post;

/**
 * Class for manage and control normalizers in pipeline
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class PipelineNormalizer implements PostNormalizer {
	private final List<PostNormalizer> normalizers;

	public PipelineNormalizer() {
		this.normalizers = new ArrayList<>();

		// create normalizers
		// PostNormalizer postUrlNorm = new PostUrlNormalizer();

		// add normalizers
		// normalizers.add(postUrlNorm);
	}

	@Override
	public List<Post> normalizer(List<Post> posts) {
		for (PostNormalizer norm : normalizers) {
			posts = norm.normalizer(posts);
		}

		return posts;
	}

}
