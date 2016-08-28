package persistence;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class DatabaseManager {

	// database connections
	private static ComboPooledDataSource cpds = null;

	/**
	 * Configure database and connection pooling.
	 * 
	 * @throws PropertyVetoException
	 */
	private DatabaseManager() throws PropertyVetoException,
			FileNotFoundException {
		// read database configurations from properties file
		Properties prop = new Properties();

		try {
			InputStream inFileProp = new FileInputStream(
					"confs/database.properties");
			prop.load(inFileProp);
			inFileProp.close();
		} catch (IOException e) {
			throw new FileNotFoundException(
					"Property file not found in resources.");
		}

		// set configurations
		cpds = new ComboPooledDataSource();
		cpds.setDriverClass(prop.getProperty("driver"));
		cpds.setJdbcUrl(prop.getProperty("url"));
		cpds.setUser(prop.getProperty("user"));
		cpds.setPassword(prop.getProperty("password"));

		// set connection pooling
		cpds.setMinPoolSize(3);
		cpds.setMaxPoolSize(150);
		cpds.setAcquireIncrement(15);
		cpds.setMaxStatements(30);
	}

	/**
	 * Get database connection
	 * 
	 * @return database connection.
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		if (cpds == null) {
			try {
				@SuppressWarnings("unused")
				DatabaseManager dm = new DatabaseManager();
			} catch (PropertyVetoException | FileNotFoundException ex) {
				Logger.getLogger(DatabaseManager.class.getName()).log(
						Level.SEVERE, null, ex);
				return null;
			}
		}
		return cpds.getConnection();
	}

}
