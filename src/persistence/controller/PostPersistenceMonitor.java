package persistence.controller;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import persistence.FileManager;
import persistence.entities.SimplifiedPost;
import persistence.entities.Post;
import search.SearchConfiguration;

/**
 * Thread to store posts in database
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class PostPersistenceMonitor {
	private final PostsController postsController;
	private String jsonFile;

	private static PostPersistenceMonitor singlePostPersistenceMonitor = null;

	private PostPersistenceMonitor() {
		postsController = new PostsController();

		try {
			jsonFile = SearchConfiguration.readProperties("config.properties")
					.getProperty("jsonbase");
		} catch (FileNotFoundException ex) {
			Logger.getLogger(PostPersistenceMonitor.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

	public synchronized void writeDatabase(List<Post> posts) {
		for (Post post : posts) {
			SimplifiedPost simplePost = (SimplifiedPost) post;

			if (!postsController.hasPost(simplePost)) {
				postsController.persist(simplePost);
				FileManager.saveJsonToFile(simplePost.getPostJSON().toString(),
						jsonFile);
				// .toJSONString(), jsonFile);
			}
		}
	}

	public static PostPersistenceMonitor getInstance() {
		if (singlePostPersistenceMonitor == null) {
			singlePostPersistenceMonitor = new PostPersistenceMonitor();
		}

		return singlePostPersistenceMonitor;
	}
}
