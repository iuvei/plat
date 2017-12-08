package com.gameportal.manage.reportform.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.gameportal.manage.fcrecord.model.FcRecord;
import com.gameportal.manage.fcrecord.service.IFcRecordService;
import com.gameportal.manage.reportform.service.IReportFormService;
import com.gameportal.manage.user.model.UserInfo;
import com.gameportal.manage.user.service.IUserInfoService;
import com.gameportal.portal.util.DateUtil;

/**
 * 收集每日首存记录。
 */
public class CollectFcMember {
	
	@Resource(name="reportFormService")
	private IReportFormService reportFormService;
	@Resource(name="fcRecordService")
	private IFcRecordService fcRecordService;
	@Resource(name="userInfoServiceImpl")
	private IUserInfoService userInfoService;
	
	public void run(){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("nowDate", DateUtil.FormatDate(new Date()));
		map.put("orderField", "p.deposittime desc");
		List<Map<String, Object>> list= reportFormService.getFirstPayNumberResult(map,0,Integer.MAX_VALUE);
		FcRecord record =null;
		UserInfo userInfo = null;
		for(Map<String, Object> datas :list){
			map=new HashMap<String,Object>();
			map.put("account", datas.get("acounts"));
			record = fcRecordService.getObject(map);
			if(record != null){
				break;
			}else{
				record = new FcRecord();
				record.setAccount(String.valueOf(datas.get("acounts")));
				record.setUsername(datas.get("username")==null?"":String.valueOf(datas.get("username")));
				record.setMoney(String.valueOf(datas.get("amounts")));
				record.setPaytime(String.valueOf(datas.get("depotime")));
				userInfo= userInfoService.queryById(Long.valueOf(datas.get("uiid")+""));
				record.setPhone(userInfo.getPhone());
				if(userInfo.getPuiid()>0){
					userInfo= userInfoService.queryById(userInfo.getPuiid());
					record.setPname(userInfo.getAccount());
				}
				fcRecordService.insert(record);
			}
		}
	}
}
