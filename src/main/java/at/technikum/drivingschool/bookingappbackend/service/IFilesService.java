package at.technikum.drivingschool.bookingappbackend.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface IFilesService {

    public Resource getFile(String name);
    public String uploadFile(MultipartFile file);
}
