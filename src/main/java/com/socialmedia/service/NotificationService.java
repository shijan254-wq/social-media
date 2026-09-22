package com.socialmedia.service;

import com.socialmedia.dto.NotificationDTO;
import com.socialmedia.entity.Notification;
import com.socialmedia.entity.User;
import com.socialmedia.repository.NotificationRepository;
import com.socialmedia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createNotification(Long userId, Long fromUserId, String type, 
                                   Long referenceId, String message) {
        
        if (userId.equals(fromUserId)) return;
        
        User user = userRepository.findById(userId).orElse(null);
        User fromUser = userRepository.findById(fromUserId).orElse(null);
        
        if (user == null || fromUser == null) return;
        
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setFromUser(fromUser);
        notification.setType(type);
        notification.setReferenceId(referenceId);
        notification.setMessage(message);
        notification.setIsRead(false);
        
        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Long countUnread(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsReadByUserId(userId);
    }

    @Transactional
    public void deleteByReference(Long referenceId, String type) {
        notificationRepository.deleteByReferenceAndType(referenceId, type);
    }

    private NotificationDTO toDTO(Notification n) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(n.getId());
        dto.setType(n.getType());
        dto.setMessage(n.getMessage());
        dto.setReferenceId(n.getReferenceId());
        dto.setIsRead(n.getIsRead());
        dto.setCreatedAt(n.getCreatedAt());
        
        User fromUser = n.getFromUser();
        dto.setFromUserId(fromUser.getId());
        dto.setFromUsername(fromUser.getUsername());
        dto.setFromFullName(fromUser.getFullName());
        dto.setFromProfilePicture(fromUser.getProfilePicture());
        
        return dto;
    }
}
