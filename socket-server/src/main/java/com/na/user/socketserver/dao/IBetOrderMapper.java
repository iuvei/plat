package com.na.user.socketserver.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.na.baccarat.socketserver.entity.BetOrder;

/**
 * Created by Administrator on 2017/4/26 0026.
 */
@Mapper
public interface IBetOrderMapper {

	@Select("select * from bet_order where game_table_id = #{tableId} and round_id = #{roundId}")
	List<BetOrder> getBetOrderByTable(@Param("tableId") int tableId,@Param("roundId") long roundId);
	
	@Select("select * from bet_order where game_table_id = #{tableId} and bet_type = #{betType} and round_id = #{roundId}")
	List<BetOrder> getBetOrderByTableAndType(@Param("tableId") int tableId, @Param("betType") int type, @Param("roundId") long roundId);
	
    @Select("select * from bet_order where virtualgametable_id = #{vtableId} and round_id = #{roundId}")
    List<BetOrder> getBetOrderByVirtualTable(@Param("vtableId") int vtableId,@Param("roundId") long roundId);
    
    @Select("select * from bet_order where round_id = #{roundId}")
    List<BetOrder> getBetOrderByRound(@Param("roundId") long roundId);
    
    @Update("UPDATE bet_order SET user_id=#{userId}, login_name=#{loginName}, trade_item_id=#{tradeItemId}, game_id=#{gameId}, "
    		+ "game_table_id=#{gameTableId}, virtualgametable_id=#{virtualgametableId}, play_id=#{playId}, boots_id=#{bootsId},"
    		+ "round_id=#{roundId}, round_num=#{roundNum}, boots_num=#{bootsNum}, amount=#{amount},"
    		+ "bet_rate=#{betRate}, wash_percentage=#{washPercentage}, o_percentage=#{oPercentage}, bet_time=#{betTime}, status=#{status},"
    		+ "win_lost_status=#{winLostStatus}, win_lost_amount=#{winLostAmount}, settle_rate=#{settleRate},"
    		+ "settle_time=#{settleTime}, valid_amount=#{validAmount}, user_pre_balance=#{userPreBalance}, "
    		+ "round_result=#{roundResult}, source=#{source}, bet_type=#{betType}, table_type=#{tableType} WHERE id = #{id}")
    int update(BetOrder order);


    public void addBetOrder(BetOrder betOrder);
    
    public void batchAddBetOrder(List<BetOrder> orderList);
    
    public int batchUpdate(List<BetOrder> orderList);
    
    @Select("select * from bet_order where game_table_id = #{tableId} and round_id = #{roundId} and source = #{source}")
	List<BetOrder> getBetOrderByTableSource(@Param("tableId") int tableId,@Param("roundId") long roundId, @Param("source") int source);

    @Select("select user_id as userId ,trade_item_key as tradeItemKey ,amount from bet_order where (trade_item_key = 'B' or trade_item_key = 'P') and round_id = #{roundId} and source = 1 ")
	List<BetOrder> getBetOrderByRoundNoTradeItem(Long rid);
    
    int getBetOrderByUserCount(@Param("userId") Long userId, @Param("beginDate") String beginDate, @Param("endDate") String endDate);
    
	List<Map> getBetOrderByUser(@Param("userId") Long userId, @Param("begin") int begin, @Param("end") int end,
			@Param("beginDate") String beginDate, @Param("endDate") String endDate);
    
    @Select("select count(1) from bet_order where user_id = #{userId} and round_id = #{round_id} and win_lost_status is NULL")
    int getBetOrderByRoundCount(@Param("userId") Long userId, @Param("round_id") Long round_id);
    
    @Select(" select count(*) from bet_order where user_id=#{userId} and status in(1,2) and bet_time>=DATE_ADD(NOW(),INTERVAL -1 HOUR) ")
    int findUnSettleCount(@Param("userId") Long userId);
}
