package de.hm.edu.carads.models;

public class MetaInformation {
	public String created;
	public String lastModified;
	
	public MetaInformation(){
		this.created = "";
		this.lastModified = "";
	}
	
	
	public String getLastModified() {
		return lastModified;
	}


	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}


	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		if(this.created.isEmpty())
			this.created = created;
		this.lastModified = created;
	}
	
	
}
