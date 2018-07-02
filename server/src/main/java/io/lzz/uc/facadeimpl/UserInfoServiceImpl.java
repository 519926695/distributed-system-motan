package io.lzz.uc.facadeimpl;

import io.lzz.rpc.uc.api.IUserInfoService;
import io.lzz.rpc.uc.dto.LogInRequest;
import io.lzz.rpc.uc.dto.LogInResponse;
import org.springframework.beans.factory.annotation.Autowired;

public class UserInfoServiceImpl implements IUserInfoService{

	@Override
	public LogInResponse login(LogInRequest request) throws Exception {
		LogInResponse response = new LogInResponse();
		return response;
	}
}
 