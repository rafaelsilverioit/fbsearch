package filters;

import java.util.ArrayList;
import java.util.List;

import persistence.FileManager;
import persistence.entities.Post;

/**
 * Class for manage and control filters in pipeline
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class PipelineFilter implements PostFilter {
	private final List<PostFilter> filters;

	public PipelineFilter() {
		this.filters = new ArrayList<>();

		// create filters
		List<String> spamUrls = FileManager
				.readStringLineFromFile("confs/blacklist_urls.conf");
		PostFilter spamUrlsFilter = new SpamUrlsFilter(spamUrls);

		List<String> spamWords = FileManager
				.readStringLineFromFile("confs/blacklist_words.conf");

		List<String> alertWords = FileManager
				.readStringLineFromFile("confs/alert_keywords.conf");

		PostFilter spamWordsFilter = new SpamWordsFilter(spamWords, alertWords);

		// add filters by order
		this.filters.add(spamUrlsFilter);
		this.filters.add(spamWordsFilter);
	}

	@Override
	public List<Post> filter(List<Post> posts) {
		for (PostFilter filter : filters) {
			posts = filter.filter(posts);
		}
		return posts;
	}
}