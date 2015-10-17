package com.salama.workflow.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {
	private static SimpleDateFormat DATE_FORMAT_YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
	
	public static String formatDateYYYYMMDD(Date date) {
		return DATE_FORMAT_YYYYMMDD.format(date);
	}
}
