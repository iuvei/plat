package com.gameportal.web.mail.model;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * 邮件服务列表接收器。
 * @author sum
 *
 */
@XStreamAlias("serverList")
public class MailServer {
	@XStreamImplicit(itemFieldName = "server")
	private List<MailSenderInfo> servers = new ArrayList<MailSenderInfo>();

	public List<MailSenderInfo> getServers() {
		return servers;
	}

	public void setServers(List<MailSenderInfo> servers) {
		this.servers = servers;
	}
}
