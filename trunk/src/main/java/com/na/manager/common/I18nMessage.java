package com.na.manager.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 国际化信息。
 * Created by sunny on 2017/6/22 0022.
 */
@Component
public class I18nMessage {
    @Autowired
    private MessageSource messageSource;

    public String getMessage(String code,Locale locale){
        try {
            return messageSource.getMessage(code, null, locale);
        }catch (NoSuchMessageException ex){
            return code;
        }
    }

    public String getMessage(String code){
        Locale locale = LocaleContextHolder.getLocale();
        return getMessage(code,locale);
    }

    public String getMessage(String code,Object[] args){
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code,args,locale);
    }
}
