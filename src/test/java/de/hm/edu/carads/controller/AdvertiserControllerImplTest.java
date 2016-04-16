package de.hm.edu.carads.controller;

import static org.junit.Assert.*;

import javax.naming.directory.InvalidAttributesException;
import javax.ws.rs.core.NoContentException;

import org.junit.Test;

import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;
import de.hm.edu.carads.db.DatabaseControllerImpl;
import de.hm.edu.carads.db.util.DatabaseFactory;
import de.hm.edu.carads.models.Advertiser;

public class AdvertiserControllerImplTest {
	
	private static String EMAIL = "muster.mann@mustermann.com";
	private static String FIRSTNAME = "Muster";
	private static String LASTNAME = "Mann";

	@Test
	public void addAvertiserTest() {
		AdvertiserController ac = getController();
		try {
			Advertiser advertiser = ac.addEntity(makeNewAdvertiser());
			assertNotNull(advertiser.getId());
			assertEquals(1, ac.getEntityCount());
			assertEquals(LASTNAME, ac.getEntity(advertiser.getId()).getLastName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	(expected = AlreadyExistsException.class)
	public void addAnotherAvertiserWithSameEmailTest() throws Exception {
		AdvertiserController ac = getController();
		ac.addEntity(makeNewAdvertiser());
		ac.addEntity(makeNewAdvertiser());
	}
	
	@Test
	(expected = InvalidAttributesException.class)
	public void addAvertiserWithInvalidInformation() throws Exception {
		AdvertiserController ac = getController();
		Advertiser ad = makeNewAdvertiser();
		ad.setEmail("notvalid");
		ac.addEntity(ad);
	}
	
	@Test
	public void deleteAdvertiserTest(){
		AdvertiserController ac = getController();
		try {
			Advertiser advertiser = ac.addEntity(makeNewAdvertiser());
			assertEquals(1, ac.getEntityCount());
			ac.deleteEntity(advertiser.getEmail());
			assertEquals(0, ac.getEntityCount());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	(expected = NoContentException.class)
	public void getNotExistingAdvertiserTest() throws Exception{
		AdvertiserController ac = getController();
		ac.getEntity("123123123");
	}
	
	
	private Advertiser makeNewAdvertiser(){
		Advertiser adv = new Advertiser(EMAIL, FIRSTNAME, LASTNAME);
		return adv;
	}
	
	private AdvertiserController getController(){
		return new AdvertiserControllerImpl(new DatabaseControllerImpl(DatabaseFactory.INST_TEST));
	}

}
