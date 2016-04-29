package de.hm.edu.carads.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import de.hm.edu.carads.models.util.Fellow;
import de.hm.edu.carads.models.util.FellowState;
import de.hm.edu.carads.models.util.Model;

public class Campaign extends Model{

	private String title;
	private String description;
	private String campaignBudget;
	private String startDate;
	private String endDate;
	private Collection<Image> images;
	private Collection<Fellow> fellows;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String name) {
		this.title = name;
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
	public boolean addFellow(String carId){
		if(this.fellows == null)
			this.fellows = new ArrayList<Fellow>();
		if(carId == null || carId.isEmpty())
			return false;
		if(isCarAFellow(carId))
			return false;
		
		System.out.println("asd");
		fellows.add(new Fellow(carId, FellowState.ASKED));
		return true;
	}
	
	public Collection<Fellow> getFellows(){
		return this.fellows;
	}
	public void setFellows(Collection<Fellow> fellows){
		this.fellows = fellows;
	}
	
	public boolean isCarAFellow(String carId){
		Iterator<Fellow> it = fellows.iterator();
		while(it.hasNext()){
			Fellow f = it.next();
			if(f.getCarId().equals(carId))
				return true;
		}
		return false;
	}
	
	
}
