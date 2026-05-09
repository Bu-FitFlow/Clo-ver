package com.fitflow.clover.global.image.service;

import com.fitflow.clover.global.image.entity.Image;
import com.fitflow.clover.global.image.repository.ImageRepository;
import com.fitflow.clover.global.infra.aws.S3UploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {
    private final S3UploadUtil s3UploadUtil;
    private final ImageRepository imageRepository;

    @Transactional
    public void saveImages(List<MultipartFile> files, Image.ReferenceType type, Long referenceId, String dirName) {
        if (files == null || files.isEmpty()) return;

        IntStream.range(0, files.size()).forEach(i -> {
            MultipartFile file = files.get(i);
            try {
                String imageUrl = s3UploadUtil.upload(file, dirName);
                Image image = Image.builder()
                        .imageUrl(imageUrl)
                        .referenceType(type)
                        .referenceId(referenceId)
                        .sortOrder(i)
                        .build();
                imageRepository.save(image);
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드에 실패했습니다.", e);
            }
        });
    }

    public List<String> getImageUrlList(Image.ReferenceType type, Long referenceId) {
        return imageRepository.findByReferenceTypeAndReferenceIdOrderBySortOrderAsc(type, referenceId)
                .stream()
                .map(Image::getImageUrl)
                .toList();

    }

    @Transactional
    public void deleteImages(Image.ReferenceType type, Long referenceId) {
        List<Image> images = imageRepository.findByReferenceTypeAndReferenceIdOrderBySortOrderAsc(type, referenceId);
        images.forEach(image -> s3UploadUtil.delete(image.getImageUrl()));
        imageRepository.deleteAll(images);
    }

    public Map<Long, List<String>> getImageUrlMap(Image.ReferenceType type, List<Long> referenceIds) {
        if (referenceIds == null || referenceIds.isEmpty()) {
            return Collections.emptyMap();
        }

        // 1. IN 쿼리로 이미지들을 한방에 싹 다 가져옵니다.
        List<Image> images = imageRepository.findByReferenceTypeAndReferenceIdInOrderBySortOrderAsc(type, referenceIds);

        // 2. 상품 ID(referenceId) 기준으로 그룹화해서 Map을 만듭니다.
        return images.stream()
                .collect(Collectors.groupingBy(
                        Image::getReferenceId,
                        Collectors.mapping(Image::getImageUrl, Collectors.toList())
                ));
    }
}
