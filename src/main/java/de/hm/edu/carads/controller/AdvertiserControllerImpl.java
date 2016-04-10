package de.hm.edu.carads.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import de.hm.edu.carads.database.DatabaseController;
import de.hm.edu.carads.database.DatabaseControllerImpl;
import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Driver;

public class AdvertiserControllerImpl implements AdvertiserController{

	private DatabaseController dbController;
	private Gson gson;
	
	public AdvertiserControllerImpl(String environment){
		dbController = new DatabaseControllerImpl(environment);
		gson = new Gson();
	}
	@Override
	public Collection<Advertiser> getAdvertiser() {
		List<DBObject> advertisers = dbController.getAllEntities(Advertiser.class);
		List<Advertiser> smallList = new ArrayList<Advertiser>();
		
		Iterator<DBObject> it = advertisers.iterator();
		
		while(it.hasNext()){
			smallList.add(makeAdvertiserFromBasicDBObject((BasicDBObject)it.next()));
		}
		return smallList;
	}

	@Override
	public Advertiser getAdvertiser(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Advertiser addAdvertiser(Advertiser advertiser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Advertiser changeAdvertiser(String adID, Advertiser advertiser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteAdvertiser(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existAdvertiserByEmail(String email) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existAdvertiserById(String id) {
		// TODO Auto-generated method stub
		return false;
	}
	
private Advertiser makeAdvertiserFromBasicDBObject(BasicDBObject dbObj){
		
		Advertiser advertiser = gson.fromJson(dbObj.toJson(), Advertiser.class);
		advertiser.setId(dbObj.getString("_id"));
		return advertiser;
	}

}
