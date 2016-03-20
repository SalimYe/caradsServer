package de.hm.edu.carads.controller;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

import de.hm.edu.carads.database.DatabaseController;
import de.hm.edu.carads.database.DatabaseControllerImpl;
import de.hm.edu.carads.database.PropertiesLoader;
import de.hm.edu.carads.models.Driver;

public class DriverControllerImpl implements DriverController{

	private List<Driver> list;
	private DatabaseController dbController;
	private Gson gson;
	

	public DriverControllerImpl(){
		PropertiesLoader pLoader = new PropertiesLoader();
		dbController = new DatabaseControllerImpl(pLoader.getPropertyString("DB_HOST"), Integer.parseInt(pLoader.getPropertyString("DB_PORT")), pLoader.getPropertyString("DB_NAME"));
		gson = new Gson();
	}
	
	@Override
	public Collection<Driver> getDrivers() {
		
		return new ArrayList<Driver>();
	}

	@Override
	public Driver getDriver(String id) {
		BasicDBObject dbObj = dbController.getEntity(Driver.class, id);
		return makeDriverFromBasicDBObject(dbObj);
	}

	@Override
	public Driver addDriver(Driver driver) {
		BasicDBObject dbObj = dbController.addEntity(Driver.class, BasicDBObject.parse(gson.toJson(driver)));

		return makeDriverFromBasicDBObject(dbObj);
	}

	@Override
	public void deleteDriver(String id) {
		list.remove(0);
	}

	@Override
	public Collection<Driver> getDrivers(int startAt, int length) {
		List<DBObject> drivers = dbController.getEntities(Driver.class);
		List<Driver> smallList = new ArrayList<Driver>();
		
		Iterator<DBObject> it = drivers.iterator();
		
		while(it.hasNext()){
			smallList.add(makeDriverFromBasicDBObject((BasicDBObject)it.next()));
		}
		
		return smallList;
	}

	@Override
	public int getDriverCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public boolean isIDInList(String id){
		Iterator<Driver> it = list.iterator();
		while(it.hasNext()){
			if(it.next().getId().equals(id))
				return true;
		}
		return false;
	}
	
	public boolean isEmailInList(String email){
		Iterator<Driver> it = list.iterator();
		while(it.hasNext()){
			if(it.next().getEmail().equals(email))
				return true;
		}
		return false;
	}
	
	public String makeNewId(String email){
		try {
			byte[] bytesOfMessage = email.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytesOfMessage);
			return new StringBuffer().append(thedigest).toString();
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	@Override
	public Driver changeDriver(Driver driver) {
		if(!isIDInList(driver.getId())){
			return null;
		}
		Iterator<Driver> it = list.iterator();
		
		
		return driver;
	}
	@Override
	public boolean existDriver(String email) {
		if(dbController.existEntityByEmail(Driver.class, email))
			return true;
		
		return false;
	}

	private Driver makeDriverFromBasicDBObject(BasicDBObject dbObj){
		
		Driver driver = gson.fromJson(dbObj.toJson(), Driver.class);
		driver.setId(dbObj.getString("_id"));
		return driver;
	}
}
