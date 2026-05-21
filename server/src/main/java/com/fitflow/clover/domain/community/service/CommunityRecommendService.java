package com.fitflow.clover.domain.community.service;

import com.fitflow.clover.domain.community.dto.response.CommunityRecommendResponse;
import com.fitflow.clover.domain.community.entity.BoardType;
import com.fitflow.clover.domain.community.repository.CommunityRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityRecommendService {

    private final CommunityRepository communityRepository;

    public List<CommunityRecommendResponse> getPopularPosts(String boardType) {
        BoardType type = BoardType.valueOf(boardType.toUpperCase());

        return communityRepository.findByBoardType(type).stream()
                .sorted((c1, c2) -> Integer.compare(c2.getViewCount(), c1.getViewCount()))
                .limit(5)
                .map(c -> new CommunityRecommendResponse(
                        c.getCommunityId(),
                        c.getBoardType().name(),
                        c.getTitle(),
                        null,
                        "인기유저",
                        c.getViewCount(),
                        c.getCommentCount()
                ))
                .collect(Collectors.toList());
    }
}