package persistence.dao;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import persistence.entities.SimplifiedPost;
import persistence.DatabaseManager;

import util.DateManager;

/**
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class PostsDAO {
	private final String queryPostExist = "select exists(select 1 from posts where post_id=?)";
	private PreparedStatement pstmtSelect;

	public PostsDAO() {
		try {
			pstmtSelect = DatabaseManager.getConnection().prepareStatement(
					queryPostExist);
		} catch (SQLException ex) {
			Logger.getLogger(PostsDAO.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	/**
	 * Insert a post on database, also is included on his own data tables: user,
	 * urls, hashtags, and user mentions!
	 * 
	 * @param post
	 *            - Post.
	 * @return Boolean - post inserted or not inserted
	 * @throws PropertyVetoException
	 */
	public boolean insert(SimplifiedPost post) throws PropertyVetoException {

		int isInserted = 0;
		Connection connection = null;
		Statement statement = null;
		String sql = "INSERT INTO posts ("
				+ "post_id,"
				+ "user_id," // + "user,"
				+ "created_at," + "updated_at," + "message," + "description,"
				+ "likes_count," + "comments_count," + "shares_count,"
				+ "url_quantity," + "hashtags_quantity," + "post_type"
				+ ") VALUES (" // removed + "user_mentions_quantity,"
				+ '\''
				+ post.getPostId()
				+ '\''
				+ ","
				+ '\''
				+ post.getUserId()
				+ '\''
				+ ","
				// + '\''
				// + post.getUserFromPost()
				// + '\''
				// + ","
				+ '\''
				+ DateManager.formatterDateDB.format(post.getCreatedTime()
						.getTime())
				+ '\''
				+ ","
				+ '\''
				+ DateManager.formatterDateDB.format(post.getUpdatedTime()
						.getTime())
				+ '\''
				+ ","
				+ '\''
				+ post.getMessage()
				+ '\''
				+ ","
				+ '\''
				+ post.getDescription()
				+ '\''
				+ ","
				+ post.getLikesCount()
				+ ","
				+ post.getCommentsCount()
				+ ","
				+ post.getSharesCount()
				+ ","
				+ post.getUrlQuantity()
				+ ","
				+ post.getHashtagsQuantity() + ","
				// + post.getUserMentionsQuantity()
				// + ","
				+ '\'' + post.getPostType() + '\'' + ");";

		// System.out.println(sql);
		try {
			connection = DatabaseManager.getConnection();
			statement = connection.createStatement();
			isInserted = statement.executeUpdate(sql);
			statement.close();
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
		}

		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isInserted == 1; // inserted 1 register
	}

	public boolean hasPost(SimplifiedPost post) {
		// System.out.println(post.getForPrintPostInformation());
		try {
			pstmtSelect.setString(1, post.getPostId());
			ResultSet rs = pstmtSelect.executeQuery();
			rs.next();
			return rs.getBoolean(1);
		} catch (SQLException ex) {
			Logger.getLogger(PostsDAO.class.getName()).log(Level.SEVERE, null,
					ex);
		}

		return false;
	}

	/**
	 * Get all posts from database
	 * 
	 * @return posts - List of posts
	 * @throws PropertyVetoException
	 */
	public List<SimplifiedPost> getAllPosts() throws PropertyVetoException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSqlSelect = null;
		List<SimplifiedPost> listOfPostsFromDB = new ArrayList<SimplifiedPost>();
		String sql = "SELECT * FROM posts;";

		try {
			connection = DatabaseManager.getConnection();
			statement = connection.createStatement();
			resultSqlSelect = statement.executeQuery(sql);
			while (resultSqlSelect.next()) {
				// post
				String postId = resultSqlSelect.getString("post_id");

				SimplifiedPost post = new SimplifiedPost();

				Calendar postDate = DateManager
						.formatedStringDBToCalendar(resultSqlSelect
								.getString("created_at"));
				Calendar postUpdate = DateManager
						.formatedStringDBToCalendar(resultSqlSelect
								.getString("updated_at"));

				post.setCreatedTime(postDate.getTime());
				post.setUpdatedTime(postUpdate.getTime());
				// post.setUserId(resultSqlSelect.getString("user_id"));
				post.setMessage(resultSqlSelect.getString("message"));
				post.setDescription(resultSqlSelect.getString("description"));
				post.setLikesCount(resultSqlSelect.getInt("likes_count"));
				post.setCommentsCount(resultSqlSelect.getInt("comments_count"));
				post.setSharesCount(resultSqlSelect.getInt("shares_count"));
				post.setUrlQuantity(resultSqlSelect.getInt("url_quantity"));
				post.setHashtagsQuantity(resultSqlSelect
						.getInt("hashtags_quantity"));
				// post.setUserMentionsQuantity(resultSqlSelect
				// .getInt("user_mentions_quantity"));

				post.setUserFromPost(PostsUsersDAO.getUserByUserId(post
						.getUserId()));
				post.setUrlsFromPost(PostsUrlsDAO.getUrlsByPostId(postId));
				post.setHashtagsFromPost(PostsHashtagsDAO
						.getHashtagsByPostId(postId));
				new PostsExtraInfoDAO();
				// post.setUserMentionsFromPost(PostsUserMentionsDAO
				// .getUserMentionsByPostId(postId));
				post.setPostExtraInfo(PostsExtraInfoDAO
						.getExtraInfoByPostId(postId));

				post.setPostId(postId);
				// charAt(0) to return a char
				post.setPostType(resultSqlSelect.getString("post_type").charAt(
						0));
				listOfPostsFromDB.add(post);
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

		return listOfPostsFromDB;
	}

	/**
	 * Get posts from a specific day
	 * 
	 * @param day
	 *            - Date
	 * @return - List of posts from this day
	 * @throws PropertyVetoException
	 */
	public List<SimplifiedPost> getPostsFromADay(Calendar day)
			throws PropertyVetoException {
		return this.getPostsFromStartToStopDate(day, day);
	}

	/**
	 * Get posts between an interval of dates
	 * 
	 * @param startDate
	 *            - Start date interval
	 * @param stopDate
	 *            - Stop date interval
	 * @return - List of posts
	 * @throws PropertyVetoException
	 */
	public List<SimplifiedPost> getPostsFromStartToStopDate(Calendar startDate,
			Calendar stopDate) throws PropertyVetoException {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSqlSelect = null;
		List<SimplifiedPost> listOfPostsFromDB = new ArrayList<SimplifiedPost>();

		String stringStartDate = DateManager
				.dateToFormatedStringDBOnlyDate(startDate);
		String stringStopDate = DateManager
				.dateToFormatedStringDBOnlyDate(stopDate);

		String sql = "SELECT * FROM posts WHERE created_at BETWEEN \'"
				+ stringStartDate + " 00:00:00\' AND \'" + stringStopDate
				+ " 23:59:59\';";

		try {
			connection = DatabaseManager.getConnection();
			statement = connection.createStatement();
			resultSqlSelect = statement.executeQuery(sql);
			while (resultSqlSelect.next()) {
				// post
				String postId = resultSqlSelect.getString("post_id");

				SimplifiedPost post = new SimplifiedPost();

				Calendar postDate = DateManager
						.formatedStringDBToCalendar(resultSqlSelect
								.getString("created_at"));
				Calendar postUpdate = DateManager
						.formatedStringDBToCalendar(resultSqlSelect
								.getString("updated_at"));

				post.setCreatedTime(postDate.getTime());
				post.setUpdatedTime(postUpdate.getTime());
				// post.setUserId(resultSqlSelect.getString("user_id"));
				post.setMessage(resultSqlSelect.getString("message"));
				post.setDescription(resultSqlSelect.getString("description"));
				post.setLikesCount(resultSqlSelect.getInt("likes_count"));
				post.setCommentsCount(resultSqlSelect.getInt("comments_count"));
				post.setSharesCount(resultSqlSelect.getInt("shares_count"));
				post.setUrlQuantity(resultSqlSelect.getInt("url_quantity"));
				post.setHashtagsQuantity(resultSqlSelect
						.getInt("hashtags_quantity"));
				// post.setUserMentionsQuantity(resultSqlSelect
				// .getInt("user_mentions_quantity"));

				post.setUserFromPost(PostsUsersDAO.getUserByUserId(post
						.getUserId()));
				post.setUrlsFromPost(PostsUrlsDAO.getUrlsByPostId(postId));
				post.setHashtagsFromPost(PostsHashtagsDAO
						.getHashtagsByPostId(postId));
				// post.setUserMentionsFromPost(PostsUserMentionsDAO
				// .getUserMentionsByPostId(postId));
				post.setPostExtraInfo(PostsExtraInfoDAO
						.getExtraInfoByPostId(postId));

				post.setPostId(postId);
				// charAt(0) to return a char
				post.setPostType(resultSqlSelect.getString("post_type").charAt(
						0));
				listOfPostsFromDB.add(post);
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

		return listOfPostsFromDB;
	}
}