package de.hm.edu.carads.controller;

import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;


public interface DriverController extends AbstractEntityController<Driver>{
	//Auto
	public Car getCar(String entitiyid) throws Exception;
	public Car addCar(String entitiyid, Car car) throws Exception;
	public void deleteCar(String entitiyid) throws Exception;
}
