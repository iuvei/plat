package com.na.gate.task;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.na.gate.common.GateConfig;
import com.na.gate.common.SpringContextUtil;
import com.na.gate.entity.SyncBetOrderFailRecord;
import com.na.gate.proto.SocketClient;
import com.na.gate.service.ISyncBetOrderFailRecordService;
import com.na.manager.remote.FindBetOrderRequest;

/**
 * 推送投注记录
 */
@Component
public class SenderBetOrderTask implements Job {
	private Logger logger = Logger.getLogger(SenderBetOrderTask.class);
//	private String lastDate;
//	private SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
//		SocketClient socketClient =(SocketClient)SpringContextUtil.getBean("socketClient");
//		IPlatformSyncRecordService platformSyncRecordService =(IPlatformSyncRecordService)SpringContextUtil.getBean("platformSyncRecordServiceImpl");
//		GateConfig gateConfig = (GateConfig)SpringContextUtil.getBean("gateConfig");
//		FindBetOrderRequest request = new FindBetOrderRequest();
//        if(lastDate==null){
//        	PlatformSyncRecord lastSyncRecord = platformSyncRecordService.findLastSyncRecord();
//        	if(lastSyncRecord ==null){
//        		Calendar cal = Calendar.getInstance();
//        		cal.setTime(new Date());
//        		cal.add(Calendar.MINUTE,-10);
//        		lastDate = df.format(cal.getTime());
//        	}else{
//        		lastDate = lastSyncRecord.getEndTime();
//        	}
//        }
//        request.setStartTime(lastDate);
//        request.setEndTime(df.format(new Date()));
//        request.setParentId(gateConfig.getPlatformProxyUserRoot());
//        request.setPageSize(1000);
//        platformSyncRecordService.add(new PlatformSyncRecord(request.getStartTime(),request.getEndTime()));
//        
//		socketClient.sendBetOrder(request);
//		request.setParentId(gateConfig.getPlatformMerchantRoot());
//		socketClient.sendBetOrder(request);
//		lastDate = request.getEndTime();
		
		SocketClient socketClient =(SocketClient)SpringContextUtil.getBean("socketClient");
		ISyncBetOrderFailRecordService syncBetOrderFailRecordService =(ISyncBetOrderFailRecordService)SpringContextUtil.getBean("syncBetOrderFailRecordServiceImpl");
		GateConfig gateConfig = (GateConfig)SpringContextUtil.getBean("gateConfig");
        List<SyncBetOrderFailRecord> syncBetOrderFailRecordList = syncBetOrderFailRecordService.findAll();
        if(CollectionUtils.isEmpty(syncBetOrderFailRecordList)){
          return;	
        }
        FindBetOrderRequest request = new FindBetOrderRequest();
        request.setPageSize(1000);
        boolean flag =true;
        try {
        	 for(SyncBetOrderFailRecord data:syncBetOrderFailRecordList){
            	 request.setParentId(gateConfig.getPlatformProxyUserRoot());
            	 request.setPath(gateConfig.getPlatformProxyUserPath());
            	 request.setRoundId(data.getRoundId());
            	 
            	 flag =socketClient.sendBetOrder(request);
            	 if(!flag){
            		 break;
            	 }
            	 request.setParentId(gateConfig.getPlatformMerchantRoot());
            	 request.setPath(gateConfig.getPlatformMerchantPath());
         		 socketClient.sendBetOrder(request);
	         	 if(!flag){
	           		 break;
	           	 }
	         	 syncBetOrderFailRecordService.delete(data.getRoundId());
            }
		} catch (Exception e) {
			e.getMessage();
			logger.error("定时推送投注记录失败",e);
		}
       
	}
}
