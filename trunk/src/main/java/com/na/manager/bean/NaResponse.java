package com.na.manager.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回前台对象。
 * Created by sunny on 2017/4/4 0004.
 */
public class NaResponse<T> implements Serializable{

	private static final long serialVersionUID = -7017720128150064456L;
	private static final int SUCCESS = 0;
    private static final int ERROR = 1;
    //未授权
    private static final int UN_AUTH = 2;

    //0 成功 1 失败
    private int status;
    private String msg;
    private T data;

    public static NaResponse createSuccess(){
        NaResponse naResponse = new NaResponse();
        naResponse.status = SUCCESS;
        return naResponse;
    }

    public static <T> NaResponse createSuccess(T data){
        NaResponse naResponse = createSuccess();
        naResponse.data = data;
        return naResponse;
    }

    public static <T> NaResponse createSuccess(String key,T data){
        NaResponse naResponse = createSuccess();
        Map<String,T> temp = new HashMap<String,T>();
        temp.put(key,data);
        naResponse.data = temp;
        return naResponse;
    }

    public void put(String key,T data){
        Map<String,T> temp;
        if(this.data==null){
            temp = new HashMap<String,T>();
            this.data = (T)temp;
        }else {
            if(this.data instanceof Map) {
                temp = (Map<String, T>) this.data;
            }else {
                throw new RuntimeException("data 为map或null时才可以使用本方法");
            }
        }
        temp.put(key,data);
    }

    public static <T> NaResponse createError(){
        NaResponse naResponse = new NaResponse();
        naResponse.status = ERROR;
        return naResponse;
    }

    public static <T> NaResponse createError(String msg){
        NaResponse naResponse = new NaResponse();
        naResponse.status = ERROR;
        naResponse.msg = msg;
        return naResponse;
    }

    public static <T> NaResponse createUnAuthError(String msg){
        NaResponse naResponse = new NaResponse();
        naResponse.status = UN_AUTH;
        naResponse.msg = msg;
        return naResponse;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
