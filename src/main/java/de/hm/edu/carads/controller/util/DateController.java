package de.hm.edu.carads.controller.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import de.hm.edu.carads.models.util.TimeFrame;

public class DateController {
	public static final String DATE_FORMAT_METAINFORMATION = "dd.MM.yyyy HH:mm:ss"; 
	public static final String DATE_FORMAT_CAMPAIGNTIME = "yyyy-MM-dd";
	public static final String DATE_FORMAT_CAMPAIGNTIME_ALTERNATIVE = "dd.MM.yyyy";
	
	final static Logger logger = Logger.getLogger(DateController.class);
	
	public static Date fromStringToDate(String date){
		DateFormat df = new SimpleDateFormat(DATE_FORMAT_CAMPAIGNTIME);
		try {
			return df.parse(date.split("T")[0]);
		} catch (ParseException e) {
			System.err.println("Clould not parse " + date + " with Format: "+DATE_FORMAT_CAMPAIGNTIME);
			System.err.println("trying next with Format: "+DATE_FORMAT_CAMPAIGNTIME_ALTERNATIVE);
			//logger.info("First Parsing failed. Trying next");
		}
		
		//second try
		df = new SimpleDateFormat(DATE_FORMAT_CAMPAIGNTIME_ALTERNATIVE);
		try {
			return df.parse(date);
			
		} catch (ParseException e) {
			e.printStackTrace();
			logger.error("last Parsing of " + date + " failed");
		} 
		return null;
	}
	
	
	
	public static boolean isABeforeB(String a, String b){
		if(a==null || a.isEmpty() || b==null || b.isEmpty()){
			throw new IllegalArgumentException();
		}		
		Date date1 = DateController.fromStringToDate(a);
		Date date2 = DateController.fromStringToDate(b);
		
		if(date1.compareTo(date2)<=0)
			return true;
		return false;
	}
	
	public static boolean areTimesOverlapping(TimeFrame a, TimeFrame b){
		
		if(isABeforeB(a.end, b.start))
			return false;
		if(isABeforeB(b.end, a.start))
			return false;

		return true;
	}
}
