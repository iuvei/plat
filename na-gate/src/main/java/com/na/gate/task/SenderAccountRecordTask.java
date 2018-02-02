package com.na.gate.task;

import java.util.LinkedHashSet;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.na.gate.cache.GateCache;
import com.na.gate.common.SpringContextUtil;
import com.na.gate.proto.SocketClient;

/**
 * 推送账单记录
 */
@Component
public class SenderAccountRecordTask implements Job {
	private Logger logger = LoggerFactory.getLogger(SenderAccountRecordTask.class);
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		SocketClient socketClient =(SocketClient)SpringContextUtil.getBean("socketClient");
		if(CollectionUtils.isEmpty(GateCache.ROUNDSET)) return;
		
		Set<Long> tempSet = new LinkedHashSet<>();
        try {
        	for(Long roundId :GateCache.ROUNDSET){
        		socketClient.sendAwardAccountRecord(roundId);
        		tempSet.add(roundId);
        	}
		} catch (Exception e) {
			e.getMessage();
			logger.error("定时失败流水记录异常",e);
		}
        if(CollectionUtils.isEmpty(tempSet)){
        	GateCache.ROUNDSET.remove(tempSet);
        }
	}
}
