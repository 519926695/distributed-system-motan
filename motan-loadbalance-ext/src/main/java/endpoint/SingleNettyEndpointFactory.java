package endpoint;

import com.weibo.api.motan.extension.SpiMeta;
import com.weibo.api.motan.rpc.URL;
import com.weibo.api.motan.transport.Client;
import com.weibo.api.motan.transport.MessageHandler;
import com.weibo.api.motan.transport.Server;
import com.weibo.api.motan.transport.netty.NettyServer;
import com.weibo.api.motan.transport.support.AbstractEndpointFactory;

@SpiMeta(name="single-endpoint")
public class SingleNettyEndpointFactory extends AbstractEndpointFactory {

	@Override
	protected Server innerCreateServer(URL url, MessageHandler messageHandler) {
		return new NettyServer(url, messageHandler);
	}

	@Override
	protected Client innerCreateClient(URL url) {
		return new SingleNettyClient(url);
	}

}
