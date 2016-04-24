package de.hm.edu.carads.controller;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.directory.InvalidAttributesException;
import javax.ws.rs.core.NoContentException;

import org.junit.Before;
import org.junit.Test;

import de.hm.edu.carads.db.DatabaseControllerImpl;
import de.hm.edu.carads.db.util.DatabaseFactory;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;
import de.hm.edu.carads.models.MetaInformation;
import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;

public class DriverControllerTest {

	private static String EMAIL = "muster.mann@hm.edu";
	private static String FIRSTNAME = "Muster";
	private static String LASTNAME = "Mann";

	private static String CARBRAND = "Mercedes";
	private static String CARMODEL = "E-Klasse";
	private static String CARCOLOR = "red";

	@Test
	public void addDriverTest() throws Exception {
		DriverController dc = getDriverController();

		assertEquals(0, dc.getEntityCount());

		Driver driver;

		driver = dc.addEntity(makeNewDriver());
		assertNotNull(driver.getId());
		assertEquals(LASTNAME, driver.getLastName());
		assertEquals(1, dc.getEntityCount());

	}

	@Test
	public void metaInformationTest() throws Exception {
		DriverController dc = getDriverController();
		DateFormat df = new SimpleDateFormat(MetaInformation.DATE_FORMAT);
		Driver driver;

		driver = dc.addEntity(makeNewDriver());
		Date timeCreated = df.parse(driver.getMetaInformation().getCreated());
		Date timeUpdated = df.parse(driver.getMetaInformation()
				.getLastModified());

		assertTrue(timeUpdated.compareTo(timeCreated) == 0);

		Thread.sleep(1000);
		dc.addCar(driver.getId(), makeNewCar());
		driver = dc.getEntity(driver.getId());
		timeUpdated = df.parse(driver.getMetaInformation().getLastModified());

		assertTrue(timeUpdated.compareTo(timeCreated) > 0);

	}

	@Test
	public void getAllDriversTest() throws Exception {
		DriverController dc = getDriverController();

		Driver driver2;

		dc.addEntity(makeNewDriver());
		assertEquals(1, dc.getAllEntities().size());

		driver2 = makeNewDriver();
		driver2.setEmail("neu@gmx.de");
		driver2 = dc.addEntity(driver2);
		assertEquals(2, dc.getAllEntities().size());

	}

	@Test(expected = AlreadyExistsException.class)
	public void addDriverWhenSameEmailExistTest() throws Exception {
		DriverController dc = getDriverController();
		dc.addEntity(makeNewDriver());
		dc.addEntity(makeNewDriver());
	}

	@Test(expected = InvalidAttributesException.class)
	public void addDriverWithInvalidDataTest() throws Exception {
		DriverController dc = getDriverController();
		dc.addEntity(new Driver("asd", FIRSTNAME, LASTNAME));
	}

	@Test
	public void readFromDBTest() throws Exception {
		DriverController dc = getDriverController();

		Driver driver = dc.addEntity(makeNewDriver());
		String id = driver.getId();
		assertEquals(driver.getFirstName(), dc.getEntity(id).getFirstName());

	}

	@Test(expected = NoContentException.class)
	public void readWrongIDTest() throws Exception {
		DriverController dc = getDriverController();
		dc.getEntity("1123");
	}

	@Test
	public void changeDriverTest() throws Exception {
		DriverController dc = getDriverController();

		// Fahrer wird erstellt.
		Driver driver = makeNewDriver();
		driver.setBirthdate("1988");
		driver = dc.addEntity(driver);

		// Fahrer wird verändert.
		driver.setFirstName("Benni");
		dc.changeEntity(driver.getId(), driver);

		// Daten werden aus der DB geholt und ueberprueft.
		Driver driverNew = dc.getEntity(driver.getId());
		assertEquals("Benni", driverNew.getFirstName());
		assertEquals("1988", driverNew.getBirthdate());
	}

	@Test
	public void changeDriverWithOtherEmailTest() throws Exception {
		DriverController dc = getDriverController();

		// Fahrer wird erstellt.
		Driver driver = makeNewDriver();
		driver = dc.addEntity(driver);

		// Fahrer wird verändert.
		driver.setEmail("bla@asd.de");
		dc.changeEntity(driver.getId(), driver);
		assertEquals(driver.getEmail(), dc.getEntity(driver.getId()).getEmail());
	}

	@Test
	public void changeDriverTest2() throws Exception {
		DriverController dc = getDriverController();

		// Fahrer wird erstellt.
		Driver driver = makeNewDriver();
		driver = dc.addEntity(driver);

		// Fahrer wird verändert.
		driver.setCountry("Norway");
		dc.changeEntity(driver.getId(), driver);
		assertEquals(driver.getEmail(), dc.getEntity(driver.getId()).getEmail());
	}

	@Test
	public void changeDriverWithLessInformationTest() throws Exception {
		DriverController dc = getDriverController();

		Driver driver = makeNewDriver();
		driver.addCar(makeNewCar());
		driver.setCity("Munich");
		driver = dc.addEntity(driver);

		String id = driver.getId();

		Driver newDriver = makeNewDriver();
		newDriver = dc.changeEntity(id, newDriver);
		assertNotEquals("Munich", newDriver.getCity());
	}

	@Test
	public void addCarToDriverTest() throws Exception {
		DriverController dc = getDriverController();

		Driver driver = dc.addEntity(makeNewDriver());
		dc.addCar(driver.getId(), makeNewCar());
		assertFalse(dc.getEntity(driver.getId()).getCars().isEmpty());
		//Car car = dc.getCar(driver.getId());
//		assertEquals(CARCOLOR, car.getColor());
//		assertEquals(CARBRAND, car.getBrand());
//		assertEquals(CARMODEL, car.getModel());

	}
	
	@Test
	public void addCarsToDriverTest() throws Exception {
		DriverController dc = getDriverController();
		Driver driver = dc.addEntity(makeNewDriver());
		
		Car car1 = new Car();
		car1.setBrand("BMW");
		car1.setModel("1er");
		Car car2 = new Car();
		car2.setBrand("Ford");
		car2.setModel("Ka");
		
		dc.addCar(driver.getId(), car1);
		dc.addCar(driver.getId(), car2);
		
		assertEquals(2, dc.getEntity(driver.getId()).getCars().size());
	}
	
	@Test
	public void addCarToDriverAndGetIDTest() throws Exception {
		DriverController dc = getDriverController();
		Driver driver = dc.addEntity(makeNewDriver());
		
		Car car1 = new Car();
		car1.setBrand("BMW");
		car1.setModel("1er");
		
		Car addedCar = dc.addCar(driver.getId(), car1);
		assertFalse(addedCar.getId().isEmpty());
	}
	
	@Test
	public void addCarToDriverAndGetIDTest2() throws Exception {
		DriverController dc = getDriverController();
		Driver driver = dc.addEntity(makeNewDriver());
		
		Car addedCar1 = dc.addCar(driver.getId(), makeNewCar());
		Car addedCar2 = dc.addCar(driver.getId(), makeNewCar());
		
		assertNotEquals(addedCar1.getId(), addedCar2.getId());
	}
	
	@Test
	public void getSpecificCarTest() throws Exception {
		DriverController dc = getDriverController();
		Driver driver = dc.addEntity(makeNewDriver());
		

		String id = dc.addCar(driver.getId(), makeNewCar()).getId();
		
		Car car = dc.getCar(driver.getId(), id);
		assertEquals(CARBRAND, car.getBrand());
	}

	@Test
	public void deleteDriverTest() throws Exception {
		DriverController dc = getDriverController();

		Driver driver = dc.addEntity(makeNewDriver());
		assertEquals(1, dc.getEntityCount());
		dc.deleteEntity(driver.getId());
		assertEquals(0, dc.getEntityCount());
	}

	@Test(expected = NoContentException.class)
	public void deleteCarFailureTest() throws Exception {
		DriverController dc = getDriverController();

		Driver d = dc.addEntity(makeNewDriver());
		Car car = dc.addCar(d.getId(), makeNewCar());
		assertNotNull(dc.getCar(d.getId(), car.getId()));

		dc.deleteCar(d.getId(), car.getId());
		dc.getCar(d.getId(), car.getId());
	}
	
	@Test
	public void updateCarTest() throws Exception{
		DriverController dc = getDriverController();
		Driver d = dc.addEntity(makeNewDriver());
		Car car = dc.addCar(d.getId(), makeNewCar());
		String id = car.getId();
		Car newCar = new Car();
		newCar.setBrand("Daihatsu");
		newCar.setModel("Cuore");
		
		dc.updateCar(d.getId(), id, newCar);
		
		Car updated = dc.getCar(d.getId(), id);
		assertEquals("Cuore", updated.getModel());
	}
	
	@Test (expected = NoContentException.class)
	public void updateNotExistingCar() throws Exception{
		DriverController dc = getDriverController();
		Driver d = dc.addEntity(makeNewDriver());
		
		Car newCar = new Car();
		newCar.setBrand("Daihatsu");
		newCar.setModel("Cuore");
		
		dc.updateCar(d.getId(), "11234123", newCar);
	}
	
	@Test (expected = InvalidAttributesException.class)
	public void updateCarWithInvalidInformation() throws Exception{
		DriverController dc = getDriverController();
		Driver d = dc.addEntity(makeNewDriver());
		Car car = dc.addCar(d.getId(), makeNewCar());
		Car newCar = new Car();
		newCar.setBrand("Daihatsu");
		
		dc.updateCar(d.getId(), car.getId(), newCar);
	}

	@Test(expected = AlreadyExistsException.class)
	public void updateDriverWithSameEmailTest() throws Exception {
		DriverController dc = getDriverController();

		Driver driver1, driver2;
		driver1 = makeNewDriver();
		driver2 = new Driver("othe@this.de", "Joe", "Don");
		dc.addEntity(driver1);
		driver2 = dc.addEntity(driver2);
		driver2.setEmail(EMAIL);

		dc.changeEntity(driver2.getId(), driver2);
	}

	private DriverController getDriverController() {
		return new DriverControllerImpl(new DatabaseControllerImpl(
				DatabaseFactory.INST_TEST));
	}

	private Driver makeNewDriver() {
		Driver driver = new Driver(EMAIL, FIRSTNAME, LASTNAME);
		driver.setBirthdate("2000");
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
