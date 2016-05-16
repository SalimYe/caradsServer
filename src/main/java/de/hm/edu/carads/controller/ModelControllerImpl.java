package de.hm.edu.carads.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.naming.directory.InvalidAttributesException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.NoContentException;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;
import de.hm.edu.carads.controller.util.AbstractEntityController;
import de.hm.edu.carads.controller.util.AbstractEntityControllerImpl;
import de.hm.edu.carads.controller.util.EntityValidator;
import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.db.ModelCollection;
import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Campaign;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;
import de.hm.edu.carads.models.comm.EnrichedCampaign;
import de.hm.edu.carads.models.comm.EnrichedFellow;
import de.hm.edu.carads.models.comm.Fellow;
import de.hm.edu.carads.models.comm.OfferInformation;
import de.hm.edu.carads.models.util.DateController;
import de.hm.edu.carads.models.util.FellowState;
import de.hm.edu.carads.models.util.TimeFrame;

/**
 * 
 * @author Benjamin Keckes
 *
 */
public class ModelControllerImpl implements ModelController {
	protected DatabaseController dbController;
	protected Gson gson;
	private AbstractEntityController<Driver> driverController;
	private AbstractEntityController<Advertiser> advertiserController;
	final static Logger logger = Logger.getLogger(ModelControllerImpl.class);
	
	public ModelControllerImpl(DatabaseController dbController){
		this.dbController = dbController;
		this.gson = new Gson();
		driverController = new AbstractEntityControllerImpl<Driver>(ModelCollection.DRIVER, dbController);
		advertiserController = new AbstractEntityControllerImpl<Advertiser>(ModelCollection.ADVERTISER, dbController);
	}
	
	private Driver filterDriver(Driver driver){
		driver.deleteSomeInformation();
		return driver;
	}

	@Override
	public Collection<Driver> getAllDrivers() {
		return driverController.getAllEntities();
	}

	@Override
	public Driver addDriver(Driver driver) throws Exception {
		return driverController.addEntity(driver);
	}

	@Override
	public Driver getDriver(String id) throws Exception {
		return driverController.getEntity(id);
	}

	@Override
	public Driver getDriverByMail(String mail) throws Exception {
		return driverController.getEntityByMail(mail);
	}

	@Override
	public void deleteDriver(String id) throws NoContentException {
		driverController.deleteEntity(id);
	}

	@Override
	public void updateDriver(String id, Driver updatedDriver)
			throws Exception {
		driverController.changeEntity(id, updatedDriver);
	}

	@Override
	public long getDriverCount() {
		return driverController.getEntityCount();
	}

	@Override
	public Collection<Advertiser> getAllAdvertisers() {
		return advertiserController.getAllEntities();
	}

	@Override
	public Advertiser addAdvertiser(Advertiser advertiser) throws Exception {
		return advertiserController.addEntity(advertiser);
	}

	@Override
	public Advertiser getAdvertiser(String id) throws Exception {
		return advertiserController.getEntity(id);
	}

	@Override
	public Advertiser getAdvertiserByMail(String mail) throws Exception {
		return advertiserController.getEntityByMail(mail);
	}

	@Override
	public void deleteAdvertiser(String id) throws NoContentException {
		advertiserController.deleteEntity(id);
	}

	@Override
	public void updateAdvertiser(String id, Advertiser updatedAdvertiser)
			throws Exception {
		advertiserController.changeEntity(id, updatedAdvertiser);
	}

	@Override
	public long getAdvertiserCount() {
		return advertiserController.getEntityCount();
	}

	@Override
	public Car getCar(String driverId, String carId) throws Exception {
		Driver driver = driverController.getEntity(driverId);
		Car car = driver.getCar(carId);
		if(car==null){
			logger.error("Car "+carId+" not found");
			throw new NoContentException("not found");
		}
		return car;
	}

	@Override
	public Collection<Car> getCars(String driverId) throws Exception {
		Driver driver = driverController.getEntity(driverId);
		return driver.getCars();
	}

	@Override
	public Car addCar(String driverId, Car car) throws Exception {
		if (!EntityValidator.isEntityValid(car)) {
			logger.error("Car not valid");
			throw new InvalidAttributesException();
		}

		Driver driver = driverController.getEntity(driverId);

		car.setId(dbController.getNewId());
		car.renewMetaInformation();
		car.cleanBeforeSaving();
		driver.addCar(car);

		driver.getMetaInformation().update();
		
		driverController.changeEntity(driverId, driver);
		
		return car;
	}

	@Override
	public void deleteCar(String driverId, String carId) throws Exception {
		Driver driver = driverController.getEntity(driverId);
		driver.removeCar(carId);
		driver.getMetaInformation().update();
		logger.info("Deleting car "+carId);
		driverController.changeEntity(driverId, driver);
	}

	@Override
	public Car updateCar(String driverId, String carId, Car car)
			throws Exception {
		if (!EntityValidator.isEntityValid(car)) {
			logger.error("Car invalid");
			throw new InvalidAttributesException();
		}
		Driver driver = driverController.getEntity(driverId);
		Car oldCar = driver.getCar(carId);
		if(!driver.removeCar(carId)){
			logger.error("Could not find Car in Driver "+driverId);
			throw new NoContentException("Could not find Car in Driver "+driverId);
		}
			
		car.update(oldCar.getMetaInformation());
		car.setId(carId);
		driver.addCar(car);
		
		logger.info("Updating car " +carId+" for driver "+driverId);
		driverController.changeEntity(driverId, driver);
		
		return car;
	}

	@Override
	public Collection<Car> getAllCars() throws Exception {
		Collection<Car> allCars = new ArrayList<Car>();
		
		Iterator<Driver> drivers = driverController.getAllEntities().iterator();
		while(drivers.hasNext()){
			allCars.addAll(drivers.next().getCars());
		}
		return allCars;
	}

	@Override
	public Collection<OfferInformation> getOfferInformation(String driverId)
			throws Exception {
		Collection<OfferInformation> offers = new ArrayList<OfferInformation>();

		Iterator<Car> carIterator = this.getCars(driverId).iterator();

		while (carIterator.hasNext()) {
			Car car = carIterator.next();
			// Collection<Campaign> requestingCampaigns = new
			// ArrayList<Campaign>();
			Iterator<Campaign> requestingCampaignsIterator = this.getCarRequestingCampaigns(car.getId()).iterator();
			while (requestingCampaignsIterator.hasNext()) {
				Campaign reqCamp = requestingCampaignsIterator.next();
				FellowState state = getStateFromCampaign(car.getId(), reqCamp);
				Advertiser advertiser = this.getAdvertiserFromCampaign(reqCamp.getId());

				// Loesche unnoetige Informationen
				reqCamp.setFellows(null);
				advertiser.removeAllCampaigns();

				offers.add(new OfferInformation(car, advertiser, reqCamp, state));
			}
		}
		return offers;
	}

	@Override
	public void respondToOffer(String carId, String advertiserId,
			String campaignId, String respond) throws Exception {
		Campaign campaign = this.getCampaign(advertiserId, campaignId);
		Iterator<Fellow> fellowIterator = campaign.getFellows().iterator();
		Collection<Fellow> updatedFellows = new ArrayList<Fellow>();
		while (fellowIterator.hasNext()) {
			Fellow fellow = fellowIterator.next();
			if (fellow.getCarId().equals(carId)) {
				fellow.setState(getFellowState(respond));
			}
			updatedFellows.add(fellow);
		}
		campaign.setFellows(updatedFellows);
		logger.info(carId+" responded with "+respond+ " to campaign "+campaign.getTitle()+" ("+campaignId+")");
		this.updateCampaign(advertiserId, campaignId, campaign);
	}

	@Override
	public Collection<Car> getAvailableCars(String advertiserId,
			String campaignId) throws Exception {
		Collection<Car> availableCars = new ArrayList<Car>();

		Campaign campaign = advertiserController.getEntity(advertiserId).getCampaign(campaignId);

		Iterator<Car> allReducedCars = reduceAlreadyAskedCars(campaign,
				this.getAllCars()).iterator();
		while (allReducedCars.hasNext()) {
			Car car = allReducedCars.next();
			if (!isCarOccupiedInTime(car.getId(), campaign.getStartDate(),
					campaign.getEndDate()))
				availableCars.add(car);
		}

		return availableCars;
	}

	@Override
	public Campaign addCampaign(String advertiserId, Campaign campaign)
			throws Exception {
		if(!EntityValidator.isEntityValid(campaign)){
			logger.error("Campaign not valid");
			throw new IllegalArgumentException();
		}
			
		
		Advertiser ad = advertiserController.getEntity(advertiserId);
		
		campaign.setId(dbController.getNewId());
		campaign.renewMetaInformation();
		ad.addCampaign(campaign);
		ad.getMetaInformation().update();
		logger.info("saving Campaign for "+advertiserId);
		advertiserController.changeEntity(advertiserId, ad);
		return campaign;
	}

	@Override
	public Campaign getCampaign(String advertiserId, String campaignId)
			throws Exception {
		Advertiser advertiser = advertiserController.getEntity(advertiserId);
		Campaign c = advertiser.getCampaign(campaignId);
		if(c==null){
			logger.error("Campaign "+ campaignId +" not valid");
			throw new NoContentException(campaignId + " not found");
		}
		
		return c;
	}

	@Override
	public void deleteCampaign(String advertiserId, String campaignId)
			throws Exception {
		Advertiser advertiser = advertiserController.getEntity(advertiserId);
		if(!advertiser.removeCampaign(campaignId)){
			logger.error("Campaign "+campaignId+" not found");
			throw new NoContentException(campaignId + " not found");
		}
			
		advertiser.getMetaInformation().update();
		logger.info("Campaign "+campaignId+" removed");
		advertiserController.changeEntity(advertiserId, advertiser);
	}

	@Override
	public Campaign updateCampaign(String advertiserId, String campaignId,
			Campaign campaign) throws Exception {
		if(!EntityValidator.isEntityValid(campaign)){
			logger.error("Campaign invalid");
			throw new InvalidAttributesException();
		}
			
		
		Advertiser advertiser = advertiserController.getEntity(advertiserId);
		Campaign oldC = advertiser.getCampaign(campaignId);
		if(oldC == null)
			throw new NoContentException(campaignId + " not found");

	
		advertiser.removeCampaign(campaignId);
		campaign.update(oldC.getMetaInformation());
		campaign.setId(campaignId);
		
		if(campaign.getFellows().isEmpty())
			campaign.setFellows(oldC.getFellows());
		
		advertiser.addCampaign(campaign);
		logger.info("Campaign "+campaignId+" updated");
		advertiserController.changeEntity(advertiserId, advertiser);
		return campaign;
	}

	@Override
	public Collection<Campaign> getCampaigns(String advertiserId)
			throws Exception {
		Advertiser advertiser = advertiserController.getEntity(advertiserId);
		return advertiser.getCampaigns();
	}

	@Override
	public Campaign requestVehicleForCampaign(String advertiserId,
			String campaignId, String carId) throws Exception {
		Advertiser advertiser = advertiserController.getEntity(advertiserId);
		Campaign campaign = advertiser.getCampaign(campaignId);
		
		if(isCarOccupiedInTime(carId, campaign.getStartDate(), campaign.getEndDate())){
			logger.error("Cant make request. Car "+carId+" is already in another Campaign at this time");
			throw new AlreadyExistsException();
		}
		if(campaign.isCarAFellow(carId)){
			logger.error("Cant make request. Car "+carId+" was requested for this campaign already");
			throw new AlreadyExistsException();
		}
		if(!campaign.addFellow(carId)){
			logger.error("Car information was wrong");
			throw new IllegalArgumentException();
		}
			
		logger.info("Campaign "+campaignId+" made an offer to "+carId);
		return this.updateCampaign(advertiserId, campaignId, campaign);
	}

	@Override
	public Collection<Campaign> getCarRequestingCampaigns(String carid) {
		Collection<Campaign> carCampaigns = new ArrayList<Campaign>();
		
		Iterator<Campaign> it = getAllCampaigns().iterator();
		while(it.hasNext()){
			Campaign c = it.next();
			Iterator<Fellow> fellowIterator = c.getFellows().iterator();
			while(fellowIterator.hasNext()){
				Fellow fellow = fellowIterator.next();
				if(fellow.getCarId().equals(carid))
					carCampaigns.add(c);
			}
		}
		return carCampaigns;
	}

	@Override
	public Advertiser getAdvertiserFromCampaign(String campaignId)
			throws Exception {
		Iterator<Advertiser> advertIterator = advertiserController.getAllEntities().iterator();
		while(advertIterator.hasNext()){
			Advertiser ad = advertIterator.next();
			if(ad.containsCampaign(campaignId))
				return ad;
		}
		logger.error("No Advertiser for Campaign "+campaignId+" found");
		throw new NotFoundException();
	}

	@Override
	public boolean isCarOccupiedInTime(String carId, String start, String end)
			throws Exception {
		Iterator<Campaign> it = this.getAllCampaignsInTime(start, end).iterator();
		while(it.hasNext()){
			Campaign campaign = it.next();
			if(campaign.isCarAFellow(carId) && campaign.hasFellowAccepted(carId))
				return true;
		}
		return false;
	}
	
	private Collection<Campaign> getAllCampaignsInTime(String start, String end){
		Collection<Campaign> inTimeCampaigns = new ArrayList<Campaign>();
		Iterator<Campaign> it = getAllCampaigns().iterator();
		while(it.hasNext()){
			Campaign c = it.next();
			if(DateController.areTimesOverlapping(new TimeFrame(start, end), new TimeFrame(c.getStartDate(), c.getEndDate())))
				inTimeCampaigns.add(c);
		}
		return inTimeCampaigns;
	}
	
	
	private Collection<Campaign> getAllCampaigns(){
		Collection<Campaign> allCampaigns = new ArrayList<Campaign>();
		
		Iterator<Advertiser> advertisers = advertiserController.getAllEntities().iterator();
		while(advertisers.hasNext()){
			allCampaigns.addAll(advertisers.next().getCampaigns());
		}
		return allCampaigns;
	}
	
	private FellowState getStateFromCampaign(String carid, Campaign campaign) {

		Iterator<Fellow> fellowIterator = campaign.getFellows().iterator();
		while (fellowIterator.hasNext()) {
			Fellow fellow = fellowIterator.next();
			if (fellow.getCarId().equals(carid)) {
				return fellow.getState();
			}
		}
		return null;
	}
	
	private FellowState getFellowState(String state) {
		if (state.equals(FellowState.ACCEPTED.toString())) {
			return FellowState.ACCEPTED;
		} else if (state.equals(FellowState.ASKED.toString())) {
			return FellowState.ASKED;
		} else if (state.equals(FellowState.REJECTED.toString()))
			;
		return FellowState.REJECTED;
	}
	
	/**
	 * Diese Methode nimmt eine Kampagne und eine Liste an Fahrzeugen entgegen.
	 * Die List wird um alle Fahrtzeuge reduziert, welche bereits fue die
	 * Kampagne angefragt wurden. Dabei spielt es keine Rolle ob die Fahrzeuge
	 * bereits geantwortet haben.
	 * 
	 * @param campaign
	 * @param cars
	 * @return reducedCarList
	 */
	private Collection<Car> reduceAlreadyAskedCars(Campaign campaign,
			Collection<Car> cars) {
		Collection<Car> reduced = new ArrayList<Car>();
		Collection<Fellow> fellows = campaign.getFellows();
		Iterator<Car> carIterator = cars.iterator();
		boolean isAFellow;
		while (carIterator.hasNext()) {
			isAFellow = false;
			Car car = carIterator.next();
			Iterator<Fellow> fellowIterator = fellows.iterator();
			while (fellowIterator.hasNext()) {
				Fellow fellow = fellowIterator.next();
				if (car.getId().equals(fellow.getCarId())) {
					isAFellow = true;
					break;
				}

			}
			if (!isAFellow)
				reduced.add(car);
		}
		return reduced;
	}

	@Override
	public EnrichedCampaign getEnrichedCampaign(String advertiserId,
			String campaignId) throws Exception {
		Campaign campaign = this.getCampaign(advertiserId, campaignId);
		EnrichedCampaign enrichedCampaign = new EnrichedCampaign(campaign, getEnrichedFellows(campaign.getFellows()));
		return enrichedCampaign;
	}
	
	private Collection<EnrichedFellow> getEnrichedFellows(Collection<Fellow> fellows) throws Exception{
		Collection<EnrichedFellow> enrichedFellows = new ArrayList<EnrichedFellow>();
		Iterator<Fellow> fellowIt = fellows.iterator();
		
		Collection<Car> allCars = getAllCars();
		while(fellowIt.hasNext()){
			Fellow f = fellowIt.next();
			Car c = getOneCarFromCarCollection(allCars, f.getCarId());
			if(c!=null)
				enrichedFellows.add(new EnrichedFellow(f.getState(), c));
		}
		return enrichedFellows;
	}
	
	private Car getOneCarFromCarCollection(Collection<Car> cars, String carId){
		Iterator<Car> it = cars.iterator();
		while(it.hasNext()){
			Car c = it.next();
			if(c.getId().equals(carId))
				return c;
		}
		return null;
	}
}
