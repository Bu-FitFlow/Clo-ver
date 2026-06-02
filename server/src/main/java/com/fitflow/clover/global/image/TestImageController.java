package com.fitflow.clover.global.image;

import com.fitflow.clover.global.infra.aws.S3UploadUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Image Test", description = "S3 이미지 업로드/삭제 테스트용 API")
@RestController
@RequestMapping("/api/test/image")
@RequiredArgsConstructor
public class TestImageController {
    private final S3UploadUtil s3UploadUtil;

    @Operation(summary = "이미지 업로드 테스트")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadTest(@RequestParam MultipartFile file) throws IOException {
        String uploadedUrl = s3UploadUtil.upload(file, "test");
        return "업로드 성공! CDN URL: " + uploadedUrl;
    }

    @Operation(summary = "이미지 삭제 테스트")
    @DeleteMapping
    public String deleteTest(@RequestParam String fileUrl) {
        s3UploadUtil.delete(fileUrl);
        return "삭제 성공 완료 요청됨 (에러가 안 났다면 성공입니다!)";
    }

}
