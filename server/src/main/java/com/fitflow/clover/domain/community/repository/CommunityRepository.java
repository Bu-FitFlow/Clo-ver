package com.fitflow.clover.domain.community.repository;

import com.fitflow.clover.domain.community.entity.BoardType;
import com.fitflow.clover.domain.community.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {

    List<Community> findByBoardType(BoardType boardType);

    List<Community> findByMemberId(Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("update Community c set c.viewCount = c.viewCount + 1 where c.communityId = :id")
    void updateViewCount(@Param("id") Long id);

    @Query("SELECT c FROM Community c WHERE c.title LIKE %:keyword% OR c.content LIKE %:keyword%")
    List<Community> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT c FROM Community c WHERE c.boardType = :boardType AND (c.title LIKE %:keyword% OR c.content LIKE %:keyword%)")
    List<Community> searchByKeywordAndBoardType(@Param("keyword") String keyword, @Param("boardType") BoardType boardType);
}

