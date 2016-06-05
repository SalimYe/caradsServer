package de.hm.edu.carads.models;

import static org.junit.Assert.*;

import org.junit.Test;

public class AdvertiserTest {

	@Test
	public void getCampaignTest() {
		Advertiser ad1 = new Advertiser("test@asd.de", "F", "G");
	
		Campaign c1 = new Campaign();
		c1.setCampaignBudget("1000");
		c1.setDescription("Beschreibung");
		c1.setStartDate("01.01.2015");
		c1.setEndDate("30.01.2015");
		c1.setName("Red Bull Promo");
		
		Campaign c2 = new Campaign();
		c2.setCampaignBudget("2000");
		c2.setDescription("Beschreibung");
		c2.setStartDate("01.02.2015");
		c2.setEndDate("30.04.2015");
		c2.setName("BMW Promo");
	}

}
