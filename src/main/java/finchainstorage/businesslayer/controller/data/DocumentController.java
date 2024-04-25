package finchainstorage.businesslayer.controller.data;


import finchainstorage.businesslayer.services.implementaion.EmployeeServicesImpl;
import finchainstorage.persistancelayer.awss3.AWSCloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

        System.out.println("File metadata: "+getMetadata);
        //Employee ID will be retrieved from the in memory storage using session id of this request

        //Issue transaction to verify the employee is authorised to upload the document - Hardcoded employee id
         employeeAuthServing.uploadDocument(getMetadata, "fin-Employee002", file.getName());

        try {
            //Upload the document to the S3 bucket
            s3Service.uploadDocument(file);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Failed to upload document");
        }
        return ResponseEntity.ok("Document uploaded successfully");
    }


    @GetMapping("/datamanager")
    public ResponseEntity<?> downloadDocument(@RequestParam("fileName") String fileName) {

        //Need to verify the employee is authorised to download the document

        try {
            return ResponseEntity.ok(s3Service.downloadDocument(fileName));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Failed to download document");
        }
    }
}
