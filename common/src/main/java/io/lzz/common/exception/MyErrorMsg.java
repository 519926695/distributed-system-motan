package io.lzz.common.exception;

import io.lzz.common.utils.SlfLogService;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 错误信息获取
 */
public class MyErrorMsg {
	// 设置当前的语言环境
	private static Locale local = Locale.getDefault();
	private static ResourceBundle bundle = ResourceBundle.getBundle(
			"language/myres", local);

	public static String get(Integer key, Object... args) {
		try {
			String msg = bundle.getString(key.toString());
            if (null != args && args.length > 0)
                msg = MessageFormat.format(msg,args);
			return msg;
		} catch (Exception e) {
			SlfLogService.error("ErrorMsg.get not have key={}" + key);
		}
		return "";
	}

	public static String get(Integer key, String lang,Object... args) {
		Locale loLang = new Locale(lang);

		ResourceBundle bundle2 = ResourceBundle.getBundle("language/myres",
				loLang);
		try {
            String msg = bundle2.getString(key.toString());
            if (null != args && args.length > 0)
                msg = MessageFormat.format(msg,args);
			return msg;
		} catch (Exception e) {
			SlfLogService.error("ErrorMsg.get not have key={}" + key);
		}
		return "";
	}

	public static String get(String key, String lang,Object... args) {
		Locale loLang = new Locale(lang);

		ResourceBundle bundle2 = ResourceBundle.getBundle("language/myres",
				loLang);
		try {
			String msg = bundle2.getString(key.toString());
			if (null != args && args.length > 0)
				msg = MessageFormat.format(msg,args);
			return msg;
		} catch (Exception e) {
			SlfLogService.error("ErrorMsg.get not have key={}" + key);
		}
		return "";
	}

	public static void main(String[] args) {
		System.out.println(get(10001, "fr",null));
	}
}
