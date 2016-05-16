package de.hm.edu.carads.controller;

import java.util.Collection;

import javax.ws.rs.core.NoContentException;

import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Campaign;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;
import de.hm.edu.carads.models.comm.EnrichedCampaign;
import de.hm.edu.carads.models.comm.OfferInformation;

public interface ModelController {
	public Collection<Driver> getAllDrivers();
	public Driver addDriver(Driver driver) throws Exception;
	public Driver getDriver(String id) throws Exception;
	public Driver getDriverByMail(String mail)throws Exception;
	public void deleteDriver(String id) throws NoContentException;
	public void updateDriver(String id, Driver updatedDriver) throws Exception;
	public long getDriverCount();
	
	public Collection<Advertiser> getAllAdvertisers();
	public Advertiser addAdvertiser(Advertiser driver) throws Exception;
	public Advertiser getAdvertiser(String id) throws Exception;
	public Advertiser getAdvertiserByMail(String mail)throws Exception;
	public void deleteAdvertiser(String id) throws NoContentException;
	public void updateAdvertiser(String id, Advertiser updatedAdvertiser) throws Exception;
	public long getAdvertiserCount();
	
	public Car getCar(String driverId, String carId) throws Exception;
	public Collection<Car> getCars(String driverId) throws Exception;
	public Car addCar(String driverId, Car car) throws Exception;
	public void deleteCar(String driverId, String carId) throws Exception;
	public Car updateCar(String driverId, String carId, Car car) throws Exception;
	public Collection<Car> getAllCars() throws Exception;
	
	public Campaign addCampaign(String advertiserId, Campaign campaign) throws Exception;
	public Campaign getCampaign(String advertiserId, String campaignId) throws Exception;
	public EnrichedCampaign getEnrichedCampaign(String advertiserId, String campaignId) throws Exception;
	public void deleteCampaign(String advertiserId, String campaignId) throws Exception;
	public Campaign updateCampaign(String advertiserId, String campaignId, Campaign campaign) throws Exception;
	public Collection<Campaign> getCampaigns(String advertiserId) throws Exception;
	
	public Campaign requestVehicleForCampaign(String advertiserId, String campaignId, String carId) throws Exception;
	public Collection<Campaign> getCarRequestingCampaigns(String carid);
	public Advertiser getAdvertiserFromCampaign(String campaignId) throws Exception;
	public boolean isCarOccupiedInTime(String carId, String start, String end) throws Exception;
	
	public Collection<OfferInformation> getOfferInformation(String driverId) throws Exception;
	public void respondToOffer(String carId, String advertiserId,  String campaignId, String respond) throws Exception;
	public Collection<Car> getAvailableCars(String advertiserId, String campaignId) throws Exception;
}
