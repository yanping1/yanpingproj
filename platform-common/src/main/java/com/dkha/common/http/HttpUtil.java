package com.dkha.common.http;

import com.alibaba.fastjson.JSON;
import com.dkha.common.validate.UtilValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author Spring
 * @Since 2019/10/31 18:21
 * @Description
 */
@Component
public class HttpUtil<T> {

    public static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    @Autowired
    private RestTemplate restTemplate;

    /**
     * post request
     * @param url
     * @param requestObj request params
     * @param resultClass
     * @param <T>
     * @return
     */
    public <T> T post(String url, Object requestObj, Class<T> resultClass) {
        T t = restTemplate.postForObject(url, this.getHeaders(requestObj), resultClass);
        logger.info("post result:  {}", JSON.toJSONString(resultClass));
        return t;
    }

    /**
     * put request
     * @param url
     * @param requestObj
     * @param resultClass
     * @param <T>
     * @return
     */
    public <T> T put(String url, Object requestObj, Class<T> resultClass) {
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, this.getHeaders(requestObj), resultClass);
        logger.info("put result:  {}", JSON.toJSONString(resultClass));
        return responseEntity.getBody();
    }

    /**
     * get request
     * @param url
     * @param resultClass
     * @param <T>
     * @return
     */
    public <T> T get(String url, Class<T> resultClass) {
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.GET, this.getHeaders(null), resultClass);
        logger.info("get result:  {}", JSON.toJSONString(resultClass));
        return responseEntity.getBody();
    }

    public <T> T delete(String url, Class<T> resultClass) {
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, this.getHeaders(null), resultClass);

        logger.info("delete result:  {}", JSON.toJSONString(resultClass));
        return responseEntity.getBody();
    }

    /**
     * 判断返回是否成功
     * @param resultObject
     * @return
     */
    public boolean isSuccess(Object resultObject) {
        String resultJson = JSON.toJSONString(resultObject);
        Map<String, Object> map = JSON.parseObject(resultJson, Map.class);
        if (UtilValidate.isNotEmpty(map) && map.get("rtn").toString().equals("0")) {
            return true;
        } else {
            return false;
        }
    }

    /**\
     * 获取POST请求参数
     * @param request
     * @return
     */
    public static String getPostRequestParam(HttpServletRequest request) throws IOException {
        String bodyStr = getRequestPostBytes(request);
        return new String(bodyStr.getBytes(), "UTF-8");
    }

    /**
     * 描述:获取 post 请求的 byte[] 数组
     * <pre>
     * 举例：
     * </pre>
     * @param request
     * @return
     * @throws IOException
     */
    public static String getRequestPostBytes(HttpServletRequest request) throws IOException {

        BufferedReader br = request.getReader();

        String str;
        String wholeStr = "";
        while((str = br.readLine()) != null){
            wholeStr += str;
        }
        return wholeStr.toString();
    }

    /**
     * 获取HTTP请求头信息
     * @param request
     * @return
     */
    public static Map<String, String> getRequestHeadMsg(HttpServletRequest request) {
        Map<String, String> requestHeaderMsgMap = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            requestHeaderMsgMap.put(key, value);
        }
        return requestHeaderMsgMap;
    }

    /**
     * 请求头设置
     */
    private HttpEntity getHeaders(Object requestObject) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        //请求url不包含login，则需要设置请求头
        /*if (!(JSON.toJSONString(requestObject).contains("name") && JSON.toJSONString(requestObject).contains("password"))) {
            headers.add("session_id", FaceSessionCache.get());
        }*/

        HttpEntity<String> headerEntity = new HttpEntity<>(UtilValidate.isEmpty(requestObject) ? null : JSON.toJSONString(requestObject), headers);

        logger.info("request data:  {}", JSON.toJSONString(requestObject));
        return headerEntity;
    }
}
