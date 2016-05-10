package de.hm.edu.carads.models.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateController {
	public static final String DATE_FORMAT_METAINFORMATION = "dd.MM.yyyy HH:mm:ss"; 
	public static final String DATE_FORMAT_CAMPAIGNTIME = "dd.MM.yyyy";
	
	private static Date fromStringToDate(String date){
		DateFormat df = new SimpleDateFormat(DATE_FORMAT_CAMPAIGNTIME);
		try {
			return df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
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
