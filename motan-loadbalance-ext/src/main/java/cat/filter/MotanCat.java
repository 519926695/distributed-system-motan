package cat.filter;

import com.dianping.cat.Cat;

public class MotanCat {

	 private static boolean isEnable=true;

	/**
	 * 禁用dubbo cat
	 */
    public static void disable(){
        isEnable=false;
    }

    /**
     * 启用dubbo cat
     */
    public static void enable(){
        isEnable=true;
    }

    /**
     * 是否有效
     * @return
     */
    public static boolean isEnable(){
        return isEnable&&Cat.getManager().isCatEnabled();
    }
}
