package de.hm.edu.carads.models.util;


/**
 * Diese Klasse referenziert Fahrzeuge innerhalb einer Kampagne und beinhaltet den Status der Buchungsanfrage.
 * @author Bk
 *
 */
public class Fellow {
	
	/**
	 * Referenz zur Fahrzeug ID
	 */
	private String carId;
	
	/**
	 * Status der Anfrage (Angefragt, Bestaetigt, Abgelehnt)
	 */
	private FellowState state;

	/**
	 * Konstruktor mit allen Attributen.
	 * @param carId
	 * @param state
	 */
	public Fellow(String carId, FellowState state) {
		this.carId = carId;
		this.state = state;
	}

	/**
	 * Gibt den Anfragestatus zuerueck.
	 * @return
	 */
	public FellowState getState() {
		return state;
	}

	/**
	 * Setzt den Status.
	 * @param state
	 */
	public void setState(FellowState state) {
		this.state = state;
	}

	/**
	 * Gibt die Fahrzeug ID zurueck.
	 * @return
	 */
	public String getCarId() {
		if (this.carId == null)
			this.carId = "";
		return carId;
	}
}
