package de.hm.edu.carads.controller;

import static org.junit.Assert.*;

import javax.naming.directory.InvalidAttributesException;
import javax.ws.rs.core.NoContentException;

import org.junit.Before;
import org.junit.Test;

import de.hm.edu.carads.db.DatabaseControllerImpl;
import de.hm.edu.carads.db.util.DatabaseFactory;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;
import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;
public class DriverControllerTest {

	private static String EMAIL = "muster.mann@hm.edu";
	private static String FIRSTNAME = "Muster";
	private static String LASTNAME = "Mann";
	
	private static String CARBRAND = "Mercedes";
	private static String CARMODEL = "E-Klasse";
	private static String CARCOLOR = "red";

	@Test
	
	public void addDriverTest() {
		DriverController dc = new DriverControllerImpl(new DatabaseControllerImpl(DatabaseFactory.INST_TEST));
		//DriverController dc = new DriverControllerImpl(DatabaseFactory.INST_TEST);
		
		assertEquals(0, dc.getEntityCount());
		
		Driver driver;
		try {
			driver = dc.addEntity(makeNewDriver());
			assertNotNull(driver.getId());
			assertEquals(LASTNAME, driver.getLastName());
			assertEquals(1, dc.getEntityCount());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	(expected=AlreadyExistsException.class)
	public void addDriverWhenSameEmailExistTest() throws Exception {
		DriverController dc = new DriverControllerImpl(new DatabaseControllerImpl(DatabaseFactory.INST_TEST));
		Driver driver = dc.addEntity(makeNewDriver());
		dc.addEntity(makeNewDriver());
	}
	
	@Test
	(expected=InvalidAttributesException.class)
	public void addDriverWithInvalidDataTest() throws Exception {
		DriverController dc = new DriverControllerImpl(new DatabaseControllerImpl(DatabaseFactory.INST_TEST));
		dc.addEntity(new Driver("asd", FIRSTNAME, LASTNAME));
	}
	
	@Test
	public void readFromDBTest() {
		DriverController dc = new DriverControllerImpl(new DatabaseControllerImpl(DatabaseFactory.INST_TEST));

		try {
			Driver driver = dc.addEntity(makeNewDriver());
			String id = driver.getId();
			assertEquals(driver.getFirstName(), dc.getEntity(id).getFirstName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	(expected = NoContentException.class)
	public void readWrongIDTest() throws Exception {
		DriverController dc = new DriverControllerImpl(new DatabaseControllerImpl(DatabaseFactory.INST_TEST));
		dc.getEntity("1123");
	}
	
	@Test
	public void changeDriverTest(){
		DriverController dc = new DriverControllerImpl(new DatabaseControllerImpl(DatabaseFactory.INST_TEST));
		
		try {
			//Fahrer wird erstellt.
			Driver driver = makeNewDriver();
			driver.setBirthdate("1988");
			driver = dc.addEntity(driver);
			
			//Fahrer wird verändert.
			driver.setFirstName("Benni");
			dc.changeEntity(driver.getId(), driver);
			
			//Daten werden aus der DB geholt und ueberprueft.
			Driver driverNew = dc.getEntity(driver.getId());
			assertEquals("Benni", driverNew.getFirstName());
			assertEquals("1988", driverNew.getBirthdate());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void changeDriverWithOtherEmailTest(){
		DriverController dc = new DriverControllerImpl(new DatabaseControllerImpl(DatabaseFactory.INST_TEST));
		
		try {
			//Fahrer wird erstellt.
			Driver driver = makeNewDriver();
			driver = dc.addEntity(driver);
			
			//Fahrer wird verändert.
			driver.setEmail("bla@asd.de");
			dc.changeEntity(driver.getId(), driver);
			assertEquals(driver.getEmail(), dc.getEntity(driver.getEmail()).getEmail());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void changeDriverWithOtherEmailTest2(){
		DriverController dc = new DriverControllerImpl(new DatabaseControllerImpl(DatabaseFactory.INST_TEST));
		
		try {
			//Fahrer wird erstellt.
			Driver driver = makeNewDriver();
			driver = dc.addEntity(driver);
			
			//Fahrer wird verändert.
			driver.setCountry("Norway");
			dc.changeEntity(driver.getId(), driver);
			assertEquals(driver.getEmail(), dc.getEntity(driver.getEmail()).getEmail());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void addCarToDriverTest(){
		DriverController dc = new DriverControllerImpl(new DatabaseControllerImpl(DatabaseFactory.INST_TEST));
		try {
			Driver driver = dc.addEntity(makeNewDriver());
			dc.addCar(driver.getId(), makeNewCar());
			Car car = dc.getCar(driver.getId());
			assertEquals(CARCOLOR, car.getColor());
			assertEquals(CARBRAND, car.getBrand());
			assertEquals(CARMODEL, car.getModel());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	(expected = NoContentException.class)
	public void deleteCar() throws Exception{
		DriverController dc = new DriverControllerImpl(new DatabaseControllerImpl(DatabaseFactory.INST_TEST));
		
		Driver d = dc.addEntity(makeNewDriver());
		dc.addCar(d.getId(), makeNewCar());
		assertNotNull(dc.getCar(d.getId()));
		
		dc.deleteCar(d.getId());
		dc.getCar(d.getId());
	}
	
	private Driver makeNewDriver(){
		Driver driver = new Driver(EMAIL, FIRSTNAME, LASTNAME);
		driver.setBirthdate("2000");
		return driver;
	}
	
	private Car makeNewCar(){
		Car car = new Car();
		car.setBrand(CARBRAND);
		car.setModel(CARMODEL);
		car.setColor(CARCOLOR);
		return car;
	}
	
	@Before
	public void resetDB(){
		DatabaseFactory.dropTestDB();
		DriverController dc = new DriverControllerImpl(new DatabaseControllerImpl(DatabaseFactory.INST_TEST));
	}

}
