package de.hm.edu.carads.models;

import java.util.Collection;

public class Car {
	private String id;
	private String brand;
	private String model;
	private String color;
	private int buildYear;
	private int milage;
	private String description;
	private Collection<Image> images;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
}
