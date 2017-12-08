package com.gameportal.web.game.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gameportal.web.game.handler.IGameServiceHandler;
import com.gameportal.web.game.model.AGElectronic;
import com.gameportal.web.game.model.AGINElectronic;
import com.gameportal.web.game.model.GamePlatform;
import com.gameportal.web.game.model.MGElectronic;
import com.gameportal.web.game.model.SAElectronic;
import com.gameportal.web.game.service.IGamePlatformService;
import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.util.Blowfish;
import com.gameportal.web.util.CookieUtil;
import com.gameportal.web.util.FileUtils;
import com.gameportal.web.util.PageTool;
import com.gameportal.web.util.WebConst;

/**
 * MG电子游戏登录
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/electronic")
public class MGElectronicGameController{

    @Resource(name = "redisServiceImpl")
    private IRedisService iRedisService = null;
    @Resource(name = "gamePlatformServiceImpl")
    private IGamePlatformService gamePlatformService = null;
    @Resource(name = "userInfoServiceImpl")
    private IUserInfoService userInfoService;
    @Resource(name = "gamePlatformHandlerMap")
    private Map<String, IGameServiceHandler> gamePlatformHandlerMap = null;
    private static final Logger logger = Logger.getLogger(MGElectronicGameController.class);
    private static final int outinRecordPageSize = 16;
    private static final int audltOutinRecordPageSize = 12;
  
    @RequestMapping(value = "/index")
    public String getElectronicList(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            HttpServletRequest request,HttpServletResponse response) {
        pageNo = StringUtils.isEmpty(ObjectUtils.toString(pageNo)) ? 1 : pageNo;
        String vuid = CookieUtil.getOrCreateVuid(request, response);
        request.setAttribute("vuid", vuid);
        if(StringUtils.isEmpty(type)){
            type ="1";
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("categoryID", type);
        params.put("sortColumns", "Sequence ASC");
        params.put("limitParams", (pageNo-1)*outinRecordPageSize+","+outinRecordPageSize);
        Long total = 0l;
        params.put("status", 0);
        List<MGElectronic> listElectronic = gamePlatformService.queryElectronic(params, pageNo, outinRecordPageSize);
        total = gamePlatformService.queryElectronicCount(params);
        request.setAttribute("listElectronic", listElectronic);
        long pageCount = PageTool.getPage(total, outinRecordPageSize);
        request.setAttribute("total", total);
        request.setAttribute("pageCount", pageCount);
        request.setAttribute("pageNo", pageNo);
        request.setAttribute("type", type);
        return "home/electronicGame";
    }
    
    /**
     * 获取AGIN老虎机游戏列表
     * @param type
     * @param pageNo
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/aginElec")
    public String getAginElecList(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            HttpServletRequest request,HttpServletResponse response) {
        pageNo = StringUtils.isEmpty(ObjectUtils.toString(pageNo)) ? 1 : pageNo;
        String vuid = CookieUtil.getOrCreateVuid(request, response);
        request.setAttribute("vuid", vuid);
        Map<String, Object> params = new HashMap<String, Object>();
        if(StringUtils.isEmpty(type)){
        	type ="1";
        }
        params.put("category", type);
        params.put("sortColumns", "Sequence ASC");
        params.put("limitParams", (pageNo-1)*outinRecordPageSize+","+outinRecordPageSize);
        Long total = 0l;
        params.put("status", 1);
        List<AGINElectronic> listElectronic = gamePlatformService.queryAginElec(params);
        total = gamePlatformService.queryAginElecCount(params);
        request.setAttribute("listElectronic", listElectronic);
        long pageCount = PageTool.getPage(total, outinRecordPageSize);
        request.setAttribute("total", total);
        request.setAttribute("pageCount", pageCount);
        request.setAttribute("pageNo", pageNo);
        request.setAttribute("type", type);
        return "home/aginElectronicGame";
    }
    
    /**
     * 获取SA老虎机列表
     * @param type
     * @param pageNo
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/saElecGame")
    public String getAdultElecList(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            HttpServletRequest request,HttpServletResponse response) {
        pageNo = StringUtils.isEmpty(ObjectUtils.toString(pageNo)) ? 1 : pageNo;
        String vuid = CookieUtil.getOrCreateVuid(request, response);
        request.setAttribute("vuid", vuid);
        Map<String, Object> params = new HashMap<String, Object>();
//        if(StringUtils.isEmpty(type)){
//        	type ="Flash";
//        }
//        params.put("gameType", type);
//        params.put("sortColumns", "sequence ASC");
        params.put("limitParams", (pageNo-1)*audltOutinRecordPageSize+","+audltOutinRecordPageSize);
        Long total = 0l;
        params.put("status", 1);
        params.put("sortColumns", "Sequence ASC");
        List<SAElectronic> listElectronic = gamePlatformService.querySAElec(params);
        total = gamePlatformService.querySAElecCount(params);
        request.setAttribute("listElectronic", listElectronic);
        long pageCount = PageTool.getPage(total, audltOutinRecordPageSize);
        request.setAttribute("total", total);
        request.setAttribute("pageCount", pageCount);
        request.setAttribute("pageNo", pageNo);
        request.setAttribute("type", type);
        return "home/saElecGame";
    }

    @RequestMapping(value = "/agElectronic")
    public String agElectronic(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            HttpServletRequest request,HttpServletResponse response) {
        pageNo = StringUtils.isEmpty(ObjectUtils.toString(pageNo)) ? 1 : pageNo;
        String vuid = CookieUtil.getOrCreateVuid(request, response);
        request.setAttribute("vuid", vuid);
        Map<String, Object> params = new HashMap<String, Object>();
        type = StringUtils.isEmpty(type) ? "Classic Slot" : type;
        params.put("category", type);
        params.put("sortColumns", "electronicid ASC");
        params.put("limitParams", (pageNo-1)*outinRecordPageSize+","+outinRecordPageSize);
        Long total = 0l;
        params.put("status", 1);
        params.put("clientID", 1001);
        List<AGElectronic> listElectronic = gamePlatformService.queryAGElectronic(params);
        total = gamePlatformService.queryAgElectronicCount(params);
        request.setAttribute("listElectronic", listElectronic);
        long pageCount = PageTool.getPage(total, outinRecordPageSize);
        request.setAttribute("total", total);
        request.setAttribute("pageCount", pageCount);
        request.setAttribute("pageNo", pageNo);
        request.setAttribute("type", type);
        return "home/agElectronicGame";
    }

    @RequestMapping(value = "/GameHall")
    public String GameHall(
            @RequestParam(value = "vuid", required = false) String vuid,
            @RequestParam(value = "gameName", required = false) String gameName,
            HttpServletRequest request,HttpServletResponse response){
        if(!StringUtils.isNotBlank(gameName)){
            request.setAttribute("msg", "请选择您要进入的游戏。");
            return "home/loginslotmsg";
        }
        if(!StringUtils.isNotBlank(vuid)){
            vuid = CookieUtil.getOrCreateVuid(request, response);
        }
        String key = vuid + "GAMEPORTAL_USER";
        Class<Object> c = null;
        try {
            UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
            // 获取用户最新信息。
            userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
            String platforms = (userInfo.getPlatforms()==null?"":userInfo.getPlatforms());
            if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
                GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(WebConst.PT);
                if(gamePlatform == null){
                    response.sendRedirect("/game/gamefixed.do?game="+WebConst.PT);
                }
                IGameServiceHandler gameInstance = gamePlatformHandlerMap.get(WebConst.PT);
                if(StringUtils.isEmpty(platforms) || platforms.indexOf(gamePlatform.getGpid()+"") ==-1){
                	// 游戏帐号不存在，程序自动创建
                	String code = (String) gameInstance.createAccount(userInfo,gamePlatform, null);
                	if(!"0".equals(code)){
                		logger.info("登录PT电子游戏->创建账号CODE："+code);
                		return null;
                	}else{
                		if(StringUtils.isNotEmpty(platforms)){
                			platforms +=",";
                		}
                		platforms += gamePlatform.getGpid();
                		userInfo.setPlatforms(platforms);
                		// 更新用户信息
                		userInfoService.updateUserInfo(userInfo);
                		iRedisService.addToRedis(key, userInfo);
                	}
                }else{
                	String code = (String) gameInstance.edit(userInfo,gamePlatform, null);
                	if(!"0".equals(code)){
                		logger.info("{"+userInfo.getAccount()+"}更新PT用户信息失败。");
                	}
                }
                Blowfish bf = new Blowfish(WebConst.DECRYPT_KEY);
                request.setAttribute("uaccount", WebConst.PT_API_USERNAME_PREFIX+userInfo.getAccount().toUpperCase());
                request.setAttribute("upwd", bf.decryptString(userInfo.getPasswd()));
                request.setAttribute("gameName", gameName);
                return "home/SlotGameHall";
            }else{
                request.setAttribute("msg", "您还没有登录或登录超时。");
                return "home/loginslotmsg";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "home/loginslotmsg";
    }

    @RequestMapping(value = "/GamePlay")
    public String SlotGamePlay(
            @RequestParam(value = "name", required = false) String gameName,
            HttpServletRequest request,HttpServletResponse response){
        request.setAttribute("name", gameName);
        return "home/SlotGamePlay";
    }

    @RequestMapping(value = "/loginPTGame")
    @ResponseBody
    public String loginPTGame(
            @RequestParam(value = "gameName", required = false) String gameName,
            HttpServletRequest request,HttpServletResponse response){
        String vuid = CookieUtil.getOrCreateVuid(request, response);
        String key = vuid + "GAMEPORTAL_USER";
        Class<Object> c = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("gameName", gameName);
            UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
            // 查询玩家信息
            userInfo = userInfoService.findUserInfoId(userInfo.getUiid());
            String platforms = (userInfo.getPlatforms()==null?"":userInfo.getPlatforms());
            if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
                GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(WebConst.PT);
                if(gamePlatform == null){
                    response.sendRedirect("/game/gamefixed.do?game="+WebConst.PT);
                }
                if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
                    IGameServiceHandler gameInstance = gamePlatformHandlerMap.get(WebConst.PT);
                    if(StringUtils.isEmpty(platforms) || platforms.indexOf(gamePlatform.getGpid()+"") ==-1){
                    	// 游戏帐号不存在，程序自动创建
                    	String code = (String) gameInstance.createAccount(userInfo,gamePlatform, null);
                    	if(!"0".equals(code)){
                    		logger.info("登录PT电子游戏->创建账号CODE："+code);
                    		return null;
                    	}else{
                    		if(StringUtils.isNotEmpty(platforms)){
                    			platforms +=",";
                    		}
                    		platforms +=gamePlatform.getGpid();
                    		// 更新用户信息
                    		userInfo.setPlatforms(platforms);
                    		userInfoService.updateUserInfo(userInfo);
                    		iRedisService.addToRedis(key, userInfo);
                    	}
                    }
                    String gameurl = (String)gameInstance.loginGame(userInfo, gamePlatform, params);
                    if(null != gameurl && !"".equals(gameurl)){
                        response.sendRedirect(gameurl);
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 退出PT游戏。
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/logoutPTGame")
    @ResponseBody
    public String logoutPTGame(HttpServletRequest request,HttpServletResponse response){
        String vuid = CookieUtil.getOrCreateVuid(request, response);
        String key = vuid + "GAMEPORTAL_USER";
        Class<Object> c = null;
        try {
            UserInfo userInfo = (UserInfo) iRedisService.getRedisResult(key, c);
            if(userInfo != null){
                GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(WebConst.PT);
                if(gamePlatform == null){
                	return null;
                }
                IGameServiceHandler gameInstance = gamePlatformHandlerMap.get(WebConst.PT);
                String result = (String)gameInstance.logoutGame(userInfo, gamePlatform, null);
                if(!"0".equals(result)){
                    logger.info("关闭浏览器调用PT登出游戏接口失败。");
                }
                return null;
            }
        } catch (Exception e) {
            logger.error("退出PT游戏失败。",e);
        }
        return null;
    }
    
    
    @RequestMapping(value = "/mgPCWebDownload")
    public String mgPCWebDownload(
            @RequestParam(value = "name", required = false) String gameName,
            HttpServletRequest request,HttpServletResponse response){
        
        ServletOutputStream out = null;
        InputStream inStream = null;
        try {
            inStream = MGElectronicGameController.class.getResourceAsStream("/data/Launch98.exe");
            response.setHeader("Content-Disposition", "attachment; filename=Launch98.exe");
            out = response.getOutputStream();
            //循环取出流中的数据
            byte[] b = new byte[1024];
            int len;
            while((len=inStream.read(b)) >0){
                out.write(b,0,len);
            }
            response.setStatus(response.SC_OK );
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
           if(out != null){
               try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
           }
           if(inStream != null){
               try {
                   inStream.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
        }
        return null;
    }
    
    @RequestMapping(value = "/mgWapWebDownload")
    public String mgWapWebDownload(
            @RequestParam(value = "name", required = false) String gameName,
            HttpServletRequest request,HttpServletResponse response){
        
        ServletOutputStream out = null;
        InputStream inStream = null;
        try {
            inStream = MGElectronicGameController.class.getResourceAsStream("/data/mg_mobile.apk");
            response.setHeader("Content-Disposition", "attachment; filename=mg_mobile.apk");
            out = response.getOutputStream();
            //循环取出流中的数据
            byte[] b = new byte[1024];
            int len;
            while((len=inStream.read(b)) >0){
                out.write(b,0,len);
            }
            response.setStatus(response.SC_OK );
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
           if(out != null){
               try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
           }
           if(inStream != null){
               try {
                   inStream.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
        }
        return null;
    }
    
    public static void main(String[] args) {
		String path="C:\\Users\\admin\\Desktop\\QB\\和记MG接口\\2017新接口\\MG_Game_List_July_2017_Dashur.xlsx";
		// 初始化输入流
				InputStream is = null;
				try {
					// 根据新建的文件实例化输入流
					is = new FileInputStream(path);
					File file = new File(path);
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
					int q=999;
					for (int r = 1; r < 451; r++) {
						q++;
						Row row = sheet.getRow(r);
						if (row == null) {
							continue;
						}
						// 循环Excel的列
						String a="";
						String b="",d="",e="";
						double x=0,y=0;
						for (int c = 0; c < totalCells; c++) {
							Cell cell = row.getCell(c);
							if (null != cell) {
								if(c==4 || c==5){
									if(cell.getNumericCellValue()==0){
										continue;
									}
								}
								if(c ==0){
									a=cell.getStringCellValue();
								}else if(c ==1){
									b=cell.getStringCellValue();
								}else if(c ==4){
									x=cell.getNumericCellValue();
								}else if(c ==5){
									y=cell.getNumericCellValue();
								}else if(c ==18){
									d=cell.getStringCellValue();
								}else if(c ==26){
									e=cell.getStringCellValue();
									if(e.indexOf("/")!=-1){
										e = e.split("/")[0].trim()+".png";
									}else{
										e +=".png";
									}
									if(".png".equals(e)) continue;
								}
							}
						}
						String s="INSERT INTO `mg_electronic_new` VALUES ("+q+", '"+d+"', '"+a+"', '"+a+"', '"+e+"', '"+a+"', '"+b+"', null, '"+(int)x+"', '"+(int)y+"', '1', '400', "+q+", '1');";
						System.out.println(s);
					}
				} catch (Exception e) {
					e.printStackTrace();
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
	}
}
