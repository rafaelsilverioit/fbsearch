package search;

import java.io.IOException;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Get customized logger to FacebookSearch
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class FacebookLoggerFactory {

	private FacebookLoggerFactory() {

	}

	public static Logger getInstance(String logName, String fileName) {
		// simple log format
		System.setProperty("java.util.logging.SimpleFormatter.format",
				"[%1$tc] %4$s: %5$s %n");

		// define an instance to logger
		Logger logger = Logger.getLogger(logName);

		try {
			// specifies the log location
			FileHandler fh = new FileHandler(fileName, true);

			fh.setFormatter(new SimpleFormatter());

			// links handler to logger
			logger.addHandler(fh);

			// removes log from console
			logger.setUseParentHandlers(false);
		} catch (IOException ioe) {
			System.err.println("Error while accessing log file. " + ioe);
			return null;
		}
		return logger;
	}
}