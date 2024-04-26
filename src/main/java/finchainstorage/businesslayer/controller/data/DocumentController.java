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

        //Employee ID must be retrieved here in the server side using the session id that
        //comes in the request header.
        //Why? Because the employee ID is not available in the request body of the upload document request
        //Reason: As the employee ID is the most sensitive data, it should not be sent for every request
        //The client side only sends the file to be uploaded and the session id.


        //For the sake of the demo, I hardcoded a registered ID to issue this transaction
        //because the demo requires starting from the post-registration process
        //and the server side is implemented with in memory data structure that get cleared
        //when the server is restarted.

        //Optionally, I can demo with the employee ID that I am currently registering and enrolling
        // in the network. But there's a demo for the network response when a user is already post-registered
        //and this ID user is used for that.
         employeeAuthServing.uploadDocument(getMetadata, "fin-Employee011", file.getOriginalFilename());

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
