package com.hhplus.concert_ticketing.config;

import com.hhplus.concert_ticketing.interfaces.filter.QueueFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<QueueFilter> queueFilter() {
        FilterRegistrationBean<QueueFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new QueueFilter());
        registrationBean.addUrlPatterns("/api/v1/queue/*"); // 특정 경로에만 필터 적용
        registrationBean.setOrder(1); // 필터 순서 지정 (필요 시)
        return registrationBean;
    }
}