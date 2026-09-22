package com.socialmedia.service;

import com.socialmedia.dto.PostRequest;
import com.socialmedia.dto.PostResponse;
import com.socialmedia.entity.Post;
import com.socialmedia.entity.User;
import com.socialmedia.exception.ResourceNotFoundException;
import com.socialmedia.exception.UnauthorizedException;
import com.socialmedia.repository.LikeRepository;
import com.socialmedia.repository.PostRepository;
import com.socialmedia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public PostResponse createPost(Long userId, PostRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Mtumiaji haipatikani"));

        Post post = new Post();
        post.setContent(request.getContent());
        post.setImageUrl(request.getImageUrl());
        post.setUser(user);
        post.setLikesCount(0);
        post.setCommentsCount(0);

        Post saved = postRepository.save(post);
        return toResponse(saved, userId);
    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id, Long currentUserId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post haipatikani"));
        return toResponse(post, currentUserId);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getUserPosts(Long userId, Long currentUserId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return posts.map(p -> toResponse(p, currentUserId));
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getFeed(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findFeedForUser(userId, pageable);
        return posts.map(p -> toResponse(p, userId));
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getAllPosts(Long currentUserId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        return posts.map(p -> toResponse(p, currentUserId));
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post haipatikani"));

        if (!post.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Huwezi kufuta post ya mtu mwingine");
        }

        postRepository.delete(post);
    }

    @Transactional
    public PostResponse updatePost(Long postId, Long userId, PostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post haipatikani"));

        if (!post.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Huwezi kubadilisha post ya mtu mwingine");
        }

        post.setContent(request.getContent());
        if (request.getImageUrl() != null) {
            post.setImageUrl(request.getImageUrl());
        }

        Post updated = postRepository.save(post);
        return toResponse(updated, userId);
    }

    private PostResponse toResponse(Post post, Long currentUserId) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setContent(post.getContent());
        response.setImageUrl(post.getImageUrl());
        response.setLikesCount(post.getLikesCount());
        response.setCommentsCount(post.getCommentsCount());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());

        User user = post.getUser();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setFullName(user.getFullName());
        response.setUserProfilePicture(user.getProfilePicture());

        if (currentUserId != null) {
            response.setIsLiked(likeRepository.existsByPostIdAndUserId(post.getId(), currentUserId));
        } else {
            response.setIsLiked(false);
        }

        return response;
    }
}
