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
import persistence.entities.PostUserMention;

/**
 * NOT IN USE YET!!
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class PostsUserMentionsDAO {

	/**
	 * Insert user mentions from a post on database.
	 * 
	 * @param userMentions
	 *            - User mention from a post.
	 * @throws PropertyVetoException
	 */
	public void insert(PostUserMention userMentions)
			throws PropertyVetoException {
		Connection connection = null;
		Statement statement = null;

		String sql = "INSERT INTO userMentions (" + "post_id," + "user_id,"
				+ "name" + ") VALUES (" + '\'' + userMentions.getPost_id()
				+ '\'' + "," + '\'' + userMentions.getUser_id() + '\'' + ","
				+ '\'' + userMentions.getName() + '\'' + ");";

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
	 * Get user mentions from a post using the post identification.
	 * 
	 * @param postId
	 *            - Post IDentification number.
	 * @return - User mentions from a post.
	 * @throws PropertyVetoException
	 */
	public static List<PostUserMention> getUserMentionsByPostId(String postId)
			throws PropertyVetoException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSqlSelect = null;

		List<PostUserMention> listOfPostsUserMentionsFromDB = new ArrayList<PostUserMention>();
		String sql = "SELECT * FROM userMentions WHERE post_id = \'" + postId
				+ "\';";

		try {
			connection = DatabaseManager.getConnection();
			statement = connection.createStatement();
			resultSqlSelect = statement.executeQuery(sql);
			while (resultSqlSelect.next()) {
				PostUserMention userMention = new PostUserMention();
				userMention.setPost_id(resultSqlSelect.getString("post_id"));
				userMention.setUser_id(resultSqlSelect.getString("user_id"));
				userMention.setName(resultSqlSelect.getString("name"));
				listOfPostsUserMentionsFromDB.add(userMention);
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

		return listOfPostsUserMentionsFromDB;
	}
}