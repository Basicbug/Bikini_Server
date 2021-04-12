package com.basicbug.bikini.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class S3ImageUploader implements ImageUploader {

    private AmazonS3 amazonS3;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setUpS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        amazonS3 = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(region)
            .build();
    }

    @Override
    public String upload(MultipartFile multipartFile, String dirName) {
        try {
            final File file = convertToFile(multipartFile);
            final String fileName = generateFileName(file, dirName);
            final String imageUrl = uploadToBucket(file, fileName);
            removeFile(file);

            return imageUrl;
        } catch (Exception exception) {
            log.error("Exception occurs while upload image " + exception.getMessage());
        }
        return "";
    }

    private String generateFileName(File file, String dirName) {
        return dirName + File.separator + UUID.randomUUID().toString() + "-" + file.getName();
    }

    private String uploadToBucket(File file, String fileName) {
        final PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, file)
                                                    .withCannedAcl(CannedAccessControlList.PublicRead);
        try {
            amazonS3.putObject(putObjectRequest);
        } catch (AmazonServiceException amazonServiceException) {
            log.error("AmazonServiceException ${}", amazonServiceException.getMessage());
            return "";
        } catch (SdkClientException sdkClientException) {
            log.error("Sdk client exception ${}", sdkClientException.getMessage());
            return "";
        }

        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private void removeFile(File file) {
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            log.error("Remove file failure ${}", e.getMessage());
        }
    }

    private File convertToFile(MultipartFile multipartFile) {
        String originalFilename = Objects.requireNonNull(multipartFile.getOriginalFilename());
        File newFile = new File(originalFilename);

        try {
            if (newFile.createNewFile()) {
                return writeTo(multipartFile, newFile);
            }
        } catch (IOException exception) {
            log.error("Fail to convertToFile ${}", exception.getMessage());
        }

        throw new RuntimeException("convertToFile is failed");
    }

    private File writeTo(MultipartFile data, File destFile) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(destFile)) {
            fileOutputStream.write(data.getBytes());
        }
        return destFile;
    }
}
