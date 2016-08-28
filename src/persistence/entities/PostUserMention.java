package persistence.entities;

/**
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class PostUserMention {
	// Post ID
	private String post_id;
	// User mentioned ID
	private String user_id;
	// User name
	private String name;

	public String getPost_id() {
		return post_id;
	}

	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		String userMentions = "user id: " + this.user_id + ", name : "
				+ this.name;
		return userMentions;
	}
}
