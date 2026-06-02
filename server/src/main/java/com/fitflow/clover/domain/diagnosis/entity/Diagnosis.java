package com.fitflow.clover.domain.diagnosis.entity;

import com.fitflow.clover.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "bodytype")
public class Diagnosis extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bodytype_id")
    private Long id;

    @Column(name = "member_id", nullable = false, unique = true)
    private Long memberId;

    @Column(nullable = false)
    private Integer height;

    @Column(nullable = false)
    private Integer weight;

    @Column(name = "obesity_type", length = 50, nullable = false)
    private String obesityType;

    @Column(name = "face_shape", length = 50, nullable = false)
    private String faceShape;

    @Column(name = "personal_color", length = 50, nullable = false)
    private String personalColor;

    @Column(name = "result_title", nullable = false)
    private String resultTitle;

    @Column(name = "result_recommend", columnDefinition = "TEXT", nullable = false)
    private String resultRecommend;

    protected Diagnosis() {
    }

    public Diagnosis(Long memberId, Integer height, Integer weight, String obesityType,
                     String faceShape, String personalColor, String resultTitle, String resultRecommend) {
        this.memberId = memberId;
        this.height = height;
        this.weight = weight;
        this.obesityType = obesityType;
        this.faceShape = faceShape;
        this.personalColor = personalColor;
        this.resultTitle = resultTitle;
        this.resultRecommend = resultRecommend;
    }

    public void updateValues(Integer height, Integer weight, String obesityType,
                             String faceShape, String personalColor, String resultTitle, String resultRecommend) {
        this.height = height;
        this.weight = weight;
        this.obesityType = obesityType;
        this.faceShape = faceShape;
        this.personalColor = personalColor;
        this.resultTitle = resultTitle;
        this.resultRecommend = resultRecommend;
    }
}
