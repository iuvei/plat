package com.na.manager.service;

import com.na.manager.bean.NaResponse;
import com.na.manager.entity.Advertise;

import java.util.List;

/**
 * v
 *
 * @create 2017-07
 */
public interface IAdvertiseService {
    List<Advertise> search();

    NaResponse<Object> create(Advertise advertiseRequest);

    NaResponse<Object> update(Advertise advertiseRequest);

    NaResponse<Object> delete(Integer id);
}
