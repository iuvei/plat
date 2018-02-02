package com.na.manager.util;

import com.na.manager.enums.LanguageEnum;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * v
 *
 * @create 2017-08
 */
public class LocalLanguageUtil {

    /**
     * 设置国际化
     * @param request
     * @param userLanguageEnum
     */
    public static void localLanguage(HttpServletRequest request, LanguageEnum userLanguageEnum) {
        Locale locale ;
        switch (userLanguageEnum){
            case China:
                locale = Locale.CHINA;
                break;
            case ChinaTaiwan:
                locale = Locale.TAIWAN;
                break;
            case English:
                locale = Locale.ENGLISH;
                break;
            case Korean:
                locale = Locale.KOREA;
                break;
            default:
                locale = Locale.CHINA;
        }

        LocaleContextHolder.setLocale(locale);
        request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
    }

}
