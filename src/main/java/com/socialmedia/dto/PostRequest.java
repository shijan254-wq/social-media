package com.socialmedia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    @NotBlank(message = "Maudhui yanahitajika")
    @Size(max = 2000, message = "Maudhui yasiwe zaidi ya herufi 2000")
    private String content;

    private String imageUrl;
}
