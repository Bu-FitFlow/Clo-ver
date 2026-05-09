package com.fitflow.clover.domain.product.controller;

import com.fitflow.clover.domain.product.dto.request.ProductCreateRequest;
import com.fitflow.clover.domain.product.dto.request.ProductUpdateRequest;
import com.fitflow.clover.domain.product.dto.response.ProductDetailResponse;
import com.fitflow.clover.domain.product.dto.response.ProductListResponse;
import com.fitflow.clover.domain.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "상품 관리", description = "상품 등록, 조회, 수정, 삭제 관련 API")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "상품 등록")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createProduct(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @ModelAttribute ProductCreateRequest request) {
        Long memberId = Long.parseLong(userDetails.getUsername());

        Long savedProductId = productService.createProduct(memberId, request, request.images());

        return ResponseEntity.ok(savedProductId + "번 상품이 성공적으로 등록되었습니다.");
    }

    @Operation(summary = "상품 전체 목록 조회")
    @GetMapping
    public ResponseEntity<List<ProductListResponse>> getProductList() {
        List<ProductListResponse> response = productService.getProductList();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상품 상세 조회")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailResponse> getProductDetail(@PathVariable Long productId) {
        ProductDetailResponse response = productService.getProductDetail(productId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상품 수정")
    @PatchMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateProduct(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId,
            @Valid @ModelAttribute ProductUpdateRequest request,
            List<MultipartFile> images) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        productService.updateProduct(memberId, productId, request, request.images());

        return ResponseEntity.ok("상품 정보가 성공적으로 수정되었습니다.");
    }

    @Operation(summary = "상품 삭제")
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        productService.deleteProduct(memberId, productId);
        return ResponseEntity.ok("상품이 성공적으로 삭제되었습니다.");
    }
}
