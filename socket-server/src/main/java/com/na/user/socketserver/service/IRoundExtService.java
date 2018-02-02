package com.na.user.socketserver.service;

import com.na.user.socketserver.entity.RoundExtPO;

/**
 * Created by Administrator on 2017/4/26 0026.
 */
public interface IRoundExtService {
	
    /**
     * 根据靴获得RoundExt
     * @return
     */
	public RoundExtPO getRoundExt(long rid);

	public void add(RoundExtPO ext);
	
	public void update(RoundExtPO ext);
	
	
}
