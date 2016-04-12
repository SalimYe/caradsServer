package de.hm.edu.carads.models;

public abstract class Model {
	protected String id;
	
	public String getId() {
		return id;
	}

	public boolean setId(String id) {
		if(this.id == null || this.id.isEmpty()){
			this.id = id;
			return true;
		}
		else return false;
	}
}
