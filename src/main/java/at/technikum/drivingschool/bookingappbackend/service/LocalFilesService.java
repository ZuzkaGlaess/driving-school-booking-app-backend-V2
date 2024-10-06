package at.technikum.drivingschool.bookingappbackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFilesService implements IFilesService {

    @Value("${localFiles.filePath}")
    private String filePath;
    public String uploadFile(MultipartFile file) {
        String newFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(
                filePath,
                newFileName
        );

        try {
            Files.copy(file.getInputStream(), uploadPath);
            return newFileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Resource getFile(String name) {
        Path downloadPath = Paths.get(
                filePath,
                name
        );
        return new FileSystemResource(downloadPath);
    }
}
