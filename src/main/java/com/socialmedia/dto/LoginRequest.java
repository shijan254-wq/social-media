package com.socialmedia.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Username au email inahitajika")
    private String username;

    @NotBlank(message = "Password inahitajika")
    private String password;
}
