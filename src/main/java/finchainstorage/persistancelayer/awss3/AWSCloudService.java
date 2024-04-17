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

    private final AmazonS3 s3Client;
    private final String BUCKET_NAME;
    @Autowired
    public AWSCloudService(AmazonS3 s3Client,  @Value("${aws.s3.bucket.name}") String bucketName) {
        this.s3Client = s3Client;
        this.BUCKET_NAME = bucketName;
    }
    public S3ObjectInputStream downloadDocument(String fileName) {

        try {
            S3Object s3Object = s3Client.getObject(BUCKET_NAME, fileName);
            return s3Object.getObjectContent();
        } catch (Exception e) {

            throw new RuntimeException("Failed to download document");
        }
    }

    public  void uploadDocument(MultipartFile file)  {
        try {
            s3Client.putObject(BUCKET_NAME, file.getOriginalFilename(), file.getInputStream(), getMetaData(file));

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
