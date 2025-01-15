package com.stdev.futurebox.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class GcpStorageConfig {

    @Value("${gcp.storage.bucket-name}")
    private String bucketName;

    @Bean
    public Storage storage() throws IOException {
        String credentialsJson = System.getenv("GCP_CREDENTIALS");
        
        if (credentialsJson == null || credentialsJson.isEmpty()) {
            throw new IllegalStateException(
                "GCP_CREDENTIALS 환경변수가 설정되지 않았습니다. " +
                "서비스 계정 키 JSON을 환경변수에 설정해주세요."
            );
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(
            new ByteArrayInputStream(credentialsJson.getBytes())
        );
        
        return StorageOptions.newBuilder()
            .setCredentials(credentials)
            .build()
            .getService();
    }
} 