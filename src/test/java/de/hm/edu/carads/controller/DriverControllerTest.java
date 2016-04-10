package de.hm.edu.carads.controller;

import static org.junit.Assert.*;

import javax.naming.directory.InvalidAttributesException;
import javax.ws.rs.core.NoContentException;

import org.junit.Before;
import org.junit.Test;

import de.hm.edu.carads.db.util.DatabaseFactory;
import de.hm.edu.carads.models.Driver;
import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;
public class DriverControllerTest {

	private static String EMAIL = "muster.mann@hm.edu";
	private static String FIRSTNAME = "Muster";
	private static String LASTNAME = "Mann";
	

	@Test
	
	public void addDriverTest() {
		DriverController dc = new DriverControllerImpl(DatabaseFactory.INST_TEST);
		
		assertEquals(0, dc.getDriverCount());
		
		Driver driver;
		try {
			driver = dc.addDriver(makeNewDriver());
			assertNotNull(driver.getId());
			assertEquals(LASTNAME, driver.getLastName());
			assertEquals(1, dc.getDriverCount());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	(expected=AlreadyExistsException.class)
	public void addDriverWhenSameEmailExistTest() throws Exception {
		DriverController dc = new DriverControllerImpl(DatabaseFactory.INST_TEST);
		Driver driver = dc.addDriver(makeNewDriver());
		dc.addDriver(makeNewDriver());
	}
	
	@Test
	(expected=InvalidAttributesException.class)
	public void addDriverWithInvalidDataTest() throws Exception {
		DriverController dc = new DriverControllerImpl(DatabaseFactory.INST_TEST);
		dc.addDriver(new Driver("asd", FIRSTNAME, LASTNAME));
	}
	
	@Test
	public void readFromDBTest() {
		DriverController dc = new DriverControllerImpl(DatabaseFactory.INST_TEST);

		try {
			Driver driver = dc.addDriver(makeNewDriver());
			String id = driver.getId();
			assertEquals(driver.getFirstName(), dc.getDriver(id).getFirstName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	(expected = NoContentException.class)
	public void readWrongIDTest() throws Exception {
		DriverController dc = new DriverControllerImpl(DatabaseFactory.INST_TEST);

		dc.getDriver("123456");
	}
	
	@Test
	public void changeDriverTest(){
		DriverController dc = new DriverControllerImpl(DatabaseFactory.INST_TEST);
		
		try {
			//Fahrer wird erstellt.
			Driver driver = makeNewDriver();
			driver.setBirthdate("1988");
			driver = dc.addDriver(driver);
			
			//Fahrer wird verändert.
			driver.setFirstName("Benni");
			dc.changeDriver(driver.getId(), driver);
			
			//Daten werden aus der DB geholt und ueberprueft.
			Driver driverNew = dc.getDriver(driver.getId());
			assertEquals("Benni", driverNew.getFirstName());
			assertEquals("1988", driverNew.getBirthdate());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void changeDriverWithOtherEmailTest(){
		DriverController dc = new DriverControllerImpl(DatabaseFactory.INST_TEST);
		
		try {
			//Fahrer wird erstellt.
			Driver driver = makeNewDriver();
			driver = dc.addDriver(driver);
			
			//Fahrer wird verändert.
			driver.setEmail("bla@asd.de");
			dc.changeDriver(driver.getId(), driver);
			assertEquals(driver.getEmail(), dc.getDriver(driver.getEmail()).getEmail());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Driver makeNewDriver(){
		Driver driver = new Driver(EMAIL, FIRSTNAME, LASTNAME);
		driver.setBirthdate("2000");
		return driver;
	}
	
	@Before
	public void resetDB(){
		DatabaseFactory.dropTestDB();
		DriverController dc = new DriverControllerImpl(DatabaseFactory.INST_TEST);
	}

}
