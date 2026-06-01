package com.fitflow.clover.global.image.repository;

import com.fitflow.clover.global.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByReferenceTypeAndReferenceIdOrderBySortOrderAsc(
            Image.ReferenceType referenceType,
            Long referenceId
    );

    List<Image> findByReferenceTypeAndReferenceIdInOrderBySortOrderAsc(
            Image.ReferenceType referenceType,
            List<Long> referenceIds
    );
}