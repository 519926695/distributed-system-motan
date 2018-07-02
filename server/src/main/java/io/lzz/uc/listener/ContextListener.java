package io.lzz.uc.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.weibo.api.motan.log.LogService;
import io.lzz.common.utils.SlfLogService;
import org.springframework.context.support.ClassPathXmlApplicationContext;



import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.util.MotanSwitcherUtil;

/**
 * 资源管理中心
 * @author yuxiaowei
 * 
 */
public class ContextListener implements ServletContextListener {

	/**
	 * @param sce
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		SlfLogService.info("webapp server is stop!!!");
	}

	public void contextInitialized(ServletContextEvent se) {
		//启动spring容器
		//local test
//		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
//				new String[] {"classpath*:motan_service.xml","classpath*:test_motan_client.xml"});
		
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] {"classpath*:motan_client.xml","classpath*:motan_service.xml"});
		
		MotanSwitcherUtil.setSwitcherValue(MotanConstants.REGISTRY_HEARTBEAT_SWITCHER, true);
		ctx.registerShutdownHook();
		SlfLogService.info("webapp server is started!");
		
	}
}
