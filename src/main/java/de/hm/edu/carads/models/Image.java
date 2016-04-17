package de.hm.edu.carads.models;

public class Image extends Model{
	
	private String id;
	private String altText;
	private boolean isTitle;
	
	public String getAltText() {
		return altText;
	}
	public void setAltText(String altText) {
		this.altText = altText;
	}
	public boolean isTitle() {
		return isTitle;
	}
	public void setTitle(boolean isTitle) {
		this.isTitle = isTitle;
	}
}
