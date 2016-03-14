package com.justjames.beertour;

import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class UtilsTest {
	
	@Test
	public void testGetUTC() {
		String gmtTime = Utils.getUTC();
		System.out.println(String.format("getUTC : '%s'",gmtTime));
		assertTrue("Not UTC offset! " + gmtTime, gmtTime.endsWith("Z"));
	}
	
	@Test
	public void testUTCNow() {
		Date now = Utils.utcNow();
		SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		System.out.println(String.format("testUTCNow : '%s'",dtf.format(now)));
	}
	
	@Test
	public void testUTCDateStr() {
		Date now = Utils.utcNow();
		System.out.println(String.format("testUTCDateStr : '%s'",Utils.utcDateStr(now)));
	}

}
