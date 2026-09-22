package com.socialmedia.controller;

import com.socialmedia.dto.ApiResponse;
import com.socialmedia.dto.PostRequest;
import com.socialmedia.dto.PostResponse;
import com.socialmedia.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @RequestParam Long userId,
            @Valid @RequestBody PostRequest request) {

        PostResponse post = postService.createPost(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Post imeundwa!", post));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable Long id) {
        PostResponse post = postService.getPostById(id, null);
        return ResponseEntity.ok(ApiResponse.success("Post imepatikana", post));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PostResponse> posts = postService.getAllPosts(null, page, size);
        return ResponseEntity.ok(ApiResponse.success("Posts zote", posts));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getUserPosts(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PostResponse> posts = postService.getUserPosts(userId, null, page, size);
        return ResponseEntity.ok(ApiResponse.success("Posts za mtumiaji", posts));
    }

    @GetMapping("/feed")
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getFeed(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PostResponse> posts = postService.getFeed(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success("Feed yako", posts));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable Long id,
            @RequestParam Long userId,
            @Valid @RequestBody PostRequest request) {

        PostResponse post = postService.updatePost(id, userId, request);
        return ResponseEntity.ok(ApiResponse.success("Post imebadilishwa", post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable Long id,
            @RequestParam Long userId) {

        postService.deletePost(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Post imefutwa"));
    }
}
