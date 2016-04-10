package de.hm.edu.carads.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.hm.edu.carads.models.Driver;

public class EntityValidatorTest {

	private static String id = "123";
	private static String email = "muster.mann@hm.edu";
	private static String firstName = "Muster";
	private static String lastName = "Mann";

	private static String brand = "Mercedes";
	private static String model = "A-Klasse";
	private static String color = "blue";
	
	@Test
	public void testNormalNewDriver() {
		Driver driver = new Driver(this.email, this.firstName, this.lastName);
		assertTrue(EntityValidator.isNewDriverValid(driver));
	}

	@Test
	public void testInvalidEmailFromDriver() {
		Driver driver = new Driver("", this.firstName, this.lastName);
		assertFalse(EntityValidator.isNewDriverValid(driver));
	}
	
	@Test
	public void testInvalidEmailFromDriver2() {
		Driver driver = new Driver("name", this.firstName, this.lastName);
		assertFalse(EntityValidator.isNewDriverValid(driver));
	}
	
	@Test
	public void testInvalidEmailFromDriver3() {
		Driver driver = new Driver("asd@", this.firstName, this.lastName);
		assertFalse(EntityValidator.isNewDriverValid(driver));
	}
	
	@Test
	public void testInvalidEmailFromDriver4() {
		Driver driver = new Driver("asd@d", this.firstName, this.lastName);
		assertFalse(EntityValidator.isNewDriverValid(driver));
	}
	
	@Test
	public void testInvalidEmailFromDriver5() {
		Driver driver = new Driver("asd@asd@.de", this.firstName, this.lastName);
		assertFalse(EntityValidator.isNewDriverValid(driver));
	}
	
	@Test
	public void testValidEmailFromDriver() {
		Driver driver = new Driver("asd@dd.de", this.firstName, this.lastName);
		assertTrue(EntityValidator.isNewDriverValid(driver));
	}
	
}
