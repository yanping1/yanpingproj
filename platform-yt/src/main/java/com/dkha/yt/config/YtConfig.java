package com.dkha.yt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: YtConfig
 * @Description: 读取yt配置信息
 * @author: yanping
 * @date: 2019-12-31
 * @Copyright: 成都电科惠安
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "yt")
public class YtConfig {
    private String ip;
    private String port;
    private String prefix;
    private String username;
    private String password;
}
