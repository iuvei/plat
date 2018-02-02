package com.na.user.socketserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Set;

/**
 * 网络参数配置。
 * Created by sunny on 2017/4/26 0026.
 */
@Configuration
@PropertySource("classpath:config/socketio.properties")
public class SocketIoConfig {
    @Value("${server.socket.port}")
    private int serverPort;

    @Value("${server.maxHttpContentLength}")
    private int maxHttpContentLength;

    @Value("${server.workerThreads}")
    private int workerThreads;

    @Value("${server.bossThreads}")
    private int bossThreads;

    @Value("${server.acceptBackLog}")
    private int acceptBackLog;

    @Value("${server.tcpSendBufferSize}")
    private int tcpSendBufferSize;

    @Value("${server.tcpReceiveBufferSize}")
    private int tcpReceiveBufferSize;

    /**设置管道关闭不活动时间30分钟*/
    @Value("${server.closeTimeout}")
    private int serverCloseTimeout;
    
    @Value("${games}")
    private String games;

    //设置心跳超时时间
    @Value("${server.heartbeatTimeout}")
    private int heartbeatTimeout=60;
    //设置心跳频率
    @Value("${server.heartbeatInterval}")
    private int heartbeatInterval=25;

    /**
     * 用于生成主键（betOrder,account_record）.
     * 集群分组标识，数据范围0-31
     */
    @Value("${server.datacenter.id}")
    private short datacenterId;

    /**
     * 用于生成主键（betOrder,account_record）.
     * 集群机器标识，数据范围0-31
     */
    @Value("${server.worker.id}")
    private short workerId;


    //会员线程池最大数
    @Value("${server.pool.clientThreads}")
    private int poolClientThreads;
    //荷官线程核心线程数
    @Value("${server.pool.dealerThreadCorePoolSize}")
    private int poolDealerThreadCorePoolSize;
    //荷官线程线程活动保持时间
    @Value("${server.pool.keepAliveTime}")
    private int poolKeepAliveTime;

    //荷官IP，多个用,隔开
    @Value("#{'${server.dealerIps}'.split(',')}")
    private Set<String> dealerIps;

    //普通桌结算线程数
    @Value("${server.normalTableThreadPool}")
    private int normalTableThreadPool;

    //竞咪桌结算线程数
    @Value("${server.beingMiTableThreadPool}")
    private int beingMiTableThreadPool;

    //跨域配置文件路径
    @Value("${server.crossDomainPolicy}")
    private String crossDomainPolicy;

    @Value("${server.origin}")
    private String origin;
    
    @Value("${server.demo.demoAgentName}")
    private String demoAgentName;
    
    @Value("${server.demo.apiDemoAgentName}")
    private String apiDemoAgentName;
    
    @Value("${server.demo.accountnumber}")
    private Integer demoAccountNumber;
    
    @Value("${server.demo.initbalance}")
    private Integer demoAccountInitBalance;
    
    @Value("${server.secretKey.defaultKey}")
	private String defaultKey;
	
	@Value("${server.secretKey.interval}")
	private Integer changeKeyInterval;
	
	@Value("${server.onlyRoomLine}")
	private Integer onlyRoomLine;
    
    public String getCrossDomainPolicy() {
		return crossDomainPolicy;
	}

	public void setCrossDomainPolicy(String crossDomainPolicy) {
		this.crossDomainPolicy = crossDomainPolicy;
	}

	public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public void setMaxHttpContentLength(int maxHttpContentLength) {
        this.maxHttpContentLength = maxHttpContentLength;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }

    public void setTcpSendBufferSize(int tcpSendBufferSize) {
        this.tcpSendBufferSize = tcpSendBufferSize;
    }

    public void setTcpReceiveBufferSize(int tcpReceiveBufferSize) {
        this.tcpReceiveBufferSize = tcpReceiveBufferSize;
    }

    public void setServerCloseTimeout(int serverCloseTimeout) {
        this.serverCloseTimeout = serverCloseTimeout;
    }

    public int getMaxHttpContentLength() {
        return maxHttpContentLength;
    }

    public int getServerCloseTimeout() {
        return serverCloseTimeout;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    public int getTcpSendBufferSize() {
        return tcpSendBufferSize;
    }

    public int getTcpReceiveBufferSize() {
        return tcpReceiveBufferSize;
    }

    public int getServerPort() {

        return serverPort;
    }

	public String getGames() {
		return games;
	}

	public void setGames(String games) {
		this.games = games;
	}

    public short getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(short datacenterId) {
        this.datacenterId = datacenterId;
    }

    public short getWorkerId() {
        return workerId;
    }

    public void setWorkerId(short workerId) {
        this.workerId = workerId;
    }

    public int getHeartbeatTimeout() {
        return heartbeatTimeout;
    }

    public void setHeartbeatTimeout(int heartbeatTimeout) {
        this.heartbeatTimeout = heartbeatTimeout;
    }

    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(int heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    public int getBossThreads() {
        return bossThreads;
    }

    public void setBossThreads(int bossThreads) {
        this.bossThreads = bossThreads;
    }

    public int getPoolClientThreads() {
        return poolClientThreads;
    }

    public void setPoolClientThreads(int poolClientThreads) {
        this.poolClientThreads = poolClientThreads;
    }

    public int getPoolDealerThreadCorePoolSize() {
        return poolDealerThreadCorePoolSize;
    }

    public void setPoolDealerThreadCorePoolSize(int poolDealerThreadCorePoolSize) {
        this.poolDealerThreadCorePoolSize = poolDealerThreadCorePoolSize;
    }

    public int getPoolKeepAliveTime() {
        return poolKeepAliveTime;
    }

    public void setPoolKeepAliveTime(int poolKeepAliveTime) {
        this.poolKeepAliveTime = poolKeepAliveTime;
    }

    public Set<String> getDealerIps() {
        return dealerIps;
    }

    public void setDealerIps(Set<String> dealerIps) {
        this.dealerIps = dealerIps;
    }

    public int getNormalTableThreadPool() {
        return normalTableThreadPool;
    }

    public void setNormalTableThreadPool(int normalTableThreadPool) {
        this.normalTableThreadPool = normalTableThreadPool;
    }

    public int getBeingMiTableThreadPool() {
        return beingMiTableThreadPool;
    }

    public void setBeingMiTableThreadPool(int beingMiTableThreadPool) {
        this.beingMiTableThreadPool = beingMiTableThreadPool;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

	public String getDemoAgentName() {
		return demoAgentName;
	}

	public void setDemoAgentName(String demoAgentName) {
		this.demoAgentName = demoAgentName;
	}

	public Integer getDemoAccountNumber() {
		return demoAccountNumber;
	}

	public void setDemoAccountNumber(Integer demoAccountNumber) {
		this.demoAccountNumber = demoAccountNumber;
	}

	public Integer getDemoAccountInitBalance() {
		return demoAccountInitBalance;
	}

	public void setDemoAccountInitBalance(Integer demoAccountInitBalance) {
		this.demoAccountInitBalance = demoAccountInitBalance;
	}

	public String getApiDemoAgentName() {
		return apiDemoAgentName;
	}

	public void setApiDemoAgentName(String apiDemoAgentName) {
		this.apiDemoAgentName = apiDemoAgentName;
	}

	public String getDefaultKey() {
		return defaultKey;
	}

	public void setDefaultKey(String defaultKey) {
		this.defaultKey = defaultKey;
	}

	public Integer getChangeKeyInterval() {
		return changeKeyInterval;
	}

	public void setChangeKeyInterval(Integer changeKeyInterval) {
		this.changeKeyInterval = changeKeyInterval;
	}

	public Integer getOnlyRoomLine() {
		return onlyRoomLine;
	}

	public void setOnlyRoomLine(Integer onlyRoomLine) {
		this.onlyRoomLine = onlyRoomLine;
	}


	
}
