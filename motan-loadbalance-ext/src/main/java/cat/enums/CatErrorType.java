package cat.enums;

public enum CatErrorType {

	MOTAN_FRAMEWORK_EXCEPTION("MotanFrameworkException"),
	MOTAN_SERVICE_EXCEPTION("MotanServiceException"),
	MOTAN_BIZ_EXCEPTION("MotanBizException");
	
	private String name;

	private CatErrorType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
