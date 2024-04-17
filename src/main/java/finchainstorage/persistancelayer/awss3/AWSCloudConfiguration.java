package finchainstorage.persistancelayer.awss3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AWSCloudConfiguration {

    @Value("${aws.access_key_id}")
    private static String ACCESS_KEY = "AKIA4JQPFYWIE3POOJUQ";
    @Value("${aws.secret_access_key}")
    private static String SECRET_ACCESS_KEY = "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";
    @Value("${aws.region}")
    private static String REGION = "us-east-1";


    @Bean
    public static AmazonS3 connectToS3() {
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_ACCESS_KEY);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(REGION)
                .build();
    }


}
