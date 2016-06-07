package de.hm.edu.carads.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import javax.naming.directory.InvalidAttributesException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.NoContentException;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;
import de.hm.edu.carads.controller.exceptions.HasConstraintException;
import de.hm.edu.carads.controller.util.AbstractEntityController;
import de.hm.edu.carads.controller.util.AbstractEntityControllerImpl;
import de.hm.edu.carads.controller.util.DateController;
import de.hm.edu.carads.controller.util.EntityValidator;
import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.db.ModelCollection;
import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Campaign;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;
import de.hm.edu.carads.models.util.Fellow;
import de.hm.edu.carads.models.util.FellowState;
import de.hm.edu.carads.models.util.TimeFrame;
import de.hm.edu.carads.transaction.EnrichedCampaign;
import de.hm.edu.carads.transaction.EnrichedFellow;
import de.hm.edu.carads.transaction.OfferInformation;

/**
 * Diese Klasserepräsentiert die Logik der Applikation. Es werden Methoden
 * zum Anlegen, Bearbeiten und Löschen für Fahrer, Fahrzeuge, Werbende und Kampagnen angeboten.
 * Weitere Methoden, die der Logik der Applikation dienen werden hier abgebildet.
 * 
 * @author Benjamin Keckes
 *
 */
public class ApplicationControllerImpl implements ApplicationController {
	/**
	 * Schnittstelle zur Datenbank.
	 */
	protected DatabaseController dbController;
	
	/**
	 * Dieses Objekt ist für das Parsen der JSON Strukturen zuständig.
	 */
	protected Gson gson;
	
	/**
	 * Hierueber werden Fahrer und Fahrzeuge verwaltet.
	 */
	private AbstractEntityController<Driver> driverController;
	
	/**
	 * Hierueber werden Werbende und Kampagnen verwaltet.
	 * 
	 */
	private AbstractEntityController<Advertiser> advertiserController;
	
	/**
	 * Der Logger.
	 */
	final static Logger logger = Logger.getLogger(ApplicationControllerImpl.class);
	
	/**
	 * Der Konstruktor mit dem DatenbankController als Parameter.
	 * @param dbController
	 */
	public ApplicationControllerImpl(DatabaseController dbController){
		this.dbController = dbController;
		this.gson = new Gson();
		driverController = new AbstractEntityControllerImpl<Driver>(ModelCollection.DRIVER, dbController);
		advertiserController = new AbstractEntityControllerImpl<Advertiser>(ModelCollection.ADVERTISER, dbController);
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

	
	/**
	 * Diese Methode loescht einen Fahrer. Davor werden alle
	 * Fahrzeuge des Fahrers ueberprueft ob noch bestehende Kampagnen
	 * damit zusammenhängen. Falls kein Fahrzeug in einer laufenden oder
	 * zukuenfigen Kampagne steckt, kann der Fahrer geloescht werden.
	 * @param id
	 * @throws Exception
	 */
	@Override
	public void deleteDriver(String id) throws Exception {
		Iterator<Car> carIterator = getDriver(id).getCars().iterator();
		while(carIterator.hasNext()){
			if(isCarBooked(carIterator.next().getId()))
				throw new HasConstraintException();
		}
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

	/**
	 * Diese Methode loescht einen Werbenden. Zuvor wird ueberprueft ob
	 * die zugehoerigen Kampagnen geloescht werden koennen. Wenn ja, wird der
	 * Werbende geloescht, ansonsten bleibt er bestehen.
	 * @param advertiserId
	 * @throws Exception
	 */
	@Override
	public void deleteAdvertiser(String advertiserId) throws Exception {
		Iterator<Campaign> it = advertiserController.getEntity(advertiserId).getCampaigns().iterator();
		while(it.hasNext()){
			if(hasCampaignBookedFellows(it.next())){
				logger.info("Advertiser" + advertiserId + " could not removed");
				throw new HasConstraintException();
			}
		}
		advertiserController.deleteEntity(advertiserId);
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

	/**
	 * Das Fahrzeug mit der carId des Fahrers mit der driverId wird angezeigt.
	 * @param driverId, carId
	 * @return Fahrzeug.
	 * @throws Exception
	 */
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

	/**
	 * Alle Fahrzeuge des Fahrers mit der driverId werden als Sammlung 
	 * zurueck gegeben.
	 * @param driverId
	 * @return Collection<Car>
	 * @throws Exception
	 */
	@Override
	public Collection<Car> getCars(String driverId) throws Exception {
		Driver driver = driverController.getEntity(driverId);
		return driver.getCars();
	}

	/**
	 * Ein Auto wird dem Fahrer mit der driverId hinzugefuegt. Davor werden
	 * alle Daten auf Gueltigkeit geprüft. Die Fahrerinformationen werden anschließend
	 * erneuert und in die Datenbank geschrieben.
	 */
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

	/**
	 * Wenn das Auto nicht an Kampagnen teilnimmt, wird es geloescht.
	 * @param driverId, carId
	 * @throws Exception
	 */
	@Override
	public void deleteCar(String driverId, String carId) throws Exception {
		
		if(isCarBooked(carId)){
			logger.info("Car cloud not be deleted. It is still booked in a campaign.");
			throw new HasConstraintException();
		}
		logger.info("going to remove car from " + driverId);
		Driver driver = driverController.getEntity(driverId);
		logger.info("going to remove car from2 " + driver.getId());
		driver.removeCar(carId);
		driver.getMetaInformation().update();
		logger.info("Deleting car "+carId);
		driverController.changeEntity(driverId, driver);
	}
	
	/**
	 * Wahrheitswert ueber folgende Bedingungen:
	 * Das Fahrzeug ist momentan von keiner laufenden Kampagne gebucht.
	 * Das Fahrzeug ist fuer keine in der Zukunft liegenden Kampagne gebucht.
	 * @param carId
	 * @return true when booked
	 */
	private boolean isCarBooked(String carId) throws Exception{
		DateFormat df = new SimpleDateFormat(DateController.DATE_FORMAT_CAMPAIGNTIME);
		String now = df.format(Calendar.getInstance().getTime());
		logger.info("Looking for Campaigns from " +now);
		return isCarOccupiedInTime(carId, now, "2199-12-31");
	}

	/**
	 * Das Fahrzeug mit der carId des Fahrers mit der driverId wird geaendert.
	 * Alle Daten aus dem car Parameter werden dafuer verwendet. Alte Informationen 
	 * werden ueberschrieben.
	 * @param driverId, carId, car
	 * @return geandertes Fahrzeug
	 * @throws Exception
	 */
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

	/**
	 * Alle Fahrzeuge von allen Fahrern werden zurueck gegeben.
	 * @return Sammlung aller Fahrzeuge
	 * @throws Exception
	 */
	@Override
	public Collection<Car> getAllCars() throws Exception {
		Collection<Car> allCars = new ArrayList<Car>();
		
		Iterator<Driver> drivers = driverController.getAllEntities().iterator();
		while(drivers.hasNext()){
			allCars.addAll(drivers.next().getCars());
		}
		return allCars;
	}

	/**
	 * Diese Methode gibt alle Angebote an den Fahrer mit der driverId zurueck.
	 * Der Fahrer erhaehlt so Informationen ueber das angefragte Fahrzeug, welche Kampagne
	 * daran interessiert ist und von welchem Werbenden das Angebot stammt.
	 * Es werden auch abgelehnte und vergangene Campagnen angezeigt.
	 * 
	 * @param driverId
	 * @return Sammlung aller Angebote
	 * @throws Exception
	 */
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

	/**
	 * Diese Methode dient als Antwort des Fahrers an einen Werbenden.
	 * Er gibt Antwort mit welchem Auto er an welcher Kampagne teilnehmen möchte 
	 * oder nicht. Andere Anfragen mit ueberlappenden Zeitraeumen werden automatisch abgesagt.
	 * @param carId, advertiserId, campaignId, response
	 * @throws Exception
	 */
	@Override
	public void respondToOffer(String carId, String advertiserId, String campaignId, String respond) throws Exception {
		Campaign campaign = this.getCampaign(advertiserId, campaignId);
		updateFellowSate(carId, advertiserId, campaign, getFellowState(respond));
		
		rejectAllOverlappingCampaigns(carId, campaign);
		
	}
	
	/**
	 * Diese Methode erteilt allen Kampagnen fuer dieses Fahrzeug eine absage, wenn der
	 * Kampagnen-Zeitraum ueberlappt. 
	 * @param carId
	 * @param campaign
	 * @throws Exception
	 */
	private void rejectAllOverlappingCampaigns(String carId, Campaign campaign) throws Exception{
		
		Iterator<Campaign> it = getAllCampaignsInTime(campaign.getStartDate(), campaign.getEndDate()).iterator();
		while(it.hasNext()){
			Campaign c = it.next();
			if(!c.getId().equals(campaign.getId())){
				if(checkCarStateInCampaign(carId, c, FellowState.ASKED)){
					String advertiserId = getAdvertiserFromCampaign(c.getId()).getId();
					updateFellowSate(carId, advertiserId, c, FellowState.REJECTED);
				}
			}
		}
	}
	
	/**
	 * Der Status eines Fahrzeugs innerhalb der Kampagne wird ueberprueft.
	 * @param carId
	 * @param campaign
	 * @param state
	 * @return true wenn state uebereinstimmt
	 */
	private boolean checkCarStateInCampaign(String carId, Campaign campaign, FellowState state){
		Fellow fellow = campaign.getFellow(carId);
		if(fellow!=null){
			if(fellow.getState().equals(state))
				return true;
		}
		return false;
	}
	
	/**
	 * Diese Methode aendert den Status eines Fahrzeugs innerhalb einer speziellen Kampagne.
	 * @param carId
	 * @param advertiserId
	 * @param campaign
	 * @param respond
	 * @return geaenderte Kampagne
	 * @throws Exception
	 */
	private Campaign updateFellowSate(String carId, String advertiserId, Campaign campaign, FellowState respond) throws Exception{
		Iterator<Fellow> fellowIterator = campaign.getFellows().iterator();
		Collection<Fellow> updatedFellows = new ArrayList<Fellow>();
		while (fellowIterator.hasNext()) {
			Fellow fellow = fellowIterator.next();
			if (fellow.getCarId().equals(carId)) {
				fellow.setState(respond);
			}
			updatedFellows.add(fellow);
		}
		campaign.setFellows(updatedFellows);
		logger.info(carId+" responded with "+respond+ " to campaign "+campaign.getName()+" ("+campaign.getId()+")");
		this.updateCampaign(advertiserId, campaign.getId(), campaign);
		return campaign;
	}
	
	

	/**
	 * Es werden alle Fahrzeuge zurueck gegeben, welche fuer diese Kampagne zur Verfügung stehen.
	 * Dabei werden nur Fahrzeuge angezeigt die in diesem Zeitraum noch frei sind und nicht bereits 
	 * fuer diese Kampagne gebucht wurden.
	 * @param advertiserId, campaignId
	 * @return Sammlung aller Fahrzeuge die angefragt werden koennen
	 * @throws Exception
	 */
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

	/**
	 * Eine neue Kampagne wird zum Werbenden hinzugefuegt, falls sie
	 * gueltige Angaben hat.
	 * @param advertiserId, campaign
	 * @throws Exception
	 */
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

	/**
	 * Eine Kampagne eines Werbenden wird aus der Datenbank gelesen.
	 * @param advertiserId, campaignId
	 * @return Kampagne mit der campaginId
	 * @throws Exception
	 */
	@Override
	public Campaign getCampaign(String advertiserId, String campaignId)
			throws Exception {
		Advertiser advertiser = advertiserController.getEntity(advertiserId);
		Campaign c = advertiser.getCampaign(campaignId);
		if(c==null){
			logger.error("Campaign "+ campaignId +" not found");
			throw new NoContentException(campaignId + " not found");
		}
		
		return c;
	}

	/**
	 * Eine Kampagne wird geloescht. Es wird davor ueberprueft ob die
	 * Kampagne Fahrzeuge angefragt hat und diese bereits zugesagt haben.
	 * @param advertiserId, campaignId
	 * @throws Exception
	 */
	@Override
	public void deleteCampaign(String advertiserId, String campaignId)
			throws Exception {
		Advertiser advertiser = advertiserController.getEntity(advertiserId);
		Campaign campaign = advertiser.getCampaign(campaignId);
		
		if(hasCampaignBookedFellows(campaign))
			throw new HasConstraintException();
		
		if(!advertiser.removeCampaign(campaign)){
			logger.error("Campaign "+campaignId+" not found");
			throw new NoContentException(campaignId + " not found");
		}
			
		advertiser.getMetaInformation().update();
		logger.info("Campaign "+campaignId+" removed");
		advertiserController.changeEntity(advertiserId, advertiser);
	}
	
	/**
	 * Diese Methode ueberprueft ob die Kampagne bereits Fahrzeuge
	 * besitzt, bei denen der Fahrer das Angebot angenommen hat.
	 * @param campaign
	 * @return Wahrheitswert
	 */
	private boolean hasCampaignBookedFellows(Campaign campaign){
		Iterator<Fellow> fellowIterator = campaign.getFellows().iterator();
		while(fellowIterator.hasNext()){
			if(fellowIterator.next().getState().equals(FellowState.ACCEPTED)){
				logger.info("Campaign "+ campaign.getId() +" has constraints");
				return true;
			}
		}
		return false;
	}

	/**
	 * Eine Kampagne wird geaendert. Die neuen Daten muessen aber valide sein.
	 * @param advertiserId, campaignId, campaign
	 * @return geanderte Kampagne
	 * @throws Exception
	 */
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

	
		advertiser.removeCampaign(oldC);
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

	/**
	 * Diese Methode dient zur Anfrage von Fahrzeugen fuer eine Kampagne.
	 * Es wird ueberprueft ob das Auto in dem Zeitraum der Kampagne bereits in einer
	 * anderen Kampagne gebucht wurde, oder ob dieses Fahrzeug bereits fuer diese
	 * Kampagne angefragt wurde.
	 * @param advertiserId, campaignId, carId
	 * @return Kampagne mit angefragten Fahrzeugen
	 * @throws Exception
	 */
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

	/**
	 * Diese Methode gibt alle Kampagnen zurueck die das Auto angefragt haben.
	 * @param carId
	 * @return Sammlung der anfragenden Kampagnen.
	 */
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

	/**
	 * Diese Methode zeigt den Ersteller der Kampagne
	 * @param campaignId
	 * @return Werbender der diese Kampagne erstellt hat
	 * @throws Exception
	 */
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

	/**
	 * Es wird ueberprueft ob das Fahrzeug in angegebenen Zeitraum bereits
	 * angefragt wurde und ob es da schon zugeseagt hat.
	 * @param carId, start, end
	 * @return Wahrheitswert ob Fahrzeug nicht mehr Verfuegbar ist
	 * @throws Exception
	 */
	@Override
	public boolean isCarOccupiedInTime(String carId, String start, String end) throws Exception {
		Iterator<Campaign> it = this.getAllCampaignsInTime(start, end).iterator();
		
		while(it.hasNext()){
			Campaign campaign = it.next();
			if(campaign.isCarAFellow(carId) && campaign.hasFellowAccepted(carId))
				return true;
		}
		return false;
	}
	
	/**
	 * Gibt alle Kampagnen in diesem Zeitraum zurueck. Es reicht auch schon eine Ueberschneidung.
	 * @param start
	 * @param end
	 * @return Sammlung der Kampagnen die diesen Zeitraum ueberschneiden.
	 */
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
	
	/**
	 * Gibt alle Kampagnen von allen Werbenden zurueck.
	 * @return Sammlung aller Kampagnen
	 */
	private Collection<Campaign> getAllCampaigns(){
		Collection<Campaign> allCampaigns = new ArrayList<Campaign>();
		
		Iterator<Advertiser> advertisers = advertiserController.getAllEntities().iterator();
		while(advertisers.hasNext()){
			allCampaigns.addAll(advertisers.next().getCampaigns());
		}
		return allCampaigns;
	}
	
	/**
	 * Gibt des Status eines Fahrzeugs innerhalb einer Kampagne zurueck.
	 * @param carid
	 * @param campaign
	 * @return Anfragestatus
	 */
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
	
	private FellowState getFellowState(String state) throws InvalidAttributesException {
		if (state.equals(FellowState.ACCEPTED.toString())) {
			return FellowState.ACCEPTED;
		} else if (state.equals(FellowState.ASKED.toString())) {
			return FellowState.ASKED;
		} else if (state.equals(FellowState.REJECTED.toString()))
			return FellowState.REJECTED;
		throw new InvalidAttributesException();
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

	/**
	 * Diese Methode dient zur Ausgabe an die API und erweitert
	 * alle angefragten Fahrzeuge mit den benötigten Daten rund um das Fahrzeug.
	 */
	@Override
	public EnrichedCampaign getEnrichedCampaign(String advertiserId, String campaignId) throws Exception {
		Campaign campaign = this.getCampaign(advertiserId, campaignId);
		EnrichedCampaign enrichedCampaign = new EnrichedCampaign(campaign, getEnrichedFellows(campaign.getFellows()));
		return enrichedCampaign;
	}
	
	/**
	 * Diese Methode erweitert alle Fahrzeuginformationen einer Sammlung von Angefragten Fahrzeugen.
	 * @param fellows
	 * @return Sammlung erweiterter Fahrzeuginformationen
	 * @throws Exception
	 */
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
	
	/**
	 * Gibt ein spezielles Fahrzeug aus einer Liste zurueck. Falls kein Fahrzeug der Liste
	 * die carId hat, wird null zurueck gegeben.
	 * @param cars
	 * @param carId
	 * @return Fahrzeug mit carId
	 */
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
