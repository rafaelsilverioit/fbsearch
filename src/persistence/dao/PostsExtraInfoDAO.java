package persistence.dao;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistence.DatabaseManager;
import persistence.entities.PostClassificationData;
import persistence.entities.PostExtraInfo;

/**
 * Store and recover TweetExtraInfo about posts in database
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class PostsExtraInfoDAO {
	private final String insertClassification = "INSERT INTO classification("
			+ "post_id," + "algorithm," + "alert_level" + ") VALUES ("
			+ "?,?,?);";
	private PreparedStatement pstmInsertClassification;

	public PostsExtraInfoDAO() {
		try {
			pstmInsertClassification = DatabaseManager.getConnection()
					.prepareStatement(insertClassification);
		} catch (SQLException ex) {
			Logger.getLogger(PostsDAO.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	/**
	 * Insert extra info on database
	 * 
	 * @param postId
	 *            - Post IDentification number
	 * @param extraInfo
	 *            - Object with extra information
	 * @throws PropertyVetoException
	 */
	public void insert(String postId, PostExtraInfo extraInfo)
			throws PropertyVetoException {
		try {
			// casting - TODO: the correct is analyze the type of ExtraInfo
			PostClassificationData postClassification = (PostClassificationData) extraInfo;

			pstmInsertClassification.setString(1, postId);
			pstmInsertClassification.setString(2,
					postClassification.getAlgorithmName());
			pstmInsertClassification.setFloat(3,
					postClassification.getAlertLevel());

			pstmInsertClassification.executeUpdate();
		} catch (SQLException ex) {
			Logger.getLogger(PostsDAO.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	/**
	 * Get PostExtraInfo using post id
	 * 
	 * @param postId
	 *            - Post IDentification number
	 * @return - PostExtraInfo associated to postId
	 * @throws - PropertyVetoException
	 */
	public static List<PostExtraInfo> getExtraInfoByPostId(String postId)
			throws PropertyVetoException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSqlSelect = null;

		List<PostExtraInfo> listOfPostsExtraInfo = new ArrayList<>();

		String sql = "SELECT * FROM classification WHERE post_id = \'" + postId
				+ "\';";

		try {
			connection = DatabaseManager.getConnection();
			statement = connection.createStatement();
			resultSqlSelect = statement.executeQuery(sql);

			while (resultSqlSelect.next()) {
				String algorithmDB = resultSqlSelect.getString("algorithm");
				Float alertLevelDB = resultSqlSelect.getFloat("alert_level");

				PostExtraInfo extraInfo = new PostClassificationData(
						alertLevelDB, algorithmDB);
				listOfPostsExtraInfo.add(extraInfo);
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

		return listOfPostsExtraInfo;
	}
}