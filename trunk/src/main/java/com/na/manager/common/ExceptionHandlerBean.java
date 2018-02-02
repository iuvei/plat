package com.na.manager.common;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.na.manager.bean.NaResponse;

/**
 * 统一异常处理器。
 * Created by sunny on 2017/6/22 0022.
 */
@ControllerAdvice
public class ExceptionHandlerBean extends ResponseEntityExceptionHandler{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private I18nMessage i18nMessage;

    /**
     * 数据找不到异常
     * @param ex
     * @param request
     * @return
     * @throws IOException
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleDataNotFoundException(RuntimeException ex, WebRequest request) throws IOException {
        logger.error(ex.getMessage(),ex);
        if(ex instanceof BusinessException){
        	 BusinessException bs =(BusinessException)ex;
        	 return ResponseEntity.ok().body(NaResponse.createError(i18nMessage.getMessage(bs.getKey(),bs.getArgs())));
        }
        return ResponseEntity.ok().body(NaResponse.createError(i18nMessage.getMessage(ex.getMessage())));
    }

}
