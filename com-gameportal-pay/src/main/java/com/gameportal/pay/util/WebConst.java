package com.gameportal.pay.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 常量类
 * 
 * @author menu
 * @date 20160427
 */
public class WebConst {
	// 用户每天可抽奖最大次数
	public static final Integer LOTTERY_MAX_TIMES = 10;

	public static final String CHARSET = "UTF-8";
	// 捷付私钥
	public static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAPKk6qXvazVBP1jLA2kCMA1lvZNcY0I7Lv+zLi1nFesJRy82rEDArG/R40z2lfES+n4mQBooFnccOzSnl7nBYSAmHalNfO+BlmRQZzcZCovoPBtiXms1ygpZmF1QNRlkOl1BZeZ6O1PncoJwC8RNGkKUwTzXdX3gWIynXVTVggHRAgMBAAECgYAvglrvYpF7O+tLV3SzetotxV35HH3S2gO5zTudaDHCc26cr/fKNe+NdWssBpwVN8IUMSkb9f1wFPcCuE3c8SAy6aoSO4Ny2hAE0PUJO+XVLdsWOM4OwJCUor3Ubp++bp3jngA020SoJP0dzdZ9VVfsFyLN86H7WlScUWejPh18gQJBAPk+Pat/WBA2BjZW6dly7wLpQkWIJZeJAznM08g2umZeZ8WE39V2N4IoPNibNrV4OeXEkBF9MXjSqyEe1oMMui8CQQD5OOD82Y3qna0+k0pbYaVwz0Fp/54QiqjksYnr0g+LBRdmvEK+b38n5fyYT8WJKfxA4mYZ696sJvIAvR+5OgP/AkEAzp80jMnC/Z1rxPyEXIoVbnplQDM/xsIE9JPDNgV4vYihynL/BH0+Qx6MYp0523LRvJMZRAZfTuSGjax89KNGbQJBAONScFXyErKH9Df5nX9Qx9joEuCemgy4jWlcT1NcnXTOxhOdcLvHc5JvA0HisWl1dHOyjjLipI8eTutBlqgbNP0CQDt7eu2bn80ujXcjfIcFnTQ1qlYxapb+eZ7QHQANXGiEUJjj5qubEAk9aRCJy6V5ovzTy51pVSck+MWkuXOXiNI=";
	// 捷付公钥
	public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCd8NpHBMTb4zFU0fh7XjIeofBHs1roEzFcPCgTVi/DKLQ/PKoP+X1XW4HIus24F+TE70xc3k2HMvrTeQi++7GDH9L0E9Y6iZ6uIA5gWNeqddXHyRqouS8DPReM1OnLCkz4b8x6MX/pHIe5KqTuAAvVK9IVOz7bQQexL1cC3AEeyQIDAQAB";
	
	public static List<String> getSubjectList() {
		List<String> list = new ArrayList<>();
		list.add("苹果6s中国风手机套");
		list.add("6plus硅胶防摔壳");
		list.add("手机木质壳木防摔创意定制");
		list.add("iphone6s手机壳挂绳");
		list.add("商务大气板扣平滑皮带");
		list.add("皮带防过敏中青年腰带");
		list.add("腰带青年牛皮裤带");
		list.add("针扣皮带 ");
		list.add("帆布腰带");
		list.add("光面皮带");
		list.add("针扣皮带");
		list.add("拼接t桖短袖");
		list.add("t桖男短袖纯棉");
		list.add("t桖男短袖宽松");
		list.add("t桖男短袖修身");
		list.add("连衣裙");
		list.add("半身裙");
		list.add("大码女装");
		list.add("打底裤");
		list.add("职业套装");
		list.add("中老年服装");
		list.add("水果罐头");
		list.add("肉类罐头");
		list.add("水产罐头");
		list.add("粥羹罐头");
		list.add("蔬菜罐头");
		list.add("牛奶");
		list.add("雪橙子");
		list.add("冰糖雪梨24瓶简装");
		list.add("非赣南脐橙夏橙子包邮");
		list.add("红肉脐橙");
		list.add("秭归夏橙");
		list.add("信丰脐橙");
		list.add("脐橙");
		return list;
	}

}
