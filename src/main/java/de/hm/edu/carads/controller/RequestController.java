package de.hm.edu.carads.controller;

import java.util.Collection;

import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.comm.OfferInformation;

public interface RequestController {
	public Collection<OfferInformation> getOfferInformation(String driverId) throws Exception;
	public void respondToOffer(String driverId, String carId, String advertiserId,  String campaignId, String respond) throws Exception;
	public Collection<Car> getAvailableCars(String advertiserId, String campaignId) throws Exception;
}
