package com.na.manager.action;

import com.na.manager.bean.NaResponse;
import com.na.manager.bean.PercentConfigSearchRequest;
import com.na.manager.common.annotation.Auth;
import com.na.manager.entity.PercentageConfig;
import com.na.manager.service.IPercentageConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * v
 *
 * @create 2017-07
 */
@RestController
@RequestMapping("/percentageConfig")
@Auth("PercentageConfig")
public class PercentageConfigAction {

    @Autowired
    private IPercentageConfigService percentageConfigService;

    @PostMapping("/search/{userId}")
    public NaResponse search(@PathVariable("userId") Long userId) {
        PercentConfigSearchRequest searchRequest = new PercentConfigSearchRequest();
        searchRequest.setUserId(userId);
        return NaResponse.createSuccess(percentageConfigService.queryPercentConfigByPage(searchRequest));
    }

    @PostMapping("/create")
    public NaResponse create(@RequestBody PercentageConfig percentageConfig) {
        percentageConfigService.add(percentageConfig);
        return NaResponse.createSuccess();
    }

    @PostMapping("/deleteById/{id}")
    public NaResponse delete(@PathVariable("id") Integer id) {
        percentageConfigService.delete(id);
        return NaResponse.createSuccess();
    }


    @PostMapping("/update")
    public NaResponse update(@RequestBody PercentageConfig percentageConfig) {
        percentageConfigService.update(percentageConfig);
        return NaResponse.createSuccess();
    }
}
