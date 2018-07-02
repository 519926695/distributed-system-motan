package io.lzz.common.utils;

import java.math.BigDecimal;

/**
 * 
 * 
 * @ClassName ParamUtil
 * @Description sql查询变量工资
 * @author longzanzheng
 * @dateTime 2017年11月23日 下午9:15:25
 *
 */
public class ParamUtil {
	/**
	 * 
	 * @Title likeParam
	 * @Description 模糊查询处理
	 * @param value
	 * @return
	 */
	public static String likeParam(String value) {
		String name = value.toLowerCase();
		name = name.replace("%", "\\%");
		name = name.replace("_", "\\_");
		name = "%" + name + "%";
		return name;
	}
}
