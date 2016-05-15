package de.hm.edu.carads.models.comm;

import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.util.FellowState;


public class EnrichedFellow {
	
	private Car car;
	private FellowState state;
	
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
