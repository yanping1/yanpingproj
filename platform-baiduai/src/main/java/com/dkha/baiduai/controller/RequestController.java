package com.dkha.baiduai.controller;

import com.dkha.baiduai.common.constant.ApiQueryConstant;
import com.dkha.baiduai.service.FaceService;
import com.dkha.common.entity.vo.ApiVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hechenggang
 * @Date 2019/12/30 10:25
 */
@RequestMapping()
@RestController
@Api(description = "api请求分发")
public class RequestController {
    @Autowired
    private FaceService faceServiceImpl;

    @PostMapping(value = "/request")
    @ApiOperation(value = "请求分发")
    public ApiVO request(@RequestBody ApiVO apiVO, HttpServletResponse response, HttpServletRequest request) {
        ApiVO res = new ApiVO();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        if (apiVO == null) {
            res.setCode(-1);
            res.setMessage("请求格式有误,不能解析命令！");
            return res;
        }
        String CMDStr = apiVO.getCmd();
        if (StringUtil.isBlank(CMDStr)) {
            apiVO.setCode(-1);
            apiVO.setMessage("请求格式有误,不能解析CMD命令！");
            return res;
        }
        switch (CMDStr) {
            case ApiQueryConstant
                    .FACE_FEATURE_ADD:
                res = faceServiceImpl.addUser(apiVO);
                break;
            case ApiQueryConstant.FACE_FEATURE_DELETE:
                res = faceServiceImpl.deleteUser(apiVO);
                break;
        }

        return res;


    }

}
