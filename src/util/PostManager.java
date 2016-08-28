package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class PostManager {

	/**
	 * Fetch 4 days ago date
	 * 
	 * @return sinceDate - 4 days ago date
	 */
	public static Date getLastDays() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss");
		Date sinceDate = null;

		String today = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
				.format(new Date());

		try { 
			sinceDate = dateFormat.parse(today);
			sinceDate.setTime(sinceDate.getTime() - (1000 * 360000));
 
			return sinceDate;
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * Fetch today's date
	 * 
	 * @return sinceDate - Today's date
	 */
	public static Date getSinceDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss");
		Date sinceDate = null;

		String today = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
				.format(new Date());

		try { 
			sinceDate = dateFormat.parse(today);
			sinceDate.setTime(sinceDate.getTime() - 10 * 60000);
 
			return sinceDate;
		} catch (ParseException e) {
			return null;
		}
	}
}
