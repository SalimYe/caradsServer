package de.hm.edu.carads.controller;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.naming.directory.InvalidAttributesException;
import javax.ws.rs.core.NoContentException;

import org.junit.Before;
import org.junit.Test;

import de.hm.edu.carads.db.DatabaseControllerImpl;
import de.hm.edu.carads.db.util.DatabaseFactory;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;
import de.hm.edu.carads.models.util.MetaInformation;
import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;
import de.hm.edu.carads.controller.util.DateController;

public class DriverControllerTest {

	private static String EMAIL = "muster.mann@hm.edu";
	private static String EMAILTWO = "flosch@web.de";
	private static String FIRSTNAME = "Muster";
	private static String LASTNAME = "Mann";

	private static String CARBRAND = "Mercedes";
	private static String CARMODEL = "E-Klasse";
	private static String CARCOLOR = "red";
	
	private ApplicationController modelController = new ApplicationControllerImpl(new DatabaseControllerImpl(
			DatabaseFactory.INST_TEST));

	@Test
	public void addDriverTest() throws Exception {
		assertEquals(0, modelController.getDriverCount());

		Driver driver = modelController.addDriver(makeNewDriver());
		assertNotNull(driver.getId());
		assertEquals(LASTNAME, driver.getLastName());
		assertEquals(1, modelController.getDriverCount());
	}

	@Test
	public void getAllDriversTest() throws Exception {
		Driver driver = modelController.addDriver(makeNewDriver());
		assertEquals(1, modelController.getAllDrivers().size());
		Driver driver2 = makeNewDriver();
		driver2.setEmail("neu@gmx.de");
		driver2 = modelController.addDriver(driver2);
		assertEquals(2, modelController.getAllDrivers().size());
	}
        
        @Test
	public void getDriverByIdTest() throws Exception {
        Driver driver = modelController.addDriver(makeNewDriver());
        Driver dbDriver = modelController.getDriverByMail(EMAIL);
		assertEquals(EMAIL, dbDriver.getEmail());
	}

	@Test(expected = AlreadyExistsException.class)
	public void addDriverWhenSameEmailExistTest() throws Exception {
		modelController.addDriver(makeNewDriver());
		modelController.addDriver(makeNewDriver());
	}

	@Test(expected = InvalidAttributesException.class)
	public void addDriverWithInvalidDataTest() throws Exception {
		modelController.addDriver(new Driver("notvalid", FIRSTNAME, LASTNAME));
	}

	@Test
	public void readFromDBTest() throws Exception {
		Driver driver = modelController.addDriver(makeNewDriver());
		String id = driver.getId();
		assertEquals(driver.getFirstName(), modelController.getDriver(id).getFirstName());
	}

	@Test(expected = NoContentException.class)
	public void readWrongIDTest() throws Exception {
		modelController.getDriver("123");
	}

	@Test
	public void changeDriverWithOtherEmailTest() throws Exception {
		Driver driver = modelController.addDriver(makeNewDriver());

		// Fahrer wird verändert.
		driver.setEmail("bla@asd.de");
		modelController.updateDriver(driver.getId(), driver);
		assertEquals(driver.getEmail(), modelController.getDriver(driver.getId()).getEmail());
	}

	@Test
	public void addCarToDriverTest() throws Exception {
		Driver driver = modelController.addDriver(makeNewDriver());
		
		modelController.addCar(driver.getId(), makeNewCar());
		assertFalse(modelController.getDriver(driver.getId()).getCars().isEmpty());
	}
	
	@Test
	public void addCarsToDriverTest() throws Exception {
		Driver driver = modelController.addDriver(makeNewDriver());
		
		Car car1 = new Car();
		car1.setBrand("BMW");
		car1.setModel("1er");
		car1.setColor("red");
		Car car2 = new Car();
		car2.setBrand("Ford");
		car2.setModel("Ka");
		car2.setColor("red");
		modelController.addCar(driver.getId(), car1);
		modelController.addCar(driver.getId(), car2);
		
		assertEquals(2, modelController.getCars(driver.getId()).size());
	}
	
	@Test
	public void addCarToDriverAndGetIDTest() throws Exception {
		Driver driver = modelController.addDriver(makeNewDriver());
		
		Car car1 = new Car();
		car1.setBrand("BMW");
		car1.setModel("1er");
		car1.setColor("red");
		Car addedCar = modelController.addCar(driver.getId(), car1);
		assertFalse(addedCar.getId().isEmpty());
	}
	
	@Test
	public void addCarToDriverAndGetIDTest2() throws Exception {
		Driver driver = modelController.addDriver(makeNewDriver());
		
		//Gleiches Auto geht
		Car addedCar1 = modelController.addCar(driver.getId(), makeNewCar());
		Car addedCar2 = modelController.addCar(driver.getId(), makeNewCar());
		
		assertNotEquals(addedCar1.getId(), addedCar2.getId());
	}
	
	@Test
	public void getSpecificCarTest() throws Exception {
		Driver driver = modelController.addDriver(makeNewDriver());
		String id = modelController.addCar(driver.getId(), makeNewCar()).getId();
		
		Car car = modelController.getCar(driver.getId(), id);
		assertEquals(CARBRAND, car.getBrand());
	}

	@Test
	public void deleteDriverTest() throws Exception {
		Driver driver = modelController.addDriver(makeNewDriver());
		assertEquals(1, modelController.getDriverCount());
		modelController.deleteDriver(driver.getId());
		assertEquals(0, modelController.getDriverCount());
	}

	@Test(expected = NoContentException.class)
	public void deleteCarFailureTest() throws Exception {
		Driver d = modelController.addDriver(makeNewDriver());
		Car car = modelController.addCar(d.getId(), makeNewCar());
		assertNotNull(modelController.getCar(d.getId(), car.getId()));

		modelController.deleteCar(d.getId(), car.getId());
		modelController.getCar(d.getId(), car.getId());
	}
	
	@Test
	public void updateCarTest() throws Exception{
		Driver d = modelController.addDriver(makeNewDriver());
		Car car = modelController.addCar(d.getId(), makeNewCar());
		String id = car.getId();
		Car newCar = new Car();
		newCar.setBrand("Daihatsu");
		newCar.setModel("Cuore");
		newCar.setColor("red");
		modelController.updateCar(d.getId(), id, newCar);
		
		Car updated = modelController.getCar(d.getId(), id);
		assertEquals("Cuore", updated.getModel());
	}
	
	@Test (expected = NoContentException.class)
	public void updateNotExistingCar() throws Exception{
		Driver d = modelController.addDriver(makeNewDriver());
		
		Car newCar = new Car();
		newCar.setBrand("Daihatsu");
		newCar.setModel("Cuore");
		newCar.setColor("red");
		modelController.updateCar(d.getId(), "11234123", newCar);
	}
	
	@Test (expected = InvalidAttributesException.class)
	public void updateCarWithInvalidInformation() throws Exception{
		Driver d = modelController.addDriver(makeNewDriver());
		Car car = modelController.addCar(d.getId(), makeNewCar());
		Car newCar = new Car();
		newCar.setBrand("Daihatsu");
		
		modelController.updateCar(d.getId(), car.getId(), newCar);
	}

	@Test(expected = AlreadyExistsException.class)
	public void updateDriverWithSameEmailTest() throws Exception {
		Driver driver1, driver2;
		driver1 = makeNewDriver();
		driver2 = new Driver("othe@this.de", "Joe", "Don");
		driver2.setTitle("Herr");
		driver2.setBirthdate("2000");
		driver2.setZip("80333");
		driver2.setCity("Munich");
		driver2.setCountry("Deutschland");
		driver2.setStreet("Loth");
		driver2.setDescription("Ich bin ein Student");
		driver2.setPhone("089-123");
		driver2.setOccupation("Student");
		driver2.setLicenseDate("01.01.2000");
		Driver d = modelController.addDriver(driver1);
		Driver d2 = modelController.addDriver(driver2);
		d2.setEmail(EMAIL);

		modelController.updateDriver(d2.getId(), d2);
	}
	
	@Test
	public void getDriverInfoInCarTest() throws Exception{
		Driver driver = modelController.addDriver(makeNewDriver());
		Car car = modelController.addCar(driver.getId(), makeNewCar());
		
		Iterator<Car> cars = modelController.getCars(driver.getId()).iterator(); 
		Car checkCar = cars.next();
		assertEquals(driver.getId(), checkCar.getDriverId());
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

	@Before
	public void resetDB() {
		DatabaseFactory.dropTestDB();
	}

}
