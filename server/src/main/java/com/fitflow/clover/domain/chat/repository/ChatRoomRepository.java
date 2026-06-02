package com.fitflow.clover.domain.chat.repository;

import com.fitflow.clover.domain.chat.entity.ChatRoom;
import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
    Optional<ChatRoom> findByProductAndBuyer(Product product, Member buyer);
}
