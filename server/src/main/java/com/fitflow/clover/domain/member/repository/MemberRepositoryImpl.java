package com.fitflow.clover.domain.member.repository;

import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.entity.QMember;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> searchMembers(String keyword, String role) {
        QMember member = QMember.member;
        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.trim().isEmpty()) {
            builder.and(member.name.containsIgnoreCase(keyword)
                    .or(member.nickname.containsIgnoreCase(keyword)));
        }

        if (role != null && !role.trim().isEmpty()) {
            builder.and(member.role.eq(role));
        }

        builder.and(member.isDeleted.isFalse());

        return queryFactory.selectFrom(member)
                .where(builder)
                .orderBy(member.createdAt.desc()) // 최신 가입자 순
                .fetch();
    }
}
