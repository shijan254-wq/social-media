package com.socialmedia.repository;

import com.socialmedia.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);

    Page<Post> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @Query(value = "SELECT p FROM Post p JOIN FETCH p.user ORDER BY p.createdAt DESC",
           countQuery = "SELECT COUNT(p) FROM Post p")
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query(value = "SELECT p FROM Post p JOIN FETCH p.user WHERE p.user.id IN " +
                   "(SELECT f.following.id FROM Follower f WHERE f.follower.id = :userId) " +
                   "OR p.user.id = :userId " +
                   "ORDER BY p.createdAt DESC",
           countQuery = "SELECT COUNT(p) FROM Post p WHERE p.user.id IN " +
                        "(SELECT f.following.id FROM Follower f WHERE f.follower.id = :userId) " +
                        "OR p.user.id = :userId")
    Page<Post> findFeedForUser(@Param("userId") Long userId, Pageable pageable);

    List<Post> findByContentContainingIgnoreCaseOrderByCreatedAtDesc(String keyword);

    Long countByUserId(Long userId);
}
