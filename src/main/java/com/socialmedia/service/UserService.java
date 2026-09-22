package com.socialmedia.service;

import com.socialmedia.dto.*;
import com.socialmedia.entity.User;
import com.socialmedia.exception.BadRequestException;
import com.socialmedia.exception.ResourceNotFoundException;
import com.socialmedia.repository.FollowerRepository;
import com.socialmedia.repository.PostRepository;
import com.socialmedia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FollowerRepository followerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username imetumika tayari");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email imetumika tayari");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setBio("");
        user.setProfilePicture("");

        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mtumiaji haipatikani"));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Mtumiaji haipatikani"));
    }

    public UserResponse getUserResponse(Long id, Long currentUserId) {
        User user = getUserById(id);

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setBio(user.getBio());
        response.setProfilePicture(user.getProfilePicture());
        response.setCreatedAt(user.getCreatedAt());

        response.setFollowersCount(followerRepository.countByFollowingId(id));
        response.setFollowingCount(followerRepository.countByFollowerId(id));
        response.setPostsCount(postRepository.countByUserId(id));

        if (currentUserId != null && !currentUserId.equals(id)) {
            response.setIsFollowing(
                followerRepository.existsByFollowerIdAndFollowingId(currentUserId, id)
            );
        } else {
            response.setIsFollowing(false);
        }

        return response;
    }

    public List<UserResponse> searchUsers(String keyword, Long currentUserId) {
        List<User> users = userRepository
                .findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCase(keyword, keyword);

        return users.stream()
                .map(u -> getUserResponse(u.getId(), currentUserId))
                .collect(Collectors.toList());
    }

    public List<UserResponse> getFollowing(Long userId, Long currentUserId) {
        List<User> users = userRepository.findFollowingByUserId(userId);
        return users.stream()
                .map(u -> getUserResponse(u.getId(), currentUserId))
                .collect(Collectors.toList());
    }

    public List<UserResponse> getFollowers(Long userId, Long currentUserId) {
        List<User> users = userRepository.findFollowersByUserId(userId);
        return users.stream()
                .map(u -> getUserResponse(u.getId(), currentUserId))
                .collect(Collectors.toList());
    }

    @Transactional
    public User updateProfile(Long userId, UpdateProfileRequest request) {
        User user = getUserById(userId);

        if (request.getFullName() != null && !request.getFullName().isEmpty()) {
            user.setFullName(request.getFullName());
        }

        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }

        if (request.getProfilePicture() != null) {
            user.setProfilePicture(request.getProfilePicture());
        }

        return userRepository.save(user);
    }
}
