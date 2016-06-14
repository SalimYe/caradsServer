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
		driver.setTitle("Herr");
		driver.setBirthdate("2000");
		driver.setStreet("Lothstr.34");
		driver.setZip("80333");
		driver.setCity("Munich");
		driver.setCountry("Deutschland");
		driver.setDescription("Ich bin ein Student");
		driver.setPhone("089-123");
		driver.setOccupation("Student");
		driver.setLicenseDate("01.01.2000");
		assertTrue(EntityValidator.isEntityValid(driver));
	}

	@Test
	public void testInvalidEmailFromDriver() {
		Driver driver = new Driver("", FIRSTNAME, LASTNAME);
		driver.setBirthdate("2000");
		driver.setZip("80333");
		driver.setCity("Munich");
		driver.setDescription("Ich bin ein Student");
		driver.setPhone("089-123");
		driver.setOccupation("Student");
		driver.setLicenseDate("01.01.2000");
		assertFalse(EntityValidator.isEntityValid(driver));
	}

	@Test
	public void testInvalidEmailFromDriver2() {
		Driver driver = new Driver("name", FIRSTNAME, LASTNAME);
		driver.setBirthdate("2000");
		driver.setZip("80333");
		driver.setCity("Munich");
		driver.setDescription("Ich bin ein Student");
		driver.setPhone("089-123");
		driver.setOccupation("Student");
		driver.setLicenseDate("01.01.2000");
		assertFalse(EntityValidator.isEntityValid(driver));
	}

	@Test
	public void testInvalidEmailFromDriver3() {
		Driver driver = new Driver("asd@", FIRSTNAME, LASTNAME);
		driver.setBirthdate("2000");
		driver.setZip("80333");
		driver.setCity("Munich");
		driver.setDescription("Ich bin ein Student");
		driver.setPhone("089-123");
		driver.setOccupation("Student");
		driver.setLicenseDate("01.01.2000");
		assertFalse(EntityValidator.isEntityValid(driver));
	}

	@Test
	public void testInvalidEmailFromDriver4() {
		Driver driver = new Driver("asd@d", FIRSTNAME, LASTNAME);
		driver.setBirthdate("2000");
		driver.setZip("80333");
		driver.setCity("Munich");
		driver.setDescription("Ich bin ein Student");
		driver.setPhone("089-123");
		driver.setOccupation("Student");
		driver.setLicenseDate("01.01.2000");
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
		driver.setTitle("Herr");
		driver.setBirthdate("2000");
		driver.setStreet("Lothstr.34");
		driver.setZip("80333");
		driver.setCity("Munich");
		driver.setCountry("Deutschland");
		driver.setDescription("Ich bin ein Student");
		driver.setPhone("089-123");
		driver.setOccupation("Student");
		driver.setLicenseDate("01.01.2000");
		assertTrue(EntityValidator.isEntityValid(driver));
	}

	@Test
	public void testValidAdvertiser() {
		Advertiser ad1 = new Advertiser(EMAIL, FIRSTNAME, "Neuer");
		ad1.setCity("Muenchen");
		ad1.setCountry("Deutschland");
		ad1.setZip("80338");
		ad1.setStreet("Lothstr. 14");
		ad1.setPhone("089-1234");
		ad1.setCompany("HM");
		ad1.setTitle("Herr");
		assertTrue(EntityValidator.isEntityValid(ad1));
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
