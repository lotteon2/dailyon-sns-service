package com.dailyon.snsservice.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

  private final AmazonS3 amazonS3;

  public String getPreSignedUrl(String bucket, String filePath) {
    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        getGeneratePreSignedUrlRequest(bucket, filePath);

    return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
  }

  private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(
      String bucket, String fileName) {
    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(bucket, fileName)
            .withMethod(HttpMethod.PUT)
            .withExpiration(getPreSignedUrlExpiration());
    generatePresignedUrlRequest.addRequestParameter(
        Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());
    return generatePresignedUrlRequest;
  }

  private Date getPreSignedUrlExpiration() {
    Date expiration = new Date();
    long expTimeMillis = expiration.getTime();
    expTimeMillis += 1000 * 60 * 5;
    expiration.setTime(expTimeMillis);
    return expiration;
  }
}
