package de.hm.edu.carads.controller;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;

public interface DriverController {
	//Fahrer
	public Collection<Driver> getDrivers(int startAt, int length);
	public Driver getDriver(String id) throws Exception;
	public Driver addDriver(Driver driver) throws Exception;
	public Driver changeDriver(String driverid, Driver driver) throws Exception;
	public void deleteDriver(String id) throws Exception;
	//Auto
	public Car getCar(String driverid) throws Exception;
	public Car addCar(String driverid, Car car) throws Exception;
	public void deleteCar(String driverid) throws Exception;
	
	public long getDriverCount();
}
