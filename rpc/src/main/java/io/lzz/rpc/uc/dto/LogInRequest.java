package io.lzz.rpc.uc.dto;

public class LogInRequest {
	private String email;
	private String password;//已MD5的密码
	private int type;
	private String thirdToken;
	private String local;
	
	/**
	 * 第三方登录若未注册时使用
	 */
	private int sellerBrandId;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getThirdToken() {
		return thirdToken;
	}
	public void setThirdToken(String thirdToken) {
		this.thirdToken = thirdToken;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public int getSellerBrandId() {
		return sellerBrandId;
	}
	public void setSellerBrandId(int sellerBrandId) {
		this.sellerBrandId = sellerBrandId;
	}
}
