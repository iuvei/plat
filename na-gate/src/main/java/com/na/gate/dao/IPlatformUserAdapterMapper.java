package com.na.gate.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.na.gate.entity.PlatformUserAdapter;
import com.na.gate.util.SimpleSelectInExtendedLanguageDriver;

/**
 * Created by sunny on 2017/7/26 0026.
 */
@Mapper
public interface IPlatformUserAdapterMapper {
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    @Insert("insert into `platform_user_adapter` (`type`,`platform_user_id`,`live_user_id`,platform_user_name,platform_parent_id)\n" +
            "values (#{type},#{platformUserId},#{liveUserId},#{platformUserName},#{platformParentId})")
    void add(PlatformUserAdapter userAdapter);

    @Select("SELECT * FROM platform_user_adapter WHERE platform_user_id=#{platformUserId} and type=#{type}")
    PlatformUserAdapter findBy(@Param("platformUserId") String platformUserId, @Param("type") int type);

    @Select("SELECT * FROM platform_user_adapter WHERE platform_user_id=#{platformUserId} and type!=3")
    PlatformUserAdapter findMerchantBy(@Param("platformUserId") String platformUserId);
    
    @Select("SELECT * FROM platform_user_adapter WHERE platform_parent_id=#{platformParentId} and type!=3")
    PlatformUserAdapter findMerchantByParentId(@Param("platformParentId") String platformParentId);

    @Select("SELECT * FROM platform_user_adapter WHERE live_user_id=#{liveUserId}")
    PlatformUserAdapter findByLiveUserId(Long liveUserId);

    @Select("SELECT * FROM platform_user_adapter WHERE platform_user_name=#{platformUserName}")
    PlatformUserAdapter findByPlatformUserName(String platformUserName);

    @Lang(SimpleSelectInExtendedLanguageDriver.class)
    @Select("SELECT * FROM platform_user_adapter WHERE live_user_id in (#{liveUserIds})")
    List<PlatformUserAdapter> findByLiverUserIds(@Param("liveUserIds") Set<Long> liveUserIds);
    
    @Select("SELECT * FROM platform_user_adapter")
    List<PlatformUserAdapter> findAll();
}
