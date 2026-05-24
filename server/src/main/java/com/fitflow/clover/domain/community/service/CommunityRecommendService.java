package com.fitflow.clover.domain.community.service;

import com.fitflow.clover.domain.community.dto.response.CommunityRecommendResponse;
import com.fitflow.clover.domain.community.entity.BoardType;
import com.fitflow.clover.domain.community.repository.CommunityRepository;
import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityRecommendService {

    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;

    public List<CommunityRecommendResponse> getPopularPosts() {
        List<CommunityRecommendResponse> result = new ArrayList<>();

        for (BoardType type : new BoardType[]{BoardType.FREE, BoardType.REVIEW, BoardType.STYLING}) {
            List<CommunityRecommendResponse> posts = communityRepository.findByBoardType(type).stream()
                    .sorted((c1, c2) -> Integer.compare(c2.getWishlistCount(), c1.getWishlistCount()))
                    .limit(5)
                    .map(c -> {
                        Member writer = memberRepository.findById(c.getMemberId()).orElse(null);
                        return new CommunityRecommendResponse(
                                c.getCommunityId(),
                                c.getBoardType().name(),
                                c.getTitle(),
                                null,
                                writer != null ? writer.getNickname() : "알 수 없음",
                                c.getViewCount(),
                                c.getCommentCount(),
                                c.getWishlistCount()
                        );
                    })
                    .collect(Collectors.toList());

            result.addAll(posts);
        }

        return result;
    }
}