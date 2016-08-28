package util;

/**
 * Modified from TwitterSearch 
 * <br>Project by:<br>
 * 
 * @author Luiz Arthur Feitosa dos Santos and Rodrigo Campiolo
 * @email luizsantos@utfpr.edu.br and rcampiolo@utfpr.edu.br
 */
public class StringManager {

	/**
	 * Remove wrong characters from string (like, ', ", non printables);
	 * 
	 * @param string
	 *            - string.
	 * @return string - String without wrong characters.
	 */
	public static String removeTroubleCharactersFromString(String string) {
		return string.replaceAll("[\'\"\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", "?");
	}
}
