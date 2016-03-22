package de.hm.edu.carads.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MetaInformationController {
	public static String makeDate(){
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return df.format(Calendar.getInstance().getTime());
	}
}
