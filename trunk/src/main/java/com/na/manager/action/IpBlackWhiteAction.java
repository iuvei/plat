package com.na.manager.action;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Preconditions;
import com.na.manager.bean.IpBlackWhiteSearchRequest;
import com.na.manager.bean.NaResponse;
import com.na.manager.common.annotation.Auth;
import com.na.manager.entity.IpBlackWhiteAddr;
import com.na.manager.service.IIpBlackWhiteService;
import com.na.manager.util.FileUtils;


/**
 * @author andy
 * @date 2017年6月24日 下午6:02:41
 * 
 */
@RestController
@RequestMapping("/ipBlackWhiteList")
@Auth("IpBlackWhiteList")
public class IpBlackWhiteAction {
	
	private static final Logger logger =LoggerFactory.getLogger(IpBlackWhiteAction.class);

	@Autowired
	private IIpBlackWhiteService ipBlackWhiteService;

	@RequestMapping("/search")
	public NaResponse<IpBlackWhiteSearchRequest> search(@RequestBody IpBlackWhiteSearchRequest searchRequest) {
		return NaResponse.createSuccess(ipBlackWhiteService.queryListByPage(searchRequest));
	}

	@PostMapping("/create")
	public NaResponse create(@RequestBody IpBlackWhiteAddr ipBlackWhiteAddr) {
		ipBlackWhiteService.insert(ipBlackWhiteAddr);
		return NaResponse.createSuccess();
	}
	
	@PostMapping("/delete")
	public NaResponse delete(@RequestBody IpBlackWhiteAddr IpBlackWhiteAddr) {
		ipBlackWhiteService.delete(IpBlackWhiteAddr);
		return NaResponse.createSuccess();
	}
	
	@RequestMapping("/uploadExcel")
    @ResponseBody
    @Auth(isPublic=true)
    public NaResponse uploadExcel(@RequestParam("Filedata")MultipartFile file,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	String fileName = file.getOriginalFilename();
    	Preconditions.checkArgument(FileUtils.isCSV(fileName),"upload.file.format.error");
    	 // 文件保存路径  
        String filePath = request.getSession().getServletContext().getRealPath("/") + "upload/"+ fileName;
        logger.info("上传路径为："+filePath);  
    	// 转存文件  
    	File dest =new File(filePath);
    	if(!dest.getParentFile().exists()){
    		dest.getParentFile().mkdir();
    	}
        file.transferTo(dest);  
        return NaResponse.createSuccess(filePath);
    }
    
    @PostMapping("/batchAdd")
	public NaResponse batchAdd(@RequestBody IpBlackWhiteAddr ipBlackWhiteAddr) {
    	try {
    		ipBlackWhiteService.insert(ipBlackWhiteAddr);
    		return NaResponse.createSuccess();
		} catch (Exception e) {
			logger.error("批量导入黑白名单ip异常。",e);
			return NaResponse.createError();
		}
	}
}
