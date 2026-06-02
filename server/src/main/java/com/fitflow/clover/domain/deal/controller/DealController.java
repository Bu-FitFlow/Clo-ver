package com.fitflow.clover.domain.deal.controller;

import com.fitflow.clover.domain.deal.dto.request.DealCreateRequest;
import com.fitflow.clover.domain.deal.dto.request.DealStatusUpdateRequest;
import com.fitflow.clover.domain.deal.dto.response.DealResponse;
import com.fitflow.clover.domain.deal.entity.DealRole;
import com.fitflow.clover.domain.deal.service.DealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "거래 관리", description = "거래 생성 및 상태 관리 관련 API")
@RestController
@RequestMapping("/api/deals")
@RequiredArgsConstructor
public class DealController {
    private final DealService dealService;

    @Operation(summary = "거래 생성", description = "구매자, 판매자, 상품 ID를 전달받아 새로운 거래를 생성합니다. (초기 상태: IN_PROGRESS)")
    @PostMapping
    public ResponseEntity<Long> createDeal(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody DealCreateRequest request) {
        Long buyerId = Long.parseLong(userDetails.getUsername());

        Long dealId = dealService.createDeal(
                buyerId,
                request.sellerId(),
                request.productId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(dealId);
    }

    @Operation(summary = "거래 상태 변경", description = "기존 거래의 상태를 변경합니다. (예: IN_PROGRESS -> COMPLETED)")
    @PatchMapping("/{dealId}/status")
    public ResponseEntity<Void> updateDealStatus(
            @PathVariable Long dealId,
            @RequestBody DealStatusUpdateRequest request) {
        dealService.updateDealStatus(dealId, request.dealStatus());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "내 거래 내역 조회", description = "내가 구매하거나 판매한 거래 내역을 무한 스크롤(Slice) 방식으로 조회합니다. (role: BUYER 또는 SELLER)")
    @GetMapping("/me")
    public ResponseEntity<Slice<DealResponse>> getMyDeals(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam DealRole role,
            @PageableDefault Pageable pageable) {
        Long memberId = Long.parseLong(userDetails.getUsername());

        Slice<DealResponse> response = dealService.getMyDeals(memberId, role, pageable);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "거래 삭제", description = "특정 거래 내역을 삭제합니다. (단, 완료된 거래는 삭제 불가, 참여자만 삭제 가능)")
    @DeleteMapping("/{dealId}")
    public ResponseEntity<String> deleteDeal(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long dealId) {

        Long memberId = Long.parseLong(userDetails.getUsername());
        dealService.deleteDeal(memberId, dealId);

        return ResponseEntity.ok("거래 내역이 성공적으로 삭제되었습니다.");
    }
}
