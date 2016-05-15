package de.hm.edu.carads.models;

import java.util.Collection;

import de.hm.edu.carads.models.util.Model;

public class Car extends Model{
	private String brand;
	private String model;
	private String color;
	private int buildYear;
	private int mileage;
	private String description;
	private Collection<Image> images;
	
	//nur fuer die Ausgabe
	private String driverId;
	private String driverZip;
	
	public Car(){
		super();
	}
	
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	public void cleanBeforeSaving(){
		this.driverId=null;
		this.driverZip=null;
	}
	public void setDriverInformation(String driverId, String driverZip){
		this.driverId = driverId;
		this.driverZip=driverZip;
	}

	public String getDriverId() {
		return driverId;
	}
	
	
}
