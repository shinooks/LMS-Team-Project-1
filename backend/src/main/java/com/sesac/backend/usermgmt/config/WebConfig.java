package com.sesac.backend.usermgmt.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // 기본적으로 정적 페이지는 핸들러가 자동으로 구성됨.
    // 실행 환경의 오류로 경로를 수동으로 등록했음.
    // 다른 방법으로는 별도의 컨트롤러를 구성해 매핑해주면 된다.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
