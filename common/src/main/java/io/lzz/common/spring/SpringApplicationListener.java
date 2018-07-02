package io.lzz.common.spring;

import io.lzz.common.utils.SlfLogService;
import io.lzz.common.utils.SpringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import io.lzz.common.spring.ApplicationStartCompelete;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.annotation.Order;


public class SpringApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		ApplicationContext applicationContext = event.getApplicationContext();
		
		SpringUtil.setApplicationContext(applicationContext);
		
		Map<String, ApplicationStartCompelete> callbackMap= applicationContext.getBeansOfType(ApplicationStartCompelete.class);
		
		if(callbackMap != null && callbackMap.size() > 0){
			List<ApplicationStartCompelete> callbacks =new ArrayList<>(callbackMap.values());
			
			Collections.sort(callbacks, new Comparator<ApplicationStartCompelete>() {
				
				@Override
				public int compare(ApplicationStartCompelete o1, ApplicationStartCompelete o2) {
					
					Order order1 = o1.getClass().getAnnotation(Order.class);
					Order order2 = o2.getClass().getAnnotation(Order.class);
					
					int value1 = Integer.MIN_VALUE;
					int value2 = Integer.MIN_VALUE;
					if(order1 != null ){
						value1 = order1.value();
					}
					if(order2 != null){
						value2 = order2.value();
					}
					
					return value2-value1;
				}
			});
			
			for(ApplicationStartCompelete callback:callbacks){
				
				SlfLogService.debug("callback {} compeleteCallback",callback.getClass().getName());
				try{
					callback.compeleteCallback();
				}catch(Throwable e){
					SlfLogService.error("{}.compeleteCallback failed",callback.getClass(),e);
				}
			}
		}
	}

}
