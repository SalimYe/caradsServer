package de.hm.edu.carads.models;

import java.util.ArrayList;
import java.util.Collection;

public class Campaign extends Model{

	private String title;
	private String description;
	private String campaignBudget;
	private String startDate;
	private String endDate;
	private Collection<Image> images;
	
	
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
	
	
}
