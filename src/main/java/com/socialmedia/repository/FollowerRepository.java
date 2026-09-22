package com.socialmedia.repository;

import com.socialmedia.entity.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {

    Optional<Follower> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Long countByFollowingId(Long userId);

    Long countByFollowerId(Long userId);

    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
