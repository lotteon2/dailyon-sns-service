package com.dailyon.snsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.TimeZone;

@EnableScheduling
@EnableFeignClients
@EnableJpaAuditing
@EnableEurekaClient
@SpringBootApplication
public class SnsServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SnsServiceApplication.class, args);
  }

  @PostConstruct
  public void setTimezoneToSeoul() {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
  }
}
