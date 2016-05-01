package de.hm.edu.carads.models.comm;

import de.hm.edu.carads.models.util.FellowState;

public class Response {
	private String carId;
	private String campaignId;
	private FellowState response;
	
	public Response(String carId, String campaignId, FellowState response) {
		this.carId = carId;
		this.campaignId = campaignId;
		this.response = response;
	}	
}
