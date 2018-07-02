package io.lzz.client.dto;

import io.lzz.client.constant.BusinessMsg;
import io.lzz.client.constant.BusinessStatus;



public class BaseResponse {
	
	private int status = BusinessStatus.OK;
	private String msg = BusinessMsg.REQUEST_SUCCESS;
	private ResponseData data;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public ResponseData getData() {
		return data;
	}
	public void setData(ResponseData data) {
		this.data = data;
	}
}
