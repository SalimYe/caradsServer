package de.hm.edu.carads.models.util;

import static org.junit.Assert.*;

import org.junit.Test;

import de.hm.edu.carads.models.Campaign;

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
	public void test3() {
		String date1 = "01.01.2000";
		String date2 = "20.01.2000";
		assertTrue(DateController.isAAfterB(date2, date1));
	}

	@Test
	public void test4() {
		String date1 = "01.01.2000";
		String date2 = "20.01.2000";
		assertFalse(DateController.isAAfterB(date1, date2));
	}
	
	@Test
	public void campaignOverlappingTest1(){
		/*
		 *	|---|			A
		 *			|---|	B 
		 */
		
		TimeFrame a = new TimeFrame();
		a.start = "01.01.2000";
		a.end = "10.01.2000";
		TimeFrame b = new TimeFrame();
		b.start = "15.01.2000";
		b.end = "30.02.2000";
		
		assertFalse(DateController.isAOverlappingB(a, b));
		
	}
	
	@Test
	public void campaignOverlappingTest2(){
		/*
		 *	|--------|		A
		 *			|---|	B 
		 */
		
		TimeFrame a = new TimeFrame();
		a.start = "01.01.2000";
		a.end = "17.01.2000";
		TimeFrame b = new TimeFrame();
		b.start = "15.01.2000";
		b.end = "30.02.2000";
		
		assertTrue(DateController.isAOverlappingB(a, b));
		
	}
	
	@Test
	public void campaignOverlappingTest3(){
		/*
		 *	|-------------|	A
		 *			|---|	B 
		 */
		
		TimeFrame a = new TimeFrame();
		a.start = "01.01.2000";
		a.end = "17.03.2000";
		TimeFrame b = new TimeFrame();
		b.start = "15.01.2000";
		b.end = "30.02.2000";
		
		assertTrue(DateController.isAOverlappingB(a, b));
		
	}
	
	@Test
	public void campaignOverlappingTest4(){
		/*
		 *			|----|	A
		 *	|---|			B 
		 */
		
		TimeFrame a = new TimeFrame();
		a.start = "01.01.2000";
		a.end = "17.01.2000";
		TimeFrame b = new TimeFrame();
		b.start = "15.01.2000";
		b.end = "30.02.2000";
		
		assertFalse(DateController.isAOverlappingB(b, a));
		
	}
}
