package persistence;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import persistence.entities.Post;
import persistence.entities.SimplifiedPost;

/**
 * Write data into files
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class FileManager {
	/**
	 * Save posts into json file
	 * 
	 * @param jsonText
	 *            - json post format
	 * @param fileName
	 *            - output file
	 */
	public static void saveJsonToFile(String jsonText, String fileName) {
		try {
			FileWriter fW = new FileWriter(fileName, true);
			BufferedWriter out = new BufferedWriter(fW);
			out.write(jsonText + "\n");
			out.close();
		} catch (IOException e) {
			System.err
					.println("Error while writing into " + fileName + ".json");
		}
	}

	/**
	 * Read all posts from a json file
	 * 
	 * @param path
	 *            - path + filename
	 * @return list of Posts
	 */
	// @SuppressWarnings("unchecked")
	public static List<Post> readPostsFromJsonFile(String path) {

		try {
			Path filePath = new File(path).toPath();
			byte[] list = Files.readAllBytes(filePath);
			String jsonPosts = "[" + new String(list) + "]";

			new JSONValue();
			Object obj = JSONValue.parse(jsonPosts);
			JSONArray jsonArrayAllPosts = (JSONArray) obj;

			List<Post> postsList = new ArrayList<Post>();

			for (Object jsonArrayAllPost : jsonArrayAllPosts) {
				JSONObject postJSON = (JSONObject) jsonArrayAllPost;
				Post post = new SimplifiedPost(postJSON);
				postsList.add(post);
			}

			return postsList;
		} catch (IOException e) {
			Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE,
					null, e);
		}
		return null; // error
	}

	/**
	 * Read string from file and store in a list.
	 * 
	 * @param path
	 *            - path and filename
	 * @return list - list of string
	 */
	public static List<String> readStringLineFromFile(String path) {
		List<String> stringList = null;

		try {
			Path filePath = new File(path).toPath();
			Charset charset = Charset.defaultCharset();
			stringList = Files.readAllLines(filePath, charset);
		} catch (IOException ex) {
			Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE,
					null, ex);
		}

		return stringList;
	}

	/**
	 * Read lines from file and remove white spaces (begin and end of strings)
	 * and commentaries
	 * 
	 * @param path
	 *            - filename
	 * @return list of cleaned strings
	 */
	public static List<String> readCleanStringLineFromFile(String path) {
		List<String> stringList = readStringLineFromFile(path);
		List<String> cleanStringList = new ArrayList<>();

		for (String str : stringList) {
			@SuppressWarnings("unused")
			String cleanString = str.trim();

			// if it isn't a commentary or white line, add to list of cleaned
			// strings
			if (!str.startsWith("#") && !str.isEmpty()) {
				cleanStringList.add(str);
			}
		}

		return cleanStringList;
	}

	/**
	 * Write lines to file
	 * 
	 * @param path
	 *            - path to file
	 * @param lines
	 *            - list of strings
	 */
	public static void writeStringLineToFile(String path, List<String> lines) {
		try (FileWriter writer = new FileWriter(path)) {
			for (String line : lines) {
				writer.write(line + "\n");
			}
		} catch (IOException e) {
			Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE,
					null, e);
		}
	}
}