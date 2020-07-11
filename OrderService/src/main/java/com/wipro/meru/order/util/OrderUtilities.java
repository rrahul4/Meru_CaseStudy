package com.wipro.meru.order.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderUtilities {

	public static String getCurrentTimeStampInString() {
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMddHHmmss"); 
		return dtFormat.format(new Date());
	}
}
