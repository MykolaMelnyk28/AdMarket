package com.api.admarket;

import com.api.admarket.config.props.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@EnableMethodSecurity
@SpringBootApplication
public class AdmarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdmarketApplication.class, args);
    }

    @Bean
    CommandLineRunner init(MinioClient minioClient, MinioProperties minioProps) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                useDefaultMinioBucket();
            }

            private void useDefaultMinioBucket() throws Exception  {
                boolean exists = minioClient.bucketExists(BucketExistsArgs.builder()
                        .bucket(minioProps.getBucket())
                        .build()
                );
                if (!exists) {
                    minioClient.makeBucket(
                            MakeBucketArgs.builder()
                                    .bucket(minioProps.getBucket())
                                    .build()
                    );
                }
            }
        };
    }

}
