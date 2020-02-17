package com.dkha.common.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author Spring
 * @Since 2019/8/15 14:10
 * @Description Spring上下文工具类
 */
public class ContextUtil {

    /**
     * get request
     * @return
     */
    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    /**
     * get session
     * @return
     */
    public static HttpSession getSession() {
        HttpSession session = getRequest().getSession();
        return session;
    }
}
