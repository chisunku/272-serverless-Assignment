package com.example.assignment;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@RestController
@CrossOrigin(origins = "*")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	@Autowired
	private AmazonS3 s3Client;

	@Value("${application.bucket.name}")
	private String bucketName;

	@PostMapping("/upload")
	public String uploadFile(@RequestParam(value = "file") MultipartFile file) {
		System.out.println("in upload mtd");
		File fileObj = convertMultiPartFileToFile(file);
		s3Client.putObject(new PutObjectRequest(bucketName, file.getOriginalFilename(), fileObj));
		fileObj.delete();
		return file.getOriginalFilename();
	}
	private File convertMultiPartFileToFile(MultipartFile file) {
		File convertedFile = new File(file.getOriginalFilename());
		try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
			fos.write(file.getBytes());
		} catch (IOException e) {
			System.out.println(e);
		}
		return convertedFile;
	}

}

@Configuration
class config {
	@Value("${cloud.aws.credentials.access-key}")
	private String accessKey;
	@Value("${cloud.aws.credentials.secret-key}")
	private String accessSecret;
	@Value("${cloud.aws.region.static}")
	private String region;
	@Bean
	public AmazonS3 s3Client() {
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);
		return AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(region).build();
	}

}
