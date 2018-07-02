package io.lzz.common.spring;


/**
 * 程序启动完成的回调接口,实现了本接口的spring组件将会在程序启动之后调用compeleteCallback</br>
 * 调用顺序可以使用Order注解在实现本接口的类上,按Order设置的value升序进行调用</br>
 * 没有Order注解的话,顺序默认为最大整数
 */
public interface ApplicationStartCompelete {

	/**
	 * 程序启动完成的回调
	 */
	public void compeleteCallback();
}
