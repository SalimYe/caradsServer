package de.hm.edu.carads.models.comm;

import de.hm.edu.carads.models.util.FellowState;

public class Fellow {
	private String carid;
	private FellowState state;

	public Fellow(String carId, FellowState state) {
		this.carid = carId;
		this.state = state;
	}

	public FellowState getState() {
		return state;
	}

	public void setState(FellowState state) {
		this.state = state;
	}

	public String getCarId() {
		if (this.carid == null)
			this.carid = "";
		return carid;
	}
}
