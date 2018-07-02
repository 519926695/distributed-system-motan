package io.lzz.rpc.config.dev;

import com.weibo.api.motan.config.ProtocolConfig;
import com.weibo.api.motan.config.springsupport.ProtocolConfigBean;
import com.weibo.api.motan.config.springsupport.RegistryConfigBean;
import com.zz.common.util.PropertiesLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.annotation.Order;

import java.util.Properties;

@Configuration
@Order(value=1)
public class CommonConfig {

	
	//所有config共享
	@Bean(name="client_zookeeper")
	public RegistryConfigBean registry(){
		RegistryConfigBean registryConfigBean = new RegistryConfigBean();
		//读取配置文件
		String conf="zookeeper.properties";
		Properties properties = PropertiesLoader.loadProperties(conf);
		String address=null;
		if(properties!=null){
			address=properties.getProperty("address");
		}
		if(address==null)
		{
			address="127.0.0.1:2181";//默认使用测试环境的zookeeper
		}
		registryConfigBean.setAddress(address);
		
		registryConfigBean.setBeanName("client_zookeeper");
		registryConfigBean.setRegProtocol("zookeeper");
		return registryConfigBean;
	}
	
	/**
	 * 不使用扩展的一致性hash，采用默认的分布式算法(因为是无状态请求)
	 * @return
	 */
	@Bean(name="client_protocol")
	@Order(value=1)
	public ProtocolConfig protocolConfig(){
		ProtocolConfigBean protocolConfigBean = new ProtocolConfigBean();
		
		protocolConfigBean.setId("client_protocol");
		protocolConfigBean.setName("motan");
		protocolConfigBean.setCodec("proto-codec");
		protocolConfigBean.setDefault(true);
		protocolConfigBean.setEndpointFactory("single-endpoint");
//		protocolConfigBean.setFilter("cat");
		
		return protocolConfigBean;
	}
}
