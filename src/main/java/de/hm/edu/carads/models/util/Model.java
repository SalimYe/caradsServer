package de.hm.edu.carads.models.util;

/**
 * Diese Klasse dient als Vater-Klasse fuer Entitaeten und beinhaltet allgemeine Attribute
 * und Methoden.
 * @author BK
 *
 */
public abstract class Model {
	/**
	 * Jede Entitaet hat eine ID.
	 */
	protected String id="";
	
	/**
	 * Jede Entitaet hat Metainformationen.
	 */
	protected MetaInformation meta;
	
	/**
	 * Im Konstruktor wird erstmal die MetaInformation initialisiert.
	 */
	public Model(){
		meta = new MetaInformation();
	}
	
	public String getId() {
		return id;
	}

	public boolean setId(String id) {
		if(this.id == null || this.id.isEmpty()){
			this.id = id;
			return true;
		}
		else return false;
	}
	
	/**
	 * Die Metainfirmation wird aktualisiert.
	 * @param oldMeta
	 */
	public void update(MetaInformation oldMeta){
		meta = oldMeta;
		meta.update();
	}
	
	
	public MetaInformation getMetaInformation(){
		return this.meta;
	}
	
	/**
	 * Ein Reset fuer die Metainformation.
	 */
	public void renewMetaInformation(){
		if(this.meta==null)
			this.meta = new MetaInformation();
		meta.makeNewMetaInformation();
	}
}
