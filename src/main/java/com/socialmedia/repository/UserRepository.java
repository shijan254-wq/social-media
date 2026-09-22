package com.socialmedia.repository;

import com.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    List<User> findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCase(
            String username, String fullName);

    @Query("SELECT u FROM User u JOIN Follower f ON f.following = u WHERE f.follower.id = :userId")
    List<User> findFollowingByUserId(@Param("userId") Long userId);

    @Query("SELECT u FROM User u JOIN Follower f ON f.follower = u WHERE f.following.id = :userId")
    List<User> findFollowersByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(f) FROM Follower f WHERE f.following.id = :userId")
    Long countFollowers(@Param("userId") Long userId);

    @Query("SELECT COUNT(f) FROM Follower f WHERE f.follower.id = :userId")
    Long countFollowing(@Param("userId") Long userId);
}
