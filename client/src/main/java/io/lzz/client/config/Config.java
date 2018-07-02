package io.lzz.client.config;

import com.jfinal.config.*;

import io.lzz.client.interceptor.OptionsInterceptor;
import io.lzz.common.utils.SlfLogService;


/**
 * API引导式配置
 */
public class Config extends JFinalConfig {

	/**
	 * 配置常量
	 */
	@Override
	public void configConstant(Constants me) {
		//设置文件最大上传50MB
		me.setMaxPostSize(50 * 1024 * 2014);
		// 加载少量必要配置，随后可用getProperty(...)获取值
		loadPropertyFile("config.properties");
		String tt=this.getProperty("test");
		System.out.println("configConstant========="+tt);
	}

	/**
	 * 配置路由
	 */
	@Override
	public void configRoute(Routes me) {
	}

	@Override
	public void afterJFinalStart() {
		init();
		SlfLogService.info("[ec-station project start success!!!]");
		super.afterJFinalStart();
	}

	/**
	 * 初始化
	 */
	public void init() {
		initJvm();
		SlfLogService.info("init success.");
	}

	/**
	 * 初始化JVM
	 */
	private void initJvm() {
		SlfLogService.info("init Jvm success.");
	}


	@Override
	public void configHandler(Handlers arg0) {
	}

	@Override
	public void configInterceptor(Interceptors arg0) {
		//添加拦截器
		arg0.add(new OptionsInterceptor());
	}

	@Override
	public void configPlugin(Plugins arg0) {
	}

}
