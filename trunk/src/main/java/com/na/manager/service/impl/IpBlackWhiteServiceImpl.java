package com.na.manager.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.csvreader.CsvReader;
import com.google.common.base.Preconditions;
import com.na.manager.bean.IpBlackWhiteSearchRequest;
import com.na.manager.bean.Page;
import com.na.manager.cache.AppCache;
import com.na.manager.dao.IIpBlackWhiteMapper;
import com.na.manager.entity.IpBlackWhiteAddr;
import com.na.manager.service.IIpBlackWhiteService;
import com.na.manager.util.FileUtils;
import com.na.manager.util.IpUtils;

/**
 * @author andy
 * @date 2017年6月24日 下午5:56:09
 * 
 */
@Service
@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
public class IpBlackWhiteServiceImpl implements IIpBlackWhiteService {
	private org.slf4j.Logger logger = LoggerFactory.getLogger(IpBlackWhiteServiceImpl.class);
	
	@Autowired
	private IIpBlackWhiteMapper ipBlackWhiteMapper;
//	@Autowired
//    private StringRedisTemplate stringRedisTemplate;

	@Override
	@Transactional(readOnly = true)
	public Page<IpBlackWhiteAddr> queryListByPage(IpBlackWhiteSearchRequest searchRequest) {
		Page<IpBlackWhiteAddr> page = new Page<>(searchRequest);
		if(StringUtils.isNotEmpty(searchRequest.getIp())){
			searchRequest.setStartNum(IpUtils.ip2Num(searchRequest.getIp().trim()));
		}
		page.setData(ipBlackWhiteMapper.queryListByPage(searchRequest));
		page.setTotal(ipBlackWhiteMapper.count(searchRequest));
		return page;
	}

	@Override
	public void insert(IpBlackWhiteAddr ipBlackWhiteAddr) {
		if(StringUtils.isEmpty(ipBlackWhiteAddr.getFilePath())){
			ipBlackWhiteAddr.setCreateBy(AppCache.getCurrentUser().getLoginName());
			ipBlackWhiteAddr.setCreateTime(new Date());
			//检查数据不存在该类型的ip
			List<IpBlackWhiteAddr> dataIpList = ipBlackWhiteMapper.list(ipBlackWhiteAddr);
			Map<String, String> existIpMap =new HashMap<>();
			dataIpList.forEach(item->{
				existIpMap.put(StringUtils.join(new Object[]{item.getIpType(),item.getPlatType(),item.getStart(),item.getEnd()},"."), item.getStart());
			});
			//去重
			Preconditions.checkArgument(!existIpMap.containsKey(StringUtils.join(new Object[]{ipBlackWhiteAddr.getIpType(),ipBlackWhiteAddr.getPlatType(),ipBlackWhiteAddr.getStart(),ipBlackWhiteAddr.getEnd()},".")),"black.white.ip.repeat");
			ipBlackWhiteAddr.setStartNum(IpUtils.ip2Num(ipBlackWhiteAddr.getStart()));
			ipBlackWhiteAddr.setEndNum(IpUtils.ip2Num(ipBlackWhiteAddr.getEnd()));
			List<IpBlackWhiteAddr> list = new ArrayList<>();
			list.add(ipBlackWhiteAddr);
			ipBlackWhiteMapper.insert(list);
		}else{
			ipBlackWhiteMapper.insert(readCSV(ipBlackWhiteAddr));
		}
		
		//同步redis缓存
//		Set<String> newIps = new HashSet<>();
//		newIps.add(StringUtils.join(new Object[]{ipBlackWhiteAddr.getIpType(),ipBlackWhiteAddr.getPlatType(),ipBlackWhiteAddr.getStartNum(),ipBlackWhiteAddr.getEndNum()},"."));
//		if(!CollectionUtils.isEmpty(newIps)){
//			BoundSetOperations<String, String> ops = stringRedisTemplate.boundSetOps(RedisKeyEnum.IP_BLACK_WHITE_LIST.get());
//			ops.add(newIps.toArray(new String[]{}));
//		}
		AppCache.initBlackWhiteIpMap(ipBlackWhiteMapper.findAll());
	}

	@Override
	public void delete(IpBlackWhiteAddr ipBlackWhiteAddr) {
		List<IpBlackWhiteAddr> dataIpList = ipBlackWhiteMapper.list(ipBlackWhiteAddr);
		ipBlackWhiteMapper.delete(ipBlackWhiteAddr.getIds());
		Set<String> newIps = new HashSet<>();
		dataIpList.forEach(item->{
			newIps.add(StringUtils.join(new Object[]{item.getIpType(),item.getPlatType(),ipBlackWhiteAddr.getStartNum(),ipBlackWhiteAddr.getEndNum()},"."));
		});
		//同步redis缓存
//		if(!CollectionUtils.isEmpty(dataIpList)){
//			BoundSetOperations<String, String> ops = stringRedisTemplate.boundSetOps(RedisKeyEnum.IP_BLACK_WHITE_LIST.get());
//			ops.remove(newIps.toArray(new String[]{}));
//		}
		AppCache.initBlackWhiteIpMap(ipBlackWhiteMapper.findAll());
	}

	@Override
	public List<IpBlackWhiteAddr> findAll() {
		return ipBlackWhiteMapper.findAll();
	}
	
	private List<IpBlackWhiteAddr> readCSV(IpBlackWhiteAddr ipBlackWhiteAddr) {
		List<IpBlackWhiteAddr> list = new ArrayList<>();
		IpBlackWhiteAddr blackWhiteIp = null;
		String line;
		String[] record;
		File file = new File(ipBlackWhiteAddr.getFilePath());
		Preconditions.checkArgument(FileUtils.isCSV(file.getName()),"upload.file.format.error");
		try {
			// 创建CSV读对象
			CsvReader csvReader = new CsvReader(ipBlackWhiteAddr.getFilePath());
			//csvReader.readHeaders();
			while (csvReader.readRecord()) {
				line = csvReader.getRawRecord();
				if (StringUtils.isNotEmpty(line)) {
					if(line.indexOf(ipBlackWhiteAddr.getArea()) ==-1){
						continue;
					}else if (line.indexOf(":") != -1) {
						break;
					} else {
						record = line.replace("\"", "").split(",");
						if (record.length >= 3) {
							blackWhiteIp = new IpBlackWhiteAddr();
							blackWhiteIp.setStart(record[0].trim());
							blackWhiteIp.setStartNum(IpUtils.ip2Num(blackWhiteIp.getStart()));
							blackWhiteIp.setEnd(record[1].trim());
							blackWhiteIp.setEndNum(IpUtils.ip2Num(blackWhiteIp.getEnd()));
							blackWhiteIp.setArea(record[2].trim());
						}
					}
				}
				blackWhiteIp.setCreateBy(AppCache.getCurrentUser().getLoginName());
				blackWhiteIp.setCreateTime(new Date());
				blackWhiteIp.setPlatType(ipBlackWhiteAddr.getPlatType());
				blackWhiteIp.setIpType(ipBlackWhiteAddr.getIpType());
				blackWhiteIp.setRemark(ipBlackWhiteAddr.getRemark());
				list.add(blackWhiteIp);
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("批量导入黑白名单ip，CSV解析文件异常", e);
		}
		return list;
	}

	private List<IpBlackWhiteAddr> readExcelValue(IpBlackWhiteAddr ipBlackWhiteAddr) {
		List<IpBlackWhiteAddr> list = new ArrayList<>();
		IpBlackWhiteAddr blackWhiteIp=null;
		// 初始化输入流
		InputStream is = null;
		try {
			// 根据新建的文件实例化输入流
			is = new FileInputStream(ipBlackWhiteAddr.getFilePath());
			File file = new File(ipBlackWhiteAddr.getFilePath());
			Preconditions.checkArgument(FileUtils.validateExcel(file.getName()),"upload.file.format.error");
			// 根据版本选择创建Workbook的方式
			Workbook wb = null;
			// 根据文件名判断文件是2003版本还是2007版本
			if (FileUtils.isExcel2007(file.getName())) {
				wb = new XSSFWorkbook(is);
			} else {
				wb = new HSSFWorkbook(is);
			}
			// 得到第一个shell
			Sheet sheet = wb.getSheetAt(0);
			// 得到Excel的行数
			int totalRows = sheet.getPhysicalNumberOfRows();
			// 总列数
			int totalCells = 0;
			// 得到Excel的列数(前提是有行数)，从第二行算起
			if (totalRows >= 1 && sheet.getRow(1) != null) {
				totalCells = sheet.getRow(1).getPhysicalNumberOfCells();
			}
			// 循环Excel行数,从第二行开始。标题不入库
			for (int r = 1; r < totalRows; r++) {
				Row row = sheet.getRow(r);
				if (row == null) {
					continue;
				}
				blackWhiteIp = new IpBlackWhiteAddr();
				blackWhiteIp.setCreateBy(AppCache.getCurrentUser().getLoginName());
				blackWhiteIp.setCreateTime(new Date());
				blackWhiteIp.setPlatType(ipBlackWhiteAddr.getPlatType());
				blackWhiteIp.setIpType(ipBlackWhiteAddr.getIpType());
				blackWhiteIp.setRemark(ipBlackWhiteAddr.getRemark());
				// 循环Excel的列
				for (int c = 0; c < totalCells; c++) {
					Cell cell = row.getCell(c);
					if (null != cell) {
						if(cell.getStringCellValue().indexOf(":")!=-1){
							break;
						}
						if (c == 0) {
							blackWhiteIp.setStart(cell.getStringCellValue());
							blackWhiteIp.setStartNum(IpUtils.ip2Num(cell.getStringCellValue()));
						} else if (c == 1) {
							blackWhiteIp.setEnd(cell.getStringCellValue());
							blackWhiteIp.setEndNum(IpUtils.ip2Num(cell.getStringCellValue()));
						}else if(c == 2){
							if(cell.getStringCellValue().equals(ipBlackWhiteAddr.getArea())){
								break;
							}else{
								blackWhiteIp.setArea(cell.getStringCellValue());
							}
						}
					}
				}
				list.add(blackWhiteIp);
			}
			// 删除上传的临时文件
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("批量导入黑白名单ip，Excel解析文件异常", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					is = null;
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
}
