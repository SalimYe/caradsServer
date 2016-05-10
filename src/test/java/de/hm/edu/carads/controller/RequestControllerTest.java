package de.hm.edu.carads.controller;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import de.hm.edu.carads.db.DatabaseControllerImpl;
import de.hm.edu.carads.db.util.DatabaseFactory;
import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Campaign;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;
import de.hm.edu.carads.models.comm.OfferInformation;
import de.hm.edu.carads.models.util.FellowState;

public class RequestControllerTest {
	private static String EMAIL = "muster.mann@mustermann.com";
    private static String EMAIL2 = "flosch@bosch.de";
	private static String FIRSTNAME = "Muster";
	private static String LASTNAME = "Mann";
	private static String CARBRAND = "Mercedes";
	private static String CARMODEL = "E-Klasse";
	private static String CARCOLOR = "red";

	private RequestController requestController;
	private AdvertiserController advertiserController;
	private DriverController driverController;
	
	@Before
	public void resetDB() {
		DatabaseFactory.dropTestDB();
		advertiserController = getAdvertiserController();
		driverController = getDriverController();
		requestController = getRequestController();
	}
	
	@Test
	public void offerInformationSizeTest() throws Exception {
		Advertiser ad = advertiserController.addEntity(makeNewAdvertiser());
		Campaign camp = advertiserController.addCampaign(ad.getId(), makeNewCampaign());
		Driver driver = driverController.addEntity(makeNewDriver());
		Car car = driverController.addCar(driver.getId(), makeNewCar());
		
		advertiserController.addVehicleToCampaign(ad.getId(), camp.getId(), car.getId());
		assertEquals(1, requestController.getOfferInformation(driver.getId()).size());
	}
	
	@Test
	public void offerInformationSizeTest2() throws Exception {
		Driver driver = driverController.addEntity(makeNewDriver());
		driverController.addCar(driver.getId(), makeNewCar());

		assertEquals(0, requestController.getOfferInformation(driver.getId()).size());
	}
	
	@Test
	public void offerToJustOneCarTest() throws Exception {
		Advertiser ad = advertiserController.addEntity(makeNewAdvertiser());
		Campaign camp = advertiserController.addCampaign(ad.getId(), makeNewCampaign());
		Driver driver = driverController.addEntity(makeNewDriver());
		Car car = driverController.addCar(driver.getId(), makeNewCar());
		driverController.addCar(driver.getId(), makeNewCar());
		
		advertiserController.addVehicleToCampaign(ad.getId(), camp.getId(), car.getId());
		assertEquals(1, requestController.getOfferInformation(driver.getId()).size());
	}
	
	@Test
	public void responseTest() throws Exception {
		Advertiser ad = advertiserController.addEntity(makeNewAdvertiser());
		Campaign camp = advertiserController.addCampaign(ad.getId(), makeNewCampaign());
		Driver driver = driverController.addEntity(makeNewDriver());
		Car car = driverController.addCar(driver.getId(), makeNewCar());
		
		advertiserController.addVehicleToCampaign(ad.getId(), camp.getId(), car.getId());
		
		requestController.respondToOffer(driver.getId(), car.getId(), ad.getId(), camp.getId(), "ACCEPTED");
		Iterator<OfferInformation> offers = requestController.getOfferInformation(driver.getId()).iterator();
		OfferInformation offer = offers.next();
		assertEquals(FellowState.ACCEPTED, offer.getState());
	}
	
	@Test
	public void availableCarsTest() throws Exception {
		Advertiser ad1 = new Advertiser("franz@redbull.com", "Franz", "Kafka");
		Advertiser ad2 = new Advertiser("joe@bmw.de", "Joe", "Norb");
		Campaign c1 = new Campaign();
		c1.setCampaignBudget("1000");
		c1.setDescription("Beschreibung");
		c1.setStartDate("01.01.2015");
		c1.setEndDate("30.01.2015");
		c1.setTitle("Red Bull Promo");
		
		Campaign c2 = new Campaign();
		c2.setCampaignBudget("2000");
		c2.setDescription("Beschreibung");
		c2.setStartDate("01.02.2015");
		c2.setEndDate("30.04.2015");
		c2.setTitle("BMW Promo");
		
		Campaign c3 = new Campaign();
		c3.setCampaignBudget("2000");
		c3.setDescription("Beschreibung");
		c3.setStartDate("15.01.2015");
		c3.setEndDate("12.02.2015");
		c3.setTitle("5er GT Promo");
		
		Driver d1 = new Driver("flo@hm.edu", "Florian", "Mustermann");
		d1.setZip("80335");
		d1.setCity("Munich");
		
		Driver d2 = makeNewDriver();
		
		Car car1 = new Car();
		car1.setBrand("Ford");
		car1.setModel("Mustang");
		car1.setColor("grey");
		
		Car car3 = new Car();
		car3.setBrand("Ford");
		car3.setModel("Mondeo");
		car3.setColor("black");
		
		Car car4 = new Car();
		car4.setBrand("BMW");
		car4.setModel("1er");
		car4.setColor("white");
		
		Car car2 = makeNewCar();
		
		//Entitäten verknüpfen
		ad1 = advertiserController.addEntity(ad1);
		ad2 = advertiserController.addEntity(ad2);
		c1 = advertiserController.addCampaign(ad1.getId(), c1);
		c2 = advertiserController.addCampaign(ad2.getId(), c2);
		c3 = advertiserController.addCampaign(ad2.getId(), c3);
		
		d1 = driverController.addEntity(d1);
		d2 = driverController.addEntity(d2);
		car1 = driverController.addCar(d1.getId(), car1);
		car2 = driverController.addCar(d1.getId(), car2);
		car3 = driverController.addCar(d2.getId(), car3);
		car4 = driverController.addCar(d2.getId(), car4);
		
		//Alle Fahrzeuge werden als frei angezeigt
		assertEquals(4, requestController.getAvailableCars(c1.getStartDate(), c1.getEndDate()).size());
		
		//Car1 wird fuer Kampagne 1 angefragt.
		advertiserController.addVehicleToCampaign(ad1.getId(), c1.getId(), car1.getId());
		//In dem Zeitraum ist das Auto fuer andere Kampagnen noch sichtbar, weil noch nicht zugesagt wurde
		assertEquals(4, requestController.getAvailableCars(c3.getStartDate(), c3.getEndDate()).size());
		//Car1 sagt zu
		requestController.respondToOffer(d1.getId(), car1.getId(), ad1.getId(), c1.getId(), FellowState.ACCEPTED.toString());
		
		//Das Auto ist nicht mehr sichtbar, weil in diesem Zeitraum schon zugesagt wurde
		assertEquals(3, requestController.getAvailableCars(c3.getStartDate(), c3.getEndDate()).size());
		
		//Alle Autos sind aber noch fuer eine Kampagne sichtbar, die in einem anderen Zeitraum statt findet
		assertEquals(4, requestController.getAvailableCars(c2.getStartDate(), c2.getEndDate()).size());
		
	}
	
	private RequestController getRequestController(){
		if(requestController == null)
			requestController = new RequestControllerImpl(getDriverController(), getAdvertiserController());
		return requestController;
	}
	
	private DriverController getDriverController() {
		if(driverController == null)
			driverController = new DriverControllerImpl(new DatabaseControllerImpl(
				DatabaseFactory.INST_TEST));
		return driverController;
	}
	
	private AdvertiserController getAdvertiserController() {
		if(advertiserController==null)
			advertiserController = new AdvertiserControllerImpl(new DatabaseControllerImpl(
				DatabaseFactory.INST_TEST));
		return advertiserController;
	}
	
	private Campaign makeNewCampaign(){
		Campaign c = new Campaign();
		c.setTitle("Red Bull Icerace");
		c.setCampaignBudget("10000 Euro");
		c.setStartDate("01.01.2000");
		c.setEndDate("31.01.2000");
		return c;
	}
	
	private Advertiser makeNewAdvertiser() {
		Advertiser adv = new Advertiser(EMAIL, FIRSTNAME, LASTNAME);
		return adv;
	}
	
	private Driver makeNewDriver() {
		Driver driver = new Driver(EMAIL, FIRSTNAME, LASTNAME);
		driver.setBirthdate("2000");
		driver.setZip("80333");
		driver.setCity("Munich");
		return driver;
	}

	private Car makeNewCar() {
		Car car = new Car();
		car.setBrand(CARBRAND);
		car.setModel(CARMODEL);
		car.setColor(CARCOLOR);
		return car;
	}
	
	private void addSomeTestEntities() throws Exception{
		Advertiser ad1 = new Advertiser("franz@redbull.com", "Franz", "Kafka");
		Advertiser ad2 = new Advertiser("joe@bmw.de", "Joe", "Norb");
		Campaign c1 = new Campaign();
		c1.setCampaignBudget("1000");
		c1.setDescription("Beschreibung");
		c1.setStartDate("01.01.2015");
		c1.setEndDate("30.01.2015");
		c1.setTitle("Red Bull Promo");
		
		Campaign c2 = new Campaign();
		c2.setCampaignBudget("2000");
		c2.setDescription("Beschreibung");
		c2.setStartDate("01.02.2015");
		c2.setEndDate("30.04.2015");
		c2.setTitle("BMW Promo");
		
		Campaign c3 = new Campaign();
		c3.setCampaignBudget("2000");
		c3.setDescription("Beschreibung");
		c3.setStartDate("15.01.2015");
		c3.setEndDate("12.02.2015");
		c3.setTitle("5er GT Promo");
		
		Driver d1 = new Driver("flo@hm.edu", "Florian", "Mustermann");
		d1.setZip("80335");
		d1.setCity("Munich");
		
		Driver d2 = makeNewDriver();
		
		Car car1 = new Car();
		car1.setBrand("Ford");
		car1.setModel("Mustang");
		car1.setColor("grey");
		
		Car car3 = new Car();
		car3.setBrand("Ford");
		car3.setModel("Mondeo");
		car3.setColor("black");
		
		Car car4 = new Car();
		car4.setBrand("BMW");
		car4.setModel("1er");
		car4.setColor("white");
		
		Car car2 = makeNewCar();
		
		//Entitäten verknüpfen
		
		ad1.addCampaign(c1);
		ad2.addCampaign(c2);
		ad2.addCampaign(c3);
		
		d1.addCar(car1);
		d1.addCar(car2);
		d2.addCar(car3);
		d2.addCar(car4);		
	}

}
