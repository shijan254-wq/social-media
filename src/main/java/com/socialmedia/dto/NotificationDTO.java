package com.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {

    private Long id;
    private String type;
    private String message;
    private Long referenceId;
    private Boolean isRead;
    private LocalDateTime createdAt;

    private Long fromUserId;
    private String fromUsername;
    private String fromFullName;
    private String fromProfilePicture;
}
