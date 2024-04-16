package finchainstorage.persistancelayer.awss3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.stereotype.Component;


@Component
public class AWSCloud {

    private static final String BUCKET_NAME = "finchainstorage";
    private static final String ACCESS_KEY = "access";
    private static final String SECRET_ACCESS_KEY = "secret";

    public static AmazonS3 connectToS3() {
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_ACCESS_KEY);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion("us-east-1")
                .build();
    }

    public static S3ObjectInputStream downloadDocument(String documentName) {
        AmazonS3 s3Client = connectToS3();
        S3Object s3Object = s3Client.getObject(BUCKET_NAME, documentName);
        return s3Object.getObjectContent();
        //S3ObjectInputStream s3ObjectInputStream;
    }

    public static void uploadDocument(String documentName, String documentPath) {
        AmazonS3 s3Client = connectToS3();
        s3Client.putObject(BUCKET_NAME, documentName, documentPath);
    }




}
