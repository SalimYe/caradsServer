package de.hm.edu.carads.controller;

import java.util.Collection;

import de.hm.edu.carads.models.comm.OfferInformation;

public interface RequestController {
	public Collection<OfferInformation> getOfferInformation(String driverId) throws Exception;
	public OfferInformation respondToOffer(String driverId, String campaignId, String respond) throws Exception;
}
