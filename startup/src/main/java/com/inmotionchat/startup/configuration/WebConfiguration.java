package com.inmotionchat.startup.configuration;

import com.inmotionchat.startup.ServiceOnlineInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    public ServiceOnlineInterceptor serviceOnlineInterceptor() {
        return new ServiceOnlineInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(serviceOnlineInterceptor());
    }

}
