package io.lzz.common.exception;

/**
 * 电商业务异常
 * 
 * @author xw
 *
 */
public class MyException extends Exception {
	private static final long serialVersionUID = 1L;
	private int errorCode;
	private String msg;
	private Object[] args;

	/**
	 *
	 * @param errorCode
	 * @param e
	 */
	public MyException(io.lzz.common.exception.MyErrorCode errorCode, Throwable e) {
		this(errorCode, null, e);
	}

	/**
	 *
	 * @param errorCode
	 * @param msg
	 */
	public MyException(io.lzz.common.exception.MyErrorCode errorCode, String msg) {
		this(errorCode, msg, null);
	}

	/**
	 *
	 * @param errorCode
	 * @param msg
	 * @param args
	 *            动态提示参数,例:价格必须大于{0},参数传50得价格必须大于50
	 */
	public MyException(io.lzz.common.exception.MyErrorCode errorCode, String msg, Object... args) {
		this(errorCode, msg, null, args);
	}

	/**
	 *
	 * @param errorCode
	 * @param msg
	 * @param e
	 * @param args
	 *            动态提示参数,例:价格必须大于{0},参数传50得价格必须大于50
	 */
	public MyException(io.lzz.common.exception.MyErrorCode errorCode, String msg, Throwable e,
					   Object... args) {
		super("ErrCode:" + errorCode + ",msg=" + msg, e);
		this.errorCode = errorCode.getValue();
		this.msg = msg;
		this.args = args;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public int getCode(){
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public void setErrorCode(io.lzz.common.exception.MyErrorCode errorCode) {
		this.errorCode = errorCode.getValue();
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
