package de.hm.edu.carads.models.util;

import static org.junit.Assert.*;

import org.junit.Test;

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
}
