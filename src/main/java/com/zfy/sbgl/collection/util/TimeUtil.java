package com.zfy.sbgl.collection.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xiangzy
 * @date 2015-9-13
 *
 */
public class TimeUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static Timestamp parse (String str){ 
		Date date;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace(); 
			return null;
		}
		return new Timestamp(date.getTime());
	}
	
	public static String format(Timestamp time){
		return sdf.format(new Date(time.getTime()));
	}
	
	public static String getCurrTimeStr(){
		return sdf.format(new Date(System.currentTimeMillis()));
		
	}
}
