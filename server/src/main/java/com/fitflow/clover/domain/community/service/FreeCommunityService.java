package com.fitflow.clover.domain.community.service;

import com.fitflow.clover.domain.community.dto.request.FreePostRequest;
import com.fitflow.clover.domain.community.dto.response.CommunityListResponse;
import com.fitflow.clover.domain.community.dto.response.FreeDetailResponse;
import com.fitflow.clover.domain.community.entity.BoardType;
import com.fitflow.clover.domain.community.entity.Community;
import com.fitflow.clover.domain.community.repository.CommentRepository;
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
public class FreeCommunityService {

    private final CommunityRepository communityRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    public List<CommunityListResponse> getFreeList() {
        return communityRepository.findByBoardType(BoardType.FREE).stream()
                .map(c -> new CommunityListResponse(
                        c.getCommunityId(),
                        c.getTitle(),
                        c.getMemberId(),
                        c.getViewCount(),
                        c.getCommentCount(),
                        c.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createFreePost(FreePostRequest request, Long memberId) {
        Community community = Community.builder()
                .boardType(BoardType.FREE)
                .memberId(memberId)
                .title(request.title())
                .content(request.content())
                .build();
        return communityRepository.save(community).getCommunityId();
    }

    public FreeDetailResponse getFreePost(Long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 자유 게시글입니다."));

        Member writer = memberRepository.findById(community.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        return new FreeDetailResponse(
                community.getCommunityId(),
                community.getTitle(),
                community.getContent(),
                List.of(),
                writer.getNickname(),
                null,
                community.getViewCount(),
                community.getCommentCount(),
                community.getCreatedAt()
        );
    }

    @Transactional
    public void updateFreePost(Long communityId, FreePostRequest request, Long memberId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 자유 게시글입니다."));

        if (!community.getMemberId().equals(memberId)) {
            throw new IllegalStateException("본인 글만 수정할 수 있습니다.");
        }

        community.updateFreePost(request.title(), request.content());
    }

    @Transactional
    public void deleteFreePost(Long communityId, Long memberId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 자유 게시글입니다."));

        if (!community.getMemberId().equals(memberId)) {
            throw new IllegalStateException("본인 글만 삭제할 수 있습니다.");
        }

        communityRepository.delete(community);
    }

}