package com.fitflow.clover.domain.product.service;

import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.repository.MemberRepository;
import com.fitflow.clover.domain.product.dto.request.ProductCreateRequest;
import com.fitflow.clover.domain.product.dto.request.ProductSearchCondition;
import com.fitflow.clover.domain.product.dto.request.ProductUpdateRequest;
import com.fitflow.clover.domain.product.dto.response.ProductDetailResponse;
import com.fitflow.clover.domain.product.dto.response.ProductListResponse;
import com.fitflow.clover.domain.product.entity.*;
import com.fitflow.clover.domain.product.repository.CategoryRepository;
import com.fitflow.clover.domain.product.repository.HashtagRepository;
import com.fitflow.clover.domain.product.repository.ProductRepository;
import com.fitflow.clover.domain.product.repository.WishlistRepository;
import com.fitflow.clover.global.error.CustomException;
import com.fitflow.clover.global.error.ErrorCode;
import com.fitflow.clover.global.image.entity.Image;
import com.fitflow.clover.global.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final HashtagRepository hashtagRepository;
    private final WishlistRepository wishlistRepository;
    private final ImageService imageService;

    @Transactional
    public Long createProduct(Long memberId, ProductCreateRequest request, List<MultipartFile> images) {
        Member seller = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        Product product = Product.builder()
                .seller(seller)
                .category(category)
                .name(request.name())
                .price(request.price())
                .content(request.content())
                .size(request.size())
                .grade(request.grade())
                .tradingArea(request.tradingArea())
                .recommendedType(request.recommendedType())
                .build();

        if (request.hashtags() != null && !request.hashtags().isEmpty()) {
            for (String tagName : request.hashtags()) {
                Hashtag hashtag = hashtagRepository.findByTagName(tagName)
                        .orElseGet(() -> hashtagRepository.save(Hashtag.builder().tagName(tagName).build()));

                ProductHashtag productHashtag = ProductHashtag.builder()
                        .product(product)
                        .hashtag(hashtag)
                        .build();

                product.addProductHashtag(productHashtag);
            }
        }

        Product savedProduct = productRepository.save(product);

        if (images != null && !images.isEmpty()) {
            imageService.saveImages(
                    images,
                    Image.ReferenceType.PRODUCT,
                    savedProduct.getProductId(),
                    "product"
            );
        }

        return savedProduct.getProductId();
    }

    @Transactional
    public ProductDetailResponse getProductDetail(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        product.increaseViewCount();

        List<String> imageUrls = imageService.getImageUrlList(Image.ReferenceType.PRODUCT, productId);
        List<String> hashtags = product.getProductHashtags().stream()
                .map(ph -> ph.getHashtag().getTagName())
                .toList();
        return ProductDetailResponse.from(product, imageUrls, hashtags);
    }

    public Slice<ProductListResponse> getProductList(ProductSearchCondition condition, Pageable pageable) {
        Slice<Product> productSlice = productRepository.searchProducts(condition, pageable);

        List<Product> products = productSlice.getContent();

        List<Long> productIds = products.stream()
                .map(Product::getProductId)
                .toList();

        Map<Long, List<String>> imageUrlMap = imageService.getImageUrlMap(Image.ReferenceType.PRODUCT, productIds);

        List<ProductListResponse> content = products.stream()
                .map(product -> {
                    List<String> images = imageUrlMap.getOrDefault(product.getProductId(), Collections.emptyList());
                    String thumbnail = images.isEmpty() ? null : images.getFirst();
                    return ProductListResponse.from(product, thumbnail);
                })
                .toList();

        return new SliceImpl<>(content, pageable, productSlice.hasNext());

    }

    @Transactional
    public void updateProduct(Long memberId, Long productId, ProductUpdateRequest request, List<MultipartFile> images) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        if (!product.getSeller().getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        product.update(category, request);

        product.getProductHashtags().clear();
        if (request.hashtags() != null && !request.hashtags().isEmpty()) {
            for (String tagName : request.hashtags()) {
                Hashtag hashtag = hashtagRepository.findByTagName(tagName)
                        .orElseGet(() -> hashtagRepository.save(Hashtag.builder().tagName(tagName).build()));

                product.addProductHashtag(ProductHashtag.builder()
                        .product(product)
                        .hashtag(hashtag)
                        .build());
            }
        }

        if (images != null && !images.isEmpty()) {
            imageService.deleteImages(Image.ReferenceType.PRODUCT, productId);
            imageService.saveImages(images, Image.ReferenceType.PRODUCT, productId, "product");
        }
    }

    @Transactional
    public void deleteProduct(Long memberId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        if (!product.getSeller().getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        product.changeStatus(ProductStatus.DELETED);
    }

    @Transactional
    public String toggleWishlist(Long memberId, Long productId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        Optional<Wishlist> existingWishlist = wishlistRepository.findByMember_MemberIdAndProduct_ProductId(memberId, productId);

        if (existingWishlist.isPresent()) {
            wishlistRepository.delete(existingWishlist.get());
            product.decreaseWishlistCount();
            return "찜이 취소되었습니다.";
        } else {
            Wishlist newWishlist = Wishlist.builder()
                    .member(member)
                    .product(product)
                    .build();

            wishlistRepository.save(newWishlist);
            product.increaseWishlistCount();
            return "찜 목록에 추가되었습니다.";
        }
    }
}
