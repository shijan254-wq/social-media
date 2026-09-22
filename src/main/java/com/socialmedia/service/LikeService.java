package com.socialmedia.service;

import com.socialmedia.entity.Like;
import com.socialmedia.entity.Post;
import com.socialmedia.entity.User;
import com.socialmedia.exception.ResourceNotFoundException;
import com.socialmedia.repository.LikeRepository;
import com.socialmedia.repository.PostRepository;
import com.socialmedia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    public boolean toggleLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post haipatikani"));

        if (likeRepository.existsByPostIdAndUserId(postId, userId)) {
            likeRepository.deleteByPostIdAndUserId(postId, userId);
            post.setLikesCount(Math.max(0, post.getLikesCount() - 1));
            postRepository.save(post);
            return false;
        } else {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Mtumiaji haipatikani"));

            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            likeRepository.save(like);

            post.setLikesCount(post.getLikesCount() + 1);
            postRepository.save(post);

            try {
                notificationService.createNotification(
                    post.getUser().getId(),
                    userId,
                    "LIKE",
                    postId,
                    "alipenda post yako"
                );
            } catch (Exception e) {
                System.err.println("Notification error: " + e.getMessage());
            }

            return true;
        }
    }

    public boolean hasLiked(Long postId, Long userId) {
        return likeRepository.existsByPostIdAndUserId(postId, userId);
    }

    public Long countLikes(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}
