package search;

import filters.*;
import normalizers.*;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Create instances of normalizers and filters.
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class PreprocessorFactory {
	private final HashMap<String, PostNormalizer> normalizersMap;
	private final HashMap<String, PostFilter> filtersMap;

	public PreprocessorFactory() {
		normalizersMap = new HashMap<>();
		filtersMap = new HashMap<>();
	}

	/**
	 * Get a single instance of a specific normalizer
	 * 
	 * @param normalizerName
	 *            - Normalizer Class Name
	 * @return Instance of PostNormalizer
	 */
	public PostNormalizer getNormalizerInstance(String normalizerName) {
		if (normalizersMap.containsKey(normalizerName)) {
			return normalizersMap.get(normalizerName);
		}

		try {
			PostNormalizer postNorm = (PostNormalizer) Class.forName(
					normalizerName).newInstance();
			normalizersMap.put(normalizerName, postNorm);
			return postNorm;
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException ex) {
			Logger.getLogger(PreprocessorFactory.class.getName()).log(
					Level.SEVERE, null, ex);
		}

		return null;
	}

	/**
	 * Get a single instance of a specific filter
	 * 
	 * @param filterName
	 *            - Filter Class Name
	 * @return instance of PostFilter
	 */
	public PostFilter getFilterInstance(String filterName) {
		if (filtersMap.containsKey(filterName)) {
			return filtersMap.get(filterName);
		}

		try {
			PostFilter postFilter = (PostFilter) Class.forName(filterName)
					.newInstance();
			filtersMap.put(filterName, postFilter);
			return postFilter;
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException ex) {
			Logger.getLogger(PreprocessorFactory.class.getName()).log(
					Level.SEVERE, null, ex);
		}

		return null;
	}
}
