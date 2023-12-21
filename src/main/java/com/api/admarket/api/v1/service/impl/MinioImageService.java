package com.api.admarket.api.v1.service.impl;

import com.api.admarket.api.v1.entity.image.Image;
import com.api.admarket.api.v1.service.ImageService;
import com.api.admarket.config.props.MinioProperties;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MinioImageService implements ImageService {
    private final MinioClient minioClient;
    private final MinioProperties minioProps;

    @Override
    public void upload(String filename, Image image) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioProps.getBucket())
                    .object(filename)
                    .contentType(image.getFile().getContentType())
                    .stream(image.getFile().getInputStream(), image.getFile().getSize(), -1)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Faild putObject to Minio");
        }
    }

    @Override
    public String getUrl(String filename, int expiryDays) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(minioProps.getBucket())
                    .object(filename)
                    .expiry(expiryDays, TimeUnit.DAYS)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Faild receiving url of object from Minio");
        }
    }

    @Override
    public List<String> getUrls(String directory, int expiryDays) {
        try {
            List<String> list = getObjectNames(directory);
            return list.stream().map(x -> getUrl(x, expiryDays)).toList();
        } catch (Exception e) {
            throw new RuntimeException("Faild receiving url of objects from Minio");
        }
    }

    private List<String> getObjectNames(String directory) {
        try {
            List<Result<Item>> list = new ArrayList<>();
            minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(minioProps.getBucket())
                    .prefix(directory)
                    .recursive(true)
                    .build()).forEach(list::add);

            return list.stream().map(x -> {
                try {
                    return x.get().objectName();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).toList();
        } catch (Exception e) {
            throw new RuntimeException("Faild receiving objects from Minio");
        }
    }

    @Override
    public void delete(String filename) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                            .bucket(minioProps.getBucket())
                            .object(filename)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Faild deleting objects from Minio");
        }
    }
}
