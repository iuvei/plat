package com.gameportal.manage.api;

import java.util.HashMap;
import java.util.Map;

/**
 * API异常码转移
 * 
 * @author Administrator
 *
 */
@SuppressWarnings("all")
public class APIErrorCode {
	public static final Map<String, String> map = new HashMap<String, String>() {
		{
			// ------------------------ AG 接口错误码-----------------------------------
			put("key_error", "Key值错误");
			put("network_error", "网络问题导致资料遗失");
			put("account_add_fail", "创建新账号失败, 可能是密码不正确或账号已存在");
			put("error", "其他错误, 请联络我们");

			put("account_not_exist", "账号不存在");

			put("duplicate_transfer", "重复转账");
			put("not_enough_credit", "余额不足, 未能转账");

			put("1", "失败, 订单未处理状态");
			put("2", "因无效的转账金额引致的失败");
			put("3", "非法参数");

			// ------------------------ BBIN 接口错误码-----------------------------------
			put("10002", "余额不足");
			put("10003", "转帐失败");
			put("10005", "转账额度检查错误");
			put("10011", "转帐金额格式错误");
			put("10014", "交易序号长度过长");
			put("10015", "系统忙，请稍后重试");
			put("11000", "重复转账");
			put("21000", "账号新增失败");
			put("21001", "帐号已存在");
			put("21100", "帐号新增成功");
			put("22000", "使用者未登入");
			put("25002", "帐号新增失败(第一个英文字母错误)");
			put("40015", "参数验证不通过");
			put("44000", "参数未带齐");
			put("44001", "无权限");
			put("44444", "系统维护中");
			put("44445", "系统维护中");
			put("44900", "IP不被允许");
			put("47002", "参数action错误");
			put("47003", "参数错误");
			put("50001", "成功");
			put("50002", "失败");
			put("99999", "登入成功");
		}
	};
}
