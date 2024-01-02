package com.dailyon.snsservice.config;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorder;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import com.amazonaws.xray.plugins.EC2Plugin;
import com.amazonaws.xray.plugins.EKSPlugin;
import com.amazonaws.xray.strategy.ContextMissingStrategy;
import com.amazonaws.xray.strategy.DefaultStreamingStrategy;
import com.amazonaws.xray.strategy.sampling.LocalizedSamplingStrategy;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

@Component
@Profile(value = {"prod"})
public class AWSXrayConfig {

  @PostConstruct
  public void init() {
    AWSXRayRecorderBuilder builder =
        AWSXRayRecorderBuilder.standard().withPlugin(new EC2Plugin()).withPlugin(new EKSPlugin());
    URL ruleFile = AWSXrayConfig.class.getResource("/sampling-rules.json");
    builder.withSamplingStrategy(new LocalizedSamplingStrategy(ruleFile));
    builder.withContextMissingStrategy(new IgnoreContextMissingStrategy());
    builder.withStreamingStrategy(new DefaultStreamingStrategy(30));
    AWSXRayRecorder globalRecorder = builder.build();
    AWSXRay.setGlobalRecorder(globalRecorder);
  }

  @Bean
  public FilterRegistrationBean TracingFilter() {
    String from = null;
    try {
      from = InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      from = "serverName";
    }
    FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setFilter(new AWSXRayServletFilter(from));
    registration.addUrlPatterns("/*");
    return registration;
  }

  public class IgnoreContextMissingStrategy implements ContextMissingStrategy {
    public IgnoreContextMissingStrategy() {}

    @Override
    public void contextMissing(String message, Class<? extends RuntimeException> exceptionClass) {}
  }
}
