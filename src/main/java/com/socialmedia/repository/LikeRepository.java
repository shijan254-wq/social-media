package com.socialmedia.repository;

import com.socialmedia.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);

    Boolean existsByPostIdAndUserId(Long postId, Long userId);

    Long countByPostId(Long postId);

    void deleteByPostIdAndUserId(Long postId, Long userId);
}
