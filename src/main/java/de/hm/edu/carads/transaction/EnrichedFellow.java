package de.hm.edu.carads.transaction;

import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.util.FellowState;

/**
 * Diese Klasse versteht sich als Erweiterung zur Klasse Fellow, aber erbt nicht von derer.
 * Diese Repraesentation wird zur Rueckgabe ueber die REST-API verwendet, weil hier nicht nur
 * die Referenz, sondern das komplette Fahrzeug Objekt enthalten ist.
 * @author BK
 *
 */
public class EnrichedFellow {
	
	/**
	 * Das Fahrzeug, welches Angefragt wurde.
	 */
	private Car car;
	
	/**
	 * Der aktuelle Status der Anfrage.
	 */
	private FellowState state;
	
	/**
	 * Der Konstruktor mit allen Attributen.
	 * @param state
	 * @param car
	 */
	public EnrichedFellow(FellowState state, Car car) {
		this.state = state;
		this.car = car;
	}
	public FellowState getState() {
		return state;
	}
	public void setState(FellowState state) {
		this.state = state;
	}
	public Car getCar() {
		return car;
	}
	public void setCar(Car car) {
		this.car = car;
	}
}
