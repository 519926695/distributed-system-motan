package io.lzz.client.interceptor;


import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import io.lzz.client.constant.BusinessMsg;
import io.lzz.client.constant.BusinessStatus;
import io.lzz.client.dto.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OptionsInterceptor implements Interceptor {

	@Override
	public void intercept(ActionInvocation ai) {
		HttpServletRequest request = ai.getController().getRequest();
		HttpServletResponse response = ai.getController().getResponse();
//		// 跨域问题
//		try {
//			//测试环境打开 开关
//			if(PropKit.getBoolean("test_server", false)){
//				response.addHeader("Access-Control-Allow-Origin", "*");
//			}else{
//				response.addHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
//			}
//
//		} catch (NullPointerException e) {
//			response.addHeader("Access-Control-Allow-Origin", "*");
//		}
//		response.addHeader("Access-Control-Allow-Methods", "*");
//		response.addHeader("Access-Control-Allow-Credentials", "true");

		//OPTIONS请求跳过业务
		if (request.getMethod().toUpperCase().equals("OPTIONS")) {
			BaseResponse resp = new BaseResponse();
			resp.setStatus(BusinessStatus.OK);
			resp.setMsg(BusinessMsg.REQUEST_SUCCESS);
			ai.getController().renderJson(resp);
			return;
		}
		ai.invoke();
	}

}
