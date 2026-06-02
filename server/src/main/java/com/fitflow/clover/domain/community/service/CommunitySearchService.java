package com.fitflow.clover.domain.community.service;

import com.fitflow.clover.domain.community.dto.response.CommunitySearchResponse;
import com.fitflow.clover.domain.community.repository.CommunityRepository;
import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunitySearchService {

    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private final BlockService blockService;

    public List<CommunitySearchResponse> search(String keyword, Long currentMemberId) {
        return communityRepository.searchByKeyword(keyword).stream()
                .filter(c -> !blockService.isBlocked(currentMemberId, c.getMemberId()))
                .map(c -> {
                    Member writer = memberRepository.findById(c.getMemberId()).orElse(null);
                    return new CommunitySearchResponse(
                            c.getCommunityId(),
                            c.getBoardType().name(),
                            c.getTitle(),
                            writer != null ? writer.getNickname() : "알 수 없음",
                            c.getViewCount(),
                            c.getCommentCount(),
                            c.getWishlistCount(),
                            c.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());
    }
}
