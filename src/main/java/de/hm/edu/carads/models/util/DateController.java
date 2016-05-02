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
		if(a.isEmpty())
			a="01.01.1970";
		if(b.isEmpty())
			b="01.01.1970";
		
		Date date1 = DateController.fromStringToDate(a);
		Date date2 = DateController.fromStringToDate(b);
		if(date1 == null || date2 == null)
			throw new IllegalArgumentException();
		
		if(date1.compareTo(date2)<0)
			return true;
		return false;
	}
	
	public static boolean isAAfterB(String a, String b){
		Date date1 = DateController.fromStringToDate(a);
		Date date2 = DateController.fromStringToDate(b);
		if(date1 == null || date2 == null)
			throw new IllegalArgumentException();
		
		if(date1.compareTo(date2)>0)
			return true;
		return false;
	}
}
