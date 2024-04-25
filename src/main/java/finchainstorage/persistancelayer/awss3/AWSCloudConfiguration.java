package finchainstorage.persistancelayer.awss3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@RequiredArgsConstructor
@Configuration
public class AWSCloudConfiguration {

    @Value("${aws.s3.access.key.id}")
    private String ACCESS_KEY;
    @Value("${aws.s3.secret.access.key}")
    private String SECRET_ACCESS_KEY;
    @Value("${aws.region}")
    private String REGION;


    @Bean
    public AmazonS3 connectToS3() {
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_ACCESS_KEY);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(REGION)
                .build();
    }

    public String getACCESS_KEY() {
        return ACCESS_KEY;
    }

    public String getSECRET_ACCESS_KEY() {
        return SECRET_ACCESS_KEY;
    }

    public String getREGION() {
        return REGION;
    }
}
