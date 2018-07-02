package io.lzz.common.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

	/**
	 * 获取某时区的当前时间
	 * @param timezone
	 * @return
	 */
	public static Date getCurrentDateTime(int timezone){
		TimeZone defaultTimeZone = TimeZone.getTimeZone("GMT+08:00");

		TimeZone timeZone = getTimeZone(timezone);
		if(timeZone == null){
			timeZone = defaultTimeZone;
		}
		
		long time = Calendar.getInstance(defaultTimeZone).getTimeInMillis();
		Date now = new Date(time+timeZone.getRawOffset()-defaultTimeZone.getRawOffset());
		return now;
	}
	
	/**
	 * 获取某时区的当前时间
	 * @param timezone
	 * @return
	 */
	public static Date getCurrentDateTime(String timezone){
		TimeZone defaultTimeZone = TimeZone.getDefault();

		TimeZone timeZone = TimeZone.getTimeZone(timezone);
		
		long time = Calendar.getInstance(defaultTimeZone).getTimeInMillis();
		Date now = new Date(time+timeZone.getRawOffset()-defaultTimeZone.getRawOffset());
		return now;
	}
	
	/**
	 * 
	 * @param timeZone
	 * @return
	 */
	public static Date getCurrentDateTime(TimeZone timeZone){
		TimeZone defaultTimeZone = TimeZone.getDefault();

		if(timeZone == null){
			timeZone = defaultTimeZone;
		}
		long time = Calendar.getInstance(defaultTimeZone).getTimeInMillis();
		Date now = new Date(time+timeZone.getRawOffset()-defaultTimeZone.getRawOffset());
		return now;
	}
	
	/**
	 * 获取时区
	 * @param timezone
	 * @return
	 */
	public static TimeZone getTimeZone(int timezone){
		StringBuilder timezoneID = new StringBuilder();
		if(timezone > 0){
			timezone = Math.min(timezone, 12);
		}else{
			timezone = Math.max(timezone, -12);
		}
		
		StringBuilder timezoneStr = new StringBuilder();
		timezoneStr.append(Math.abs(timezone)<10?"0"+Math.abs(timezone):Math.abs(timezone)).append(":00");
		
		timezoneID.append("GMT").append(timezone>=0?"+":"-").append(timezoneStr);
		TimeZone timeZone = TimeZone.getTimeZone(timezoneID.toString());
		return timeZone;
	}
	
	public static void main(String[] args){
		System.out.println(getCurrentDateTime(0));
	}
}
