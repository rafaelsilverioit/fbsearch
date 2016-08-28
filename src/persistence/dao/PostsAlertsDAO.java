package persistence.dao;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import persistence.DatabaseManager;
import persistence.entities.PostAlertGroup;

import util.DateManager;

/**
 *
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class PostsAlertsDAO {
	/**
	 * Insert a group of similar posts on database (the group represents an
	 * alert message)
	 * 
	 * @param postAlertGroup
	 *            - Group of similar posts
	 * @throws PropertyVetoException
	 */
	public void insert(PostAlertGroup postAlertGroup)
			throws PropertyVetoException {
		Connection connection = null;
		Statement statement = null;

		String sql = "INSERT INTO alerts (" + "group_id," + "users_quantity,"
				+ "posts_quantity," + "texts," + "first_date," + "last_date,"
				+ "burst_number," + "alert," + "type" + ") VALUES (" //
				+ '\''
				+ postAlertGroup.getGroupId()
				+ '\''
				+ ","
				+ postAlertGroup.getUserQuantity()
				+ ","
				+ postAlertGroup.getPostQuantity()
				+ ","
				+ '\''
				+ postAlertGroup.getText()
				+ '\''
				+ ","
				+ '\''
				+ DateManager.formatterDateDB.format(postAlertGroup
						.getFirstDate().getTime())
				+ '\''
				+ ","
				+ '\''
				+ DateManager.formatterDateDB.format(postAlertGroup
						.getLastDate().getTime())
				+ '\''
				+ ","
				+ postAlertGroup.getBurstNumber()
				+ ","
				+ '\''
				+ postAlertGroup.getAlert()
				+ '\''
				+ ","
				+ postAlertGroup.getType() + ");";

		try {
			connection = DatabaseManager.getConnection();
			statement = connection.createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException ex) {
			Logger.getLogger(PostsDAO.class.getName()).log(Level.SEVERE, null,
					ex);
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException ex) {
				Logger.getLogger(PostsAlertsDAO.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
	}

	/**
	 * Update last_date and posts_quantity
	 * 
	 * @param groupId
	 *            - record with Group IDentification number to be updated
	 * @param lastDate
	 *            - post's date that will be added in groupId
	 * @throws PropertyVetoException
	 */
	public void update(String groupId, Date lastDate)
			throws PropertyVetoException {
		Connection connection = null;
		Statement statement = null;

		String sql = "SELECT * FROM alerts WHERE group_id = '" + groupId + "';";

		try {
			connection = DatabaseManager.getConnection();
			statement = connection
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
			ResultSet resultSet = statement.executeQuery(sql);

			if (resultSet.next()) {
				int postsNum = resultSet.getInt("posts_quantity") + 1;
				String dateStr = DateManager.formatterDateDB.format(lastDate);
				resultSet.updateTimestamp("last_date",
						Timestamp.valueOf(dateStr));
				resultSet.updateInt("posts_quantity", postsNum);
				resultSet.updateRow();
			}

			resultSet.close();
		} catch (SQLException ex) {
			Logger.getLogger(PostsDAO.class.getName()).log(Level.SEVERE, null,
					ex);
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException ex) {
				Logger.getLogger(PostsAlertsDAO.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
	}

	/**
	 * Get relevant alerts from table
	 * 
	 * @return - List of posts grouped by this alert
	 * @throws PropertyVetoException
	 */
	public List<PostAlertGroup> getRelevantAlertMessages()
			throws PropertyVetoException {
		Connection connection = null;
		Statement statement = null;

		ResultSet resultSqlSelect;
		List<PostAlertGroup> listOfRelevantAlertMessagesFromDB = new ArrayList<>();

		String sql = "SELECT * FROM alerts WHERE type = "
				+ PostAlertGroup.TYPE_RELEVANT_ALERT_MESSAGE + ";";

		try {
			connection = DatabaseManager.getConnection();
			statement = connection.createStatement();
			resultSqlSelect = statement.executeQuery(sql);

			while (resultSqlSelect.next()) {
				// Group of similar posts (alerts)
				PostAlertGroup alerts = new PostAlertGroup();
				alerts.setGroupId(resultSqlSelect.getString("group_id"));
				alerts.setUserQuantity(resultSqlSelect.getInt("users_quantity"));
				alerts.setPostQuantity(resultSqlSelect.getInt("posts_quantity"));
				alerts.setText(resultSqlSelect.getString("texts"));
				alerts.setFirstDate(resultSqlSelect.getTimestamp("first_date"));
				alerts.setLastDate(resultSqlSelect.getTimestamp("last_date"));
				alerts.setBurstNumber(resultSqlSelect.getInt("burst_number"));
				alerts.setAlert(resultSqlSelect.getString("alert"));
				alerts.setType(resultSqlSelect.getInt("type"));
				alerts.setLogicalControl(resultSqlSelect
						.getInt("logical_control"));

				listOfRelevantAlertMessagesFromDB.add(alerts);
			}
		} catch (SQLException ex) {
			Logger.getLogger(PostsDAO.class.getName()).log(Level.SEVERE, null,
					ex);
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException ex) {
				Logger.getLogger(PostsAlertsDAO.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}

		return listOfRelevantAlertMessagesFromDB;
	}
}