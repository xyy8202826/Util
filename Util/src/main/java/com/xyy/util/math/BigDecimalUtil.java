package com.xyy.util.math;

import java.math.BigDecimal;
import java.util.Date;

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
	
	public static void main(String[] args) {
		Date begind=new Date();
		long begin=begind.getTime();
		BigDecimal bd=new BigDecimal("12.123456");
		for(int i=0;i<100;i++){
			formatHalfDown(bd, 4);
		}
		Date endd=new Date();
		long end=endd.getTime();
		System.out.println(end-begin);
	}
}
