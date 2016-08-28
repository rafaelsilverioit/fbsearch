package persistence.entities;

import org.json.simple.JSONObject;
import util.StringManager;

/**
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class PostUser {
	// User ID
	private String userId;
	// User name
	private String name;
	// User url
	private String url;
	// Process to JSON Post
	private JSONObject userJSON;

	/**
	 * Create the user using JSON.
	 * 
	 * @param obj
	 *            - User JSON (that is part from Post JSON Object).
	 */
	public PostUser(JSONObject user) {
		if (user != null) {
			userJSON = user;
			this.userId = this.getUserIdFromJSON();
			this.name = this.getNameFromJSON();
			this.url = this.getUrlFromJSON();
		}
	}

	public PostUser() {

	}

	/**
	 * Get Post ID
	 * 
	 * @return user - User ID
	 */
	public String getUserIdFromJSON() {
		return this.userJSON.get("user_id").toString();
	}

	/**
	 * Get username
	 * 
	 * @return name - Username
	 */
	public String getNameFromJSON() {
		if (this.userJSON.get("user") != null) {
			String name = this.userJSON.get("user").toString();
			name = StringManager.removeTroubleCharactersFromString(name);
			// System.out.println(name + " --- SEE");
			return name;
		}

		return "";
	}

	/**
	 * Get user url
	 * 
	 * @return url - URL from user
	 */
	public String getUrlFromJSON() {
		if (this.userJSON.get("user_url") != null) {
			return this.userJSON.get("user_url").toString();
		}

		return "";
	}

	/**
	 * Get printable information from user
	 * 
	 * @return - Printable user information
	 */
	public String getForPrintUserInformation() {
		return " user_id: " + this.userId + " name: " + this.name + " url: "
				+ this.url;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String user_id) {
		this.userId = user_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public JSONObject getUserJSON() {
		return userJSON;
	}

	public void setUserJSON(JSONObject userJSON) {
		this.userJSON = userJSON;
	}
}
