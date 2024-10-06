package at.technikum.drivingschool.bookingappbackend.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Primary
@Service
public class MinIOFilesService implements IFilesService {

    @Value("${minio.filesBucketName}")
    private String filesBucketName;

    private final MinioClient minioClient;

    public MinIOFilesService(MinioClient minioClient) {
        this.minioClient = minioClient;
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
            return new InputStreamResource(stream);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return null;
    }
}
