package io.lzz.uc.config;

import com.alibaba.fastjson.JSON;
import io.lzz.common.exception.MyErrorCode;
import io.lzz.common.exception.MyException;
import io.lzz.common.utils.SlfLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
public class AspectJAdvice {

	@Pointcut("execution(* io.lzz.uc.facadeimpl..*(..))")
	private void aspectJMethod() {
	};

	@Before(value = "aspectJMethod()")
	public void doBefore(JoinPoint joinPoint) {
		SlfLogService.info("请求{}参数:{}", joinPoint.getSignature().getName(), JSON.toJSONString(joinPoint.getArgs()));
	}

	@AfterReturning(value = "aspectJMethod()", returning = "rvt")
	public void doAfter(JoinPoint joinPoint, Object rvt) {
		SlfLogService.info("方法{}返回:{}", joinPoint.getSignature().getName(), JSON.toJSONString(rvt));
	}

	@AfterThrowing(value = "aspectJMethod()", throwing = "e")
	public void doThrowing(JoinPoint joinPoint, Exception e) throws MyException {
		if (e instanceof MyException) {
			MyException ec = (MyException) e;
			throw ec;
		} else {
			SlfLogService.error("runError", e);
			throw new MyException(MyErrorCode.SERVER, e.getMessage());
		}
	}

}
