package persistence.dao;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistence.DatabaseManager;
import persistence.entities.PostUrl;

/**
 *
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class PostsUrlsDAO {

	/**
	 * Insert post's urls on database
	 * 
	 * @param urls
	 *            - Urls from post.
	 * @throws PropertyVetoException
	 */
	public void insert(PostUrl urls) throws PropertyVetoException {
		Connection connection = null;
		Statement statement = null;

		String sql = "INSERT INTO urls (" + "post_id," + "url" + ") VALUES ("
				+ '\'' + urls.getPost_id() + '\'' + "," + '\'' + urls.getUrl()
				+ '\'' + ");";

		try {
			connection = DatabaseManager.getConnection();
			statement = connection.createStatement();
			statement.executeUpdate(sql);
			statement.close();
			connection.close();
		} catch (SQLException ex) {
			Logger.getLogger(PostsDAO.class.getName()).log(Level.SEVERE, null,
					ex);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Get Urls from a post - use the post id
	 * 
	 * @param postId
	 *            - Post IDentification number.
	 * @return - Urls from post.
	 * @throws PropertyVetoException
	 */
	public static List<PostUrl> getUrlsByPostId(String postId)
			throws PropertyVetoException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSqlSelect = null;
		List<PostUrl> listOfPostsUrlsFromDB = new ArrayList<PostUrl>();
		String sql = "SELECT * FROM urls WHERE post_id = \'" + postId + "\';";

		try {
			connection = DatabaseManager.getConnection();
			statement = connection.createStatement();
			resultSqlSelect = statement.executeQuery(sql);
			while (resultSqlSelect.next()) {
				// post
				PostUrl urls = new PostUrl();

				urls.setPost_id(resultSqlSelect.getString("post_id"));
				urls.setUrl(resultSqlSelect.getString("url"));

				listOfPostsUrlsFromDB.add(urls);
			}
		} catch (SQLException ex) {
			Logger.getLogger(PostsDAO.class.getName()).log(Level.SEVERE, null,
					ex);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return listOfPostsUrlsFromDB;
	}
}