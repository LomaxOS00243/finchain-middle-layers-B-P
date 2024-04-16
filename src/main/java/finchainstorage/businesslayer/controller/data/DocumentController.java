package finchainstorage.businesslayer.controller.data;


import finchainstorage.businesslayer.model.Employees;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import finchainstorage.persistancelayer.awss3.AWSCloud;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.Document;
import java.io.IOException;


@RestController
@CrossOrigin(origins ="*", maxAge = 3500)
@RequestMapping("/api")
public class DocumentController {

    @PostMapping("/datamanager")
    public ResponseEntity<?> uploadDocument(@RequestParam("file") MultipartFile file) {
        String documentName = file.getOriginalFilename();
        try {
            AWSCloud.uploadDocument(documentName, file.getInputStream().toString());
        } catch (RuntimeException | IOException e) {
            return ResponseEntity.badRequest().body("Failed to upload document");

    }
        return ResponseEntity.ok("Document uploaded successfully");
    }
}
