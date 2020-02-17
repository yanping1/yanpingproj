package com.dkha.baiduai.controller;

import com.baidu.aip.face.AipFace;
import com.dkha.baiduai.service.FaceService;
import com.dkha.baiduai.util.AipFaceClientUtil;
import com.dkha.common.util.Base64ImageUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.HashMap;

/**
 * @author hechenggang
 * @Date 2019/12/25 15:56
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private FaceService faceServiceImpl;

    @GetMapping("/getAuth")
    public void getAuth() {

        AipFace aipFace = AipFaceClientUtil.getInstance();
        System.out.println(aipFace);
    }

    @GetMapping("/getAipFace")
    public void getAipFace() {

        AipFace aipFace = AipFaceClientUtil.getInstance();
        File file = new File("D:\\chome_download\\20191225140201.png");
        String imgage = Base64ImageUtils.encodeImgageToBase64(file);
        JSONObject jsonObject = aipFace.detect(imgage, "BASE64", new HashMap<String, String>());
        System.out.println(jsonObject.toString(2));
    }
    @PostMapping("/addUser")
    public void addUser() {

        AipFace aipFace = AipFaceClientUtil.getInstance();
        File file = new File("D:\\chome_download\\20191225140201.png");
        String imgage = Base64ImageUtils.encodeImgageToBase64(file);
        JSONObject jsonObject = aipFace.detect(imgage, "BASE64", new HashMap<String, String>());
        System.out.println(jsonObject.toString(2));
    }
}
