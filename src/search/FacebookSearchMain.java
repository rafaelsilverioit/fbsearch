package search;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import persistence.FileManager;
import persistence.entities.Post;
import persistence.entities.PostUser;
import persistence.entities.SimplifiedPost;
import util.PostManager;
import util.Token;

import com.restfb.FacebookClient;
import com.restfb.exception.FacebookException;

import org.json.simple.JSONObject;

/**
 * Get posts from Facebook.<br>
 * 
 * The restrictions and search outputs are defined on config.properties<br>
 *
 * Useful tools: https://developers.facebook.com/tools/accesstoken/<br>
 * https://developers.facebook.com/tools/explorer/
 *
 * @author Rafael Silvério Amaral and Rodrigo Campiolo
 * @email rafael.silverio.it@gmail.com and rcampiolo@utfpr.edu.br
 */
@SuppressWarnings("unused")
public class FacebookSearchMain {
	private static Properties conf = null;
	private static Logger logger = null;
	private static FacebookClient fbClient = null;
	private static Date lastDate;

	public static void main(String[] args) throws Exception {
		int queryInterval = 0;
		int time = 0;
		int exp = 0;

		// set configurations, queries and the logger
		try {
			conf = SearchConfiguration.readProperties("config.properties");
			logger = FacebookLoggerFactory.getInstance("FacebookSearchMain",
					conf.getProperty("infolog"));
			fbClient = SearchConfiguration.getConfig("restfb.properties");
			queryInterval = Integer.parseInt(conf.getProperty("queryinterval"));
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		}

		// creates an instance of Factory for normalizers and filters
		PreprocessorFactory preprocFactory = new PreprocessorFactory();
		// creates an instance of FacebookSearch passing config details
		FacebookSearch fbSearch = new FacebookSearch(fbClient);

		try {
			conf = SearchConfiguration.readProperties("search.properties");

		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		// This will store users ids
		List<String> userId = FileManager.readStringLineFromFile("confs/"
				+ conf.getProperty("userid_file"));
		List<String> pageId = FileManager.readStringLineFromFile("confs/"
				+ conf.getProperty("pageid_file"));
		List<String> groupId = FileManager.readStringLineFromFile("confs/"
				+ conf.getProperty("groupid_file"));

		while (true) {
			// AccessToken is still valid? Or is it older than 3 hours?
			// TODO: Auto renew access token
			//if(!Token.isValid(fbClient) || exp > 10200) SearchConfiguration.getConfig("restfb.properties");
			
			// if we're running for the first time, then verifies the last 4 days
			// otherwise fetch last 10 minutes
			lastDate = time > 0 ? PostManager.getSinceDate() : PostManager.getLastDays();

			try {
				List<JSONObject> arrPagePost = null;
				List<JSONObject> arrUserPost = null;
				List<JSONObject> arrGroupPost = null;

				try {
					arrPagePost = fbSearch.getPostsFromPages(pageId, lastDate);
					arrUserPost = fbSearch.getPostsFromUsers(userId, lastDate);
					arrGroupPost = fbSearch.getPostsFromGroups(groupId,
							lastDate);
				} catch (Exception e) {
					e.printStackTrace();
				}

				List<Post> simplePosts = new ArrayList<>();

				simplePosts = parsePost(arrUserPost, simplePosts);
				simplePosts = parsePost(arrPagePost, simplePosts);
				simplePosts = parsePost(arrGroupPost, simplePosts);
				
				// create a thread to normalize and filter posts
				// in the thread, the posts also are stored in a database and
				// file
				if (simplePosts.size() > 0) {
					PostPreprocessorRunnable threadPreprocessor = new PostPreprocessorRunnable(
							simplePosts);
					threadPreprocessor
							.setNormalizer(preprocFactory
									.getNormalizerInstance("normalizers.PipelineNormalizer"));
					threadPreprocessor
							.setFilter(preprocFactory
									.getFilterInstance("filters.PortuguesePipelineFilter"));
					Thread task = new Thread(threadPreprocessor);
					task.start();
				}

				logger.log(Level.INFO, "{0} posts", simplePosts.size());

				System.out.println(MessageFormat.format("[{0}] {1}: {2} posts",
						new SimpleDateFormat("E MMM d HH:mm:ss z y")
								.format(new Date()), Level.INFO, simplePosts
								.size()));
			} catch (FacebookException fe) {
				logger.severe(fe.getMessage());
			}

			time++;
			waitTimeNextSearch(queryInterval);
			exp += 590;
		}
	}

	/**
	 * Thread for wait sometime before the next query
	 * 
	 * @param time
	 *            - time in millis
	 */
	private static void waitTimeNextSearch(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException ie) {
			logger.severe(ie.getMessage());
			System.err.println("Thread sleep failed.");
		}
	}
	
	@SuppressWarnings("unchecked")
	private static List<Post> parsePost(List<JSONObject> objPost, List<Post> simplePosts) {
		for (JSONObject obj : objPost) {
			PostUser user = new PostUser(obj);

			obj.put("user", user);

			SimplifiedPost simplifiedPost = new SimplifiedPost(obj);

			simplePosts.add(simplifiedPost);
		}
		
		return simplePosts;
	}
}