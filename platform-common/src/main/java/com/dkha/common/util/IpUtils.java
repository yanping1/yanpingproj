/**
 *  (c) .
 *
 *
 *
 *
 */

package com.dkha.common.util;

import com.dkha.common.validate.UtilValidate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * IP地址
 * @author Spring
 */
public class IpUtils {
	private static Logger logger = LoggerFactory.getLogger(IpUtils.class);

	/**
	 * 获取IP地址
	 * 
	 * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
	 * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
	 */
	public static String getIpAddr(HttpServletRequest request) {
	    String unknown = "unknown";
    	String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            //nginx代理存在的情况下
            if (UtilValidate.isNotEmpty(ip) && ip.contains(",")) {
                ip = ip.split(",")[0];
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
        	logger.error("IPUtils ERROR ", e);
        }
        
        return ip;
    }

    /**
     * 获取本地IP地址
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
