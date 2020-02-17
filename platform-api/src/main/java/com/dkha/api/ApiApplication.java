package com.dkha.api;


import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Spring
 */
@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class},
        scanBasePackages={"com.dkha.api","com.dkha.common"})
@PropertySource({"classpath:system.properties"})
@MapperScan("com.dkha.api.mappers")
public class ApiApplication {
    @Value("${max.pageSize}")
    private int maxPageSize;
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
    /**
     * mybatis-plus 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        //你的最大单页限制数量，默认 500 条，小于 0 如 -1 不受限制
        paginationInterceptor.setLimit(maxPageSize);
        //paginationInterceptor.setLimit(20000);
        return paginationInterceptor;
    }
}
