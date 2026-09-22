package com.socialmedia.controller;

import com.socialmedia.dto.ApiResponse;
import com.socialmedia.dto.AuthResponse;
import com.socialmedia.dto.LoginRequest;
import com.socialmedia.dto.RegisterRequest;
import com.socialmedia.entity.User;
import com.socialmedia.security.CustomUserDetailsService;
import com.socialmedia.security.JwtService;
import com.socialmedia.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        User user = userService.registerUser(request);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setId(user.getId());
        authResponse.setUsername(user.getUsername());
        authResponse.setEmail(user.getEmail());
        authResponse.setFullName(user.getFullName());
        authResponse.setProfilePicture(user.getProfilePicture());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Usajili umefanikiwa!", authResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        User user = userService.getUserByUsername(request.getUsername());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Password si sahihi"));
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setId(user.getId());
        authResponse.setUsername(user.getUsername());
        authResponse.setEmail(user.getEmail());
        authResponse.setFullName(user.getFullName());
        authResponse.setProfilePicture(user.getProfilePicture());

        return ResponseEntity.ok(
                ApiResponse.success("Umeingia kikamilifu!", authResponse));
    }
}
