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

import de.hm.edu.carads.models.Driver;

public class DriverControllerImpl implements DriverController{

	private List<Driver> list;
	public DriverControllerImpl(){
		list = new ArrayList<Driver>();
		list.add(new Driver("b34b41a1-2c46-4f26-ad1c-90f57d84e855", "ben.k@hm.edu", "Benni", "Keckes", "09.06.1988"));
		list.add(new Driver("3asd2", "fs@hm.edu", "Flo", "Schaeffer", "19.03.1992"));
		list.add(new Driver("gdfhj", "asd.da@asd.de", "EE", "GTT", "19.03.1992"));
		list.add(new Driver("a34b41a1-2c46-4f26-ad1c-90f57d84e855", "narf@mg.com", "FFF", "GTT", "19.03.1992"));
	}
	@Override
	public Collection<Driver> getDrivers() {
		return new ArrayList<Driver>();
	}

	@Override
	public Driver getDriver(String id) {
		Iterator<Driver> it = list.iterator();
		
		while(it.hasNext()){
			Driver d = it.next();
			if(d.getId().equals(id))
				return d;
		}
		return null;
	}

	@Override
	public Driver addDriver(Driver driver) {
		
		if(!isEmailInList(driver.getEmail())){
			if(driver.setId(UUID.randomUUID().toString()))
				return driver;	
		}
		throw new WebApplicationException(409);
	}

	@Override
	public void deleteDriver(String id) {
		list.remove(0);
	}

	@Override
	public Collection<Driver> getDrivers(int startAt, int length) {
		ArrayList<Driver> smallList = new ArrayList<Driver>();
		Iterator<Driver> it = list.iterator();
		int i = 0;
		while(it.hasNext()){
			smallList.add(it.next());
			
			if(++i>=length)
				break;
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

}
