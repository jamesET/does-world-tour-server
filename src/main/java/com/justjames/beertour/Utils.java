package com.justjames.beertour;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public final class Utils {

	static final public ZoneId appTimeZoneId = ZoneId.of("UTC");
	
	static final public SimpleDateFormat date2ZoneFmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	static final public SimpleDateFormat zone2DateFmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	public Utils() {
	}
	
	static final public String utcDateStr(Date date) {
		String dateStr = date2ZoneFmt.format(date);
		ZonedDateTime zdt = ZonedDateTime.parse(dateStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		return zdt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME); 
	}
	
	static final public Date utcNow() {
		ZonedDateTime zonedNow = ZonedDateTime.now(appTimeZoneId);
		Date now = null;
		try {
			now = zone2DateFmt.parse(zonedNow.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return now;
	}

	static final public String getUTC() {
		ZonedDateTime now = ZonedDateTime.now(appTimeZoneId);
		return now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME); 
	}

}
