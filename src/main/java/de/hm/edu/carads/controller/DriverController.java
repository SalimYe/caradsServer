package de.hm.edu.carads.controller;

import java.util.Collection;

import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;


public interface DriverController extends AbstractEntityController<Driver>{
	//Auto
	public Car getCar(String driverId, String carId) throws Exception;
	public Collection<Car> getCars(String driverId) throws Exception;
	public Car addCar(String driverId, Car car) throws Exception;
	public void deleteCar(String driverId, String carId) throws Exception;
	public Car updateCar(String driverId, String carId, Car car) throws Exception;
	
	public Collection<Car> getAllFreeCars() throws Exception;
}
