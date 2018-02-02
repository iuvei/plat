package com.na.manager.dao;

import com.na.manager.entity.Advertise;
import com.na.manager.entity.AdvertisePicture;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */
@Mapper
public interface IAdvertiseMapper {

    void insert(Advertise advertise);

    void batchInsert(List<AdvertisePicture> advertisePictures);

    void update(Advertise advertise);

    void batchUpdate(List<AdvertisePicture> advertisePictures);
    @Delete("delete from advertise where id = #{id}")
    void deleteAdvertiseById(@Param("id") Integer id);
    @Delete("delete from advertise_picture where advertise_id = #{id}")
    void deleteAdvertisePicturesByAdvertiseId(@Param("id")Integer id);
    @Select("select * from advertise")
    List<Advertise> search();
    @Select("select * from advertise_picture where advertise_id = #{id}")
    List<AdvertisePicture> searchByAdvertiseId(@Param("id")Integer id);
}
