package com.dkha.baiduai.controller;

import com.dkha.baiduai.service.FaceService;
import com.dkha.common.entity.vo.ApiVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hechenggang
 * @Date 2019/12/26 9:39
 */
@Api(tags = "人脸相关")
@RestController
@RequestMapping("/face")
public class FaceController {
    @Autowired
    private FaceService faceServiceImpl;

    @ApiOperation(value = "新增人脸")
    @PostMapping("/addUser")
    public ApiVO addUser(@ApiParam(required = true, name = "apiVo", value = "{\n" +
            "  \"data\": {\n" +
            "    \"image\": \"图片信息\",\n" +
            "   \"image_type\":\"图片类型，只支持BASE64\",\n" +
            "   \"group_id\":\"人脸库id\",\n" +
            "  },\n" +
            "\"code\":\"错误码\"\n" +
            "  \"message\": ,\n" +
            "  \"cmd\":,\n" +
            "}") @RequestBody ApiVO apiVO) {

        ApiVO result = faceServiceImpl.addUser(apiVO);
        return result;
    }

    @ApiOperation(value = "删除人脸")
    @PostMapping("/deleteUser")
    public ApiVO deleteUser(@ApiParam(required = true, name = "apiVo", value = "{\n" +
            "  \"data\": {\n" +
            "    \"user_id\": \"人脸id\",\n" +
            "   \"group_id\":\"人脸库id\",\n" +
            "   \"face_token\":\"人脸图片token\",\n" +
            "  },\n" +
            "\"code\":\"错误码\"\n" +
            "  \"message\": ,\n" +
            "  \"cmd\":,\n" +
            "}") @RequestBody ApiVO apiVO) {

        ApiVO result = faceServiceImpl.deleteUser(apiVO);
        return result;
    }
    @ApiOperation(value = "人脸检索")
    @PostMapping("/search")
    public ApiVO search(@ApiParam(required = true, name = "apiVo", value = "{\n" +
            "  \"data\": {\n" +
            "    \"image\": \"图片信息\",\n" +
            "   \"image_type\":\"图片类型，只支持BASE64\",\n" +
            "   \"group_id\":\"从指定人脸库检索，多个人脸库id逗号隔开\",\n" +
            "  },\n" +
            "\"code\":\"错误码\"\n" +
            "  \"message\": ,\n" +
            "  \"cmd\":,\n" +
            "}") @RequestBody ApiVO apiVO) {

        ApiVO result = faceServiceImpl.search(apiVO);
        return result;
    }

    @ApiOperation(value = "人脸检测")
    @PostMapping("/detect")
    public ApiVO detect(@ApiParam(required = true, name = "apiVo", value = "{\n" +
            "  \"data\": {\n" +
            "    \"imgs\": \"图片信息\",\n" +
            "  },\n" +
            "\"code\":\"错误码\"\n" +
            "  \"message\": ,\n" +
            "  \"cmd\":,\n" +
            "}") @RequestBody ApiVO apiVO) {

        ApiVO result = faceServiceImpl.detect(apiVO);
        return result;
    }
}
