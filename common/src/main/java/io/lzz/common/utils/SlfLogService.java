package io.lzz.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 日志服务
 * 
 * @author yxw
 * 
 */
public class SlfLogService {
	private final static String LOGGER_FAIL = "failLogger";
	private final static String LOGGER_DEBUG = "debugLogger";
	private final static String LOGGER_INFO = "infoLogger";
	
	private static boolean debug=true;

	
	
	public static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) {
		SlfLogService.debug = debug;
	}

	/* error */
	public static void error(String message) {
		Logger logger = LoggerFactory.getLogger(LOGGER_FAIL);
		// logger.error(message);
		logger.error(message);
	}
	/**
	 * 通过占位符动态拼接logmsg
	 * @param message	比如：XXX {}。 使用{}占位符。避免字符串连接操作，减少String对象（不可变）带来的内存开销 
	 * @param arg1		与占位符对应的参数,最后一个参数可以是异常对象，即可打印堆栈
	 */
	public static void error(String message,Object... arg1 ) {
		Logger logger = LoggerFactory.getLogger(LOGGER_FAIL);
		logger.error(message,arg1);
		if(arg1 != null && arg1.length > 0 && arg1[arg1.length-1] instanceof Throwable){
//			if(Cat.getManager().isCatEnabled()){
//				Cat.logError((Throwable) arg1[arg1.length-1]);
//			}
		}
	}
	public static void error(Throwable e) {
		Logger logger = LoggerFactory.getLogger(LOGGER_FAIL);
		logger.error("", e);
//		if(Cat.getManager().isCatEnabled()){
//			Cat.logError(e);
//		}
	}

	public static void error(String message, Throwable e) {
		Logger logger = LoggerFactory.getLogger(LOGGER_FAIL);
		logger.error(message, e);
//		if(Cat.getManager().isCatEnabled()){
//			Cat.logError(e);
//		}
	}
	/* debug */
	public static void debug(String message) {
		if(debug)
		{
			Logger logger = LoggerFactory.getLogger(LOGGER_DEBUG);
			logger.debug(message);
		}
	}
	/**
	 * 通过占位符动态拼接logmsg
	 * @param message	比如：XXX {}。 使用{}占位符。避免字符串连接操作，减少String对象（不可变）带来的内存开销 
	 * @param arg1		与占位符对应的参数
	 */
	public static void debug(String message,Object... arg1 ) {
		if(debug)
		{
			Logger logger = LoggerFactory.getLogger(LOGGER_DEBUG);
			logger.debug(message,arg1);
		}
	}
	public static void debug(String message, Throwable e) {
		if(debug)
		{
			Logger logger = LoggerFactory.getLogger(LOGGER_DEBUG);
			logger.debug(message, e);
		}
		
//		if(isflumelog){
//			String logmessage = "message:" + message + " "
//					+ CommonUtil.getExceptionStackStr(e);
//			logger.debug(logmessage);
//		}else
			
	}

	/* info */
	public static void info(String message) {
		Logger logger = LoggerFactory.getLogger(LOGGER_INFO);
		logger.info(message);
	}
	/**
	 * 通过占位符动态拼接logmsg
	 * @param message	比如：XXX {}。 使用{}占位符。避免字符串连接操作，减少String对象（不可变）带来的内存开销 
	 * @param arg1		与占位符对应的参数
	 */
	public static void info(String message,Object... arg1 ) {
		Logger logger = LoggerFactory.getLogger(LOGGER_INFO);
		logger.info(message,arg1);
	}
	public static void infoByLogger(String loggername, String message,Object... arg1) {
		Logger logger = LoggerFactory.getLogger(loggername);
		logger.info(message);
	}

	

	public static void main(String[] args) {
//		for (int i = 0; i < 2; i++) {
//			flumeCollection("functionstat", "test");
//		}
		debug("fdafdasfa{}fdsf",23234,new NullPointerException());
	}
}
