package util;

import java.io.IOException;

import java.net.URI;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Taken from TwitterSearch 
 * <br>Project by:<br>
 * 
 * @author Luiz Arthur Feitosa dos Santos and Rodrigo Campiolo
 * @email luizsantos@utfpr.edu.br and rcampiolo@utfpr.edu.br
 */
public class UrlManager {

	/**
	 * Translate short URL to long URL using service 'http://unshorten.it'
	 * 
	 * @param shortUrl
	 *            - short URL to be translated
	 * @return long URL - URL translated
	 */
	public static String unshortenUrl(String shortUrl) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String keyApi = "iBIxKSFQwmp1UeQDx09i95VT6zkWS2vA";
		try {
			HttpGet httpGet = new HttpGet("http://api.unshorten.it?shortURL="
					+ shortUrl + "&apiKey=" + keyApi);
			CloseableHttpResponse response = httpClient.execute(httpGet);

			try {
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					return EntityUtils.toString(entity);
				}
			} finally {
				response.close();
			}
		} catch (IOException | ParseException e) {
			System.out.println("Error: " + e);
		}

		return shortUrl;
	}

	/**
	 * Translate short URL to long using http requests
	 * 
	 * @param shortURL
	 * @return long URL
	 */
	public static String shortToLongURL(String shortUrl) {
		List<URI> redirectLocations = null;
		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();

		try {
			HttpClientContext context = HttpClientContext.create();
			HttpGet httpGet = new HttpGet(shortUrl);
			response = httpClient.execute(httpGet, context);

			try {
				// get all redirection locations
				redirectLocations = context.getRedirectLocations();

				int countUrl = redirectLocations.size();
				if (countUrl > 0) {
					return redirectLocations.get(countUrl - 1).toString();
				}
			} finally {
				if (response != null) {
					response.close();
				}
			}
		} catch (IOException ioe) {
			System.out.println("Error: " + ioe);
		}

		return shortUrl;
	}

}
