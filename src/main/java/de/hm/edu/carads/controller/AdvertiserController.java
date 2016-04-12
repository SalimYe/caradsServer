package de.hm.edu.carads.controller;

import java.util.Collection;

import de.hm.edu.carads.models.Advertiser;

public interface AdvertiserController {
	public Collection<Advertiser> getAdvertiser();
	public Advertiser getAdvertiser(String id);
	public Advertiser addAdvertiser(Advertiser advertiser);
	public Advertiser changeAdvertiser(String adID, Advertiser advertiser);
	public boolean deleteAdvertiser(String id);
	public boolean existAdvertiserByEmail(String email);
}
