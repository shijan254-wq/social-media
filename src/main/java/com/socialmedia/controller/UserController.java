package com.socialmedia.controller;

import com.socialmedia.dto.ApiResponse;
import com.socialmedia.dto.UpdateProfileRequest;
import com.socialmedia.dto.UserResponse;
import com.socialmedia.entity.User;
import com.socialmedia.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id) {
        UserResponse user = userService.getUserResponse(id, null);
        return ResponseEntity.ok(ApiResponse.success("Mtumiaji amepatikana", user));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserResponse>>> searchUsers(
            @RequestParam("q") String keyword) {
        List<UserResponse> users = userService.searchUsers(keyword, null);
        return ResponseEntity.ok(ApiResponse.success("Watumiaji wamepatikana", users));
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getFollowing(@PathVariable Long id) {
        List<UserResponse> users = userService.getFollowing(id, null);
        return ResponseEntity.ok(ApiResponse.success("Watu unaowafuata", users));
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getFollowers(@PathVariable Long id) {
        List<UserResponse> users = userService.getFollowers(id, null);
        return ResponseEntity.ok(ApiResponse.success("Wanaokufuata", users));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProfileRequest request) {

        User updated = userService.updateProfile(id, request);
        UserResponse response = userService.getUserResponse(updated.getId(), null);

        return ResponseEntity.ok(ApiResponse.success("Profile imebadilishwa", response));
    }
}