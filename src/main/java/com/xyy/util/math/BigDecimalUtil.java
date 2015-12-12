package com.xyy.util.math;

import java.math.BigDecimal;

public class BigDecimalUtil {
	/**
	 * 四舍五入保留
	 * @param original 原始值
	 * @param scale 小数位数
	 * @return
	 */
	public static BigDecimal formatHalfDown(BigDecimal original,int scale){
		if(original==null){
			throw new RuntimeException("format BigDecimal can not be null");
		}
		BigDecimal target=original.setScale(scale, BigDecimal.ROUND_HALF_UP);
		return target;
	}
}
