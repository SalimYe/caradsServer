package de.hm.edu.carads.controller;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;

public interface DriverController {
	public Collection<Driver> getDrivers(int startAt, int length);
	public long getDriverCount();
	
	public Driver getDriver(String id) throws Exception;
	
	public Driver addDriver(Driver driver) throws NoContentException;
	public Driver changeDriver(String driverid, Driver driver) throws NoContentException;
	public boolean deleteDriver(String id);
	public boolean existDriverByEmail(String email);
	public boolean existDriverById(String id);
	public Car getCar(String driverid) throws Exception;
	public Car addCar(String driverid, Car car) throws NoContentException;
}
