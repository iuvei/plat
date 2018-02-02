package com.na.manager.service.impl;

import com.google.common.base.Preconditions;
import com.na.manager.bean.NaResponse;
import com.na.manager.dao.IAdvertiseMapper;
import com.na.manager.entity.Advertise;
import com.na.manager.entity.AdvertisePicture;
import com.na.manager.service.IAdvertiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * v
 *
 * @create 2017-07
 */
@Service
@Transactional(propagation= Propagation.NESTED,rollbackFor=Exception.class)
public class AdvertiseServiceImpl implements IAdvertiseService{

    @Autowired
    private IAdvertiseMapper advertiseMapper;

    @Override
    public List<Advertise> search() {
        List<Advertise> advertiseList = advertiseMapper.search();
        for (Advertise advertise:advertiseList) {
            List<AdvertisePicture> advertisePictures = advertiseMapper.searchByAdvertiseId(advertise.getId());
            advertise.setAdvertisePictures(advertisePictures);
        }
        return advertiseList;
    }

    @Override
    public NaResponse<Object> create(Advertise advertiseRequest) {
        Advertise advertise = new Advertise();
        Preconditions.checkNotNull(advertiseRequest.getPlatform(),"param.null");
        Preconditions.checkNotNull(advertiseRequest.getRemark(),"param.null");
        Preconditions.checkNotNull(advertiseRequest.getType(),"param.null");
        Preconditions.checkNotNull(advertiseRequest.getAdvertisePictures(),"picture.null");
        advertise.setPlatform(advertiseRequest.getPlatform());
        advertise.setRemark(advertiseRequest.getRemark().trim());
        advertise.setType(advertiseRequest.getType());
        advertiseMapper.insert(advertise);
        List<AdvertisePicture> advertisePictures = new ArrayList<>(advertiseRequest.getAdvertisePictures().size());
        for (AdvertisePicture advertisePicture:advertiseRequest.getAdvertisePictures()) {
            AdvertisePicture advePicture = new AdvertisePicture();
            advePicture.setAdvertiseId(advertise.getId());
            Preconditions.checkNotNull(advertisePicture.getOrder(),"param.null");
            Preconditions.checkNotNull(advertisePicture.getUrl(),"param.null");
            advePicture.setOrder(advertisePicture.getOrder());
            advePicture.setUrl(advertisePicture.getUrl());
            advertisePictures.add(advePicture);
        }
        if(advertisePictures.size()>0) advertiseMapper.batchInsert(advertisePictures);
        return NaResponse.createSuccess();
    }

    @Override
    public NaResponse<Object> update(Advertise advertiseRequest) {
        Advertise advertise = new Advertise();
        Preconditions.checkNotNull(advertiseRequest.getPlatform(),"param.null");
        Preconditions.checkNotNull(advertiseRequest.getRemark(),"param.null");
        Preconditions.checkNotNull(advertiseRequest.getType(),"param.null");
        Preconditions.checkNotNull(advertiseRequest.getAdvertisePictures(),"picture.null");
        advertise.setPlatform(advertiseRequest.getPlatform());
        advertise.setRemark(advertiseRequest.getRemark().trim());
        advertise.setType(advertiseRequest.getType());
        advertise.setId(advertiseRequest.getId());
        advertiseMapper.update(advertise);
        advertiseMapper.deleteAdvertisePicturesByAdvertiseId(advertise.getId());
        List<AdvertisePicture> advertisePictures = new ArrayList<>(advertiseRequest.getAdvertisePictures().size());
        for (AdvertisePicture advertisePicture:advertiseRequest.getAdvertisePictures()) {
            AdvertisePicture advePicture = new AdvertisePicture();
            advePicture.setAdvertiseId(advertise.getId());
            Preconditions.checkNotNull(advertisePicture.getOrder(),"param.null");
            Preconditions.checkNotNull(advertisePicture.getUrl(),"param.null");
            advePicture.setOrder(advertisePicture.getOrder());
            advePicture.setUrl(advertisePicture.getUrl());
            advertisePictures.add(advePicture);
        }
        if(advertisePictures.size()>0) advertiseMapper.batchInsert(advertisePictures);
        return NaResponse.createSuccess();
    }

    @Override
    public NaResponse<Object> delete(Integer id) {
        advertiseMapper.deleteAdvertiseById(id);
        advertiseMapper.deleteAdvertisePicturesByAdvertiseId(id);
        return NaResponse.createSuccess();
    }
}
