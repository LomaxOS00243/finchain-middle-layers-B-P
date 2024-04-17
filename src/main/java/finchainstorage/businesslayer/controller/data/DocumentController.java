package finchainstorage.businesslayer.controller.data;


import finchainstorage.businesslayer.services.implementaion.EmployeeServicesImpl;
import finchainstorage.persistancelayer.awss3.AWSCloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import finchainstorage.persistancelayer.gateway.services.UtilityChaincodeService;
import org.springframework.web.multipart.MultipartFile;


@RestController
@CrossOrigin(origins ="*", maxAge = 3500)
@RequestMapping("/api")
public class DocumentController {

    private final AWSCloudService s3Service;

    private final EmployeeServicesImpl employeeAuthServing;

    @Autowired
    public DocumentController(AWSCloudService s3Service, EmployeeServicesImpl employeeAuthServing) {
        this.s3Service = s3Service;
        this.employeeAuthServing = employeeAuthServing;
    }

    @PostMapping("/datamanager")
    public ResponseEntity<?> uploadDocument(@RequestParam("file") MultipartFile file) {

        //generate a unique document name
        String getMetadata = file.getOriginalFilename()+file.getContentType()+file.getSize();

        System.out.println("Document metadata: "+getMetadata);

        employeeAuthServing.uploadDocument(getMetadata, "fs-empl001", file.getContentType());

        try {
            s3Service.uploadDocument(file);

        } catch (RuntimeException e) {

            return ResponseEntity.badRequest().body("Failed to upload document");
        }
        return ResponseEntity.ok("Document uploaded successfully");
    }


    @GetMapping("/datamanager")
    public ResponseEntity<?> downloadDocument(@RequestParam("fileName") String fileName) {
        try {
            return ResponseEntity.ok(s3Service.downloadDocument(fileName));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Failed to download document");
        }
    }
}
