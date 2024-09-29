package at.technikum.drivingschool.bookingappbackend.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class FilesService {

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
}
