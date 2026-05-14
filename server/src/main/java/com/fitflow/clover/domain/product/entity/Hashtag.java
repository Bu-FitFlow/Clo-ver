package com.fitflow.clover.domain.product.entity;

import com.fitflow.clover.global.common.BaseCreatedTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Hashtag extends BaseCreatedTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long hashtagId;

    @Column(name = "tag_name", nullable = false, unique = true, length = 50)
    private String tagName;
}
