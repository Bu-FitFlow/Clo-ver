package com.fitflow.clover.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3UploadUtil {
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${app.cdn.url}")
    private String cdnUrl;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String extension = (originalFileName != null && originalFileName.contains(".")) ? originalFileName.substring(originalFileName.lastIndexOf(".")) : "";

        String uniqueFileName = dirName + "/" + UUID.randomUUID() + extension;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(uniqueFileName)
                .contentType(multipartFile.getContentType())
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        try {
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));
        } catch (Exception e) {
            log.error("파일 업로드 실패: {}", e.getMessage());
            throw new IOException("파일 업로드에 실패했습니다.", e);
        }

        return cdnUrl + "/" + uniqueFileName;
    }

    public void delete(String fileUrl) {
        String fileKey = fileUrl.replace(cdnUrl + "/", "");
        s3Client.deleteObject(builder -> builder.bucket(bucket).key(fileKey));
    }
}
