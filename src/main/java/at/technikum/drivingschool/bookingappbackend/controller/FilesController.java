package at.technikum.drivingschool.bookingappbackend.controller;

import at.technikum.drivingschool.bookingappbackend.model.User;
import at.technikum.drivingschool.bookingappbackend.service.IFilesService;
import at.technikum.drivingschool.bookingappbackend.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Handling picture up- and download of users
 * Pictures are stored in the configured MinIO bucket
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api")
public class FilesController {

    @Autowired
    UserService userService;

    @Autowired
    IFilesService filesService;

    /**
     * Upload of a new picture
     *
     * @param file
     * @return
     */
    @PostMapping(value="/pictures", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR') or hasRole('STUDENT')")
    public ResponseEntity<String> uploadPicture(@RequestParam("picture") MultipartFile file) {
        User user = userService.getLoggedInUser();
        if(user != null) {
            String contentType = file.getContentType();
            if(contentType != null && contentType.startsWith("image")) {
                String filename = filesService.uploadFile(file);
                if (filename != null) {
                    user.setProfilePictureRef(filename);
                    userService.updateUser(user);

                    return ResponseEntity.ok("{ \"pictureName\": \"" + filename + "\" }");
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File is not an picture.");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User not found");
    }

    /**
     * Download of an existing picture
     */
    @GetMapping(value="/pictures/{name}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR') or hasRole('STUDENT')")
    public ResponseEntity<InputStreamResource> downloadPicture(@PathVariable("name") String name) {
        Resource file = filesService.getFile(name);
        if (file != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name);

            try {
                return ResponseEntity.ok()
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(new InputStreamResource(file.getInputStream()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
