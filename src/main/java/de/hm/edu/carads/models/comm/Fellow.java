package de.hm.edu.carads.models.comm;

import de.hm.edu.carads.models.util.FellowState;

public class Fellow {
	private String carId;
	private FellowState state;

	public Fellow(String carId, FellowState state) {
		this.carId = carId;
		this.state = state;
	}

	public FellowState getState() {
		return state;
	}

	public void setState(FellowState state) {
		this.state = state;
	}

	public String getCarId() {
		if (this.carId == null)
			this.carId = "";
		return carId;
	}
}
