package de.hm.edu.carads.models;

/**
 * Diese Klasse repreasentiert ein Bild.
 * @author BK
 *
 */
public class Image{
	
	/**
	 * Eindeutige ID des Bildes.
	 */
	private String id;
	
	/**
	 * Alternativtext.
	 */
	private String altText;
	
	/**
	 * Wahrheitswert darueber ob das Bild als Titelbild verwendet werden soll.
	 */
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
