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
import persistence.entities.PostHashtags;

/**
 *
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class PostsHashtagsDAO {

	/**
	 * Insert a post hashtag on database.
	 * 
	 * @param hashtag
	 * @throws PropertyVetoException
	 */
	public void insert(PostHashtags hashtag) throws PropertyVetoException {
		Connection connection = null;
		Statement statement = null;

		String sql = "INSERT INTO hashtags (" + "post_id," + "texts"
				+ ") VALUES (" + '\'' + hashtag.getPost_id() + '\'' + ","
				+ '\'' + hashtag.getText() + '\'' + ");";

		try {
			connection = DatabaseManager.getConnection();
			statement = connection.createStatement();
			statement.executeUpdate(sql);
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
	 * Get from database the post's hashtags using the Post IDentification.
	 * 
	 * @param postId
	 *            - Post identification.
	 * @return - Hashtags from a post.
	 * @throws PropertyVetoException
	 */
	public static List<PostHashtags> getHashtagsByPostId(String postId)
			throws PropertyVetoException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSqlSelect = null;

		List<PostHashtags> listOfPostsHashtagsFromDB = new ArrayList<PostHashtags>();
		String sql = "SELECT * FROM hashtags WHERE post_id = \'" + postId
				+ "\';";

		try {
			connection = DatabaseManager.getConnection();
			statement = connection.createStatement();
			resultSqlSelect = statement.executeQuery(sql);
			while (resultSqlSelect.next()) {
				PostHashtags hashtag = new PostHashtags();
				hashtag.setPost_id(resultSqlSelect.getString("post_id"));
				hashtag.setText(resultSqlSelect.getString("text"));
				listOfPostsHashtagsFromDB.add(hashtag);
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

		return listOfPostsHashtagsFromDB;
	}
}