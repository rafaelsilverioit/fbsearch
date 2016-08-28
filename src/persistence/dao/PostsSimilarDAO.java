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
import persistence.entities.Post;
import persistence.entities.PostAlertGroup;

/**
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class PostsSimilarDAO {
	/**
	 * Insert a group and its posts into database. Post ID e Group ID are the
	 * same when a group has only one post.
	 * 
	 * @param postId
	 *            - Post IDentification number
	 * @param groupId
	 *            - Group IDentification number
	 * @throws PropertyVetoException
	 */
	public void insert(String postId, String groupId)
			throws PropertyVetoException {
		Connection connection = null;
		Statement statement = null;

		try {
			connection = DatabaseManager.getConnection();
			statement = connection.createStatement();

			String sql = "INSERT INTO similar_posts(" + "group_id," + "post_id"
					+ ") VALUES ('" + groupId + "','" + postId + "'" + ");";
			System.out.println(sql);
			statement.executeQuery(sql);
		} catch (SQLException ex) {
			Logger.getLogger(PostsDAO.class.getName()).log(Level.SEVERE, null,
					ex);
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException ex) {
				Logger.getLogger(PostsSimilarDAO.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
	}

	/**
	 * Insert a group and their posts into database
	 * 
	 * @param postAlertGroup
	 *            - Group of Posts that are relevant alerts
	 * @throws PropertyVetoException
	 */
	public void insert(PostAlertGroup postAlertGroup)
			throws PropertyVetoException {
		Connection connection = null;
		Statement statement = null;

		String sql = "";

		try {
			connection = DatabaseManager.getConnection();

			for (Post post : postAlertGroup.getArraysSimilarPosts()) {
				sql = "INSERT INTO similar_posts (" + "group_id," + "post_id"
						+ ") VALUES ('" + postAlertGroup.getGroupId() + "','"
						+ post.getPostId() + "');";

				statement = connection.createStatement();
				statement.executeUpdate(sql);
			}
		} catch (SQLException ex) {
			Logger.getLogger(PostsDAO.class.getName()).log(Level.SEVERE, null,
					ex);
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException ex) {
				Logger.getLogger(PostsSimilarDAO.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
	}

	/**
	 * Select all postIds associated to a specific group
	 * 
	 * @param groupid
	 *            - Group IDentification
	 * @return list of postId
	 */
	public List<String> getPostsFromGroup(String groupId) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSqlSelect = null;

		List<String> groupPosts = new ArrayList<>();

		try {
			connection = DatabaseManager.getConnection();

			String sql = "SELECT post_id FROM similar_posts WHERE group_id = '"
					+ groupId + "'";

			statement = connection.createStatement();
			resultSqlSelect = statement.executeQuery(sql);

			while (resultSqlSelect.next()) {
				groupPosts.add(resultSqlSelect.getString("post_id"));
			}
		} catch (SQLException ex) {
			Logger.getLogger(PostsDAO.class.getName()).log(Level.SEVERE, null,
					ex);
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException ex) {
				Logger.getLogger(PostsSimilarDAO.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}

		return groupPosts;
	}
}