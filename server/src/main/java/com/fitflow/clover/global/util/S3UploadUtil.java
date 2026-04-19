package com.fitflow.clover.global.util;

import com.fitflow.clover.global.error.CustomException;
import com.fitflow.clover.global.error.ErrorCode;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3UploadUtil {
    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    public String upload(MultipartFile multipartFile, String dirName) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String uniqueFilename = dirName + "/" + UUID.randomUUID() + "_" + originalFilename;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            return s3Template.upload(bucketName, uniqueFilename, inputStream, null)
                    .getURL()
                    .toString();
        } catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
