package com.gameportal.web.cache;

import java.util.ArrayList;
import java.util.List;

import com.gameportal.web.mail.model.MailSenderInfo;

/**
 * 系统缓存类。
 * @author sum
 *
 */
public final class PortalGameCache {
	/*
	 * 邮件服务器列表。
	 */
	public static  List<MailSenderInfo> mailerverList = new ArrayList<MailSenderInfo>();
}
