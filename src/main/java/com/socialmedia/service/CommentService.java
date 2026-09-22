package com.socialmedia.service;

import com.socialmedia.dto.CommentRequest;
import com.socialmedia.dto.CommentResponse;
import com.socialmedia.entity.Comment;
import com.socialmedia.entity.Post;
import com.socialmedia.entity.User;
import com.socialmedia.exception.ResourceNotFoundException;
import com.socialmedia.exception.UnauthorizedException;
import com.socialmedia.repository.CommentRepository;
import com.socialmedia.repository.PostRepository;
import com.socialmedia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    public CommentResponse createComment(Long postId, Long userId, CommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post haipatikani"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Mtumiaji haipatikani"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setPost(post);
        comment.setUser(user);

        Comment saved = commentRepository.save(comment);

        post.setCommentsCount(post.getCommentsCount() + 1);
        postRepository.save(post);

        try {
            String preview = request.getContent();
            if (preview.length() > 50) {
                preview = preview.substring(0, 50) + "...";
            }
            
            notificationService.createNotification(
                post.getUser().getId(),
                userId,
                "COMMENT",
                postId,
                "alicomment: " + preview
            );
        } catch (Exception e) {
            System.err.println("Notification error: " + e.getMessage());
        }

        return toResponse(saved);
    }

    public List<CommentResponse> getCommentsByPost(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment haipatikani"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Huwezi kufuta comment ya mtu mwingine");
        }

        Post post = comment.getPost();
        commentRepository.delete(comment);

        post.setCommentsCount(Math.max(0, post.getCommentsCount() - 1));
        postRepository.save(post);
    }

    private CommentResponse toResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setCreatedAt(comment.getCreatedAt());

        User user = comment.getUser();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setFullName(user.getFullName());
        response.setUserProfilePicture(user.getProfilePicture());

        response.setPostId(comment.getPost().getId());

        return response;
    }
}
