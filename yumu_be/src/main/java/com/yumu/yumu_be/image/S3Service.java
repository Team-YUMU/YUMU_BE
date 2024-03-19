package com.yumu.yumu_be.image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;

@Slf4j
@Service
public class S3Service {

    private final AmazonS3 amazonS3;

    private final String bucket;

    private final String accessKey;
    private final String secretKey;

    public S3Service(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String bucket, @Value("${cloud.aws.credentials.accessKey}") String accessKey, @Value("${cloud.aws.credentials.secretKey}") String secretKey) {
        this.amazonS3 = amazonS3;
        this.bucket = bucket;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String upload(MultipartFile multipartFile, String memberId) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        System.out.println("originalFileName : "+originalFileName);
        String uniqueFileName = memberId+"/"+originalFileName;
        System.out.println("convert 전");
        File uploadFile = convert(multipartFile);
        System.out.println("convert 후");
        System.out.println("iam accessKey = "+accessKey);
        System.out.println("iam secretKey = "+secretKey);
        String uploadImageUrl = putS3(uploadFile, uniqueFileName);
        System.out.println("uploadImageUrl : "+uploadImageUrl);
        removeLocalFile(uploadFile);
        return uploadImageUrl;
    }
    private File convert(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + originalFileName;
        log.info("convert new file 생성전");
        File convertFile = new File(uniqueFileName);
        Runtime.getRuntime().exec("chmod 777 ./"+convertFile);
        Runtime.getRuntime().exec("chmod 777 "+convertFile);
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            } catch (IOException e) {
                log.error("파일 변환 중 오류 발생: {}", e.getMessage());
                throw e;
            }
            return convertFile;
        }
        throw new IllegalArgumentException(String.format("파일 변환에 실패했습니다. %s", originalFileName));
    }

    private String putS3(File uploadFile, String fileName) {
        System.out.println("이미지 저장시작");
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        System.out.println("이미지 저장완료");
        return amazonS3.getUrl(bucket, fileName).toString();
    }
    private void removeLocalFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }
    public void deleteFile(String fileName) {
        try {
            // URL 디코딩을 통해 원래의 파일 이름을 가져옵니다.
            String decodedFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("Deleting file from S3: " + decodedFileName);
            amazonS3.deleteObject(bucket, decodedFileName);
        } catch (UnsupportedEncodingException e) {
            log.error("Error while decoding the file name: {}", e.getMessage());
        }
    }

    public String updateFile(MultipartFile newFile, String oldFileName, String memberId) throws IOException {
        log.info("S3 oldFileName: " + oldFileName);
        deleteFile(oldFileName);
        return upload(newFile, memberId);
    }

}
