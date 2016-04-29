package de.hm.edu.carads.models.util;


public abstract class Model {
	protected String id="";
	protected MetaInformation meta;
	
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
	
	public void update(MetaInformation oldMeta){
		meta = oldMeta;
		meta.update();
	}
	
	public MetaInformation getMetaInformation(){
		return this.meta;
	}
	
	public void renewMetaInformation(){
		//meta = new MetaInformation();
		meta.makeNewMetaInformation();
	}
}
