package com.congresstracker.socialmonitor.repository;

import com.congresstracker.socialmonitor.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findTop10ByOrderByCreatedAtDesc();
}
