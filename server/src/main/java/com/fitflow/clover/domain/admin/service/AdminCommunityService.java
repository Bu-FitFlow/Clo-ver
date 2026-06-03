package com.fitflow.clover.domain.admin.service;

import com.fitflow.clover.domain.admin.dto.response.AdminCommunityResponse;
import com.fitflow.clover.domain.community.entity.Community;
import com.fitflow.clover.domain.community.entity.PostStatus;
import com.fitflow.clover.domain.community.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCommunityService {
    private final CommunityRepository communityRepository;

    public Page<AdminCommunityResponse> getPagedAdminCommunities(Pageable pageable) {
        return communityRepository.findAll(pageable)
                .map(AdminCommunityResponse::from);
    }

    @Transactional
    public void changePostStatus(Long communityId, String status) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        community.setPostStatus(PostStatus.valueOf(status));
    }
}
