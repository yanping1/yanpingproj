package com.dkha.baiduai;

import com.baidu.aip.face.AipFace;
import com.dkha.baiduai.common.constant.APIConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BaiduAIApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaiduAIApplication.class, args);
    }

    @Bean
    public AipFace getAipFace() {
        return new AipFace(APIConstant.APP_ID, APIConstant.API_KEY, APIConstant.SECRET_KEY);
    }
}
