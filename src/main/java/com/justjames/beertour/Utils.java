package com.justjames.beertour;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class Utils {
	
	static final public SimpleDateFormat dt = new SimpleDateFormat("EEE, MMM d, yyyy");

	static final public SimpleDateFormat dttm = new SimpleDateFormat("hh:mm aa EEE, MMM d, yyyy");
	
	static final public Date now() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("CT"));
		return cal.getTime();
	}


}
