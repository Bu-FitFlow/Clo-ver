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

    @Enumerated(EnumType.STRING)
    @Column(name = "obesity_type", length = 50)
    private BodyType obesityType;

    @Column(name = "face_shape", length = 50, nullable = false)
    private String faceShape;

    @Enumerated(EnumType.STRING)
    @Column(name = "personal_color", length = 50, nullable = false)
    private PersonalColor personalColor;

    @Column(name = "result_title", nullable = false)
    private String resultTitle;

    @Column(name = "result_recommend", columnDefinition = "TEXT", nullable = false)
    private String resultRecommend;

    protected Diagnosis() {
    }

    public Diagnosis(Long memberId, Integer height, Integer weight, BodyType obesityType,
                     String faceShape, PersonalColor personalColor, String resultTitle, String resultRecommend) {
        this.memberId = memberId;
        this.height = height;
        this.weight = weight;
        this.obesityType = obesityType;
        this.faceShape = faceShape;
        this.personalColor = personalColor;
        this.resultTitle = resultTitle;
        this.resultRecommend = resultRecommend;
    }

    public void updateValues(Integer height, Integer weight, BodyType obesityType,
                             String faceShape, PersonalColor personalColor, String resultTitle, String resultRecommend) {
        this.height = height;
        this.weight = weight;
        this.obesityType = obesityType;
        this.faceShape = faceShape;
        this.personalColor = personalColor;
        this.resultTitle = resultTitle;
        this.resultRecommend = resultRecommend;
    }

    public void updateBodyType(Integer height, Integer weight, BodyType obesityType) {
        this.height = height;
        this.weight = weight;
        this.obesityType = obesityType;
        this.resultTitle = "체형 분석 완료";
    }

    public void updatePersonalColor(PersonalColor personalColor) {
        this.personalColor = personalColor;
        this.resultTitle = "퍼스널 컬러 분석 완료";
    }
}
