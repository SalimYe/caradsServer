package de.hm.edu.carads.controller;

import static org.junit.Assert.*;

import org.junit.Test;

import de.hm.edu.carads.controller.util.EntityValidator;
import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;

public class EntityValidatorTest {

	private static String EMAIL = "muster.mann@hm.edu";
	private static String FIRSTNAME = "Muster";
	private static String LASTNAME = "Mann";

	private static String BRAND = "Mercedes";
	private static String MODEL = "A-Klasse";
	private static String COLOR = "blue";

	@Test
	public void testNormalNewDriver() {
		Driver driver = new Driver(EMAIL, FIRSTNAME, LASTNAME);
		assertTrue(EntityValidator.isEntityValid(driver));
	}

	@Test
	public void testInvalidEmailFromDriver() {
		Driver driver = new Driver("", FIRSTNAME, LASTNAME);
		assertFalse(EntityValidator.isEntityValid(driver));
	}

	@Test
	public void testInvalidEmailFromDriver2() {
		Driver driver = new Driver("name", FIRSTNAME, LASTNAME);
		assertFalse(EntityValidator.isEntityValid(driver));
	}

	@Test
	public void testInvalidEmailFromDriver3() {
		Driver driver = new Driver("asd@", FIRSTNAME, LASTNAME);
		assertFalse(EntityValidator.isEntityValid(driver));
	}

	@Test
	public void testInvalidEmailFromDriver4() {
		Driver driver = new Driver("asd@d", FIRSTNAME, LASTNAME);
		assertFalse(EntityValidator.isEntityValid(driver));
	}

	@Test
	public void testInvalidEmailFromDriver5() {
		Driver driver = new Driver("asd@asd@.de", FIRSTNAME, LASTNAME);
		assertFalse(EntityValidator.isEntityValid(driver));
	}

	@Test
	public void testValidEmailFromDriver() {
		Driver driver = new Driver("asd@dd.de", FIRSTNAME, LASTNAME);
		assertTrue(EntityValidator.isEntityValid(driver));
	}

	@Test
	public void testValidAdvertiser() {
		Advertiser newAdv = new Advertiser(EMAIL, FIRSTNAME, "Neuer");
		assertTrue(EntityValidator.isEntityValid(newAdv));
	}
	
	@Test
	public void testNullEMail() {
		Advertiser newAdv = new Advertiser(null, FIRSTNAME, LASTNAME);
		assertFalse(EntityValidator.isEntityValid((newAdv)));
	}
	
	@Test
	public void validCarTest(){
		Car car = new Car();
		car.setBrand(BRAND);
		car.setModel(MODEL);
		car.setColor(COLOR);
		assertTrue(EntityValidator.isEntityValid(car));
	}
	
	@Test
	public void inValidCarTest(){
		Car car = new Car();
		car.setColor(COLOR);
		assertFalse(EntityValidator.isEntityValid(car));
	}	
}
