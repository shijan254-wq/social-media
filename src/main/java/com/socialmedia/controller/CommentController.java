package com.socialmedia.controller;

import com.socialmedia.dto.ApiResponse;
import com.socialmedia.dto.CommentRequest;
import com.socialmedia.dto.CommentResponse;
import com.socialmedia.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long postId,
            @RequestParam Long userId,
            @Valid @RequestBody CommentRequest request) {

        CommentResponse comment = commentService.createComment(postId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Comment imeongezwa!", comment));
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(
            @PathVariable Long postId) {

        List<CommentResponse> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(ApiResponse.success("Comments za post", comments));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long id,
            @RequestParam Long userId) {

        commentService.deleteComment(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Comment imefutwa"));
    }
}
