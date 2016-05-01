package de.hm.edu.carads.models.util;

import de.hm.edu.carads.models.Campaign;
import de.hm.edu.carads.models.Car;

public class OfferInformation {
	private Car car;
	private Campaign campaign;
	private FellowState state;
	
	public OfferInformation(Car car, Campaign campaign, FellowState state){
		this.car = car;
		this.campaign = campaign;
		this.state = state;
	}
	
	
}
