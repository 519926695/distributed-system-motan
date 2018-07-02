package booststap;

import io.lzz.uc.cache.abstracts.DbCacheFactory;
import io.lzz.uc.facadeimpl.biz.impl.SignBizImple;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.util.MotanSwitcherUtil;
import com.zz.common.log.LogService;

public class UCStartUp {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] {"classpath*:motan_client.xml","classpath*:motan_service.xml"});
		
		MotanSwitcherUtil.setSwitcherValue(MotanConstants.REGISTRY_HEARTBEAT_SWITCHER, true);
		ctx.registerShutdownHook();
		
		LogService.info("rpc server is started!");
	}
}
