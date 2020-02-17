package com.dkha.common.http;

import com.dkha.common.entity.vo.ApiVO;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: SendUtil
 * @Package com.dkha.common.http
 * @author: panhui
 * @date: 2019/12/4 11:33
 * @Copyright: 成都电科慧安
 */
@Component
@Slf4j
@PropertySource({"classpath:facefilter.properties"})
public class SendUtil {

    @Value("${wyhttpurlvideo}")
    private String wy;

    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    private Gson gson;

    public  ApiVO SendWy(ApiVO apiVO)
    {
        log.error("发送数据：{}",gson.toJson(apiVO));
        return  (ApiVO) httpUtil.post(wy, apiVO, ApiVO.class);
    }
}
