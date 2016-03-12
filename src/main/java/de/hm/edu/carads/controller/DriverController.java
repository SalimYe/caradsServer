package de.hm.edu.carads.controller;

import java.util.List;

import de.hm.edu.carads.models.Driver;

public interface DriverController {
	public List<Driver> getDrivers();
	public Driver getDriver(String id);
	public Driver addDriver(Driver driver);
}
