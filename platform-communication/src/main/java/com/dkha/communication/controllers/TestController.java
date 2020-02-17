package com.dkha.communication.controllers;

import com.dkha.common.result.CommonResult;
import com.dkha.communication.httpws.cache.ChannelCache;
import com.dkha.communication.httpws.client.PictureWebSocketClient;
import com.dkha.communication.httpws.client.VideoWebSocketClient;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Spring
 * @Since 2019/12/4 18:55
 * @Description
 */
@RestController
@RequestMapping("/test")
@Api(tags = "test")
public class TestController extends CommonResult {

    @Autowired
    private VideoWebSocketClient videoWebSocketClient;
    @Autowired
    private PictureWebSocketClient pictureWebSocketClient;

    @GetMapping("/test")
    @ApiOperation(value = "发送通信数据")
    public CommonResult test() {
        /*pictureWebSocketClient.sendMessage("{\"head\":{\"protId\":\"CmdLibCreate\",\"wappId\":637353,\"serId\":4000,\"ret\":0,\"errInfo\":\"\",\"version\":\"3.2.0\"},\"libIds\":[\"hyglibtest_499\"]}");
        videoWebSocketClient.sendMessage("{\"head\":{\"protId\":\"CmdLibCreate\",\"wappId\":637353,\"serId\":4000,\"ret\":0,\"errInfo\":\"\",\"version\":\"3.2.0\"},\"libIds\":[\"hyglibtest_499\"]}");*/
        ChannelCache.get("picture").writeAndFlush(new TextWebSocketFrame("{\"head\":{\"protId\":\"CmdLibCreate\",\"wappId\":637353,\"serId\":4000,\"ret\":0,\"errInfo\":\"\",\"version\":\"3.2.0\"},\"libIds\":[\"hyglibtest_499\"]}"));
        ChannelCache.get("video").writeAndFlush(new TextWebSocketFrame("{\"head\":{\"protId\":\"CmdLibCreate\",\"wappId\":637353,\"serId\":4000,\"ret\":0,\"errInfo\":\"\",\"version\":\"3.2.0\"},\"libIds\":[\"hyglibtest_499\"]}"));
        return successResult(null);
    }
}
