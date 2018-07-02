package registry;

public class RegistryVersion {

	
	private static final String SERVICE_VERSION_PATH = "service_version";
	
	/**
	 * 
	 * @param servicePath 服务路径
	 * @return
	 */
	public static final String toNewVersionPath(String servicePath){
		return   servicePath+"/"+SERVICE_VERSION_PATH;
		
	}
	
	/**
	 * 注册中心版本路径
	 * @param versionPath
	 * @param nodeName
	 * @return
	 */
	public static final String toNewServiceVersionPath(String versionPath,String nodeName){
		return   versionPath+"/"+nodeName;
	}
	
}
