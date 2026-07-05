package com.congresstracker.socialmonitor.dto;

import com.congresstracker.socialmonitor.domain.Notification;
import com.congresstracker.socialmonitor.domain.Platform;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public final class DashboardDtos {

    private DashboardDtos() {
    }

    public record DashboardStats(
            long totalMembers,
            long connectedAccounts,
            long pendingAccounts,
            long postsToday,
            long postsThisWeek,
            long postsThisMonth,
            Map<Platform, Long> platformPosts,
            List<MemberRanking> mostActiveMembers,
            List<MemberRanking> leastActiveMembers,
            List<PostDtos.PostResponse> recentPosts,
            List<NotificationResponse> notifications
    ) {
    }

    public record MemberRanking(
            Long memberId,
            String memberName,
            String district,
            String constituency,
            long postCount,
            long engagement
    ) {
    }

    public record NotificationResponse(
            Long id,
            String type,
            String title,
            String message,
            boolean read,
            Instant createdAt
    ) {
        public static NotificationResponse from(Notification notification) {
            return new NotificationResponse(
                    notification.getId(),
                    notification.getType(),
                    notification.getTitle(),
                    notification.getMessage(),
                    notification.isRead(),
                    notification.getCreatedAt()
            );
        }
    }
}
