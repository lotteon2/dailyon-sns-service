package com.dailyon.snsservice.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.validation.constraints.NotNull;

@Configuration
@Profile("!test")
public class AWSS3Config {

  @Value("${cloud.aws.credentials.ACCESS_KEY_ID}")
  private String accessKeyId;

  @Value("${cloud.aws.credentials.SECRET_ACCESS_KEY}")
  private String secretAccessKey;

  @Value("${cloud.aws.region.static}")
  private String region;

  @NotNull
  private BasicAWSCredentials getBasicAWSCredentials() {
    return new BasicAWSCredentials(accessKeyId, secretAccessKey);
  }

  @Primary
  @Bean
  public AmazonS3 amazonS3Client() {
    return AmazonS3ClientBuilder.standard()
        .withRegion(region)
        .withCredentials(new AWSStaticCredentialsProvider(getBasicAWSCredentials()))
        .build();
  }
}
