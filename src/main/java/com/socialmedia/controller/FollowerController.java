package com.socialmedia.controller;

import com.socialmedia.dto.ApiResponse;
import com.socialmedia.service.FollowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowerController {

    private final FollowerService followerService;

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> toggleFollow(
            @PathVariable Long userId,
            @RequestParam Long currentUserId) {

        boolean following = followerService.toggleFollow(currentUserId, userId);

        Map<String, Object> data = new HashMap<>();
        data.put("following", following);
        data.put("followersCount", followerService.countFollowers(userId));
        data.put("followingCount", followerService.countFollowing(userId));

        String message = following ? "Umefuata mtumiaji!" : "Umeacha kumfuata";
        return ResponseEntity.ok(ApiResponse.success(message, data));
    }

    @GetMapping("/status/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> isFollowing(
            @PathVariable Long userId,
            @RequestParam Long currentUserId) {

        boolean following = followerService.isFollowing(currentUserId, userId);
        return ResponseEntity.ok(ApiResponse.success("Follow status", following));
    }
}
