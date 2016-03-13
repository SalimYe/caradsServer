package de.hm.edu.carads.controller;

import java.util.Collection;
import java.util.List;

import de.hm.edu.carads.models.Driver;

public interface DriverController {
	public Collection<Driver> getDrivers();
	public Collection<Driver> getDrivers(int startAt, int length);
	public int getDriverCount();
	
	public Driver getDriver(String id);
	public Driver addDriver(Driver driver);
	public void deleteDriver(String id);
}
