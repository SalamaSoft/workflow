package com.salama.workflow.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {
	public static String formatDateYYYYMMDD(Date date) {
		SimpleDateFormat DATE_FORMAT_YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
		return DATE_FORMAT_YYYYMMDD.format(date);
	}
}
