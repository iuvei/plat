package com.na.manager.action;

import com.na.manager.bean.NaResponse;
import com.na.manager.common.annotation.Auth;
import com.na.manager.entity.Advertise;
import com.na.manager.service.IAdvertiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 广告
 *
 * @create 2017-07
 */
@RestController
@RequestMapping("/advertise")
@Auth("Advertise")
public class AdvertiseAction {

    @Autowired
    private IAdvertiseService advertiseService;

    @PostMapping("/search")
    public NaResponse<Object> search(){
        return NaResponse.createSuccess(advertiseService.search());
    }
    @PostMapping("/create")
    public NaResponse<Object> create(@RequestBody Advertise advertiseRequest){
        return advertiseService.create(advertiseRequest);
    }
    @PostMapping("/update")
    public NaResponse<Object> update(@RequestBody Advertise advertiseRequest){
        return advertiseService.update(advertiseRequest);
    }

    @PostMapping("/delete/{id}")
    public NaResponse<Object> delete(@PathVariable("id") Integer id){
        return advertiseService.delete(id);
    }
}
