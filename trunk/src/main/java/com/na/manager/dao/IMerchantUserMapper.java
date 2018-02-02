package com.na.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.na.manager.entity.MerchantUser;

/**
 * 商户
 * 
 * @author alan
 * @date 2017年6月23日 上午10:50:08
 */
@Mapper
public interface IMerchantUserMapper {
	
	void add(MerchantUser merchantUser);
	
	void update(MerchantUser merchantUser);
	
	@Select("select * from merchant_user m join live_user l on l.user_id = m.user_id join user on id = m.user_id where m.user_id=#{userId}")
	MerchantUser fingById(@Param("userId") long userId);
	
	List<MerchantUser> search(MerchantUser merchantUser);
	
	@Select("select count(1) from merchant_user where merchant_prefix=#{merchantPrefix}")
	long isRepeatMerchantPerfix(@Param("merchantPrefix") String merchantPrefix);
	
	@Select("select * from merchant_user m join live_user l on l.user_id = m.user_id join user on id = m.user_id where m.number=#{number} and m.private_key=#{privateKey}")
	MerchantUser getMerchantUser(@Param("number") String number, @Param("privateKey") String privateKey);

}
