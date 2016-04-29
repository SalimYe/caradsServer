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
	private static String FIRSTNAME = "Muster";
	private static String LASTNAME = "Mann";

	@Test
	public void addAvertiserTest() throws Exception {
		AdvertiserController ac = getController();

		Advertiser advertiser = ac.addEntity(makeNewAdvertiser());
		assertNotNull(advertiser.getId());
		assertEquals(1, ac.getEntityCount());
		assertEquals(LASTNAME, ac.getEntity(advertiser.getId()).getLastName());

	}

	@Test(expected = AlreadyExistsException.class)
	public void addAnotherAvertiserWithSameEmailTest() throws Exception {
		AdvertiserController ac = getController();
		ac.addEntity(makeNewAdvertiser());
		ac.addEntity(makeNewAdvertiser());
	}

	@Test(expected = InvalidAttributesException.class)
	public void addAvertiserWithInvalidInformation() throws Exception {
		AdvertiserController ac = getController();
		Advertiser ad = makeNewAdvertiser();
		ad.setEmail("notvalid");
		ac.addEntity(ad);
	}

	@Test
	public void deleteAdvertiserTest() throws Exception {
		AdvertiserController ac = getController();

		Advertiser advertiser = ac.addEntity(makeNewAdvertiser());
		assertEquals(1, ac.getEntityCount());
		ac.deleteEntity(advertiser.getId());
		assertEquals(0, ac.getEntityCount());
	}

	@Test(expected = NoContentException.class)
	public void getNotExistingAdvertiserTest() throws Exception {
		AdvertiserController ac = getController();
		ac.getEntity("123123123");
	}

	/*
	@Test
	public void updateAdvertiserTest() throws Exception {
		AdvertiserController ac = getController();

		Advertiser oldAdv = ac.addEntity(makeNewAdvertiser());
		Advertiser newAdv = new Advertiser(EMAIL, FIRSTNAME, "Neuer");
		ac.changeEntity(oldAdv.getId(), newAdv);
		assertEquals(ac.getEntity(oldAdv.getId()).getLastName(), "Neuer");

		Image img = new Image();
		img.setId("aaa");
		newAdv.setLogo(img);
		ac.changeEntity(oldAdv.getId(), newAdv);

		newAdv.setLogo(null);
		newAdv.setCompany("BMW");
		ac.changeEntity(oldAdv.getId(), newAdv);

		assertNotNull(ac.getEntity(oldAdv.getId()).getLogo());
		assertNotNull(ac.getEntity(oldAdv.getId()).getCompany());
	}
	
	*/
	@Test(expected = AlreadyExistsException.class)
	public void updateAdvertiserWithSameEmailTest() throws Exception {
		AdvertiserController ac = getController();

		Advertiser a1, a2;
		a1 = makeNewAdvertiser();
		a2 = new Advertiser("bla@asd.de", "Jon", "Ron");
		ac.addEntity(a1);
		a2 = ac.addEntity(a2);
		a2.setEmail(EMAIL);
		
		ac.changeEntity(a2.getId(), a2);
	}
	
	@Test
	public void addCampaignTest() throws Exception{
		AdvertiserController ac = getController();
		Advertiser ad = ac.addEntity(makeNewAdvertiser());
		Campaign c = ac.addCampaign(ad.getId(), makeNewCampaign());
		assertNotNull(c);
		assertFalse(c.getId().isEmpty());
	}
	
	@Test
	public void getCampaignTest() throws Exception{
		AdvertiserController ac = getController();
		Advertiser ad = ac.addEntity(makeNewAdvertiser());
		Campaign c = ac.addCampaign(ad.getId(), makeNewCampaign());
		
		assertEquals(1, ac.getCampaigns(ad.getId()).size());
	}
	
	@Test
	public void removeCampaignTest() throws Exception{
		AdvertiserController ac = getController();
		Advertiser ad = ac.addEntity(makeNewAdvertiser());
		Campaign c = ac.addCampaign(ad.getId(), makeNewCampaign());
		ac.deleteCampaign(ad.getId(), c.getId());
		assertEquals(0, ac.getCampaigns(ad.getId()).size());
	}
	
	@Test
	public void updateCampaignTest() throws Exception{
		AdvertiserController ac = getController();
		Advertiser ad = ac.addEntity(makeNewAdvertiser());
		Campaign c = ac.addCampaign(ad.getId(), makeNewCampaign());
		
		Campaign newCampaign = new Campaign();
		newCampaign.setTitle("BMW Promo");
		
		ac.updateCampaign(ad.getId(), c.getId(), newCampaign);
		
		assertEquals("BMW Promo", ac.getCampaign(ad.getId(), c.getId()).getTitle());
	}
	
	@Test (expected = NoContentException.class)
	public void updateCampaignTest2() throws Exception{
		AdvertiserController ac = getController();
		Advertiser ad = ac.addEntity(makeNewAdvertiser());
		Campaign c = ac.addCampaign(ad.getId(), makeNewCampaign());
		
		Campaign newCampaign = new Campaign();
		newCampaign.setTitle("BMW Promo");
		
		ac.updateCampaign(ad.getId(), "123123", newCampaign);
	}

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
		return c;
	}

	private AdvertiserController getController() {
		return new AdvertiserControllerImpl(new DatabaseControllerImpl(
				DatabaseFactory.INST_TEST));
	}
}
