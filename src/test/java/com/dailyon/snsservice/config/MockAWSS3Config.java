package com.dailyon.snsservice.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.findify.s3mock.S3Mock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@TestConfiguration
public class MockAWSS3Config {

  @Bean(name = "s3Mock")
  public S3Mock s3Mock() {
    return new S3Mock.Builder().withPort(8001).withInMemoryBackend().build();
  }

  @PostConstruct
  public void startS3Mock() {
    this.s3Mock().start();
  }

  @PreDestroy
  public void destroyS3Mock() {
    this.s3Mock().stop();
  }

  @Bean
  @Primary
  public AmazonS3 amazonS3Client() {
    AwsClientBuilder.EndpointConfiguration endpoint =
        new AwsClientBuilder.EndpointConfiguration(
            "http://localhost:8001", Regions.AP_NORTHEAST_2.name());
    AmazonS3 client =
        AmazonS3ClientBuilder.standard()
            .withPathStyleAccessEnabled(true)
            .withEndpointConfiguration(endpoint)
            .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
            .build();
    return client;
  }
}
