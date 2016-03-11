package de.hm.edu.carads.controller.impl;

import java.util.ArrayList;
import java.util.List;

import de.hm.edu.carads.controller.DriverController;
import de.hm.edu.carads.models.Driver;

public class DriverControllerImpl implements DriverController{

	@Override
	public List<Driver> getDrivers() {
		List<Driver> list = new ArrayList<Driver>();
		list.add(new Driver("asd", "Benni", "Keckes", "09.06.1988"));
		list.add(new Driver("3asd2", "Flo", "Schaeffer", "19.03.1992"));
		return list;
	}

	@Override
	public Driver getDriver(String id) {
		return new Driver(id, "Benni", "Keckes", "09.06.1988");
	}

}
