package normalizers;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.waxzce.longurl.LongUrl;

import persistence.entities.SimplifiedPost;
import persistence.entities.Post;
import persistence.entities.PostUrl;

import util.UrlManager;

/**
 * Translate short URL to long URL
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class PostUrlNormalizer implements PostNormalizer {
	private Map<String, String> urlMap = new ConcurrentHashMap<String, String>(
			100, 100);

	@Override
	public List<Post> normalizer(List<Post> posts) {

		// control the map's size (max size (1000))
		if (urlMap.size() > 1000) {
			urlMap.clear();
		}

		for (Post post : posts) {
			SimplifiedPost simplePost = (SimplifiedPost) post;

			for (int i = 0; i < simplePost.getUrlQuantity(); i++) {
				PostUrl url = simplePost.getUrlsFromPost().get(i);
				String shortUrl = url.getUrl();

				// get a short url already translated
				String translatedUrl = urlMap.getOrDefault(url.getUrl(), "");
				if (!translatedUrl.isEmpty()) {
					url.setUrl(translatedUrl);
					continue;
				}

				// try to translate url
				try {
					LongUrl longUrl = new LongUrl(url.getUrl());
					url.setUrl(longUrl.getLongUrl());
				} catch (Exception e) {
					System.err
							.println("Fail to translate short URL to long URL.");
					// try another service
					String tryLongUrl = UrlManager.shortToLongURL(url.getUrl());
					if (tryLongUrl.length() > url.getUrl().length()) { // check
																		// if
																		// url
																		// was
																		// translated
						url.setUrl(tryLongUrl);
					}
				}

				// if a short url was translated, so add to map
				if (!shortUrl.equals(url.getUrl())) {
					urlMap.putIfAbsent(shortUrl, url.getUrl());
				}
			}
		}
		return posts;
	}
}