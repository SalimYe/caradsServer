package de.hm.edu.carads.controller;

import java.util.Collection;
import java.util.List;

import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;

public interface DriverController {
	public Collection<Driver> getDrivers(int startAt, int length);
	public long getDriverCount();
	public Driver getDriver(String id);
	public Driver addDriver(Driver driver);
	public Driver changeDriver(String driverid, Driver driver);
	public boolean deleteDriver(String id);
	public boolean existDriverByEmail(String email);
	public boolean existDriverById(String id);
	public Car getCar(String driverid);
	public Car addCar(String driverid, Car car);
}
