package de.hm.edu.carads.controller;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;
import de.hm.edu.carads.controller.exceptions.HasConstraintException;
import de.hm.edu.carads.db.DatabaseControllerImpl;
import de.hm.edu.carads.db.util.DatabaseFactory;
import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Campaign;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;
import de.hm.edu.carads.models.util.Fellow;
import de.hm.edu.carads.models.util.FellowState;
import de.hm.edu.carads.transaction.OfferInformation;
import de.hm.edu.carads.transaction.OfferRequest;

public class RequestControllerTest {
	private static String EMAIL = "muster.mann@mustermann.com";
	private static String FIRSTNAME = "Muster";
	private static String LASTNAME = "Mann";
	private static String CARBRAND = "Mercedes";
	private static String CARMODEL = "E-Klasse";
	private static String CARCOLOR = "red";

	private ApplicationController modelController = new ApplicationControllerImpl(new DatabaseControllerImpl(
			DatabaseFactory.INST_TEST));
	
	@Before
	public void resetDB() {
		DatabaseFactory.dropTestDB();
	}
	
	@Test
	public void offerInformationSizeTest() throws Exception {
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		Campaign camp = modelController.addCampaign(ad.getId(), makeNewCampaign());
		Driver driver = modelController.addDriver(makeNewDriver());
		Car car = modelController.addCar(driver.getId(), makeNewCar());
		
		modelController.requestVehicleForCampaign(ad.getId(), camp.getId(), car.getId());
		
		Collection<OfferInformation> info = modelController.getOfferInformation(driver.getId());
		//Fahrer sieht nur ein Angebot an ihn.
		assertEquals(1, info.size());
		
		OfferInformation offerInfo = info.iterator().next();
		
		//Der Status ist "asked".
		assertEquals(FellowState.ASKED, offerInfo.getState());
		
		//Antwort des Fahrers:
		modelController.respondToOffer(car.getId(), ad.getId(), camp.getId(), "ACCEPTED");
		
		//Nochmal Angebote checken
		info = modelController.getOfferInformation(driver.getId());
		assertEquals(1, info.size());
		offerInfo = info.iterator().next();
		
		//Der Status ist "ACCEPTED".
		assertEquals(FellowState.ACCEPTED, offerInfo.getState());
		
		Campaign campaign = modelController.getCampaign(ad.getId(), camp.getId());
		Fellow fellow = campaign.getFellow(car.getId());
		
		//Auch der Werbende sieht "ACCEPTED".
		assertEquals(FellowState.ACCEPTED, fellow.getState());
	}
	
	@Test
	public void offerInformationSizeTest2() throws Exception {
		Driver driver = modelController.addDriver(makeNewDriver());
		Car car = modelController.addCar(driver.getId(), makeNewCar());

		assertEquals(0, modelController.getOfferInformation(driver.getId()).size());
	}
	
	@Test
	public void offerToJustOneCarTest() throws Exception {
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		Campaign camp = modelController.addCampaign(ad.getId(), makeNewCampaign());
		Driver driver = modelController.addDriver(makeNewDriver());
		Car car = modelController.addCar(driver.getId(), makeNewCar());
		
		modelController.requestVehicleForCampaign(ad.getId(), camp.getId(), car.getId());
		assertEquals(1, modelController.getOfferInformation(driver.getId()).size());
	}
	
	@Test
	public void responseTest() throws Exception {
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		Campaign camp = modelController.addCampaign(ad.getId(), makeNewCampaign());
		Driver driver = modelController.addDriver(makeNewDriver());
		Car car = modelController.addCar(driver.getId(), makeNewCar());
		
		modelController.requestVehicleForCampaign(ad.getId(), camp.getId(), car.getId());
		
		modelController.respondToOffer(car.getId(), ad.getId(), camp.getId(), "ACCEPTED");
		ArrayList<OfferInformation> offers = (ArrayList<OfferInformation>) modelController.getOfferInformation(driver.getId());
		assertEquals(FellowState.ACCEPTED, offers.get(0).getState());
	}
	
	@Test
	public void responseTest2() throws Exception {
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		Campaign camp = modelController.addCampaign(ad.getId(), makeNewCampaign());
		Driver driver = modelController.addDriver(makeNewDriver());
		Car car = modelController.addCar(driver.getId(), makeNewCar());
		
		modelController.requestVehicleForCampaign(ad.getId(), camp.getId(), car.getId());
		
		modelController.respondToOffer(car.getId(), ad.getId(), camp.getId(), "REJECTED");
		Iterator<OfferInformation> offers = modelController.getOfferInformation(driver.getId()).iterator();
		OfferInformation offer = offers.next();
		assertEquals(FellowState.REJECTED, offer.getState());
	}
	
	@Test
	public void availableCarsTest() throws Exception {
		Advertiser ad1 = new Advertiser("franz@redbull.com", "Franz", "Kafka");
		ad1.setCity("Muenchen");
		ad1.setCountry("Deutschland");
		ad1.setZip("80338");
		ad1.setStreet("Lothstr. 14");
		ad1.setPhone("089-1234");
		ad1.setCompany("HM");
		ad1.setTitle("Frau");
		Advertiser ad2 = makeNewAdvertiser();
		Campaign c1 = new Campaign();
		c1.setCarBudget("1000");
		c1.setDescription("Beschreibung");
		c1.setStartDate("01.01.2015");
		c1.setEndDate("30.01.2015");
		c1.setName("Red Bull Promo");
		
		Campaign c2 = new Campaign();
		c2.setCarBudget("2000");
		c2.setDescription("Beschreibung");
		c2.setStartDate("01.02.2015");
		c2.setEndDate("30.04.2015");
		c2.setName("BMW Promo");
		
		Campaign c3 = new Campaign();
		c3.setCarBudget("2000");
		c3.setDescription("Beschreibung");
		c3.setStartDate("15.01.2015");
		c3.setEndDate("12.02.2015");
		c3.setName("5er GT Promo");
		
		Driver d1 = new Driver("flo@hm.edu", "Florian", "Mustermann");
		d1.setBirthdate("2000");
		d1.setZip("80333");
		d1.setCity("Munich");
		d1.setDescription("Ich bin ein Student");
		d1.setPhone("089-123");
		d1.setOccupation("Student");
		d1.setLicenseDate("01.01.2000");
		d1.setStreet("Blastr.14");
		d1.setCountry("Deut");
		d1.setTitle("Herr");
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
		ad1 = modelController.addAdvertiser(ad1);
		ad2 = modelController.addAdvertiser(ad2);
		c1 = modelController.addCampaign(ad1.getId(), c1);
		c2 = modelController.addCampaign(ad2.getId(), c2);
		c3 = modelController.addCampaign(ad2.getId(), c3);
		
		d1 = modelController.addDriver(d1);
		d2 = modelController.addDriver(d2);
		car1 = modelController.addCar(d1.getId(), car1);
		car2 = modelController.addCar(d1.getId(), car2);
		car3 = modelController.addCar(d2.getId(), car3);
		car4 = modelController.addCar(d2.getId(), car4);
		
		//Alle Fahrzeuge werden als frei angezeigt
		assertEquals(4, modelController.getAvailableCars(ad1.getId(), c1.getId()).size());
		
		//Car1 wird fuer Kampagne 1 angefragt.
		modelController.requestVehicleForCampaign(ad1.getId(), c1.getId(), car1.getId());
		//In dem Zeitraum ist das Auto fuer andere Kampagnen noch sichtbar, weil noch nicht zugesagt wurde
		assertEquals(4, modelController.getAvailableCars(ad2.getId(), c3.getId()).size());
		//Car1 sagt zu
		modelController.respondToOffer(car1.getId(), ad1.getId(), c1.getId(), FellowState.ACCEPTED.toString());
		
		//Das Auto ist nicht mehr sichtbar, weil in diesem Zeitraum schon zugesagt wurde
		assertEquals(3, modelController.getAvailableCars(ad2.getId(), c3.getId()).size());
		assertFalse(modelController.getAvailableCars(ad2.getId(), c3.getId()).contains(car1));
		
		//Alle Autos sind aber noch fuer eine Kampagne sichtbar, die in einem anderen Zeitraum statt findet
		assertEquals(4, modelController.getAvailableCars(ad2.getId(), c2.getId()).size());
		
	}	
	
	@Test
	(expected = HasConstraintException.class)
	public void deleteBookedCarTest() throws Exception{
		Driver driver = modelController.addDriver(makeNewDriver());
		Car car = modelController.addCar(driver.getId(), makeNewCar());
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		Campaign camp = modelController.addCampaign(ad.getId(), makeNewCampaign());
		
		modelController.requestVehicleForCampaign(ad.getId(), camp.getId(), car.getId());
		modelController.respondToOffer(car.getId(), ad.getId(), camp.getId(), "ACCEPTED");
		
		modelController.deleteCar(driver.getId(), car.getId());
	}
	
	@Test
	(expected = HasConstraintException.class)
	public void deleteBookedCarTest5() throws Exception{
		Driver driver = modelController.addDriver(makeNewDriver());
		Car car = modelController.addCar(driver.getId(), makeNewCar());
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		Campaign c = new Campaign();
		c.setName("test");
		c.setStartDate("18.05.2016");
		c.setEndDate("31.06.2016");
		c.setCarBudget("2");
		c.setDescription("Ne");
		Campaign camp = modelController.addCampaign(ad.getId(), c);
		
		modelController.requestVehicleForCampaign(ad.getId(), camp.getId(), car.getId());
		modelController.respondToOffer(car.getId(), ad.getId(), camp.getId(), "ACCEPTED");
		
		modelController.deleteCar(driver.getId(), car.getId());
	}
	
	@Test
	public void deleteBookedCarTest2() throws Exception{
		Driver driver = modelController.addDriver(makeNewDriver());
		Car car = modelController.addCar(driver.getId(), makeNewCar());
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		Campaign camp = modelController.addCampaign(ad.getId(), makeNewCampaign());
		
		modelController.requestVehicleForCampaign(ad.getId(), camp.getId(), car.getId());
		
		modelController.deleteCar(driver.getId(), car.getId());
	}
	
	@Test
	public void deleteBookedCarTest3() throws Exception{
		Driver driver = modelController.addDriver(makeNewDriver());
		Car car = modelController.addCar(driver.getId(), makeNewCar());
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		Campaign camp = modelController.addCampaign(ad.getId(), makeNewCampaign());
		
		modelController.requestVehicleForCampaign(ad.getId(), camp.getId(), car.getId());
		modelController.respondToOffer(car.getId(), ad.getId(), camp.getId(), "REJECTED");
		
		modelController.deleteCar(driver.getId(), car.getId());
	}
	
	@Test
	public void deleteBookedCarTest4() throws Exception{
		Driver driver = modelController.addDriver(makeNewDriver());
		Car car = modelController.addCar(driver.getId(), makeNewCar());
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		
		Campaign c = new Campaign();
		c.setName("test");
		c.setStartDate("01.01.2016");
		c.setEndDate("31.01.2016");
		c.setCarBudget("2");
		c.setDescription("Ne");
		Campaign camp = modelController.addCampaign(ad.getId(), c);
		
		modelController.requestVehicleForCampaign(ad.getId(), camp.getId(), car.getId());
		modelController.respondToOffer(car.getId(), ad.getId(), camp.getId(), "ACCEPTED");
		
		modelController.deleteCar(driver.getId(), car.getId());
	}
	
	@Test
	(expected = HasConstraintException.class)
	public void deleteDriverWithBookedCarTest() throws Exception{
		Driver driver = modelController.addDriver(makeNewDriver());
		Car car = modelController.addCar(driver.getId(), makeNewCar());
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		Campaign c = new Campaign();
		c.setName("test");
		c.setStartDate("18.05.2016");
		c.setEndDate("31.06.2016");
		c.setCarBudget("2");
		c.setDescription("Ne");
		Campaign camp = modelController.addCampaign(ad.getId(), c);
		
		modelController.requestVehicleForCampaign(ad.getId(), camp.getId(), car.getId());
		modelController.respondToOffer(car.getId(), ad.getId(), camp.getId(), "ACCEPTED");
		
		modelController.deleteDriver(driver.getId());
	}
	
	@Test
	(expected = HasConstraintException.class)
	public void deleteCampaignWithAcceptedFellowsTest() throws Exception{
		Driver driver = modelController.addDriver(makeNewDriver());
		Car car = modelController.addCar(driver.getId(), makeNewCar());
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		Campaign c = new Campaign();
		c.setName("test");
		c.setStartDate("18.05.2016");
		c.setEndDate("31.06.2016");
		c.setCarBudget("2");
		c.setDescription("Ne");
		Campaign camp = modelController.addCampaign(ad.getId(), c);
		
		modelController.requestVehicleForCampaign(ad.getId(), camp.getId(), car.getId());
		modelController.respondToOffer(car.getId(), ad.getId(), camp.getId(), "ACCEPTED");
		
		modelController.deleteCampaign(ad.getId(), camp.getId());
	}
	
	@Test
	(expected = HasConstraintException.class)
	public void deleteAdvertiserWithAcceptedFellowsInCampaignTest() throws Exception{
		Driver driver = modelController.addDriver(makeNewDriver());
		Car car = modelController.addCar(driver.getId(), makeNewCar());
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		Campaign c = new Campaign();
		c.setName("test");
		c.setStartDate("18.05.2016");
		c.setEndDate("31.06.2016");
		c.setCarBudget("2");
		c.setDescription("Ne");
		Campaign camp = modelController.addCampaign(ad.getId(), c);
		
		modelController.requestVehicleForCampaign(ad.getId(), camp.getId(), car.getId());
		modelController.respondToOffer(car.getId(), ad.getId(), camp.getId(), "ACCEPTED");
		
		modelController.deleteAdvertiser(ad.getId());
	}
	
	@Test
	public void automaticallyRejectAllCampaignsWithOverlappingDateTest() throws Exception{
		Driver driver = modelController.addDriver(makeNewDriver());
		Car car = modelController.addCar(driver.getId(), makeNewCar());
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		
		Campaign c = new Campaign();
		c.setName("Kampagne1");
		c.setStartDate("18.05.2016");
		c.setEndDate("31.06.2016");
		c.setCarBudget("2");
		c.setDescription("Ne");
		Campaign camp = modelController.addCampaign(ad.getId(), c);
		
		Campaign c2 = new Campaign();
		c2.setName("Kampagne2");
		c2.setStartDate("20.05.2016");
		c2.setEndDate("01.07.2016");
		c2.setCarBudget("2");
		c2.setDescription("Ne");
		Campaign camp2 = modelController.addCampaign(ad.getId(), c2);
		
		modelController.requestVehicleForCampaign(ad.getId(), camp.getId(), car.getId());
		modelController.requestVehicleForCampaign(ad.getId(), camp2.getId(), car.getId());
		
		//Der Fahrer sieht beide Angebote
		assertEquals(2, modelController.getOfferInformation(driver.getId()).size());
		
		//Der Fahrer sagt fuer die erste Kampagne zu. Die andere Kampagne soll automatisch
		//abgelehnt werden, da sich die Zeitraeume ueberlappen.
		modelController.respondToOffer(car.getId(), ad.getId(), camp.getId(), "ACCEPTED");
		
		//Der Status fuer das Auto bei der Kampagne2 ist auf rejected.
		assertEquals(FellowState.REJECTED, modelController.getCampaign(ad.getId(), camp2.getId()).getFellow(car.getId()).getState());
		
		//Der Status fuer das Auto bei der Kampagne1 ist auf ACCEPTED.
		assertEquals(FellowState.ACCEPTED, modelController.getCampaign(ad.getId(), camp.getId()).getFellow(car.getId()).getState());
	}
	
	@Test
	public void rejectOneAndAcceptAnotherTest() throws Exception{
		Driver driver = modelController.addDriver(makeNewDriver());
		Car car = modelController.addCar(driver.getId(), makeNewCar());
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		Campaign c1 = makeNewCampaign();
		c1.setStartDate("01.06.2016");
		c1.setEndDate("31.12.2016");
		Campaign campaign1 = modelController.addCampaign(ad.getId(), makeNewCampaign());
		
		Campaign c2 = new Campaign();
		c2.setName("Kampagne2");
		c2.setStartDate("09.06.2016");
		c2.setEndDate("31.12.2016");
		c2.setCarBudget("2");
		c2.setDescription("Ne");
		Campaign campaign2 = modelController.addCampaign(ad.getId(), c2);
		
		//Zwei Kampagnen mit ueberschneidenden Daten fragen ein Fahrzeug an.
		modelController.requestVehicleForCampaign(ad.getId(), campaign1.getId(), car.getId());
		modelController.requestVehicleForCampaign(ad.getId(), campaign2.getId(), car.getId());
		
		//Erst moechte der Fahrer die beiden Angebote sehen
		assertEquals(2, getOpenRequestCount(modelController.getOfferInformation(driver.getId())));
				
		//Der Fahrer moechte fuer die erste Kampagne absagen und der zweiten zusagen.
		modelController.respondToOffer(car.getId(), ad.getId(), campaign2.getId(), "REJECTED");
		
		//Erst moechte er die beiden Angebote sehen
		assertEquals(1, getOpenRequestCount(modelController.getOfferInformation(driver.getId())));
		
		modelController.respondToOffer(car.getId(), ad.getId(), campaign1.getId(), "ACCEPTED");
	}
	
	private int getOpenRequestCount(Collection<OfferInformation> collection){
		Iterator<OfferInformation> iterator = collection.iterator();
		int count = 0;
		while(iterator.hasNext()){
			OfferInformation offerInfo = iterator.next();
			if(offerInfo.getState().equals(FellowState.ASKED))
				count++;
		}
		return count;
	}
	
	private Campaign makeNewCampaign(){
		Campaign c = new Campaign();
		c.setName("Red Bull Icerace");
		c.setCarBudget("10000 Euro");
		c.setStartDate("01.01.2017");
		c.setEndDate("31.01.2017");
		c.setDescription("Ne");
		return c;
	}
	
	private Advertiser makeNewAdvertiser() {
		Advertiser adv = new Advertiser(EMAIL, FIRSTNAME, LASTNAME);
		adv.setCity("Muenchen");
		adv.setCountry("Deutschland");
		adv.setZip("80338");
		adv.setStreet("Lothstr. 14");
		adv.setPhone("089-1234");
		adv.setCompany("HM");
		adv.setTitle("Herr");
		return adv;
	}
	
	private Driver makeNewDriver() {
		Driver driver = new Driver(EMAIL, FIRSTNAME, LASTNAME);
		driver.setBirthdate("2000");
		driver.setZip("80333");
		driver.setCity("Munich");
		driver.setCountry("Deutschland");
		driver.setStreet("Lothstr. 35");
		driver.setDescription("Ich bin ein Student");
		driver.setPhone("089-123");
		driver.setOccupation("Student");
		driver.setLicenseDate("01.01.2000");
		driver.setTitle("Herr");
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
