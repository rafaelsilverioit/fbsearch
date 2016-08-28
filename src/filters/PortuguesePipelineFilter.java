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
public class PortuguesePipelineFilter implements PostFilter {
	private final List<PostFilter> filters;

	public PortuguesePipelineFilter() {
		this.filters = new ArrayList<>();

		// create filters
		List<String> spamUrls = FileManager
				.readStringLineFromFile("confs/blacklist_urls_ptbr.conf");
		PostFilter spamUrlsFilter = new SpamUrlsFilter(spamUrls);

		List<String> spamWords = FileManager
				.readStringLineFromFile("confs/blacklist_words.conf");

		List<String> alertWords = FileManager
				.readStringLineFromFile("confs/alert_keywords.conf");

		PostFilter spamWordsFilter = new SpamWordsFilter(spamWords, alertWords);

		// prioritize filter
		PostFilter weightFilter = new WeightFilter();

		// add filters by order
		this.filters.add(spamUrlsFilter);
		this.filters.add(spamWordsFilter);

		this.filters.add(weightFilter);
	}

	@Override
	public List<Post> filter(List<Post> posts) {
		for (PostFilter filter : filters) {
			posts = filter.filter(posts);
		}
		return posts;
	}
}