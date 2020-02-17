package com.dkha.common.http;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Description:
 * @Author: Spring
 * @CreateDate: 2019/8/5 14:19
 */
@Configuration
public class ConfigBean {

    /**
     * @ConditionalOnMissingBean bean不存在的情况下才注入
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
