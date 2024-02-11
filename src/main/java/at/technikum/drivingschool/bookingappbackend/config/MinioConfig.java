package at.technikum.drivingschool.bookingappbackend.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MinIO Configuration
 */
@Configuration
public class MinioConfig {

    /**
     * URL of MinIO Service - from properties file
     */
    @Value("${minio.url}")
    private String url;

    /**
     * User to access MinIO - from properties file
     */
    @Value("${minio.accessKey}")
    private String accessKey;

    /**
     * Password to access MinIO - from properties file
     */
    @Value("${minio.secretKey}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }
}
