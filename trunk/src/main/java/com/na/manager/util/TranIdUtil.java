package com.na.manager.util;

import java.util.Date;

public class TranIdUtil {

	public static long tranId = 0;
	public static String tranDate = DateUtil.string(new Date());

	public static synchronized String generateTranID() {
		if (!tranDate.equals(DateUtil.string(new Date()))) {
			tranId = 0;
		}
		tranId++;
		Date date = new Date();
		StringBuffer tran = new StringBuffer("ZR").append(String.format("%ty", date)).append(String.format("%tM", date))
				.append(String.format("%td", date)).append(String.format("%tH", date))
				.append(String.format("%tm", date)).append(String.format("%ts", date))
				.append(String.format("%tS", date)).append(String.format("%04d", tranId));
		return tran.toString();
	}
}
