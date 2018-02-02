package com.na.gate.common;

import com.na.gate.vo.PlatformResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

/**
 * 统一异常处理器。
 * Created by sunny on 2017/6/22 0022.
 */
@ControllerAdvice
public class ExceptionHandlerBean extends ResponseEntityExceptionHandler{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
        return ResponseEntity.ok().body(PlatformResponse.createError(ex.getMessage()));
    }

}
