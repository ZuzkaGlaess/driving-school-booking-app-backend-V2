package at.technikum.drivingschool.bookingappbackend.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class FilesService implements IFilesService {

    @Value("${minio.picturesBucketName}")
    private String picturesBucketName;

    @Value("${minio.filesBucketName}")
    private String filesBucketName;

    private final MinioClient minioClient;

    public FilesService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String uploadPicture(Long userId, MultipartFile file) {
        try {
            String contentType = file.getContentType();
            if(contentType != null && contentType.startsWith("image")) {
                String filename = String.valueOf(userId);

                InputStream inputStream = file.getInputStream();
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(picturesBucketName)
                        .object(filename)
                        .contentType(contentType)
                        .stream(inputStream, inputStream.available(), -1)
                        .build());
                return filename;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return null;
        }
    }

    public InputStream downloadPicture(String id) {
        try {
        InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(picturesBucketName)
                        .object(id)
                        .build()
        );
        return stream;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return null;
    }

    public String uploadFile(MultipartFile file) {
        try {
                String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                String contentType = file.getContentType();

                InputStream inputStream = file.getInputStream();
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(filesBucketName)
                        .object(filename)
                        .contentType(contentType)
                        .stream(inputStream, inputStream.available(), -1)
                        .build());
                return filename;
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return null;
        }
    }

    public Resource getFile(String name) {
        try {
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(filesBucketName)
                            .object(name)
                            .build()
            );
            new InputStreamResource(stream);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return null;
    }
}
