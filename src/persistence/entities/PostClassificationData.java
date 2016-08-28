package persistence.entities;

/**
 * Used to save post classification data
 * 
 * @author Rafael Silvério Amaral
 * @email rafael.silverio.it@gmail.com
 */
public class PostClassificationData implements PostExtraInfo {

	// alert level can be represented by a percentual or value
	// and estimate if a post represents an alert
	private final Float alertLevel;

	// store the algorithm used to calc alertLevel
	private final String algorithmName;

	public PostClassificationData(Float alertLevel, String algorithmName) {
		this.alertLevel = alertLevel;
		this.algorithmName = algorithmName;
	}

	public Float getAlertLevel() {
		return alertLevel;
	}

	public String getAlgorithmName() {
		return algorithmName;
	}

	@Override
	public String getExtraDescription() {
		return algorithmName;
	}
}
