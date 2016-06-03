package de.hm.edu.carads.models;

import java.util.Collection;

import de.hm.edu.carads.models.util.Model;

/**
 * Diese Klasse repeasentiert ein Fahrzeug.
 * @author BK
 *
 */
public class Car extends Model{
	/**
	 * Marke.
	 */
	private String brand;
	
	/**
	 * Modell
	 */
	private String model;
	
	/**
	 * Farbe.
	 */
	private String color;
	
	/**
	 * Baujahr.
	 */
	private String buildYear;
	
	/**
	 * Laufleistung.
	 */
	private int mileage;
	
	/**
	 * Beschreibung
	 */
	private String description;
	
	/**
	 * Sammlung von Bildern des Fahrzeugs.
	 */
	private Collection<Image> images;
	
	/**
	 * Nur fuer die Rueckgabe zum Benutzer. Wird nicht in der DB gespeichert.:
	 * ID des Fahrers.
	 */
	private String driverId;
	
	/**
	 * Nur fuer die Rueckgabe zum Benutzer. Wird nicht in der DB gespeichert.:
	 * PLZ des Fahrers.
	 */
	private String driverZip;
	
	/**
	 * Nur fuer die Rueckgabe zum Benutzer. Wird nicht in der DB gespeichert.:
	 * Beruf des Fahrers.
	 */
	private String driverOccupation;
	
	/**
	 * Nur fuer die Rueckgabe zum Benutzer. Wird nicht in der DB gespeichert.:
	 * Ausstelldatum des Fuehrerscheins des Fahrers.
	 */
	private String driverLicenseDate;
	
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
	
	/**
	 * Falls Informationen zum Fahrer gesetzt sind
	 * muessen sie vor dem Speichern auf null gesetzt werden. Sonst wuerden
	 * diese Informationen mit gespeichert werden und zu redundanzen fuehren.
	 */
	public void cleanBeforeSaving(){
		this.driverId=null;
		this.driverZip=null;
	}
	
	/**
	 * Vor der Ausgabe zu dem Benutzer werden die Informationen gesetzt.
	 * @param driverId
	 * @param driverZip
	 */
	public void setDriverInformation(String driverId, String driverZip, String occupation, String licenseDate){
		this.driverId = driverId;
		this.driverZip=driverZip;
		this.driverOccupation=occupation;
		this.driverLicenseDate=licenseDate;
	}

	public String getDriverId() {
		return driverId;
	}
	
	
}
