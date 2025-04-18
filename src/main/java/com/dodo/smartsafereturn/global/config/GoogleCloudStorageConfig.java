package com.dodo.smartsafereturn.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class GoogleCloudStorageConfig {
//
//    @Value("${spring.cloud.gcp.storage.credentials.location}")
//    private String credentialsLocation;

    @Value("${spring.cloud.gcp.storage.project-id}")
    private String projectId;

    @Value("${spring.cloud.gcp.storage.credentials.location:}") // prod 환경일때는 빈 문자열
    private String credentialsLocation;

    @Bean
    @Profile("dev")
    public Storage storageLocal() throws IOException {

        String credentialsPath = ResourceUtils.getFile(credentialsLocation).getAbsolutePath();

        return StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(GoogleCredentials.fromStream(new FileInputStream(credentialsPath)))
                .build()
                .getService();

    }

    @Bean
    @Profile("prod")
    public Storage storage() throws IOException {

        // 배포환경을 위한 세팅
        StorageOptions.Builder builder = StorageOptions.newBuilder();

        if (projectId != null && !projectId.isEmpty()) {
            builder.setProjectId(projectId);
        }

        // 기본 인증 매커니즘 활용
        return builder.build().getService();
    }
}
