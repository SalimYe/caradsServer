package de.hm.edu.carads.models.util;

import static org.junit.Assert.*;

import org.junit.Test;

import de.hm.edu.carads.controller.util.DateController;

/**
 * Die Datumslogik wird geprueft.
 * Dazu zaehlt das konvertieren von eingegebenen Daten und der Vergleich von Daten.
 * @author BK
 *
 */
public class DateControllerTest {

	@Test
	public void test1() {
		String date1 = "01.01.2000";
		String date2 = "20.01.2000";
		assertTrue(DateController.isABeforeB(date1, date2));
	}
	
	@Test
	public void test2() {
		String date1 = "01.01.2000";
		String date2 = "20.01.2000";
		assertFalse(DateController.isABeforeB(date2, date1));
	}
	
	@Test
	public void campaignOverlappingTest1(){
		/*
		 *	|---|			A
		 *			|---|	B 
		 */
		TimeFrame a = new TimeFrame("01.01.2000", "10.01.2000");
		TimeFrame b = new TimeFrame("15.01.2000", "30.02.2000");
		assertFalse(DateController.areTimesOverlapping(a, b));
	}
	
	@Test
	public void campaignOverlappingTest5(){
		/*
		 *			|---|	A
		 *		|---------|	B 
		 */
		TimeFrame a = new TimeFrame("01.05.2000", "10.05.2000");
		TimeFrame b = new TimeFrame("15.01.2000", "30.08.2000");
		assertTrue(DateController.areTimesOverlapping(a, b));
	}
	
	@Test
	public void campaignOverlappingTest2(){
		/*
		 *	|--------|		A
		 *			|---|	B 
		 */
		TimeFrame a = new TimeFrame("01.01.2000", "17.01.2000");
		TimeFrame b = new TimeFrame("15.01.2000", "30.02.2000");
		assertTrue(DateController.areTimesOverlapping(a, b));
	}
	
	@Test
	public void campaignOverlappingTest3(){
		/*
		 *	|-------------|	A
		 *			|---|	B 
		 */
		TimeFrame a = new TimeFrame("01.01.2000", "17.03.2000");
		TimeFrame b = new TimeFrame("15.01.2000", "30.02.2000");
		assertTrue(DateController.areTimesOverlapping(a, b));
	}
	
	@Test
	public void campaignOverlappingTest4(){
		/*
		 *			|----|	A
		 *	|---|			B 
		 */
		TimeFrame a = new TimeFrame("01.01.2001", "17.01.2001");
		TimeFrame b = new TimeFrame("15.01.2000", "30.02.2000");
		assertFalse(DateController.areTimesOverlapping(a, b));
	}
	
	@Test
	public void campaignOverlappingTest6(){
		/*
		 *	|---|			A
		 *		|---|	B 
		 */
		TimeFrame a = new TimeFrame("01.01.2000", "10.01.2000");
		TimeFrame b = new TimeFrame("10.01.2000", "30.02.2000");
		assertFalse(DateController.areTimesOverlapping(a, b));
	}
	
	@Test
	public void campaignOverlappingTest7(){
		/*
		 *			|---|	A
		 *		|---|		B 
		 */
		TimeFrame a = new TimeFrame("30.02.2000", "10.05.2000");
		TimeFrame b = new TimeFrame("10.01.2000", "30.02.2000");
	
		assertFalse(DateController.areTimesOverlapping(a, b));
	}
}
