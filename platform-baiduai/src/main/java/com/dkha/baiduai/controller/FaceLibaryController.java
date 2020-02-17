package com.dkha.baiduai.controller;

import com.dkha.baiduai.service.FaceLibary;
import com.dkha.common.entity.vo.ApiVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hechenggang
 * @Date 2019/12/26 9:42
 */
@RequestMapping
@RestController(value = "/libary")
@Api(description = "人脸库相关")
public class FaceLibaryController {

    @Resource
    private FaceLibary faceLibaryImpl;

    @PostMapping(value = "/groupAdd")
    public ApiVO groupAdd(@RequestBody ApiVO apiVO) {

        ApiVO result = faceLibaryImpl.groupAdd(apiVO);
        return result;
    }
    @PostMapping(value = "/groupDelete")
    public ApiVO groupDelete(@RequestBody ApiVO apiVO) {

        ApiVO result = faceLibaryImpl.groupDelete(apiVO);
        return result;
    }
}
