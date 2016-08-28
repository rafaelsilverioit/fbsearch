package search;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import util.Token;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Version;

/**
 * Get configurations to restFB API and FacebookSearch
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class SearchConfiguration {
	/**
	 * Read properties files from directory "confs"
	 * 
	 * @param fieName
	 *            file name
	 * @return configuration properties
	 * @throws FileNotFoundException
	 */
	public static Properties readProperties(String fileName)
			throws FileNotFoundException {
		Properties prop;

		try {
			prop = new Properties();
			InputStream inFileProp = new FileInputStream("confs/" + fileName);

			prop.load(inFileProp);

			inFileProp.close();
		} catch (IOException e) {
			throw new FileNotFoundException(
					"Property file not found in resources.");
		}
		return prop;
	}

	/**
	 * Return a configuration of restFB API. restFB configuration file must be
	 * in directory "confs".
	 * 
	 * @param fileName
	 *            file name
	 * @return configuration object used by restFB API
	 * @throws Exception 
	 */
	public static FacebookClient getConfig(String fileName)
			throws Exception {
		Properties prop = readProperties(fileName);
		
		String appid = prop.getProperty("appid");
		String appsecret = prop.getProperty("appsecret");
		
//		System.out.println(appsecret);
	
		@SuppressWarnings("deprecation")
		DefaultFacebookClient df = new DefaultFacebookClient();
		
		AccessToken accessToken = df.obtainAppAccessToken(appid, appsecret);
		
		FacebookClient facebookClient = new DefaultFacebookClient(
				"EAAWIDJSdaDgBANu7ZCzconBLjijTGVzsZBHDrPd25N0Y0vqWZBzZCkFNz8Aknm6kqKSh40YGkAiZC23ZCbfZCawnze5doTsj1Oy8wf8qje5YAB2cRFXyms5EAwYvS6nQ1Y5fMhCc79GZA4Few8yqVajuX9bzGVS6zgQOieEZA5BJjVgZDZD",
				Version.VERSION_2_3);
		
//		System.out.println(accessToken.getAccessToken());
//		System.out.println(df.debugToken(accessToken.getAccessToken()));
//		
		if(Token.isValid(facebookClient)){
			return facebookClient;
		} else {
			throw new Exception("Error while generating new access token!");
		}
	}
}