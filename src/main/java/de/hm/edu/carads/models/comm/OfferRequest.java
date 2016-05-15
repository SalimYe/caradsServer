package de.hm.edu.carads.models.comm;

/**
 * Diese Klasse repräsentiert lediglich die Anfrage an ein Fahrzeug.
 * @author BK
 *
 */
public class OfferRequest {
	/**
	 * Id eines Fahrzeugs.
	 */
	private String carid;
	
	/**
	 * Rueckgabe der FahrzeugID.
	 * @return carid
	 */
	public String getCarId(){
		return this.carid;
	}
}
