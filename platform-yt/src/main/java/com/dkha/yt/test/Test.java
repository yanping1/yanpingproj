package com.dkha.yt.test;

import com.dkha.common.util.JsonUtil;
import com.dkha.yt.config.YtConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName:
 * @Description:(please write your description)
 * @author: {开发人的姓名}
 * @date:
 * @Copyright: 成都电科惠安
 */
@RestController
@RequestMapping(value = "/test")
@Api(tags = "测试")
public class Test {
    @Autowired
    private YtConfig config;

    @GetMapping(value = "/test")
    @ApiOperation(value = "获取配置")
    public String test(){
        String s =config.toString();
        return s;
    }
}
