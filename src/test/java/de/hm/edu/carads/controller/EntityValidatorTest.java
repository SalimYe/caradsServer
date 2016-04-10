package de.hm.edu.carads.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.hm.edu.carads.controller.util.EntityValidator;
import de.hm.edu.carads.models.Driver;

public class EntityValidatorTest {

	private static String ID = "123";
	private static String EMAIL = "muster.mann@hm.edu";
	private static String FIRSTNAME = "Muster";
	private static String LASTNAME = "Mann";

	private static String brand = "Mercedes";
	private static String model = "A-Klasse";
	private static String color = "blue";
	
	@Test
	public void testNormalNewDriver() {
		Driver driver = new Driver(this.EMAIL, this.FIRSTNAME, this.LASTNAME);
		assertTrue(EntityValidator.isNewDriverValid(driver));
	}

	@Test
	public void testInvalidEmailFromDriver() {
		Driver driver = new Driver("", this.FIRSTNAME, this.LASTNAME);
		assertFalse(EntityValidator.isNewDriverValid(driver));
	}
	
	@Test
	public void testInvalidEmailFromDriver2() {
		Driver driver = new Driver("name", this.FIRSTNAME, this.LASTNAME);
		assertFalse(EntityValidator.isNewDriverValid(driver));
	}
	
	@Test
	public void testInvalidEmailFromDriver3() {
		Driver driver = new Driver("asd@", this.FIRSTNAME, this.LASTNAME);
		assertFalse(EntityValidator.isNewDriverValid(driver));
	}
	
	@Test
	public void testInvalidEmailFromDriver4() {
		Driver driver = new Driver("asd@d", this.FIRSTNAME, this.LASTNAME);
		assertFalse(EntityValidator.isNewDriverValid(driver));
	}
	
	@Test
	public void testInvalidEmailFromDriver5() {
		Driver driver = new Driver("asd@asd@.de", this.FIRSTNAME, this.LASTNAME);
		assertFalse(EntityValidator.isNewDriverValid(driver));
	}
	
	@Test
	public void testValidEmailFromDriver() {
		Driver driver = new Driver("asd@dd.de", this.FIRSTNAME, this.LASTNAME);
		assertTrue(EntityValidator.isNewDriverValid(driver));
	}
	
}
