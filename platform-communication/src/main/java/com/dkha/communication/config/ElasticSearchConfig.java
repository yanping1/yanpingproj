package com.dkha.communication.config;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @version V1.0
 * @Description: TODO:
 * @Title:
 * @Package com.dkha.communication.config
 * @author: huangyugang
 * @date: 2019/12/2 17:28
 * @Copyright: 成都电科慧安
 */
@Component
public class ElasticSearchConfig {
    /**
     * 防止netty的bug
     * java.lang.IllegalStateException: availableProcessors is already set to [4], rejecting [4]
     */
    @PostConstruct
    void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
}
