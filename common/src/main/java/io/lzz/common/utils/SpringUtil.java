package io.lzz.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;

public class SpringUtil {

	
	private static ApplicationContext ctx;
	
	public static void setApplicationContext(ApplicationContext ctx){
		SpringUtil.ctx = ctx;
	}
	
	public static <T> T getBean(Class<T> clazz){
		
		if(clazz == null || ctx == null){
			return null;
		}
		
		return ctx.getBean(clazz);
	}
	
	public static Object getBean(String name){
		if(name == null || "".equals(name) || ctx==null){
			return null;
		}
		return ctx.getBean(name);
	}
	
	public static <T>  Map<String, T> getBeans(Class<T> clazz){
		if(clazz == null || ctx == null){
			return new HashMap<String, T>();
		}
		return ctx.getBeansOfType(clazz);
	}
}
