package com.na.manager.action;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.na.manager.bean.NaResponse;
import com.na.manager.bean.vo.AccountVO;
import com.na.manager.common.annotation.Auth;
import com.na.manager.enums.RedisKeyEnum;
import com.na.manager.util.HttpUtil;
import com.na.monitor.core.NodeData;

/**
 * 服务器监控
 * 
 * @author Andy
 * @version 创建时间：2017年9月1日 下午4:06:19
 */
@RestController
@RequestMapping("/serverMonitor")
@Auth("ServerMonitor")
public class MonitorAction {
	@Autowired
	private ZkClient zkClient;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Value("${server.context-path}")
	private String serverPath;
	@Value("${server.port}")
	private String port;
	@Value("${management.context-path}")
	private String contextPath;
	@Value("${security.user.name}")
	private String userName;
	@Value("${security.user.password}")
	private String password;

	@RequestMapping("/search")
	public NaResponse<AccountVO> search() {
		List<NodeData> dataList = new ArrayList<>();
		if (zkClient.exists("/app")) {
			List<String> urls = zkClient.getChildren("/app");
			urls.forEach(url -> {
				NodeData data = zkClient.readData("/app/" + url);
				dataList.add(data);
			});
		}
		NaResponse response =NaResponse.createSuccess("data",dataList);
		response.put("flag", stringRedisTemplate.opsForValue().get(RedisKeyEnum.EMAIL_NOTICE_FLAG.get()));
		return response;
	}
	
	
	@RequestMapping("/modify/{status}")
	public NaResponse<AccountVO> modify(@PathVariable String status) {
		stringRedisTemplate.opsForValue().set(RedisKeyEnum.EMAIL_NOTICE_FLAG.get(), status);
		return NaResponse.createSuccess(status);
	}

	@RequestMapping("/{requestMethod}")
	public NaResponse<AccountVO> health(@PathVariable String requestMethod,@RequestBody NodeData nodeData) throws SocketException {
		String url = "http://" + nodeData.getServerAddress() + ":" + nodeData.getServerPort();
		if(nodeData.getContextPath()!=null){
			url += nodeData.getContextPath() + contextPath + "/"+requestMethod;
		}else{
			url += contextPath + "/"+requestMethod;
		}
		String data = HttpUtil.doGet(url,
				"Basic " + Base64.getEncoder().encodeToString((userName + ":" + password).getBytes()));
		return NaResponse.createSuccess(data);
	}
}
