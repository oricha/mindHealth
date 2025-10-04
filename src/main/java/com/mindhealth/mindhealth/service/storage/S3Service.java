package com.mindhealth.mindhealth.service.storage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3;
    private final String bucket;

    public S3Service(
            @Value("${aws.region:us-east-1}") String region,
            @Value("${aws.credentials.access-key-id:dummy}") String accessKey,
            @Value("${aws.credentials.secret-access-key:dummy}") String secret,
            @Value("${aws.s3.bucket:mindhealth-local}") String bucket) {
        this.s3 = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secret)))
                .build();
        this.bucket = bucket;
    }

    public void upload(String key, InputStream content, long contentLength, String contentType) {
        s3.putObject(bucket, key, content, null);
    }

    public URL getPresignedUrl(String key, int minutes) {
        Date expiration = new Date(System.currentTimeMillis() + minutes * 60_000L);
        return s3.generatePresignedUrl(new GeneratePresignedUrlRequest(bucket, key).withExpiration(expiration));
    }
}

