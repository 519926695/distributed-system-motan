package io.lzz.client.listener;

import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.util.MotanSwitcherUtil;
import io.lzz.common.utils.SlfLogService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 资源管理中心
 * @author yuxiaowei
 * 
 */
public class ContextListener implements ServletContextListener {

	/**
	 * @param sce
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		SlfLogService.info("webapp server is stop!!!");
	}

	@Override
	public void contextInitialized(ServletContextEvent se) {
		//启动spring容器
		String[] path = new String[] { "classpath*:spring_config.xml", "classpath*:motan_client.xml" };

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(path);
		
		MotanSwitcherUtil.setSwitcherValue(MotanConstants.REGISTRY_HEARTBEAT_SWITCHER, true);
		ctx.registerShutdownHook();

		SlfLogService.info("webapp server is started!");
		
	}
}
