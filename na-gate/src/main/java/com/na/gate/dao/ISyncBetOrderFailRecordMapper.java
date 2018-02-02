package com.na.gate.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.na.gate.entity.SyncBetOrderFailRecord;

/**
 * @author Andy
 * @version 创建时间：2017年11月10日 下午1:50:32
 */
@Mapper
public interface ISyncBetOrderFailRecordMapper {
	
	@Select("SELECT * FROM sync_betorder_fail_record GROUP BY round_id")
	List<SyncBetOrderFailRecord> findAll();

	@Insert("INSERT INTO `sync_betorder_fail_record` (`round_id`) values(#{roundId})")
	void add(@Param("roundId") Long roundId);

	@Delete("DELETE from sync_betorder_fail_record where round_id=#{roundId}")
	void delete(Long roundId);

}
