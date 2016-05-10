package de.hm.edu.carads.models.comm;

import de.hm.edu.carads.models.util.FellowState;

public class OfferResponse {
	private String carId;
	private String advertiserId;
	private String campaignId;
	private String response;

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
