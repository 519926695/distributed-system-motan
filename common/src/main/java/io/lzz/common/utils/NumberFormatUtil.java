package io.lzz.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;


public class NumberFormatUtil {

	public static String formatNumberToString(double value) {
		return formatNumberToString(value,2);
	}

	public static float formatNumber(float value) {
		return formatNumber(value,2);
	}

	/**
	 * 数字格式化
	 * @param value
	 * @param size 小数位数
	 * @return
	 */
	public static float formatNumber(float value, int size) {
		if(size >= 0){
			BigDecimal bigDecimal = new BigDecimal(value);
			bigDecimal = bigDecimal.setScale(size,BigDecimal.ROUND_HALF_UP);
			return bigDecimal.floatValue();
		}else{
			return value;
		}
	}
	
	/**
	 * 数字格式化
	 * @param value
	 * @param size 小数位数
	 * @return
	 */
	public static double formatNumber(double value, int size) {
		if(size >= 0){
			BigDecimal bigDecimal = new BigDecimal(value);
			bigDecimal = bigDecimal.setScale(size,BigDecimal.ROUND_HALF_UP);
			return bigDecimal.doubleValue();
		}else{
			return value;
		}
	}
	
	/**
	 * 格式化数字
	 * @param value
	 * @param size
	 * @return
	 */
	public static String formatNumberToString(double value,int size){
		
		StringBuilder format = new StringBuilder("#");
		if(size > 0){
			format.append(".");
			for(int index=0;index<size;index++){
				format.append("#");
			}
		}
		
		DecimalFormat decimalFormat = new DecimalFormat(format.toString());
		
		String numberStr = decimalFormat.format(formatNumber(value, size));
		if(size > 0){
			StringBuilder numberStrBuilder = new StringBuilder(numberStr);

			int dotIndex = numberStr.lastIndexOf('.');
			
			if(dotIndex > 0){
				int bit = numberStr.length() - 1 - dotIndex;
				if(bit < size){
					for(int index=0,surplus=size-bit;index<surplus;index++){
						numberStrBuilder.append("0");
					}
				}
			}else{
				numberStrBuilder.append(".00");
			}
			return numberStrBuilder.toString();
		}
		
		return numberStr;
	}

	public static String doubleTrans(double d){
		if(Math.round(d)-d==0){
			return String.valueOf((long)d);
		}
		return formatNumberToString(d,2);
	}
	
	public static void main(String[] args){
		double f = 0.00599d+0.00599d;
		System.out.println(formatNumberToString(123413423423d,2));
	}
}
