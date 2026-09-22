package com.socialmedia.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

    @Size(max = 100, message = "Jina liwe herufi 100 max")
    private String fullName;

    @Size(max = 500, message = "Bio iwe herufi 500 max")
    private String bio;

    private String profilePicture;
}
