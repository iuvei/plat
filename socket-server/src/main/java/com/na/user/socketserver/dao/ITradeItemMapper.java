package com.na.user.socketserver.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.na.baccarat.socketserver.entity.TradeItem;

/**
 * Created by Administrator on 2017/4/26 0026.
 */
@Mapper
public interface ITradeItemMapper {

    @Select("select * from trade_item where id = #{id}")
    TradeItem getTradeItemById(@Param("id") int id);
    
    @Select("select * from trade_item where play_id = #{playId}")
    List<TradeItem> getByPlay(@Param("playId") int playId);
    
}
