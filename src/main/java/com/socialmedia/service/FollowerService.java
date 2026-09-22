package com.socialmedia.service;

import com.socialmedia.entity.Follower;
import com.socialmedia.entity.User;
import com.socialmedia.exception.BadRequestException;
import com.socialmedia.exception.ResourceNotFoundException;
import com.socialmedia.repository.FollowerRepository;
import com.socialmedia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowerService {

    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    public boolean toggleFollow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new BadRequestException("Huwezi kujifuata mwenyewe");
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("Mtumiaji haipatikani"));

        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new ResourceNotFoundException("Mtumiaji haipatikani"));

        if (followerRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            followerRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
            return false;
        } else {
            Follower follow = new Follower();
            follow.setFollower(follower);
            follow.setFollowing(following);
            followerRepository.save(follow);

            try {
                notificationService.createNotification(
                    followingId,
                    followerId,
                    "FOLLOW",
                    followerId,
                    "alianza kukufuata"
                );
            } catch (Exception e) {
                System.err.println("Notification error: " + e.getMessage());
            }

            return true;
        }
    }

    public boolean isFollowing(Long followerId, Long followingId) {
        return followerRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }

    public Long countFollowers(Long userId) {
        return followerRepository.countByFollowingId(userId);
    }

    public Long countFollowing(Long userId) {
        return followerRepository.countByFollowerId(userId);
    }
}
