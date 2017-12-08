package com.gameportal.web.user.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gameportal.web.game.model.GameTransfer;
import com.gameportal.web.game.service.IGameTransferService;
import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.user.model.BetLog;
import com.gameportal.web.user.model.PayOrder;
import com.gameportal.web.user.model.UserInfo;
import com.gameportal.web.user.service.IReportQueryService;
import com.gameportal.web.user.service.IUserInfoService;
import com.gameportal.web.util.CookieUtil;
import com.gameportal.web.util.DateUtil;
import com.gameportal.web.util.PageTool;
import com.gameportal.web.util.WebConst;

@Controller
@RequestMapping(value = "/manage/reportQuery")
public class ReportQueryManageController{
    @Resource(name = "userInfoServiceImpl")
    private IUserInfoService userInfoService = null;
    @Resource(name="gameTransferServiceImpl")
    private IGameTransferService gameTransferService;
    @Resource(name = "redisServiceImpl")
    private IRedisService iRedisService = null;
    @Resource(name = "reportQueryServiceImpl")
    private IReportQueryService iReportQueryService = null;

    private static final Logger logger = Logger
            .getLogger(ReportQueryManageController.class);

    private static final int outinRecordPageSize = 10;

    public ReportQueryManageController() {
        super();
    }

    @RequestMapping(value = "/outinRecord")
    public String userDeposit(
            @RequestParam(value = "starttime", required = false) String starttime,
            @RequestParam(value = "endtime", required = false) String endtime,
            @RequestParam(value = "paytyple", required = false) Integer paytyple,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            HttpServletRequest request, HttpServletResponse response) {
        String vuid = CookieUtil.getOrCreateVuid(request, response);
        paytyple = StringUtils.isNotBlank(ObjectUtils.toString(paytyple)) ? paytyple
                : -1;
        status = StringUtils.isNotBlank(ObjectUtils.toString(status)) ? status
                : -1;
        pageNo = StringUtils.isNotBlank(ObjectUtils.toString(pageNo)) ? pageNo
                : 1;
        UserInfo userInfo = iRedisService.getRedisResult(vuid
                + "GAMEPORTAL_USER", UserInfo.class);
        if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {

            List<PayOrder> payOrderList = iReportQueryService.queryPayOrder(
                    userInfo.getUiid(), starttime, endtime, paytyple, status,pageNo,outinRecordPageSize);
            Long total = iReportQueryService.queryPayOrderCount(
                    userInfo.getUiid(), starttime, endtime, paytyple, status);
            request.setAttribute("payOrderList", payOrderList);
            long pageCount = PageTool.getPage(total, outinRecordPageSize);
            request.setAttribute("total", total);
            request.setAttribute("pageCount", pageCount);
            request.setAttribute("pageNo", pageNo);
            request.setAttribute("starttime", starttime);
            request.setAttribute("endtime", endtime);
            request.setAttribute("paytyple", paytyple);
            request.setAttribute("status", status);
            return "/manage/reportquery/outinRecord";

        }
        return "";
    }

    /**
     * 游戏下注记录
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/betRecord")
    public String betRecord(
            @RequestParam(value = "starttime", required = false) String starttime,
            @RequestParam(value = "endtime", required = false) String endtime,
            @RequestParam(value = "betflag", required = false) String betflag,
            @RequestParam(value = "platformcode", required = false) String platformcode,
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            HttpServletRequest request,HttpServletResponse response) {
        /*if(platformcode != null) {
            try {
                //解决乱码
                platformcode = new String (platformcode.getBytes("iso8859-1"),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.error("乱码转换异常。"+e);
            }
        }*/
        pageNo = StringUtils.isNotBlank(ObjectUtils.toString(pageNo)) ? pageNo : 1;
        String vuid = CookieUtil.getOrCreateVuid(request, response);
        UserInfo userInfo = iRedisService.getRedisResult(vuid
                + "GAMEPORTAL_USER", UserInfo.class);
        String startDate = starttime;
        String endDate = endtime;
        if (StringUtils.isNotBlank(ObjectUtils.toString(userInfo))) {
            Map<String, Object> params = new HashMap<String, Object>();
            if (StringUtils.isEmpty(starttime)) {
                //为空把日期推到7天前
                params.put("starttime", DateUtil.getStrByDate(new Date(), "yyyy-MM-dd")+" "+WebConst.startt);
                if(null == starttime) {
                    startDate = DateUtil.getStrByDate(DateUtil.getAddDate(new Date(), -7),"yyyy-MM-dd");
                }
            }else{
                params.put("starttime", starttime + " " + WebConst.startt);
            }
            if (StringUtils.isEmpty(endtime)) {
                params.put("endtime", DateUtil.getStrByDate(new Date(), "yyyy-MM-dd")+" "+WebConst.endt);
                if(null == endtime) {
                    endDate = DateUtil.getStrByDate(new Date(), "yyyy-MM-dd");
                }
            }else{
                params.put("endtime", endtime + " " + WebConst.endt);
            }
            if(StringUtils.isNotBlank(betflag)){
                params.put("flag", betflag);
            }
            if(StringUtils.isNotEmpty(platformcode)){
                params.put("platformcode", platformcode);
            }
            params.put("account", userInfo.getAccount());
            params.put("limitParams", (pageNo-1)*outinRecordPageSize+","+outinRecordPageSize);
            List<BetLog> gamebetlog = userInfoService.selectGameOrder(params);

            Long total = userInfoService.selectGameOrderCount(params);
            request.setAttribute("gamebetlog", gamebetlog);
            long pageCount = PageTool.getPage(total, outinRecordPageSize);
            request.setAttribute("total", total);
            request.setAttribute("pageCount", pageCount);
            request.setAttribute("pageNo", pageNo);
            request.setAttribute("starttime", startDate);
            request.setAttribute("endtime", endDate);
            request.setAttribute("betflag", betflag);
            request.setAttribute("platformcode", platformcode==null?"":platformcode);
            //(总计)下注金额总计
            BigDecimal betpointsTotal = new BigDecimal(0d);
            //(总计)有效投注金额
            BigDecimal availablebetTotal = new BigDecimal(0d);
            //(总计)输赢
            BigDecimal winorlossTotal = new BigDecimal(0d);
            /*统计当前页总数据*/
            if(gamebetlog != null && gamebetlog.size() > 0){
                for(BetLog bet : gamebetlog){
                    betpointsTotal = betpointsTotal.add(bet.getBetamount());
                    availablebetTotal = availablebetTotal.add(bet.getValidBetAmount());
                    winorlossTotal = winorlossTotal.add(bet.getProfitamount());
                }
            }
            request.setAttribute("betpointsTotal", betpointsTotal);
            request.setAttribute("availablebetTotal", availablebetTotal);
            request.setAttribute("winorlossTotal", winorlossTotal);
            params.put("limitParams", null);
            /***统计所有也数据*****/
            List<BetLog> gamebetlogAll = userInfoService.selectGameOrder(params);
            //下注金额总计
            BigDecimal betpointsAllPage = new BigDecimal(0d);
            //有效投注金额
            BigDecimal availablebetAllPage = new BigDecimal(0d);
            //输赢
            BigDecimal winorlossAllPage = new BigDecimal(0d);
            if(null != gamebetlogAll && gamebetlogAll.size() > 0){
                for(BetLog bet : gamebetlogAll){
                    betpointsAllPage = betpointsAllPage.add(bet.getBetamount());
                    availablebetAllPage = availablebetAllPage.add(bet.getValidBetAmount());
                    winorlossAllPage = winorlossAllPage.add(bet.getProfitamount());
                }
            }
            request.setAttribute("betpointsAllPage", betpointsAllPage);
            request.setAttribute("availablebetAllPage", availablebetAllPage);
            request.setAttribute("winorlossAllPage", winorlossAllPage);
        }
        return "/manage/reportquery/betRecord";
    }

    @RequestMapping(value = "/transferRecord")
    public String transferRecord(String starttime, String endtime, Integer pageNo, HttpServletRequest request, HttpServletResponse response) {
        String vuid = CookieUtil.getOrCreateVuid(request, response);
        UserInfo userInfo = iRedisService.getRedisResult(vuid + "GAMEPORTAL_USER", UserInfo.class);
        Map<String, Object> params = new HashMap<String, Object>();
        String startDate = starttime;
        String endDate = endtime;
        if (userInfo != null) {
            if (StringUtils.isEmpty(starttime)) {
                params.put("starttime", DateUtil.getStrByDate(DateUtil.getAddDate(new Date(), -7), "yyyy-MM-dd") + " " + WebConst.startt);
                if(null == starttime) {
                    startDate = DateUtil.getStrByDate(DateUtil.getAddDate(new Date(), -7), "yyyy-MM-dd");
                }
            } else {
                params.put("starttime", starttime + " " + WebConst.startt);
            }
            if (StringUtils.isEmpty(endtime)) {
                params.put("endtime", DateUtil.getStrByDate(new Date(), "yyyy-MM-dd") + " " + WebConst.endt);
                if(null == endtime) {
                    endDate = DateUtil.getStrByDate(new Date(), "yyyy-MM-dd");
                }
            } else {
                params.put("endtime", endtime + " " + WebConst.endt);
            }
            params.put("uuid", userInfo.getUiid());
        }
        try {
            params.put("sortColumns", "create_date desc");
            pageNo = StringUtils.isNotBlank(ObjectUtils.toString(pageNo)) ? pageNo : 1;
            params.put("limitParams", (pageNo-1)*outinRecordPageSize+","+outinRecordPageSize);
            List<GameTransfer> gameTransfers = gameTransferService.selectTranferOrder(params);
            Long total = gameTransferService.selectTranferOrderCount(params);
            long pageCount = PageTool.getPage(total, outinRecordPageSize);
            request.setAttribute("total", total);
            request.setAttribute("pageCount", pageCount);
            request.setAttribute("pageNo", pageNo);
            request.setAttribute("gameTransfers", gameTransfers);
            request.setAttribute("starttime", startDate);
            request.setAttribute("endtime", endDate);
        } catch (Exception e) {
            logger.info("查询转账记录异常。");
            e.printStackTrace();
        }
        return "/manage/reportquery/transferRecord";
    }

    @RequestMapping(value = "/noticeManage")
    public String noticeManage(HttpServletRequest request,
            HttpServletResponse response) {
	        return "/manage/reportquery/noticeManage";
    }

}
