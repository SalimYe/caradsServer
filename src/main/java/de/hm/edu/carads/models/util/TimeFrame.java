package de.hm.edu.carads.models.util;

/**
 * Diese Klasse wird zur Zeitberechnung verwendet und stellt einen Zeitraum dar.
 * @author BK
 * 
 */
public class TimeFrame {
	/**
	 * Start des Zeitraums.
	 */
	public String start;
	
	/**
	 * Ende des Zeitraums.
	 */
	public String end;
	
	/**
	 * Konstruktor mit beiden Attributen.
	 * @param start
	 * @param end
	 */
	public TimeFrame(String start, String end){
		this.start=start;
		this.end=end;
	}
}
