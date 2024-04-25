package finchainstorage.persistancelayer.awss3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class AWSCloudService {

    @Autowired
    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    public AWSCloudService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public String getBucketName() {
        return bucketName;
    }

    public S3ObjectInputStream downloadDocument(String fileName) {

        try {
            S3Object s3Object = s3Client.getObject(bucketName, fileName);
            return s3Object.getObjectContent();
        } catch (Exception e) {

            throw new RuntimeException("Failed to download document");
        }
    }

    public  void uploadDocument(MultipartFile file)  {
        try {
            s3Client.putObject(bucketName, file.getOriginalFilename(), file.getInputStream(), getMetaData(file));

        } catch (IOException e) {

            throw new RuntimeException("Failed to upload document");
        }
    }
    private ObjectMetadata getMetaData(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        return metadata;
    }
}
