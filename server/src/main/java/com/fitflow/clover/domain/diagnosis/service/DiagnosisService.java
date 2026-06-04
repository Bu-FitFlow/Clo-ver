package com.fitflow.clover.domain.diagnosis.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitflow.clover.domain.diagnosis.entity.Diagnosis;
import com.fitflow.clover.domain.diagnosis.repository.DiagnosisRepository;
import com.fitflow.clover.global.error.CustomException;
import com.fitflow.clover.global.error.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Transactional
public class DiagnosisService {
    private final DiagnosisRepository diagnosisRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.ai-diagnosis-url}")
    private String appAiDiagnosisUrl;

    public DiagnosisService(DiagnosisRepository diagnosisRepository) {
        this.diagnosisRepository = diagnosisRepository;
    }

    public Diagnosis createBodyScan(Long memberId, String gender, Integer height, Integer weight,
                                    MultipartFile frontImg, MultipartFile sideImg) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("gender", gender);
        body.add("front_img", frontImg.getResource());
        body.add("side_img", sideImg.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    appAiDiagnosisUrl + "/analyze/body",
                    requestEntity,
                    String.class
            );

            JsonNode rootNode = objectMapper.readTree(response.getBody());
            String obesityType = rootNode.get("body_type").asText();

            Optional<Diagnosis> existingDiagnosis = diagnosisRepository.findByMemberId(memberId);

            if (existingDiagnosis.isPresent()) {
                Diagnosis diagnosis = existingDiagnosis.get();
                diagnosis.updateBodyType(height, weight, obesityType);
                return diagnosis;
            } else {
                Diagnosis newDiagnosis = new Diagnosis(
                        memberId,
                        height,
                        weight,
                        obesityType,
                        "TBD",
                        "TBD",
                        "체형 분석 완료",
                        "추후 상세 추천이 제공됩니다."
                );
                return diagnosisRepository.save(newDiagnosis);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.AI_SERVER_ERROR);
        }
    }

    public Diagnosis createColorScan(Long memberId, MultipartFile frontImg) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("front_img", frontImg.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    appAiDiagnosisUrl + "/analyze/color",
                    requestEntity,
                    String.class
            );

            JsonNode rootNode = objectMapper.readTree(response.getBody());
            String personalColor = rootNode.get("personal_color").asText();

            Optional<Diagnosis> existingDiagnosis = diagnosisRepository.findByMemberId(memberId);

            if (existingDiagnosis.isPresent()) {
                Diagnosis diagnosis = existingDiagnosis.get();
                diagnosis.updatePersonalColor(personalColor);
                return diagnosis;
            } else {
                Diagnosis newDiagnosis = new Diagnosis(
                        memberId,
                        0,
                        0,
                        "TBD",
                        "TBD",
                        personalColor,
                        "퍼스널 컬러 분석 완료",
                        "추후 상세 추천이 제공됩니다."
                );
                return diagnosisRepository.save(newDiagnosis);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.AI_SERVER_ERROR);
        }
    }
}
