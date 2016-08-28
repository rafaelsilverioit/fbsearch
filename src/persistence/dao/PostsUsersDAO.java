package persistence.dao;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

import persistence.DatabaseManager;
import persistence.entities.PostUser;

/**
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class PostsUsersDAO {
	/**
	 * Insert user on database
	 * 
	 * @param user
	 *            - Faceboook user
	 * @throws PropertyVetoException
	 */
	@SuppressWarnings("unchecked")
	public void insert(PostUser user) throws PropertyVetoException {
		Connection connection = null;
		Statement statement = null;
		JSONObject obj = new JSONObject();
		obj.put("user", user.getName());

		String sql = "INSERT INTO users (" + "user_id," + "username," + "url"
				+ ") VALUES (" + '\'' + user.getUserId() + '\'' + "," + '\''
				+ user.getName() + '\'' + "," + '\''
				+ "facebook.com/".concat(user.getUserId()) + '\'' + ");";

		try {
			connection = DatabaseManager.getConnection();
			statement = connection.createStatement();
			statement.executeQuery(sql);
			statement.close();
			connection.close();
		} catch (SQLException ex) {
			// System.out.println();
			// ex.printStackTrace();
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
	 * Get Facebook user from database using the user ID
	 * 
	 * @param userId
	 *            - User IDentification number
	 * @return user - Facebook user
	 * @throws PropertyVetoException
	 */
	public static PostUser getUserByUserId(String userId)
			throws PropertyVetoException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSqlSelect = null;

		PostUser postUser = new PostUser();

		String sql = "SELECT * FROM users WHERE user_id = \'" + userId + "\';";

		try {
			connection = DatabaseManager.getConnection();
			statement = connection.createStatement();
			resultSqlSelect = statement.executeQuery(sql);
			resultSqlSelect.next();

			postUser.setUserId(resultSqlSelect.getString("user_id"));
			postUser.setName(resultSqlSelect.getString("username"));
			postUser.setUrl(resultSqlSelect.getString("url"));
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

		return postUser;
	}
}
