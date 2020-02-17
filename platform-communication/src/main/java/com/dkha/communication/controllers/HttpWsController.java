package com.dkha.communication.controllers;

import com.dkha.common.entity.vo.ApiVO;
import com.dkha.common.result.CommonResult;
import com.dkha.communication.httpws.cache.WappIdCache;
import com.dkha.communication.httpws.queue.RequestMsgQueue;
import com.dkha.communication.services.RequestProtocolAnalysis;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author Spring
 * @Since 2019/11/13 14:17
 * @Description
 */
@RestController
@RequestMapping("/http/ws")
@Api(tags = "http-ws")
@Slf4j
public class HttpWsController extends CommonResult {
    /**请求协议解析*/
    @Autowired
    RequestProtocolAnalysis protocolAnalysis;
    /**请求发送队列*/
    @Autowired
    RequestMsgQueue requestMsgQueue;

    @PostMapping(value = "/request")
    @ApiOperation(value = "发送FaceAI请求")
    public void sendPicRequest(@ApiParam(required = true, name = "requestdata", value = "{ \"cmd\": \"CmdLibCreate\", \n" +
            "  \"data\":{\"libIds\":[\"testlib01\",\"testlib02\",\"testlib03\"]},\n" +
            "  \"message\":\"\",\n" +
            "  \"code\":0\n" +
            "}") @RequestBody Map<String, Object> data, HttpServletResponse response, HttpServletRequest request) {
        ApiVO apiVO=new ApiVO();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        if (data == null) {
            apiVO.setCode(-1);
            apiVO.setMessage("请求格式有误,不能解析命令！");
            protocolAnalysis.SendWarningMsg(apiVO,response);
            return;
        } else {
            String CMDStr = data.get("cmd").toString();
            if (StringUtil.isBlank(CMDStr)) {
                apiVO.setCode(-1);
                apiVO.setMessage("请求格式有误,不能解析CMD命令！");
                protocolAnalysis.SendWarningMsg(apiVO,response);
                return;
            }
            if (!(WappIdCache.isActive(WappIdCache.WappIdTypeEnum.PICTURE.code) && WappIdCache.isActive(WappIdCache.WappIdTypeEnum.VIDEO.code))) {
                apiVO.setCode(-2);
                apiVO.setMessage("通讯服务未准备好！请稍等重试！");
                protocolAnalysis.SendWarningMsg(apiVO,response);
                return;
            }
            protocolAnalysis.transformRequestMSGToQueue(CMDStr,data, response);
        }
    }



}
