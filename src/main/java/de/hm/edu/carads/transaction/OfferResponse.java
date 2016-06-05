package de.hm.edu.carads.transaction;

/**
 * Diese Klasse repreasentiert eine Antwort an eine Buchungsanfrage (Fahrer an Werbenden).
 * 
 * @author BK
 *
 */
public class OfferResponse {
	
	/**
	 * Die FahrzeugID des Fahrzeugs, fuer welches die Anfrage gestellt wurde.
	 */
	private String carId;
	
	/**
	 * Die ID des Werbenden.
	 */
	private String advertiserId;
	
	/**
	 * Die ID der Kampagne.
	 */
	private String campaignId;
	
	/**
	 * Die tatsaechliche Antwort.
	 */
	private String response;

	/**
	 * Der Konstruktor mit allen Attributen.
	 * @param carId
	 * @param advertiserId
	 * @param campaignId
	 * @param response
	 */
	public OfferResponse(String carId, String advertiserId, String campaignId,
			String response) {
		this.carId = carId;
		this.advertiserId = advertiserId;
		this.campaignId = campaignId;
		this.response = response;
	}

	public String getCarId() {
		return carId;
	}

	public String getAdvertiserId() {
		return advertiserId;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public String getResponse() {
		return response;
	}
}
