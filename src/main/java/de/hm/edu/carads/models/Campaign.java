package de.hm.edu.carads.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import de.hm.edu.carads.models.util.Fellow;
import de.hm.edu.carads.models.util.FellowState;
import de.hm.edu.carads.models.util.Model;

/**
 * Die Klasse repraesentiert eine Kampagne.
 * @author BK
 *
 */
public class Campaign extends Model{

	/**
	 * Titel der Kampagne.
	 */
	private String name;
	
	/**
	 * Beschreibung.
	 */
	private String description;
	
	/**
	 * Budget der Kampagne.
	 */
	private String campaignBudget;
	
	/**
	 * Budget fuer ein Auto / Tag.
	 */
	private String carBudget;
	
	/**
	 * Datum des Kampagnenanfangs.
	 */
	private String startDate="";
	
	/**
	 * Datum des Kampagnenendes.
	 */
	private String endDate="";
	
	/**
	 * Sammlung von Bildern, welche die Kampagne beschreiben und
	 * Informationen zu den Klebefolien beinhalten.
	 */
	private Collection<Image> images;
	
	/**
	 * Sammlung der Autos, welche fuer die Kampagne angefragt wurden.
	 */
	private Collection<Fellow> fellows;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCampaignBudget() {
		return campaignBudget;
	}
	public void setCampaignBudget(String campaignBudget) {
		this.campaignBudget = campaignBudget;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Collection<Image> getImages() {
		return images;
	}
	public void setImages(Collection<Image> images) {
		this.images = images;
	}
	
	private void checkFellows(){
		if(this.fellows == null)
			this.fellows = new ArrayList<Fellow>();
	}
	/**
	 * Eine neue Anfrage an ein Fahrzeug.
	 * @param carId
	 * @return true if all Information was given.
	 */
	public boolean addFellow(String carId){
		checkFellows();
		if(carId == null || carId.isEmpty())
			return false;
		
		fellows.add(new Fellow(carId, FellowState.ASKED));
		return true;
	}
	
	/**
	 * Gibt die Sammlung der angefragten Fahrzeuge zurueck.
	 * @return Fellow Collection
	 */
	public Collection<Fellow> getFellows(){
		checkFellows();
		return this.fellows;
	}
	
	/**
	 * Die Sammlung der angefragten Fahrzeuge wird durch eine andere ersetzt.
	 * @param fellows
	 */
	public void setFellows(Collection<Fellow> fellows){
		this.fellows = fellows;
	}
	
	/**
	 * Wahrheitswert darueber ob ein Fahrzeug bereits fuer diese Kampagne angefragt wurde.
	 * @param carId
	 * @return already asked
	 */
	public boolean isCarAFellow(String carId){
		Fellow fellow = getFellow(carId);
		
		if(fellow==null)
			return false;
		return true;
		
	}
	
	/**
	 * Gibt eine Anfragestatus an ein Fahrzeug anhand der FahrzeugID zurueck.
	 * @param carId
	 * @return Anfragestatus
	 */
	public Fellow getFellow(String carId){
		if(carId==null)
			return null;
		checkFellows();
		
		Iterator<Fellow> it = fellows.iterator();
		while(it.hasNext()){
			Fellow f = it.next();
			if(f.getCarId().equals(carId))
				return f;
		}
		return null;
	}
	
	/**
	 * Wahrheitswert ob ein spezielles Fahrzeug bereits fuer die Kampagne zugesagt hat. 
	 * @param carId
	 * @return true if accepted
	 */
	public boolean hasFellowAccepted(String carId){
		Fellow fellow = getFellow(carId);
		if(fellow!=null && fellow.getState().equals(FellowState.ACCEPTED))
			return true;
		return false;
	}	
	
}
