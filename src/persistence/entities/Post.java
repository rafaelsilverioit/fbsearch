package persistence.entities;

import java.util.Date;

/**
 * Generic interface to post objects
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public interface Post {
	public String getPostId();

	public String getUserId();

	public PostUser getUserFromPost();

	public String getTo();

	public Date getCreatedTime();

	public Date getUpdatedTime();

	public String getMessage();

	public String getDescription();

	public String getLink();

	public int getLikesCount();

	public int getCommentsCount();

	public int getSharesCount();
}
