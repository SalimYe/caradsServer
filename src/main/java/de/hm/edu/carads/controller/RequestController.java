package de.hm.edu.carads.controller;

import java.util.Collection;

import de.hm.edu.carads.models.util.OfferInformation;

public interface RequestController {
	public Collection<OfferInformation> getOfferInformation(String driverId) throws Exception;
}
