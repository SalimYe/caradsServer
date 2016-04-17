package de.hm.edu.carads.models;

import java.util.Collection;

public class Car extends Model{
	private String brand;
	private String model;
	private String color;
	private int buildYear;
	private int mileage;
	private String description;
	private Collection<Image> images;
	private MetaInformation meta;
	
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
	@Override
	public void updateAttributes(Model newModel) {
		// TODO Auto-generated method stub
		
	}
}
