package com.na.manager.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Preconditions;
import com.na.manager.bean.NaResponse;
import com.na.manager.bean.Page;
import com.na.manager.bean.RobotBatchAddWithSnRequest;
import com.na.manager.bean.RobotRequest;
import com.na.manager.bean.vo.AccountVO;
import com.na.manager.common.I18nMessage;
import com.na.manager.common.annotation.Auth;
import com.na.manager.entity.LiveUser;
import com.na.manager.enums.AccountRecordType;
import com.na.manager.enums.ChangeBalanceTypeEnum;
import com.na.manager.enums.LiveUserSource;
import com.na.manager.enums.LiveUserType;
import com.na.manager.enums.UserType;
import com.na.manager.facade.IFinancialFacade;
import com.na.manager.remote.IPlatformUserRemote;
import com.na.manager.remote.IRobotActionRemote;
import com.na.manager.service.ILiveUserService;
import com.na.manager.util.FileUtils;

/**
 * 机器人管理Action
 * 
 * @author alan
 * @date 2017年8月17日 下午3:20:44
 */
@RestController
@RequestMapping("/robot")
@Auth("Robot")
public class RobotAction {

	private static final Logger log =LoggerFactory.getLogger(RobotAction.class);
	
    @Autowired
    private ILiveUserService liveUserService;
    
    @Autowired
    private IFinancialFacade financialFacade;
    
    @Autowired
    private IRobotActionRemote robotActionRemote;
    
    @Autowired
    private IPlatformUserRemote platformUserRemote;
    
    @Autowired
    private I18nMessage i18nMessage;
    
	@RequestMapping("/search")
	public NaResponse<Page<AccountVO>> search(@RequestBody RobotRequest request) {
		//固定机器人上级代理
		LiveUser agent = liveUserService.findByLoginName("robotagent");
		Page<AccountVO> page = new Page(request);
        page.setTotal(liveUserService.countByParentId(agent.getId()));
        page.setData(liveUserService.findLiveUserByParentIdForPage(agent.getId(), request.getStartRow(), request.getPageSize()));
        Map<String, Object> other = new HashMap<>();
        other.put("onlineRobotCount", liveUserService.countOnlineByParentName("robotagent"));
        page.setOtherInfo(other);
		return NaResponse.createSuccess(page);
	}
	
	@RequestMapping("/startBet/{number}")
	public NaResponse startBet(@PathVariable Integer number) {
		robotActionRemote.load(number);
		return NaResponse.createSuccess();
	}
	
	@RequestMapping("/stopBet")
	public NaResponse stopBet() {
		robotActionRemote.logout();
		return NaResponse.createSuccess();
	}
	
	@RequestMapping("/start/{id}")
	public NaResponse simpleLogin(@PathVariable Long id) {
		try {
			robotActionRemote.simpleLogin(id);
			return NaResponse.createSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return NaResponse.createError(e.getMessage());
		}
	}
	
	@RequestMapping("/stop/{id}")
	public NaResponse simpleLogout(@PathVariable Long id) {
		try {
			robotActionRemote.simpleLogout(id);
			return NaResponse.createSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return NaResponse.createError(e.getMessage());
		}
	}

    @PostMapping("/batchAdd")
	public NaResponse batchAdd(@RequestBody RobotRequest request) {
    	try {
    		//固定机器人上级代理
    		LiveUser agent = liveUserService.findByLoginName("robotagent");
    		
    		List<LiveUser> userList = readExcelValue(request.getFilePath(), agent);
    		userList.forEach( liveUser -> {
    			liveUser.setType(LiveUserType.MEMBER);
				liveUser.setSource(LiveUserSource.PROXY);
				liveUser.setChips(agent.getChips());
				liveUser.setParentId(agent.getId());
    		});
    		
    		liveUserService.betchAdd(userList);
    		return NaResponse.createSuccess();
		} catch (Exception e) {
			log.error("批量导入异常。",e);
			return NaResponse.createError(i18nMessage.getMessage(e.getMessage()));
		}
	}

	@PostMapping("/batchAddWithSn")
	public NaResponse batchAddWithSn(@RequestBody RobotBatchAddWithSnRequest request) {
    	try {
    		//固定机器人上级代理
    		LiveUser agent = liveUserService.findByLoginName("robotagent");

    		List<LiveUser> userList = new ArrayList<>();
    		for (int i=request.getStart();i<=request.getEnd();i++){
    			LiveUser user = new LiveUser();
    			user.setLoginName(request.getUserName()+i);
    			user.setNickName(request.getNickName()+i);
    			user.setPassword(request.getPassword());
    			user.setBalance(new BigDecimal(request.getInitPoint()));
    			userList.add(user);
			}

    		userList.forEach( liveUser -> {
    			liveUser.setType(LiveUserType.MEMBER);
				liveUser.setSource(LiveUserSource.PROXY);
				liveUser.setChips(agent.getChips());
				liveUser.setParentId(agent.getId());
    		});

    		liveUserService.betchAdd(userList);
    		return NaResponse.createSuccess();
		} catch (Exception e) {
			log.error("批量创建异常。",e);
			return NaResponse.createError(i18nMessage.getMessage(e.getMessage()));
		}
	}
	
	
	@PostMapping("/batchSnDeposit")
	public NaResponse batchSnDeposit(@RequestBody RobotBatchAddWithSnRequest request) {
    	try {
    		//固定机器人上级代理
    		LiveUser agent = liveUserService.findByLoginName("robotagent");
    		List<LiveUser> liveUserList =liveUserService.findLiveUserByParentId(agent.getId());
    		liveUserList.forEach(item->{
    			if(item.getBalance().doubleValue()<request.getAmount().doubleValue()){
    				financialFacade.updateBalance(item.getId(), agent.getId(),UserType.LIVE, AccountRecordType.INTO,ChangeBalanceTypeEnum.PROXY, request.getAmount().subtract(item.getBalance()), "机器人批量入账");
    			}
    		});
    		return NaResponse.createSuccess();
		} catch (Exception e) {
			log.error("批量入账异常。",e);
			return NaResponse.createError(i18nMessage.getMessage(e.getMessage()));
		}
	}
    
    @Auth(isPublic=true)
    @PostMapping("/upload")
    public NaResponse upload(@RequestParam("Filedata")MultipartFile file,HttpServletRequest request,HttpServletResponse response){
    	String fileName = file.getOriginalFilename();
    	Preconditions.checkArgument(FileUtils.validateExcel(fileName),"upload.file.format.error");
    	 // 文件保存路径  
        String filePath = request.getSession().getServletContext().getRealPath("/") + "robotUpload/"+ fileName;
        log.info("上传路径为："+filePath);
    	// 转存文件  
    	File dest =new File(filePath);
    	if(!dest.getParentFile().exists()){
    		dest.getParentFile().mkdir();
    	}
        try {
			file.transferTo(dest);
			return NaResponse.createSuccess(filePath);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("upload.failure");
		}
    }
    
    @GetMapping("/syncNumber/{number}")
    public NaResponse syncNumber(@PathVariable Long number){
    	try {
    		platformUserRemote.syncRobotOnline(number);
    		return NaResponse.createSuccess();
		} catch (Exception e) {
			log.error("同步机器人在线人数失败",e);
			return NaResponse.createError();
		}
    }
    
    /**
     * 解析Excel
     * @param path
     * @param agent
     * @return
     */
    private List<LiveUser> readExcelValue(String path, LiveUser agent) {
		// 初始化输入流
		InputStream is = null;
		List<LiveUser> userList = new ArrayList<>();
		try {
			// 根据新建的文件实例化输入流
			is = new FileInputStream(path);
			File file = new File(path);
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
			LiveUser liveUser=null;
			for (int r = 1; r < totalRows; r++) {
				Row row = sheet.getRow(r);
				if (row == null) {
					continue;
				}
				liveUser = new LiveUser();
				liveUser.setLoginName(row.getCell(0).getStringCellValue());
				liveUser.setNickName(row.getCell(1).getStringCellValue());
				String password =  "";
				if(row.getCell(2).getCellType() == Cell.CELL_TYPE_NUMERIC) {
					int i = (int) row.getCell(2).getNumericCellValue();
					password = i + "";
				} else {
					password = row.getCell(2).getStringCellValue();
				}
				liveUser.setPassword(password);
				liveUser.setBalance(new BigDecimal(row.getCell(3).getNumericCellValue()));
				userList.add(liveUser);
			}
			// 删除上传的临时文件
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("批量导入用户，Excel解析文件异常", e);
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
		return userList;
	}
    
}
