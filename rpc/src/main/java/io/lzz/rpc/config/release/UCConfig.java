package io.lzz.rpc.config.release;

import io.lzz.rpc.uc.api.IUserAddressService;
import io.lzz.rpc.uc.api.IUserInfoService;
import io.lzz.rpc.utils.Constant;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.weibo.api.motan.config.springsupport.BasicRefererConfigBean;
import com.weibo.api.motan.config.springsupport.RefererConfigBean;

@Configuration
public class UCConfig {
	@Bean(name = "ucMotanClientBasicConfig")
	public BasicRefererConfigBean baseRefererConfig() {
		BasicRefererConfigBean config = new BasicRefererConfigBean();
		config.setGroup("ec-uc");
		config.setRegistry("client_zookeeper");
		config.setCheck(false);
		config.setAccessLog(false);
		config.setThrowException(true);
		config.setModule("ec-uc");

		config.setProtocol("client_protocol");//不使用扩展的一致性hash

		config.setRequestTimeout(Constant.RPC_REQ_TIMEOUT);//请求超时时间，单位毫秒
		return config;
	}
	
	@Bean(name = "userInfoReffer")
	public RefererConfigBean<IUserInfoService> userInfoRefererConfig() {
		RefererConfigBean<IUserInfoService> config = new RefererConfigBean<IUserInfoService>();
		config.setInterface(IUserInfoService.class);
		config.setBasicReferer(baseRefererConfig());
		return config;
	}
	
	@Bean(name = "userAddressReffer")
	public RefererConfigBean<IUserAddressService> categoryRefererConfig() {
		RefererConfigBean<IUserAddressService> config = new RefererConfigBean<IUserAddressService>();
		config.setInterface(IUserAddressService.class);
		config.setBasicReferer(baseRefererConfig());
		return config;
	}
}
