package com.dailyon.snsservice.config;

import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import javax.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowedMethods("*");
  }

  @Bean
  public Filter tracingFilter() {
    return new AWSXRayServletFilter("Scorekeep");
  }
}
