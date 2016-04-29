package de.hm.edu.carads.models.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MetaInformation{
	private String created;
	private String lastModified;
	
	public static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss"; 
	
	public MetaInformation(){
//		this.created = "";
//		this.lastModified = "";
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
	
	public void makeNewMetaInformation(){
		this.created = makeDate();
		this.lastModified = makeDate();
	}
	
	public void update(){
		this.lastModified = makeDate();
	}
	
	private static String makeDate(){
		DateFormat df = new SimpleDateFormat(DATE_FORMAT);
		return df.format(Calendar.getInstance().getTime());
	}	
}
