package com.gameportal.manage.member.service.impl;

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
import com.gameportal.manage.exception.ApiException;
import com.gameportal.manage.member.dao.GameTransferDao;
import com.gameportal.manage.member.model.GameTransfer;
import com.gameportal.manage.member.model.MemberInfo;
import com.gameportal.manage.member.service.IMemberInfoService;
import com.gameportal.manage.pay.dao.PayOrderDao;
import com.gameportal.manage.pay.model.PayOrderLog;
import com.gameportal.manage.user.model.AccountMoney;
import com.gameportal.manage.user.model.UserInfo;
import com.gameportal.manage.util.Blowfish;
import com.gameportal.manage.util.ResultMsg;
import com.gameportal.manage.util.WebConstants;
import com.gameportal.portal.util.DateUtil;
import com.gameportal.portal.util.MgSoapPacket;
import com.gameportal.portal.util.PropertiesUtils;

import net.sf.json.JSONObject;

@Service("mgGameAPI")
public class MgGameAPI {
    
    @Resource(name = "memberInfoServiceImpl")
    private IMemberInfoService userInfoService = null;
    @Resource(name = "gameTransferDao")
    private GameTransferDao gameTransferDao = null;
    @Resource(name = "memberInfoServiceImpl")
    private IMemberInfoService memberInfoService = null;
    @Resource(name = "payOrderDao")
    private PayOrderDao payOrderDao = null;
    
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

    private Integer currency ;

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
        //SessionGUID=07277398-f360-468a-b6d1-c1693b1bb89c
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
            //			throw new  GameProviderException(ErrorMsg.MG_PROCESSING_ERROR);
            logger.error("MG认证异常！");
            return false;
        }
        Map<String, Object> header = new HashMap<String, Object>();
        header.put("SessionGUID", this.sessionGuid);
        header.put("ErrorCode", (Integer) 0);
        header.put("IPAddress", this.ipAddress);
        header.put("IsExtendSession", Boolean.TRUE);
        this.header = header;
        logger.info("isAuthenticate 结果信息:" + header);
        return (this.sessionGuid != null);
    }
    
    
    /**
     * 查询账户详细信息
     * @param playerAccount 此账号未加前缀
     * @return null:认证不通过  {"ErrorMessage":null,"IsSucceed":"true","ErrorCode":"0","ErrorId":null,"AccountNumber":"testdxy01","FirstName":"testdxy01","LastName":"testdxy01","NickName":null,"PokerAlias":null,"MobileNumber":null,"RelatedProduct":"Casino","AccountStatus":"Open","SuspendAccountStatus":"Open","LastEditDate":"2016-03-16T10:19:34","EMail":null,"CreditBalance":"0","Balance":"0","ProfileId":"32","RngBettingProfileId":"0","RngBettingProfileStatus":null}
     */
    public JsonNode getAccountDetails(String playerAccount){
        ObjectNode playerInfo = null ;
        if (this.isAuthenticate()) {
            playerInfo = JsonNodeFactory.instance.objectNode();
            this.apiName="GetAccountDetails";
            //给用户名加上前缀
            String accountNumber = WebConstants.MG_API_USERNAME_PREFIX + playerAccount;
            if(playerAccount.startsWith("QB77")){
            	accountNumber=playerAccount;
            }
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
     * 修改账户信息
     * @param userInfo
     * @return
     */
    public ResultMsg editAccount(MemberInfo userInfo){
        ResultMsg result = new ResultMsg();
        result.setCode("0");
        if(userInfo == null){
            result.setMessage("需要修改的用户信息为空！！");
            logger.info("MG editAccount " + result.toString());
            return result;
        }
        ObjectNode playerInfo = null ;
        if (this.isAuthenticate()) {
            playerInfo = JsonNodeFactory.instance.objectNode();
            this.apiName="EditAccount";
            //给用户名加上前缀
            String accountNumber = WebConstants.MG_API_USERNAME_PREFIX + userInfo.getAccount();
            if(userInfo.getAccount().startsWith("QB77")){
            	accountNumber=userInfo.getAccount();
            }
            Map<String, Object> body = new HashMap<String, Object>();
            body.put("accountNumber", accountNumber);
            body.put("password", userInfo.getPasswd());
            
            this.body = body;
            logger.info("MG修改账号信息请求参数："+JSONObject.fromObject(this).toString());
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
     * 查询账户余额
     * @param playerAccount
     * @return
     */
    public BigDecimal getAccountBalance_back(String playerAccount){
        BigDecimal balance = null;
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
    public BigDecimal getAccountBalance(String playerAccount){
        BigDecimal balance = null;
        ObjectNode playerInfo = null ;
        if (this.isAuthenticate()) {
            playerInfo = JsonNodeFactory.instance.objectNode();
            this.apiName="GetAccountBalance";
            //给用户名加上前缀
            String accountNumber = WebConstants.MG_API_USERNAME_PREFIX + playerAccount;
            if(playerAccount.startsWith("QB77")){
            	accountNumber=playerAccount;
            }
            Map<String, Object> body = new HashMap<String, Object>();
            body.put("delimitedAccountNumbers", accountNumber);
            this.body = body;
            logger.info("MG getAccountBalance start!!   accountNumber:"+ accountNumber );
            NodeList response = new MgSoapPacket().sendPacket(this).item(0).getChildNodes();
            logger.info("MG getAccountBalance end!!   accountNumber:"+ accountNumber + ";response:" +response);
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
        logger.info("MG getAccountBalance playerAccount:"+ playerAccount + "；结果：" + playerInfo);
        if(playerInfo == null){
            return null;
        }
        JsonNode result = playerInfo.get("IsSucceed");
        if(result == null){
            logger.info("MG getAccountBalance  无IsSucceed 字段!");
            return null;
        }
        Boolean isSucceed = Boolean.parseBoolean(result.textValue());
        if(isSucceed){
            balance = new BigDecimal(playerInfo.get("Balance").asDouble()).setScale(4,BigDecimal.ROUND_DOWN);
        }
        logger.info("getAccountBalance Balance: " + balance);
        return balance;
    }
    
    /**
     * 创建MG玩家账号
     * return 创建结果  0：成功(包括已存在)  -1：失败
     * */
    public String createPlayer(UserInfo userInfo) {
        ObjectNode playerInfo = JsonNodeFactory.instance.objectNode();
        if (this.isAuthenticate()) {
            this.apiName="AddAccount";
            String accountNumber = WebConstants.MG_API_USERNAME_PREFIX + userInfo.getAccount();
            Map<String, Object> body = new HashMap<String, Object>();
            if(userInfo.getAccount().startsWith("QB77")){
            	 body.put("firstName", "QB77");  //前缀内容
                 body.put("lastName", userInfo.getAccount().substring(4));   //内容任意             
                 body.put("accountNumber", userInfo.getAccount()); 
            }else{
            	body.put("firstName", WebConstants.MG_API_USERNAME_PREFIX);  //前缀内容
            	body.put("lastName", userInfo.getAccount());   //内容任意             
            	body.put("accountNumber", accountNumber);        
            }
            Blowfish bf = new Blowfish(WebConstants.BLOWFISH_CODE);
            body.put("password", bf.decryptString(userInfo.getApipassword())); //需要解密成原密码
            body.put("isSendGame", Boolean.FALSE); //When True a link is sent by email or SMS/WapPush (as applicable) to download the game. If not stated in XML, then “true” value is used by default. 
            body.put("BettingProfiledId", (Integer) 1); //A list of available profiles obtained from GetBettingProfileList()[说明game的情况的函数] method by “LGBetProfile” category
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
     * 用户从MG提现
     * @param playerAccount
     * @param amount
     * @return
     * @throws MalformedURLException 
     * @throws ApiException 
     */
    public boolean withdrawal(UserInfo userInfo, BigDecimal amount, GameTransfer gameTransfer) throws ApiException {
        if(amount.compareTo(new BigDecimal(0.01))<0){
            logger.error("MG withdrawal 提现金额不能小于0.01。amount：" + amount);
            return false;
        }
        ObjectNode playerInfo = JsonNodeFactory.instance.objectNode();
        BigDecimal balanceBefore = getAccountBalance(userInfo.getAccount());// 查询用户游戏平台的余额信息
        if(balanceBefore == null){
            logger.error("MG withdrawal 认证不通过！" );
            return false;
        }
        this.apiName="Withdrawal";
        //给用户加上前缀
        String accountNumber = WebConstants.MG_API_USERNAME_PREFIX + userInfo.getAccount();
        if(userInfo.getAccount().startsWith("QB77")){
        	accountNumber =userInfo.getAccount();
        }
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("accountNumber", accountNumber);
        body.put("amount", amount);
        this.body=body;
        NodeList response = null;
        boolean result = false;
        try {
            response = new MgSoapPacket().sendPacket(this).item(0).getChildNodes();
        } catch (Exception e) {//如果出现异常  可能成功也可能失败，需要再次调用余额查询确定转账结果。
            e.printStackTrace();
            BigDecimal balance = getAccountBalance(userInfo.getAccount());
            logger.info("MG withdrawal balanceBefore:"+balanceBefore+";amount:"+amount+";balance:"+balance);
            if(balance == null || balance.compareTo(balanceBefore)==0){//操作失败！
                return false;
            }else{
                result = true;
            }
        }
        if(!result){
            for (int i = 0; i < response.getLength(); i++) {
                String noddeValue = null;
                if(response.item(i).getFirstChild()!=null){
                    noddeValue = response.item(i).getFirstChild().getNodeValue();
                }
                playerInfo.put(response.item(i).getNodeName(), noddeValue); 
            }
            
            logger.info("MG withdrawal 结果信息:"+playerInfo);
            result = Boolean.parseBoolean(playerInfo.get("IsSucceed").textValue());
        }
        
        if(result){//如果转账成功 则修改用户余额、订单状态及添加流水
            String balanceAfter = playerInfo.get("Balance").textValue(); 
          //修改用户余额、订单状态及添加流水
            transferAfter(userInfo, amount.intValue(), gameTransfer, balanceAfter);
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
    public boolean deposit(UserInfo userInfo, BigDecimal amount, GameTransfer gameTransfer) throws ApiException {
        if(amount.compareTo(new BigDecimal(0.01))<0){
            logger.error("MG deposit 存款金额不能小于0.01。amount：" + amount);
            throw new ApiException("存款金额不能小于0.01");
        }
        ObjectNode playerInfo = JsonNodeFactory.instance.objectNode();
        
        // 游戏帐号不存在，程序自动创建
        String createPlayerResult = (String) createPlayer(userInfo);
        if(!createPlayerResult.equals("0")){
            logger.error("MG-deposit 创建账户时失败！" );
            throw new ApiException("创建MG账户失败！");
        }
        BigDecimal balanceBefore = getAccountBalance(userInfo.getAccount());// 查询用户游戏平台的余额信息
        if(balanceBefore == null){
            logger.error("MG deposit 认证不通过！" );
            throw new ApiException("认证不通过！");
        }
        this.apiName="Deposit";
        //给用户加上前缀
        String accountNumber = WebConstants.MG_API_USERNAME_PREFIX + userInfo.getAccount();
        if(userInfo.getAccount().startsWith("QB77")){
        	accountNumber =userInfo.getAccount();
        }
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("accountNumber", accountNumber);
        body.put("amount", amount);
        body.put("currency", this.currency);
        this.body=body;
        NodeList response = null;
        boolean result = false;
        try {
            response = new MgSoapPacket().sendPacket(this).item(0).getChildNodes();
        } catch (Exception e) {//如果出现异常  可能成功也可能失败，需要再次调用余额查询确定转账结果。
            logger.error("MG deposit 异常"+e.getMessage());
            BigDecimal balance = getAccountBalance(userInfo.getAccount());
            logger.info("MG deposit balanceBefore:"+balanceBefore+";amount:"+amount+";balance:"+balance);
            if(balance == null || balance.compareTo(balanceBefore)==0){//操作失败！
                return false;
            }else{
                result = true;
            }
        }
        if(!result){
            for (int i = 0; i < response.getLength(); i++) {
                String noddeValue = null;
                if(response.item(i).getFirstChild()!=null){
                    noddeValue = response.item(i).getFirstChild().getNodeValue();
                }
                playerInfo.put(response.item(i).getNodeName(), noddeValue); 
            }
            
            logger.info("MG deposit 结果信息:"+playerInfo);
            result = Boolean.parseBoolean(playerInfo.get("IsSucceed").textValue());
        }
        if(result){
            String balanceAfter = playerInfo.get("Balance").textValue(); 
            //如果转账成功 则修改用户余额、订单状态及添加流水
            transferAfter(userInfo, amount.intValue()*-1, gameTransfer, balanceAfter);
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
        BigDecimal amount = accountMoney.getTotalamount().add(new BigDecimal(transferNum)).setScale(2, BigDecimal.ROUND_HALF_UP);
        accountMoney.setTotalamount(amount);//transferNum:存款为负  提现为正
        accountMoney.setUpdateDate(new Date());
        boolean codes = userInfoService.updateAccountMoneyObj(accountMoney);
        if (codes == false) {
            gameTransfer.setStatus(2);
            gameTransferDao.update(gameTransfer);
            throw new ApiException("更新钱包操作失败！");
        }
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
        payOrderDao.insertPayLog(log);
    }
    
    
    public static void main(String[] args) {
//        isAuthenticateTest();
//        addAccountTest();
        getAccountBalanceTest();
//        withdrawlTest();
//        depositTest();
    }
    
    public static void getAccountBalanceTest(){
        MgGameAPI test;
        try {
            test = new MgGameAPI();
            String userName = "day200";
            BigDecimal balance = test.getAccountBalance(userName);
            System.out.println("balance:" + balance);
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
    
    
}