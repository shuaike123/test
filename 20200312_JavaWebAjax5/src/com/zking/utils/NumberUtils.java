package com.zking.utils;

/**
 * 	数字处理类
 * @author 我爱陈果果
 *
 */
public class NumberUtils {

	/**
	 * 	将字符串的数字转化成正数int
	 * @param numStr 字符串的数字
	 * @return 转化后的数，如果是0，证明转化异常
	 */
	public static int parseStr(String numStr) {
		int num = 0;
		try {
			//可能会出现异常
			num = Integer.parseInt(numStr);
		} catch (Exception e) {
			System.out.println("字符串转化异常："+e.getMessage());
		}
		return num;
	}
	
}
