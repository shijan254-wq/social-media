package com.socialmedia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Username inahitajika")
    @Size(min = 3, max = 50, message = "Username iwe herufi 3-50")
    private String username;

    @NotBlank(message = "Email inahitajika")
    @Email(message = "Email si sahihi")
    private String email;

    @NotBlank(message = "Password inahitajika")
    @Size(min = 6, message = "Password iwe herufi 6+")
    private String password;

    @NotBlank(message = "Jina kamili linahitajika")
    private String fullName;
}
