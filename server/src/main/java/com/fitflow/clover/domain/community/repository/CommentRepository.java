package com.fitflow.clover.domain.community.repository;

import com.fitflow.clover.domain.community.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByCommunityIdAndParentIdIsNull(Long communityId);


    List<Comment> findByParentId(Long parentId);
}