package de.hm.edu.carads.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.hm.edu.carads.db.DatabaseControllerImpl;
import de.hm.edu.carads.db.util.DatabaseFactory;
import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Campaign;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;

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
		Car car = driverController.addCar(driver.getId(), makeNewCar());

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
	
	private RequestController getRequestController(){
		if(requestController == null)
			requestController = new RequestControllerImpl(new DatabaseControllerImpl(DatabaseFactory.INST_TEST));
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
		return driver;
	}

	private Car makeNewCar() {
		Car car = new Car();
		car.setBrand(CARBRAND);
		car.setModel(CARMODEL);
		car.setColor(CARCOLOR);
		return car;
	}

}
