package codec;

import java.util.Map;

public class RequestProtocol {

	
	protected long requestId;
	
	protected String interfaceName;
	
	protected String methodName;
	
	protected String paramtersDesc;
	
	protected Object[] arguments;
	
	protected Map<String, String> attachments;
}
