package com.na.manager.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.na.manager.bean.NaResponse;
import com.na.manager.bean.RoundCorrectDataRequest;
import com.na.manager.common.annotation.Auth;
import com.na.manager.facade.IFinancialFacade;
import com.na.manager.service.IGameTableService;
import com.na.manager.service.IRoundService;

/**
 * Abnormal Table Manage
 * @author v
 *
 */
@RestController
@RequestMapping("/abnormalTableManage")
@Auth("AbnormalTableManager")
public class AbnormalTableAction {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IGameTableService gameTableService;
	@Autowired
	private IRoundService roundService;
	@Autowired
    private IFinancialFacade financialFacade;
	@SuppressWarnings("unchecked")
	@PostMapping("/search")
	public NaResponse<Object> searchGameTable(Integer gameId,String name,Integer page,Integer rows){
		try {
			return NaResponse.createSuccess(gameTableService.listAbnormalTables(gameId,name));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return NaResponse.createError(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/searchRound")
	public NaResponse<Object> searchRound(Integer tid){
		try {
			Preconditions.checkNotNull(tid,"param.null");
			return NaResponse.createSuccess(roundService.listAbnormalTableRound(tid));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return NaResponse.createError(e.getMessage());
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@PostMapping("/correctData")
	public NaResponse<Object> correctData(@RequestBody RoundCorrectDataRequest param){
		try {
			Preconditions.checkNotNull(param.getRid(), "param.null");
			
			if(param.getGameId() == 3 && !Strings.isNullOrEmpty(param.getResult())){
				Boolean isCorrect = roundService.updateRoundAndRoundExt(param);
				if(isCorrect)
					return NaResponse.createSuccess();
				else
					return NaResponse.createError();
			}
			
			Preconditions.checkArgument(!(Strings.isNullOrEmpty(param.getBankCard1Mode())||Strings.isNullOrEmpty(param.getBankCard2Mode())),"param.error");
			Preconditions.checkArgument(!(Strings.isNullOrEmpty(param.getPlayerCard1Mode())||Strings.isNullOrEmpty(param.getPlayerCard2Mode())),"param.error");
			Preconditions.checkArgument(!(param.getBankCard1Number() == 0 || param.getBankCard2Number()== 0),"param.error");
			Preconditions.checkArgument(!(param.getPlayerCard1Number() == 0 || param.getPlayerCard2Number() == 0),"param.error");
			Preconditions.checkArgument(!(Strings.isNullOrEmpty(param.getBankCard3Mode())&& param.getBankCard3Number() != null && param.getBankCard3Number() != 0),"param.error");
			Preconditions.checkArgument(!(!Strings.isNullOrEmpty(param.getBankCard3Mode())&& param.getBankCard3Number() == null && param.getBankCard3Number() == 0),"param.error");
			Preconditions.checkArgument(!(Strings.isNullOrEmpty(param.getPlayerCard3Mode()) && param.getPlayerCard3Number() != null && param.getPlayerCard3Number() != 0),"param.error");
			Preconditions.checkArgument(!(!Strings.isNullOrEmpty(param.getPlayerCard3Mode()) && param.getPlayerCard3Number() == null && param.getPlayerCard3Number() == 0),"param.error");

			Boolean isCorrect = roundService.updateRoundAndRoundExt(param);
			Preconditions.checkArgument(isCorrect);
			return NaResponse.createSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return NaResponse.createError(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/settlement")
	public NaResponse<Object> settleBetOrders(@RequestBody RoundCorrectDataRequest param){
		try {
			Preconditions.checkNotNull(param.getRid(), "param.null");
			return roundService.settleBetOrders(param);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return NaResponse.createError(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/refund")
	public NaResponse<Object> invalidBetOrders(@RequestBody RoundCorrectDataRequest param){
		try {
			Preconditions.checkNotNull(param.getGameTableId(), "param.null");
			Preconditions.checkNotNull(param.getRid(), "param.null");
			financialFacade.invalidBetOrders(param);
			return NaResponse.createSuccess();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return NaResponse.createError(e.getMessage());
		}
	}
	
	
}
