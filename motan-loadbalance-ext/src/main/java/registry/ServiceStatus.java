package registry;



/**
 * 
 * @author Administrator
 *
 */
public class ServiceStatus {

	private static final String SERVICE_STATUS_PATH = "service_status";
	
	public static final String toStatusPath(String servicePath){
		return   servicePath+"/"+SERVICE_STATUS_PATH;
		
	}
	
	public static final String toServiceStatusPath(String statusPath,String nodeName){
		return   statusPath+"/"+nodeName;
	}
}
