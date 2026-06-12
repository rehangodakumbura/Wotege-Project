package com.example.demo.config;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastRoomStatusChange(Map<String, Object> payload) {
        payload.put("timestamp", LocalDateTime.now().toString());
        messagingTemplate.convertAndSend("/topic/rooms", (Object) payload);
    }

    public void broadcastReservationUpdate(Map<String, Object> payload) {
        payload.put("timestamp", LocalDateTime.now().toString());
        messagingTemplate.convertAndSend("/topic/reservations", (Object) payload);
    }

    public void broadcastDashboardUpdate(Map<String, Object> payload) {
        payload.put("timestamp", LocalDateTime.now().toString());
        messagingTemplate.convertAndSend("/topic/dashboard", (Object) payload);
    }

    public void broadcastNotification(String topic, Map<String, Object> payload) {
        payload.put("timestamp", LocalDateTime.now().toString());
        messagingTemplate.convertAndSend("/topic/" + topic, (Object) payload);
    }
}
