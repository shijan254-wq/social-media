package com.socialmedia.controller;

import com.socialmedia.dto.ApiResponse;
import com.socialmedia.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> toggleLike(
            @PathVariable Long postId,
            @RequestParam Long userId) {

        boolean liked = likeService.toggleLike(postId, userId);
        Long count = likeService.countLikes(postId);

        Map<String, Object> data = new HashMap<>();
        data.put("liked", liked);
        data.put("likesCount", count);

        String message = liked ? "Umependa post!" : "Umeondoa like";
        return ResponseEntity.ok(ApiResponse.success(message, data));
    }

    @GetMapping("/post/{postId}/user/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> hasLiked(
            @PathVariable Long postId,
            @PathVariable Long userId) {

        boolean liked = likeService.hasLiked(postId, userId);
        return ResponseEntity.ok(ApiResponse.success("Like status", liked));
    }
}
