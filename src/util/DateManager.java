package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Modified from TwitterSearch Project by:
 * 
 * @author Luiz Arthur Feitosa dos Santos and Rodrigo Campiolo
 * @email luizsantos@utfpr.edu.br and rcampiolo@utfpr.edu.br
 */
public class DateManager {

	// Format datetime. old 2012 - Used to get tweet date from Json object.
	//
	// public static SimpleDateFormat formatterDatePost = new
	// SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH); 
	// 
	
	/**
	 *  New 2014/2015 - Used to get post date from Json object.
	 */
	public static SimpleDateFormat formatterDatePost = new SimpleDateFormat(
			"E MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
	
	/**
	 *  Used to format datetime.
	 */
	public static SimpleDateFormat formatterDate = new SimpleDateFormat(
			"yyyy/MMM/dd", Locale.ENGLISH);
	
	/**
	 *  Used to format datetime to be write as file name.
	 */
	public static SimpleDateFormat formatterDateToFile = new SimpleDateFormat(
			"yyyyMMMdd", Locale.ENGLISH);
	
	/** 
	 * Used to format datetime to be write as file name.
	 */
	public static SimpleDateFormat formatterDateToFileWithTime = new SimpleDateFormat(
			"yyyyMMMdd_HHmmss", Locale.ENGLISH); 
	
	/** 
	 * Datetime format required by database, including hours, minutes and seconds.
	 * 
	 * public static SimpleDateFormat formatterDateDB = new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); 
	 */
	
	/**
	 * Datetime format required by database.
	 */
	public static SimpleDateFormat formatterDateDB = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	
	/** 
	 * Datetime format required by database, but only date!
	 */
	public static SimpleDateFormat formatterDateDBOnlyDate = new SimpleDateFormat(
			"yyyy-MM-dd"); 

	/**
	 * Get current Date/Time from the system!
	 * 
	 * @return Date/Time.
	 */
	public static Calendar getCurrentDateTimeFromSystem() {
		return Calendar.getInstance();
	}

	/**
	 * Get current Date/Time from the system minus an amount of time in days!
	 * 
	 * @return Date/Time.
	 */
	public static Calendar getCurrentDateTimeMinusAmountOfDays(int periodInDays) {
		Calendar currentDate = Calendar.getInstance();
		Calendar currentDateLessPeriodTime = Calendar.getInstance(); // get
																		// current
																		// date
																		// from
																		// system.
		currentDateLessPeriodTime.setTime(currentDate.getTime());
		currentDateLessPeriodTime.add(Calendar.DAY_OF_MONTH,
				(periodInDays * -1));
		return currentDateLessPeriodTime;
	}

	/**
	 * Transform date in a formated YYYY/MMM/dd string.
	 * 
	 * @param calendar
	 *            - Date to be converted to string.
	 * @return - Date string.
	 */
	public static String dateToFormatedString(Calendar calendar) {
		Date date = calendar.getTime();
		return formatterDate.format(date).toString();
	}

	/**
	 * Transform date in a formated YYYY-MMM-dd string.
	 * 
	 * @param calendar
	 *            - Date to be converted to string.
	 * @return - Date string YYYY-MMM-dd (not time).
	 */
	public static String dateToFormatedStringDBOnlyDate(Calendar calendar) {
		Date date = calendar.getTime();
		return formatterDateDBOnlyDate.format(date).toString();
	}

	/**
	 * Transform date in a formated YYYYMMMdd string to filename (without "/").
	 * 
	 * @param calendar
	 *            - Date to be converted to string.
	 * @return - Date string.
	 */
	public static String dateToFormatedStringFileName(Calendar calendar) {
		Date date = calendar.getTime();
		return formatterDateToFile.format(date).toString();
	}

	/**
	 * Convert a string with format yyyy/MMM/dd (CAUTION month with characters,
	 * e.g JAN, FEB...) to a java calendar! Example of date 2015/Jan/02 year
	 * 2015, month 01 (Jan), day 02.
	 * 
	 * @param stringDate
	 *            - String with the format yyyy/MMM/dd.
	 * @return - Calendar with the date passed.
	 */
	public static Calendar formatedStringToDate(String stringDate) {
		try {
			System.out.println("date to be converted: " + stringDate);
			Calendar calendar = Calendar.getInstance();
			Date date = formatterDate.parse(stringDate);
			calendar.setTime(date);
			return calendar;
		} catch (ParseException ex) {
			ex.printStackTrace();
			System.out.println("ERROR during parsing string to date!");
		}
		return null;
	}

	/**
	 * Convert a string with format yyyy-MM-dd HH:mm:ss.SSS used in Mysql
	 * database to a java calendar!
	 * 
	 * @param stringDate
	 *            - String with the format yyyy-MM-dd HH:mm:ss.SSS.
	 * @return - Calendar with the date passed.
	 */
	public static Calendar formatedStringDBToCalendar(String stringDate) {
		try {
			System.out.println("date to be converted: " + stringDate);
			Calendar calendar = Calendar.getInstance();
			Date date = formatterDateDB.parse(stringDate);
			calendar.setTime(date);
			return calendar;
		} catch (ParseException ex) {
			System.out
					.println("ERROR during parsing string to date database to java!");
		}
		return null;
	}

	/**
	 * Convert a string post date with format E, MMM dd yyyy to a java calendar!
	 * Example of date: Fri, June 7 2013.
	 * 
	 * @param stringDate
	 *            - String with the format E, MMM dd yyyy.
	 * @return - Calendar with the date passed.
	 */
	public static Calendar formatedPostStringToDate(String stringDate) {
		try {
			// System.out.println("date to be converted: "+stringDate);
			Calendar calendar = Calendar.getInstance();
			Date date = formatterDatePost.parse(stringDate);
			calendar.setTime(date);
			return calendar;
		} catch (ParseException ex) {
			ex.printStackTrace();
			System.out.println("ERROR during parsing string to date!");
		}
		return null;
	}

	/**
	 * Verify if one date is between two dates (start date and stop date).
	 * 
	 * @param analyseDate
	 *            - Date to by analyzed.
	 * @param startDate
	 *            - Start date period.
	 * @param stopDate
	 *            - Stop date period.
	 * @return True if analysed date is between startDate and stopDate or False
	 *         if not!
	 */
	public static boolean verifyIfDateIsBetweenStartAndStopDate(
			Calendar analyseDate, Calendar startDate, Calendar stopDate) {
		if (analyseDate.before(startDate) || analyseDate.after(stopDate)) {
			// System.out.println("out of time");
			return false; // out the time
		}
		// System.out.println("Date is on the time:");
		return true; // on the time

	}

	/**
	 * Given a date reduce this date in an amount of days and return this
	 * reduced date.
	 * 
	 * @param dateToBeReduced
	 *            - Date that will be reduced.
	 * @param amountDaysToBeReduced
	 *            - Quantity of days to be reduced of dateToBeReduced.
	 * @return - A new date that is the result of dateToBeReduced minus
	 *         amountDaysToBeReduced.
	 */
	public static Calendar getDateMinusAmountOfDays(Calendar dateToBeReduced,
			int amountDaysToBeReduced) {
		Calendar calendarToUpdate = Calendar.getInstance();
		calendarToUpdate.setTime(dateToBeReduced.getTime());
		calendarToUpdate.add(Calendar.DAY_OF_MONTH,
				(amountDaysToBeReduced * -1));
		return calendarToUpdate;
	}

	/**
	 * Verify is one time is after that current time, is used like a timer!
	 * 
	 * @param verifyDate
	 *            - Date to be verify.
	 * @param datePeriod
	 *            - Just a text to identify the process!
	 * @return - true if current date is greater than verified date (expired
	 *         timer), or false if not (not expired timer)!
	 */
	public static boolean verifyIfTimerExpired(Calendar verifyDate,
			String datePeriod) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - yyyy/MM/dd");
		Calendar currentDate = Calendar.getInstance();
		if (currentDate.after(verifyDate)) {
			System.out.println("\n Update posts of " + datePeriod
					+ " -> verify date: " + sdf.format(verifyDate.getTime())
					+ " - current date: " + sdf.format(currentDate.getTime())
					+ "\n");
			return true; // Current date is greater, then timer expired!
		}
		return false; // Current date not is greater, then timer NOT expired!
	}

	/**
	 * Get current Date/Time from the system minus an amount of time in hours!
	 * 
	 * @param periodInDays
	 *            - Number os days.
	 * @return Date/Time.
	 */
	public static Calendar getCurrentDateTimePlusAmountOfHours(int periodInDays) {
		Calendar currentDate = Calendar.getInstance();
		Calendar currentDateLessPeriodTime = Calendar.getInstance(); // get
																		// current
																		// date
																		// from
																		// system.
		currentDateLessPeriodTime.setTime(currentDate.getTime());
		currentDateLessPeriodTime.add(Calendar.HOUR, (periodInDays * +1));
		return currentDateLessPeriodTime;
	}

	/**
	 * Get current Date/Time from the system plus an amount of time in days!
	 * 
	 * @return Date/Time.
	 */
	public static Calendar getCurrentDateTimePlusAmountDays(int periodInDays) {
		Calendar currentDate = Calendar.getInstance();
		Calendar currentDateLessPeriodTime = Calendar.getInstance(); // get
																		// current
																		// date
																		// from
																		// system.
		currentDateLessPeriodTime.setTime(currentDate.getTime());
		currentDateLessPeriodTime.add(Calendar.DAY_OF_MONTH,
				(periodInDays * +1));
		return currentDateLessPeriodTime;
	}

	/**
	 * Sleep/wait a period of time in seconds!
	 * 
	 * @param timeInSecondsToWait
	 *            - seconds to sleep
	 */
	public static void sleepTimeInSeconds(int timeInSecondsToWait) {
		try {
			Thread.sleep(timeInSecondsToWait * 1000);
		} catch (InterruptedException ex) {
			System.out.println("Error in thread sleep in waitATimeInSeconds!");
		}
	}

	/**
	 * Verify if dates are equals, but just analyzing the Year, Month, and Day!
	 * 
	 * @param date1
	 *            - One date to be analyzed.
	 * @param date2
	 *            - Another date to be analyzed.
	 * @return - True if are equals, False if not!
	 */
	public static boolean verifyIfOnlyYearMonthDayAreEquals(Calendar date1,
			Calendar date2) {
		if (date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)
				&& date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH)
				&& date1.get(Calendar.DAY_OF_MONTH) == date2
						.get(Calendar.DAY_OF_MONTH)) {
			return true; // Date are equals!
		}
		return false; // Date aren't equals!
	}

}