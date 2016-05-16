package de.hm.edu.carads.controller;

import static org.junit.Assert.*;

import javax.naming.directory.InvalidAttributesException;
import javax.ws.rs.core.NoContentException;

import org.junit.Before;
import org.junit.Test;

import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;
import de.hm.edu.carads.db.DatabaseControllerImpl;
import de.hm.edu.carads.db.util.DatabaseFactory;
import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Campaign;

public class AdvertiserControllerTest {

	private static String EMAIL = "muster.mann@mustermann.com";
        private static String EMAILTWO = "flosch@bosch.de";
	private static String FIRSTNAME = "Muster";
	private static String LASTNAME = "Mann";
	
	private ModelController modelController = new ModelControllerImpl(new DatabaseControllerImpl(
			DatabaseFactory.INST_TEST));

	@Test
	public void addAvertiserTest() throws Exception {
		

		modelController.addAdvertiser(makeNewAdvertiser());
		Advertiser advertiser = modelController.getAdvertiserByMail(EMAIL);
		assertNotNull(advertiser.getId());
		assertEquals(1, modelController.getAdvertiserCount());
		assertEquals(LASTNAME, modelController.getAdvertiser(advertiser.getId()).getLastName());

	}

	@Test(expected = AlreadyExistsException.class)
	public void addAnotherAvertiserWithSameEmailTest() throws Exception {
		modelController.addAdvertiser(makeNewAdvertiser());
		modelController.addAdvertiser(makeNewAdvertiser());
	}

	@Test(expected = InvalidAttributesException.class)
	public void addAvertiserWithInvalidInformation() throws Exception {
		Advertiser ad = new Advertiser("notvalid", FIRSTNAME, LASTNAME);
		
		modelController.addAdvertiser(ad);
	}

	@Test
	public void deleteAdvertiserTest() throws Exception {
		modelController.addAdvertiser(makeNewAdvertiser());
		
		assertEquals(1, modelController.getAdvertiserCount());
		String id = modelController.getAdvertiserByMail(EMAIL).getId();
		modelController.deleteAdvertiser(id);
		assertEquals(0, modelController.getAdvertiserCount());
	}

	@Test(expected = NoContentException.class)
	public void getNotExistingAdvertiserTest() throws Exception {
		modelController.getAdvertiser("123");
	}

	@Test(expected = AlreadyExistsException.class)
	public void updateAdvertiserWithSameEmailTest() throws Exception {
		modelController.addAdvertiser(makeNewAdvertiser());

		Advertiser ad2 =modelController.addAdvertiser(new Advertiser(EMAILTWO, "Max", "Muster"));
		ad2.setEmail(EMAIL);
		
		modelController.changeAdvertiser(ad2.getId(), ad2);	}
	
	@Test
	public void addCampaignTest() throws Exception{
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		
		Campaign c = modelController.addCampaign(ad.getId(), makeNewCampaign());
		assertNotNull(c);
		assertFalse(c.getId().isEmpty());
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void addCampaignTest2() throws Exception{
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		Campaign c = new Campaign();
		c.setTitle("BMW Promo");
		
		//Start und Ende passen nicht
		c.setStartDate("20.01.2015");
		c.setEndDate("19.01.2015");
		modelController.addCampaign(ad.getId(), c);
	}
	
	@Test
	public void getAllCampaignsTest() throws Exception{
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		modelController.addCampaign(ad.getId(), makeNewCampaign());
		
		assertEquals(1, modelController.getCampaigns(ad.getId()).size());
	}
	
	@Test (expected = NoContentException.class)
	public void getSingleCampaignTest() throws Exception{
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		modelController.addCampaign(ad.getId(), makeNewCampaign());
		
		modelController.getCampaign(ad.getId(), "");
	}
	
	@Test
	public void removeCampaignTest() throws Exception{
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		Campaign c = modelController.addCampaign(ad.getId(), makeNewCampaign());
		modelController.deleteCampaign(ad.getId(), c.getId());

		assertEquals(0, modelController.getCampaigns(ad.getId()).size());
	}
	
	@Test
	public void updateCampaignTest() throws Exception{
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		Campaign c = modelController.addCampaign(ad.getId(), makeNewCampaign());
		
		Campaign newCampaign = new Campaign();
		newCampaign.setTitle("BMW Promo");
		newCampaign.setStartDate("01.12.2016");
		newCampaign.setEndDate("02.02.2017");
		
		modelController.updateCampaign(ad.getId(), c.getId(), newCampaign);
		
		assertEquals(1, modelController.getCampaigns(ad.getId()).size());
		assertEquals("BMW Promo", modelController.getCampaign(ad.getId(), c.getId()).getTitle());
	}
	
	@Test (expected = NoContentException.class)
	public void updateCampaignTest2() throws Exception{
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		Campaign c = modelController.addCampaign(ad.getId(), makeNewCampaign());
		
		Campaign newCampaign = new Campaign();
		newCampaign.setTitle("BMW Promo");
		newCampaign.setStartDate("01.12.2016");
		newCampaign.setEndDate("02.02.2017");
		modelController.updateCampaign(ad.getId(), "123123", newCampaign);
	}
	
	@Test
	public void addCarToCampaignTest() throws Exception{
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		Campaign c = modelController.addCampaign(ad.getId(), makeNewCampaign());
		
		c = modelController.requestVehicleForCampaign(ad.getId(), c.getId(), "123");
		assertEquals(1, modelController.getCampaign(ad.getId(), c.getId()).getFellows().size());
	}
	
	@Test
	public void addCarToCampaignTest2() throws Exception{
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		Campaign c = modelController.addCampaign(ad.getId(), makeNewCampaign());
		
		modelController.requestVehicleForCampaign(ad.getId(), c.getId(), "123");
		modelController.requestVehicleForCampaign(ad.getId(), c.getId(), "124");
		modelController.requestVehicleForCampaign(ad.getId(), c.getId(), "125");
		assertEquals(3, modelController.getCampaign(ad.getId(), c.getId()).getFellows().size());
	}
	
	@Test (expected = AlreadyExistsException.class)
	public void addCarToCampaignTest3() throws Exception{
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		Campaign c = modelController.addCampaign(ad.getId(), makeNewCampaign());
		
		modelController.requestVehicleForCampaign(ad.getId(), c.getId(), "123");
		modelController.requestVehicleForCampaign(ad.getId(), c.getId(), "123");
	}
	
	/**
	 * Zwei Kampagnen fragen zum gleichen Zeitraum den selben Fahrer an.
	 * Das geht, weil er noch nicht zugesagt hat.
	 * @throws Exception
	 */
	public void addCarToSecondCampaignWithSameDateTest() throws Exception{
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		Campaign camp1 = new Campaign();
		camp1.setTitle("Fiat Promo");
		camp1.setStartDate("30.01.2000");
		camp1.setEndDate("01.03.2000");
		camp1 = modelController.addCampaign(ad.getId(), camp1);
		modelController.requestVehicleForCampaign(ad.getId(), camp1.getId(), "123");
		
		
		Advertiser adv1 = modelController.addAdvertiser(new Advertiser("neu@test.de", FIRSTNAME, LASTNAME));
		Campaign camp2 = new Campaign();
		camp2.setTitle("BMW Promo");
		camp2.setStartDate("30.01.2000");
		camp2.setEndDate("01.03.2000");
		camp2 = modelController.addCampaign(adv1.getId(), camp2);
		
		modelController.requestVehicleForCampaign(adv1.getId(), camp2.getId(), "123");
	}
	
	/**
	 * Zwei Kampagnen fragen zum gleichen Zeitraum den selben Fahrer an.
	 * Das geht nicht, weil er schon zugesagt hat.
	 * @throws Exception
	 */
	@Test (expected = AlreadyExistsException.class)
	public void addCarToSecondCampaignWithSameDateTest2() throws Exception{
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		Campaign camp1 = new Campaign();
		camp1.setTitle("Fiat Promo");
		camp1.setStartDate("30.01.2000");
		camp1.setEndDate("01.03.2000");
		camp1 = modelController.addCampaign(ad.getId(), camp1);
		modelController.requestVehicleForCampaign(ad.getId(), camp1.getId(), "123");
		
		//Zusagen
		modelController.respondToOffer("123", ad.getId(), camp1.getId(), "ACCEPTED");
		
		Advertiser adv1 = modelController.addAdvertiser(new Advertiser("neu@test.de", FIRSTNAME, LASTNAME));
		Campaign camp2 = new Campaign();
		camp2.setTitle("BMW Promo");
		camp2.setStartDate("30.01.2000");
		camp2.setEndDate("01.03.2000");
		camp2 = modelController.addCampaign(adv1.getId(), camp2);
		
		modelController.requestVehicleForCampaign(adv1.getId(), camp2.getId(), "123");
	}
	
	@Test
	public void changeCampaignAndKeepFellowsTest() throws Exception{
		Advertiser ad = modelController.addAdvertiser(makeNewAdvertiser());
		Campaign campaign = modelController.addCampaign(ad.getId(), makeNewCampaign());
		
		modelController.requestVehicleForCampaign(ad.getId(), campaign.getId(), "111");
		modelController.requestVehicleForCampaign(ad.getId(), campaign.getId(), "222");
				
		campaign = modelController.getCampaign(ad.getId(), campaign.getId());
		assertEquals(2, campaign.getFellows().size());
		
		//Aenderung der Kampagne
		campaign.setCampaignBudget("230");
		
		modelController.updateCampaign(ad.getId(), campaign.getId(), campaign);
		
		campaign = modelController.getCampaign(ad.getId(), campaign.getId());
		assertEquals(2, campaign.getFellows().size());
	}
	/*
	@Test (expected = AlreadyExistsException.class)
	public void addCarToSecondCampaignWithSameDateTest4() throws Exception{
		AdvertiserController ac = getController();
		Advertiser ad = ac.addEntity(makeNewAdvertiser());
		Campaign c = ac.addCampaign(ad.getId(), makeNewCampaign());		
		ac.addVehicleToCampaign(ad.getId(), c.getId(), "123");
		
		Advertiser adv1 = ac.addEntity(new Advertiser("neu@test.de", FIRSTNAME, LASTNAME));
		Campaign camp2 = new Campaign();
		camp2.setTitle("BMW Promo");
		camp2.setStartDate("30.12.1999");
		camp2.setEndDate("01.03.2000");
		camp2 = ac.addCampaign(adv1.getId(), camp2);
		ac.addVehicleToCampaign(adv1.getId(), camp2.getId(), "123");
	}
	*/
	@Before
	public void resetDB() {
		DatabaseFactory.dropTestDB();
	}

	private Advertiser makeNewAdvertiser() {
		Advertiser adv = new Advertiser(EMAIL, FIRSTNAME, LASTNAME);
		return adv;
	}
	
	private Campaign makeNewCampaign(){
		Campaign c = new Campaign();
		c.setTitle("Red Bull Icerace");
		c.setCampaignBudget("10000 Euro");
		c.setStartDate("01.01.2000");
		c.setEndDate("31.01.2000");
		return c;
	}
}
