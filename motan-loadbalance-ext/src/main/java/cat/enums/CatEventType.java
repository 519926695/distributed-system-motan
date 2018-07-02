package cat.enums;

public enum CatEventType {

	PROVIDER("RpcProvider","Rpc服务端"),
	PROVIDER_HOST_PORT("RpcProviderServer","Rpc服务端"),
	REFERER("RpcReferer","Rpc客户端"),
	REFERER_REQUEST("RpcRefererRequest","Rpc客户端请求"),
	REFERER_CALL("RpcRefererCall","Rpc客户端调用"),
	REFERER_CALL_SERVER("RpcRefererCallServer","Rpc客户端调用的Server");
	
	private String type;
	
	private String name;

	private CatEventType(String type, String name) {
		this.type = type;
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}
	
}
