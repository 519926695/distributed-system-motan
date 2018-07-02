package cat.enums;

public enum MotanNodeType {

	PROVIDER("Provider"),
	REFERER("Referer");
	
	private String name;

	private MotanNodeType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
