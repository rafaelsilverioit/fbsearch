package search;

import java.util.List;

import filters.PostFilter;

import normalizers.PostNormalizer;

import persistence.controller.PostPersistenceMonitor;
import persistence.entities.Post;

/**
 * Runnable to call normalizers and filters
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class PostPreprocessorRunnable implements Runnable {
	private PostNormalizer postNorm = null;
	private PostFilter postFilter = null;
	private List<Post> posts;

	public PostPreprocessorRunnable(List<Post> posts) {
		this.posts = posts;
	}

	public void setNormalizer(PostNormalizer postNormalizer) {
		this.postNorm = postNormalizer;
	}

	public void setFilter(PostFilter postFilter) {
		this.postFilter = postFilter;
	}

	public List<Post> getResult() {
		return posts;
	}

	@Override
	public void run() {
		if (postNorm != null) {
			posts = postNorm.normalizer(posts);
		}

		if (postFilter != null) {
			posts = postFilter.filter(posts);
		}

		// call a syncronized method to write posts to json and file
		PostPersistenceMonitor.getInstance().writeDatabase(posts);
	}
}