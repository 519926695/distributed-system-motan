package io.lzz.rpc.uc.api;

import io.lzz.common.exception.ECException;
import io.lzz.rpc.uc.dto.AccountInfoDto;
import io.lzz.rpc.uc.dto.LogInRequest;
import io.lzz.rpc.uc.dto.LogInResponse;
import io.lzz.rpc.uc.dto.LogOutInfoRequest;
import io.lzz.rpc.uc.dto.ModifyAccountInfoRequest;
import io.lzz.rpc.uc.dto.RegisterRequest;
import io.lzz.rpc.uc.dto.RegisterResponse;
import io.lzz.rpc.uc.dto.ResetPasswordRequest;

import com.zz.common.exception.AppException;

public interface IUserInfoService {
	/**
	 * 登录接口
	 * @param request
	 * @return 用户基础信息
	 * @throws AppException
	 */
	public LogInResponse login(LogInRequest request) throws ECException, Exception;
}