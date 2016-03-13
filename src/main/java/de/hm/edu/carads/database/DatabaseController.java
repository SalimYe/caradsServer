package de.hm.edu.carads.database;

import java.util.List;

import de.hm.edu.carads.models.Driver;

public interface DatabaseController {
	public List<Driver> getDrivers();
	public Driver getDriver(String id);
	public Driver addDriver(Driver driver);
}
