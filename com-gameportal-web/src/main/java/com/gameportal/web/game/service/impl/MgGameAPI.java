package com.gameportal.web.game.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gameportal.web.game.dao.GameTransferDao;
import com.gameportal.web.game.model.GamePlatform;
import com.gameportal.web.game.model.GameTransfer;
import com.gameportal.web.game.service.IGamePlatformService;
import com.gameportal.web.game.util.ApiException;
import com.gameportal.web.user.dao.UserPropertyDao;
import com.gameportal.web.user.model.AccountMoney;
import com.gameportal.web.user.model.PayOrderLog;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.util.Blowfish;
import com.gameportal.web.util.DateUtil;
import com.gameportal.web.util.PropertiesUtils;
import com.gameportal.web.util.ResultMsg;
import com.gameportal.web.util.WebConst;

import net.sf.json.JSONObject;

@Service("mgGameAPI")
public class MgGameAPI {
    
    @Resource(name = "userInfoServiceImpl")
    private IUserInfoService userInfoService = null;
    @Resource(name = "gameTransferDao")
    private GameTransferDao gameTransferDao = null;
    @Resource(name = "userPropertyDao")
    private UserPropertyDao userPropertyDao;
	@Resource(name = "gamePlatformServiceImpl")
	private IGamePlatformService gamePlatformService;
    
    private URL url = null;
    private final Integer SUCCESS_MG = 0;
    private String baseUri;
    private String apiName;
    private String ipAddress;
    private String sessionGuid;
    private Map<String, Object> header;
    private Map<String, Object> body;
    private String headerElement;
    private Integer errorCode;

    final Integer chineseYunCurrency = 8;
    private String agentId;
    private String agentPw;

    private Integer currency;
    
    private String account;

    Logger logger = Logger.getLogger(getClass());

    public MgGameAPI() throws IOException {
        Properties properties = PropertiesUtils.loadProperties("mg.properties");
        this.agentId = properties.getProperty("agentId");
        this.agentPw = properties.getProperty("agentPw");
        this.baseUri = properties.getProperty("baseUri");
        String currencyStr = properties.getProperty("currency");
        if(currencyStr==null || "".equals(currencyStr)){
            currencyStr = "1";
        }
        this.currency = Integer.parseInt(currencyStr);
        String urlStr = properties.getProperty("url");
        url = new URL(urlStr);
    }

    /**
     * 身份认证
     * @return
     */
    public Boolean isAuthenticate() {
        apiName = "IsAuthenticate";
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("loginName", agentId);
        body.put("pinCode", agentPw);
        this.body = body;
        NodeList response = new MgSoapPacket().sendPacket(this);
        if (response != null) {
            response = response.item(0).getChildNodes();
            for (int i = 0; i < response.getLength(); i++) {
                switch (response.item(i).getNodeName()) {
                case "ErrorCode":
                    errorCode = Integer.parseInt(response.item(i).getFirstChild().getNodeValue());
                    this.errorCode = (Integer.parseInt(response.item(i).getFirstChild().getNodeValue()));
                    if (!errorCode.equals(SUCCESS_MG)) {
                        //throw new  GameProviderException(ErrorMsg.MG_PROCESSING_ERROR+" isAuthenticate()"+GameProviderEnums.MG+" "+errorCode);
                        logger.error("MG认证失败：errorCode：" + errorCode);
                        return false;
                    }
                    break;
                case "SessionGUID":
                    this.sessionGuid = response.item(i).getFirstChild().getNodeValue();//getTextContent();
                    break;
                case "IPAddress":
                    this.ipAddress = response.item(i).getFirstChild().getNodeValue();
                    break;
                }
            }
            this.headerElement = "AgentSession";
        } else {
            //          throw new  GameProviderException(ErrorMsg.MG_PROCESSING_ERROR);
            logger.error("MG认证异常！");
            return false;
        }
        Map<String, Object> header = new HashMap<String, Object>();
        header.put("SessionGUID", this.sessionGuid);
        header.put("ErrorCode", (Integer) 0);
        header.put("IPAddress", this.ipAddress);
        header.put("IsExtendSession", Boolean.TRUE);
        this.header = header;
        //logger.info("isAuthenticate 结果信息:" + header);
        return (this.sessionGuid != null);
    }
    
    /**
     * 获取BettingProfiledId
     * @return
     */
    public String getBettingProfileList(String account){
    	this.account = account;
    	if (this.isAuthenticate()) {
    		 this.apiName = "GetBettingProfileList";
    		 logger.info(new MgSoapPacket().sendPacket(this).item(0));
    		 NodeList response =  new MgSoapPacket().sendPacket(this).item(0).getChildNodes();
    		 for (int i = 0; i < response.getLength(); i++) {
    			 NodeList nodeList = response.item(0).getChildNodes();
    			 for (int j = 0; j < nodeList.getLength(); j++) {
    				 if(nodeList.item(j).getNodeName().equals("Id")){
    					 return nodeList.item(j).getFirstChild().getNodeValue();
    				 }
    			 }
             }
    	}
    	return null;
    }
    
    /**
     * 创建MG玩家账号
     * return 创建结果  0：成功(包括已存在)  -1：失败
     * */
    public String createPlayer(UserInfo userInfo) {
    	GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(WebConst.MG);
    	if (!userInfo.isNotExist(gamePlatform.getGpname() + gamePlatform.getGpid())) {
    		 return "0";
    	}
        ObjectNode playerInfo = JsonNodeFactory.instance.objectNode();
        this.account = userInfo.getAccount();
        if (this.isAuthenticate()) {
            this.apiName="AddAccount";
            String accountNumber = WebConst.MG_API_USERNAME_PREFIX_NEW + userInfo.getAccount();
            Map<String, Object> body = new HashMap<String, Object>();
            body.put("firstName", WebConst.MG_API_USERNAME_PREFIX_NEW);  //前缀内容
            body.put("lastName", userInfo.getAccount());   //内容任意             
            body.put("accountNumber", accountNumber);        
            Blowfish bf = new Blowfish(WebConst.DECRYPT_KEY);
            body.put("password", bf.decryptString(userInfo.getPasswd())); //需要解密成原密码
            body.put("isSendGame", Boolean.FALSE); //When True a link is sent by email or SMS/WapPush (as applicable) to download the game. If not stated in XML, then “true” value is used by default. 
            body.put("BettingProfiledId", (Integer)848); //A list of available profiles obtained from GetBettingProfileList()[说明game的情况的函数] method by “LGBetProfile” category
            body.put("currency", currency ); 
            this.body = body;
            NodeList response = new MgSoapPacket().sendPacket(this).item(0).getChildNodes();
            for (int i = 0; i < response.getLength(); i++) {
                String noddeValue = null;
                if(response.item(i).getFirstChild()!=null){
                    noddeValue = response.item(i).getFirstChild().getNodeValue();
                }
                playerInfo.put(response.item(i).getNodeName(), noddeValue);  
                if (response.item(i).getNodeName().equals("ErrorCode")) {
                    this.errorCode = (Integer.parseInt(response.item(i).getFirstChild().getNodeValue()));
                    if(errorCode == 66 || errorCode == 115){//66和115都表示用户已存在
                        logger.info("MG createPlayer 结果信息(用户已存在):"+playerInfo);
                        return "0";
                    }
                }
            }
            
        }else {
            logger.error("MG createPlayer 认证不通过！" );
            return "-1";
        }
        logger.info("MG createPlayer 结果信息:"+playerInfo);
         
        if (Boolean.parseBoolean(playerInfo.get("IsSucceed").textValue())) {
            return "0";
        }else{
            return "-1";
        }
    }
    
    /**
     * 查询账户详细信息
     * @param playerAccount 此账号未加前缀
     * @return null:认证不通过  {"ErrorMessage":null,"IsSucceed":"true","ErrorCode":"0","ErrorId":null,"AccountNumber":"testdxy01","FirstName":"testdxy01","LastName":"testdxy01","NickName":null,"PokerAlias":null,"MobileNumber":null,"RelatedProduct":"Casino","AccountStatus":"Open","SuspendAccountStatus":"Open","LastEditDate":"2016-03-16T10:19:34","EMail":null,"CreditBalance":"0","Balance":"0","ProfileId":"32","RngBettingProfileId":"0","RngBettingProfileStatus":null}
     */
    public JsonNode getAccountDetails(String playerAccount){
        ObjectNode playerInfo = null ;
        this.account = playerAccount;
        if (this.isAuthenticate()) {
            playerInfo = JsonNodeFactory.instance.objectNode();
            this.apiName="GetAccountDetails";
            //给用户名加上前缀
            String accountNumber = WebConst.MG_API_USERNAME_PREFIX_NEW + playerAccount;
            Map<String, Object> body = new HashMap<String, Object>();
            body.put("accountNumber", accountNumber);
            this.body = body;
            NodeList response = new MgSoapPacket().sendPacket(this).item(0).getChildNodes();
            for (int i = 0; i < response.getLength(); i++) {
                String noddeValue = null;
                if(response.item(i).getFirstChild()!=null){
                    noddeValue = response.item(i).getFirstChild().getNodeValue();
                }
                playerInfo.put(response.item(i).getNodeName(), noddeValue); 
            }
        }else{
            logger.error("MG getAccountDetails 认证不通过！" );
        }
        logger.info("MG GetAccountDetails playerAccount:"+ playerAccount + "；结果：" + playerInfo);
        return playerInfo;
     
    }
    
    /**
     * 查询账户余额
     * @param playerAccount
     * @return
     */
    public BigDecimal getAccountBalance_back(String playerAccount){
        BigDecimal balance = null;
        this.account =playerAccount;
        JsonNode jsonNode = getAccountDetails(playerAccount);
        if(jsonNode != null){//认证通过
            if(jsonNode.get("Balance") == null){//账户不存在
                return null;
            }
            balance = BigDecimal.valueOf(jsonNode.get("Balance").asDouble());
        }
        return balance;
    }
    
    /**
     * 查询账户余额
     * @param playerAccount
     * @return
     */
    public JSONObject getAccountBalance(String playerAccount){
    	JSONObject json = new JSONObject();
    	// 如果用户不存在，直接返回0；
//    	UserInfo userInfo = userInfoService.queryUserInfo(playerAccount, 1);
//    	GamePlatform gamePlatform = gamePlatformService.queryGamePlatform(WebConst.MG);
//		if(userInfo.isNotExist(gamePlatform.getGpname()+gamePlatform.getGpid())){
//		   json.put("code", 0);
//           json.put("balance", 0);
//           json.put("msg", "用户在MG平台不存在！");
//           return json;
//		}
        BigDecimal balance = null;
        ObjectNode playerInfo = null ;
        this.account = playerAccount;
        if (this.isAuthenticate()) {
            playerInfo = JsonNodeFactory.instance.objectNode();
            this.apiName="GetAccountBalance";
            //给用户名加上前缀
            String accountNumber = WebConst.MG_API_USERNAME_PREFIX_NEW + playerAccount;
            Map<String, Object> body = new HashMap<String, Object>();
            body.put("delimitedAccountNumbers", accountNumber);
            this.body = body;
            NodeList response = new MgSoapPacket().sendPacket(this).item(0).getChildNodes();
            if(response != null && response.getLength()>0){
                Node BalanceResult = response.item(0);
                NodeList nodes = BalanceResult.getChildNodes();
                logger.info("getAccountBalance nodes.getLength():" +nodes.getLength() + "; accountNumber:"+ accountNumber );
                for (int i = 0; i < nodes.getLength(); i++) {
                    String noddeValue = null;
                    if(nodes.item(i)!=null){
                        if(nodes.item(i).getFirstChild()!=null){
                            noddeValue = nodes.item(i).getFirstChild().getNodeValue();
                        }
                    }
                    playerInfo.put(nodes.item(i).getNodeName(), noddeValue); 
                }
                
            }
        }else{
            logger.error("MG getAccountBalance 认证不通过！" );
        }
        if(playerInfo == null){
        	json.put("code", "9"); //Any other exception.
            return null;
        }
        JsonNode result = playerInfo.get("IsSucceed");
        if(result == null){
        	json.put("code", "9"); //Any other exception.
            logger.info("MG getAccountBalance  无IsSucceed 字段!");
            return json;
        }
        Boolean isSucceed = Boolean.parseBoolean(result.textValue());
        if(isSucceed){
            balance = new BigDecimal(playerInfo.get("Balance").asDouble()).setScale(4,BigDecimal.ROUND_DOWN);
            json.put("code", 0);
            json.put("balance", balance);
            return json;
        }else{
        	json.put("code", playerInfo.get("ErrorCode").textValue());
            json.put("balance", 0);
            json.put("msg", playerInfo.get("ErrorMessage").textValue());
        }
        return json;
    }
    
    /**
     * 修改账户信息
     * @param userInfo
     * @return
     */
    public ResultMsg editAccount(UserInfo userInfo){
        ResultMsg result = new ResultMsg();
        result.setCode("0");
        if(userInfo == null){
            result.setMessage("需要修改的用户信息为空！！");
            logger.info("MG editAccount " + result.toString());
            return result;
        }
        ObjectNode playerInfo = null ;
        this.account = userInfo.getAccount();
        if (this.isAuthenticate()) {
            playerInfo = JsonNodeFactory.instance.objectNode();
            this.apiName="EditAccount";
            //给用户名加上前缀
            String accountNumber = WebConst.MG_API_USERNAME_PREFIX_NEW + userInfo.getAccount();
            Map<String, Object> body = new HashMap<String, Object>();
            body.put("accountNumber", accountNumber);
            body.put("password", userInfo.getPasswd());
            body.put("BettingProfiledId", (Integer)848);
//            body.put("firstName", null);
//            body.put("lastName", null);
//            body.put("mobileNumber", null);
//            body.put("eMail", null);
//            body.put("bettingProfileId", 0);
//            body.put("rngBettingProfileId", null);
            
            this.body = body;
//            logger.info("MG修改账号信息请求参数："+JSONObject.fromObject(this).toString());
            NodeList response = new MgSoapPacket().sendPacket(this).item(0).getChildNodes();
            if(response != null && response.getLength()>0){
                logger.info("editAccount response.getLength():" +response.getLength() + "; accountNumber:"+ accountNumber );
                for (int i = 0; i < response.getLength(); i++) {
                    String noddeValue = null;
                    if(response.item(i).getFirstChild()!=null){
                        noddeValue = response.item(i).getFirstChild().getNodeValue();
                    }
                    playerInfo.put(response.item(i).getNodeName(), noddeValue); 
                }
            }
        }else{
            result.setMessage("MG editAccount 认证不通过！");
            logger.info("MG editAccount " + result.toString());
            return result;
        }
        logger.info("MG editAccount playerAccount:"+ userInfo.getAccount() + "；结果：" + playerInfo);
        if(playerInfo == null){
            result.setMessage("MG editAccount playerInfo 为空！");
            logger.info("MG editAccount " + result.toString());
            return result;
        }
        
        boolean isSucceed = Boolean.parseBoolean(playerInfo.get("IsSucceed").textValue());
        if(isSucceed){
            result.setCode("1");
        }else{
            result.setCode("err_" + playerInfo.get("ErrorCode").textValue());
            result.setMessage(playerInfo.get("ErrorMessage").textValue());
            logger.info("MG editAccount " + result.toString());
        }
        return result;
    }
    
    /**
     * 用户从MG提现
     * @param playerAccount
     * @param amount
     * @return
     * @throws MalformedURLException 
     * @throws ApiException 
     */
    public boolean withdrawal(UserInfo userInfo, BigDecimal amount, GameTransfer gameTransfer,String mgBill) throws ApiException {
        if(amount.compareTo(new BigDecimal(0.01))<0){
            logger.error("MG withdrawal 提现金额不能小于0.01。amount：" + amount);
            return false;
        }
        ObjectNode playerInfo = JsonNodeFactory.instance.objectNode();
        this.apiName="Withdrawal";
        //给用户加上前缀
        String accountNumber = WebConst.MG_API_USERNAME_PREFIX_NEW + userInfo.getAccount();
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("accountNumber", accountNumber);
        body.put("amount", amount);
        body.put("idempotencyId", mgBill);
        this.body=body;
        NodeList response = null;
        boolean result = false;
        String balanceAfter = ""; //转账后第三方账户余额
        BigDecimal balance = BigDecimal.ZERO;
        try {
            response = new MgSoapPacket().sendPacket(this).item(0).getChildNodes();
        } catch (Exception e) {//如果出现异常  可能成功也可能失败，需要再次调用余额查询确定转账结果。
            try {
                JSONObject json = getAccountBalance(userInfo.getAccount());
                if("0".equals(json.getString("code"))){
                	balance = new BigDecimal(json.getString("balance"));
                }else{
                	logger.error("查询MG游戏余额异常:["+json.getString("code"+"}"+json.getString("msg")));
                }
            } catch (Exception e2) {
            	logger.error("调用提款接口失败后，再调余额getAccountBalance查询失败", e2);
            	try {
            		JsonNode node = getAccountDetails(userInfo.getAccount());
            		if("46".equals(node.get("ErrorCode").textValue())){ //Customer account does not exists
            			balance = BigDecimal.ZERO;
            		}else{
            			balance = new BigDecimal(node.get("Balance").textValue());
            		}
				} catch (Exception e3) {
					logger.error("调用提款接口失败后，再调余额getAccountDetails查询失败", e3);
					throw new ApiException("999");
//					return false;
				}
            	result = true;
            }
            if(balance.compareTo(gameTransfer.getOtherbefore())==0 && !("0".equals(gameTransfer.getOtherbefore()))){//操作失败！
            	throw new ApiException("999");
//            	return false;
            }else{
            	result = true;
            }
        }
        balanceAfter = String.valueOf(balance);
        logger.info("MG withdrawal result == " + result+ "__accountNumber:"+accountNumber);
        if(!result){
            logger.info("MG withdrawal response.getLength(): " + response.getLength()+ "__accountNumber:"+accountNumber);
            for (int i = 0; i < response.getLength(); i++) {
                String noddeValue = null;
                if(response.item(i).getFirstChild()!=null){
                    noddeValue = response.item(i).getFirstChild().getNodeValue();
                }else{
                    logger.info("MG withdrawal response.item("+i+").getFirstChild()返回空值。"+ "__accountNumber:"+accountNumber);
                }
                playerInfo.put(response.item(i).getNodeName(), noddeValue); 
                
            }
            logger.info("MG withdrawal 结果信息:"+playerInfo);
            balanceAfter = playerInfo.get("Balance").textValue();
            result = Boolean.parseBoolean(playerInfo.get("IsSucceed").textValue());
        }
        
        if(result){//如果转账成功 则修改用户余额、订单状态及添加流水
            logger.info("用户："+accountNumber +"转账成功！");
            try {
                //修改用户余额、订单状态及添加流水
                transferAfter(userInfo, amount.intValue(), gameTransfer, balanceAfter);
            } catch (Exception e) {
                logger.error("MG withdrawal成功后本地数据修改异常："+e.getMessage());
            }
        }
        return result;
            
    }
    
    /**
     * 向MG平台存款
     * @param userInfo
     * @param amount
     * @return
     * @throws ApiException 
     */
    public boolean deposit(UserInfo userInfo, BigDecimal amount, GameTransfer gameTransfer,GamePlatform gamePlatform,String mgBill) throws ApiException {
        logger.info("MG deposit coming in! account:" + userInfo.getAccount());
        this.account = userInfo.getAccount();
        if(amount.compareTo(new BigDecimal(0.01))<0){
            logger.error("MG deposit 存款金额不能小于0.01。amount：" + amount);
            throw new ApiException("存款金额不能小于0.01");
        }
        if(amount.abs().doubleValue()>gameTransfer.getOrigamount().doubleValue()){
       	  throw new ApiException("余额不足，请充值。");
        }
        ObjectNode playerInfo = JsonNodeFactory.instance.objectNode();
	    if(userInfo.isNotExist(gamePlatform.getGpname()+gamePlatform.getGpid())){
	        // 游戏帐号不存在，程序自动创建
	        String createPlayerResult = (String) createPlayer(userInfo);
	        if(!createPlayerResult.equals("0")){
	            logger.error("MG-deposit 创建账户时失败！" );
	            throw new ApiException("创建MG账户失败！");
	        }else{
	        	userInfo.updatePlats(gamePlatform.getGpname()+gamePlatform.getGpid());
				userInfoService.updateUserInfo(userInfo);
	        }
        }
        this.apiName="Deposit";
        //给用户加上前缀
        String accountNumber = WebConst.MG_API_USERNAME_PREFIX_NEW + userInfo.getAccount();
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("accountNumber", accountNumber);
        body.put("amount", amount);
        body.put("currency", this.currency);
        this.body=body;
        NodeList response = null;
        boolean result = false;
        String balanceAfter = "";//转账后MG账户余额
        BigDecimal balance = BigDecimal.ZERO;
        try {
            response = new MgSoapPacket().sendPacket(this).item(0).getChildNodes();
        } catch (Exception e) {//如果出现异常  可能成功也可能失败，需要再次调用余额查询确定转账结果。
            try {
            	JSONObject balanceJson = getAccountBalance(userInfo.getAccount());
				if("0".equals(balanceJson.getString("code"))){
					balance = new BigDecimal(balanceJson.getString("balance"));
				}else{
					logger.error("MG转账前查询余额异常："+balanceJson.getString("msg"));
				}
            } catch (Exception e2) {
            	logger.error("调用存款接口失败后，再调余额getAccountBalance查询失败 accountNumber:"+accountNumber, e2);
            	try {
            		JsonNode node = getAccountDetails(userInfo.getAccount());
            		if("46".equals(node.get("ErrorCode").textValue())){ //Customer account does not exists
            			balance = BigDecimal.ZERO;
            		}else{
            			balance = new BigDecimal(node.get("Balance").textValue());
            		}
				} catch (Exception e3) {
					logger.error("调用存款接口失败后，再调余额getAccountDetails查询失败 accountNumber:"+accountNumber, e3);
					throw new ApiException("999");
//					return false;
				}
            	result = true;
            }
            if(balance.compareTo(gameTransfer.getOtherbefore())==0){//操作失败！
            	throw new ApiException("999");
//            	return false;
            }else{
            	result = true;
            }
        }
        balanceAfter = String.valueOf(balance);//转账后MG账户余额
        if(!result){
            logger.info("MG deposit response.getLength(): " + response.getLength());
            for (int i = 0; i < response.getLength(); i++) {
                String noddeValue = null;
                if(response.item(i).getFirstChild()!=null){
                    noddeValue = response.item(i).getFirstChild().getNodeValue();
                }else{
                    logger.info("MG deposit response.item("+i+").getFirstChild()返回空值。"+ "__accountNumber:"+accountNumber);
                }
                playerInfo.put(response.item(i).getNodeName(), noddeValue); 
            }
            
            logger.info("MG deposit 结果信息:"+playerInfo);
            balanceAfter = playerInfo.get("Balance").textValue(); 
            result = Boolean.parseBoolean(playerInfo.get("IsSucceed").textValue());
        }
        if(result){//如果转账成功 则修改用户余额、订单状态及添加流水
            logger.info("用户："+accountNumber +"存款到MG成功！");
            try {
                //修改用户余额、订单状态及添加流水
                transferAfter(userInfo, amount.intValue()*-1, gameTransfer, balanceAfter);
            } catch (Exception e) {
                logger.error("MG deposit成功后本地数据修改异常："+e.getMessage());
            }
        }
        return result;
    }
    
    /**
     * 用户转账后的操作
     * @param userInfo 用户信息
     * @param transferNum 转账金额，大于0：提取；小于0：存款
     * @param gameTransfer 游戏转账记录
     * @param balanceAfter 转账后MG平台的余额
     * @throws ApiException
     */
    private void transferAfter(UserInfo userInfo,int transferNum, GameTransfer gameTransfer, String balanceAfter) throws ApiException{
        // 查询用户游戏金额信息
        AccountMoney accountMoney = userInfoService.getAccountMoneyObj(userInfo.getUiid(), 1);
        // 更新用户金额
        accountMoney.setTotalamount(accountMoney.getTotalamount().add(new BigDecimal(transferNum)));//transferNum:存款为负  提现为正
        if(accountMoney.getTotalamount().doubleValue() < 0){
       	 throw new ApiException("余额不足，请充值！");
        }
        accountMoney.setUpdateDate(new Date());
        boolean codes = userInfoService.updateAccountMoneyObj(accountMoney);
        logger.info("transferAfter codes:"+codes);
        if (codes == false) {
            gameTransfer.setStatus(2);
            gameTransferDao.update(gameTransfer);
            throw new ApiException("更新钱包操作失败！");
        }
        // 调用第三方查询余额接口
//        balance = (String) gameInstance.queryBalance(userInfo, gamePlatform, null);
        gameTransfer.setOtherafter(new BigDecimal(balanceAfter));
        // 记录用户余额
        gameTransfer.setBalance(accountMoney.getTotalamount());
        gameTransfer.setStatus(1);
        gameTransfer.setUpdateDate(new Date());
        codes = gameTransferDao.update(gameTransfer);
        if (codes == false) {
            throw new ApiException("转账记录状态更新失败！");
        }
        // 新增帐变记录。
        PayOrderLog log = new PayOrderLog();
        log.setUiid(gameTransfer.getUuid());
        log.setOrderid(gameTransfer.getGpid().toString());
        log.setAmount(new BigDecimal(transferNum));//transferNum:存款为负  提现为正
        log.setType(9);
        log.setWalletlog(gameTransfer.getOrigamount() + ">>>" + gameTransfer.getBalance());
        log.setGamelog(gameTransfer.getOtherbefore() + ">>>" + gameTransfer.getOtherafter());
        log.setRemark(gameTransfer.getGamename() + "转入" + gameTransfer.getTogamename());
        log.setCreatetime(DateUtil.getStrByDate(gameTransfer.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
        userPropertyDao.insertPayLog(log);
    }
    
    /**
     * 进入老虎机游戏
     * @param userInfo
     * @param gameid
     * @return
     */
    public String launchSlot(UserInfo userInfo, String gameid) {      
        Blowfish bf = new Blowfish(WebConst.DECRYPT_KEY);
        //给用户加上前缀
        String accountNumber = WebConst.MG_API_USERNAME_PREFIX_NEW + userInfo.getAccount();
        /*String actionLink = "https://redirect.CONTDELIVERY.COM/Casino/Default.aspx";
        String gameLinkCode = "<script src='https://code.jquery.com/jquery-1.10.1.js'></script>"
            + "<form method='POST' name='launchSlot' id='launchSlot' action='" + actionLink + "'>"
            + "<input type='hidden' name='ul'  value='zh'/>"
            + "<input type='hidden' name='sext1' value='" + accountNumber + "'/>"
            + "<input type='hidden' name='sext2'  value='" + bf.decryptString(userInfo.getPasswd()) + "'/>"
            + "<input type='hidden' name='gameid' value='" + gameid + "'/>"
            + "<input type='hidden' name='csid' value='16113'/>"
            + "<input type='hidden' name='serverid' value='16113'/>"
            + "<input type='hidden' name='theme' value='iGamingA'/>"
            + "<input type='hidden' name='variant' value='instantplay'/>";
        return gameLinkCode+ "<script>$(function(){$('#launchSlot').submit();}); </script>";*/
        String actionLink = "https://redirect.CONTDELIVERY.COM/Casino/Default.aspx?applicationid=1023&sext1="
                            + accountNumber + "&sext2="
                            + bf.decryptString(userInfo.getPasswd()) + "&csid=16113&serverid=16113&gameid="
                            + gameid +"&ul=zh&theme=igamingA4&usertype=0&variant=instantplay";
        return actionLink;
    }
    
    /**
     * 进入真人游戏
     * @param userInfo
     * @return
     */
    public String launchLiveGame(UserInfo userInfo) {
        Blowfish bf = new Blowfish(WebConst.DECRYPT_KEY);
        //给用户加上前缀
        String accountNumber = WebConst.MG_API_USERNAME_PREFIX_NEW + userInfo.getAccount();
        String gameLinkCode = "<script src='https://code.jquery.com/jquery-1.10.1.js'></script>"
                + "<form method='POST' name='loginform' id='loginform'"
//                + " action='https://etiloader3.valueactive.eu/ETILoader/default.aspx'> "
                + " action='https://livegames.gameassists.co.uk/ETILandingPage/'> "
                + "<input type='hidden' name='LoginName' id='LoginName' value='"
                + accountNumber
                + "'/> "
                + "<input type='hidden' name='Password' id='Password' value='"
                + bf.decryptString(userInfo.getPasswd())
                + "'/> "
                + "<input type='hidden' name='CasinoID' id='CasinoID' value='16113' />"
                + "<input type='hidden' name='UL' id='UL' value='zh-cn'/>"
                + "<input type='hidden' name='ActiveCurrency' id='ActiveCurrency' value='Credits' />"
                + "<input type='hidden' name='VideoQuality' id='VideoQuality' value='AutoSD' />"
                + "<input type='hidden' name='BetProfileID' id='BetProfileID' value='0' />"
                + "<input type='hidden' name='ProductID' id='ProductID' value='2' />"
                + "<input type='hidden' name='UserType' id='UserType' value='0' />"
                + "<input type='hidden' name='ClientType' id='ClientType' value='4' />"
                + "<input type='hidden' name='ModuleID' id='ModuleID' value='70004' />"
                + "<input type='hidden' name='ClientID' id='ClientID' value='4' />"
                + "<input type='hidden' name='StartingTab' id='StartingTab' value='default' />"
                + "<input type='hidden' name='CustomLDParam' id='CustomLDParam' value='NULL' /></form>";

        return gameLinkCode+ "<script>$(function(){$('#loginform').submit();}); </script>";
    }
    
    public static void main(String[] args) throws ApiException {
    	getBettingProfileListTest();
//        isAuthenticateTest();
//        addAccountTest();
//        getAccountBalanceTest();
//        editAccountTest();
//        withdrawlTest();
        //depositTest();
        
//        getAccountDetails();
        
    }
    
    public static void depositTest() throws ApiException{
        MgGameAPI test;
        try {
            test = new MgGameAPI();
            UserInfo userInfo = new UserInfo();
            userInfo.setAccount("day200");
            userInfo.setPasswd("36b1b4b524f4ace3ceadcfe87dd42d827226a909e1c56823");
            BigDecimal amount = new BigDecimal(1.01);
            
//            boolean result = test.deposit(userInfo,amount,new GameTransfer());
//            System.out.println("result:" + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    public static void withdrawlTest(){
        MgGameAPI test;
        try {
            test = new MgGameAPI();
            UserInfo userInfo = new UserInfo();
            userInfo.setAccount("testdxy01");
            BigDecimal amount = new BigDecimal(1.01);
            boolean result = test.withdrawal(userInfo,amount);
            System.out.println("result:" + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    
    public static void getAccountBalanceTest(){
        MgGameAPI test;
        try {
            test = new MgGameAPI();
            String userName = "day200";
            JSONObject json = test.getAccountBalance(userName);
//            JsonNode node = test.getAccountDetails(userName);
            System.out.println("balance:" + json.getString("balance"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void getBettingProfileListTest(){
        MgGameAPI test;
        try {
            test = new MgGameAPI();
            String userName = "qb1234";
            System.out.println(test.getBettingProfileList(userName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void editAccountTest(){
        MgGameAPI test;
        try {
            test = new MgGameAPI();
            UserInfo userInfo = new UserInfo();
            userInfo.setAccount("menu2016");
            userInfo.setPasswd("123456");
            ResultMsg rm = test.editAccount(userInfo);
//            JsonNode node = test.getAccountDetails(userName);
            System.out.println("rm:" + rm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void getAccountDetails(){
        MgGameAPI test;
        try {
            test = new MgGameAPI();
            JsonNode node= test.getAccountDetails("day200");
            System.out.println(new BigDecimal(node.get("Balance").asText()));
//            System.out.println("rm:" + JsonUtils.toJson(node));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void isAuthenticateTest(){
        MgGameAPI test;
        try {
            test = new MgGameAPI();
            boolean authenticateResult = test.isAuthenticate();
            System.out.println("authenticateResult:" + authenticateResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void addAccountTest(){
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount("xiaogao");
        userInfo.setPasswd("dbc354b27467d3f0d732edbbd7f1e3234c6eaae304470f2f");//123456
        MgGameAPI test;
        try {
            test = new MgGameAPI();
            String result = test.createPlayer(userInfo);
            System.out.println("result:"+result);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public Map<String, Object> getHeader() {
        return header;
    }

    public void setHeader(Map<String, Object> header) {
        this.header = header;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }

    public String getHeaderElement() {
        return headerElement;
    }

    public void setHeaderElement(String headerElement) {
        this.headerElement = headerElement;
    }

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
}