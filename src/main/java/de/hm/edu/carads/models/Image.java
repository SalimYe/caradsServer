package de.hm.edu.carads.models;

public class Image {
	
	private String id;
	private String url;
	private String altText;
	private boolean isTitle;
	
	
	public Image(String id, String url, String altText, boolean isTitle) {
		super();
		this.id = id;
		this.url = url;
		this.altText = altText;
		this.isTitle = isTitle;
	}

	public String getUrl() {
		return url;
	}

	public String getAltText() {
		return altText;
	}

	public boolean isTitle() {
		return isTitle;
	}
	
	
}
