package de.hm.edu.carads.models.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hm.edu.carads.controller.util.DateController;

/**
 * Diese Klasse wird zur Verwaltung von Metainformationen verwendet.
 * So wird immer erkenntlich wann die Entitaet angelegt wurde und das letzte mal veraendert wurde.
 * @author BK
 *
 */
public class MetaInformation{
	/**
	 * Dieser Wert steht fuer das erstmalige Erstellen der Entitaet.
	 */
	private String created;
	
	/**
	 * Dieser Wert steht fuer das Datum an welchem die Entitaet das letzte mal veraendert wurde.
	 */
	private String lastModified;
	
	public MetaInformation(){
		super();
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
	
	/**
	 * Diese Methode setzt beide Attribute auf den aktuellen Zeitpunkt.
	 */
	public void makeNewMetaInformation(){
		this.created = makeDate();
		this.lastModified = makeDate();
	}
	
	/**
	 * Diese Methode erneuert das Attribut "lastModified" mit dem aktuellen Zeitpunkt.
	 */
	public void update(){
		this.lastModified = makeDate();
	}
	
	/**
	 * Der aktuelle Zeitpunkt wird als String zurueck gegeben.
	 * @return aktueller Zeitpunkt
	 */
	private static String makeDate(){
		DateFormat df = new SimpleDateFormat(DateController.DATE_FORMAT_METAINFORMATION);
		return df.format(Calendar.getInstance().getTime());
	}	
}
