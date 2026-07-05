package com.congresstracker.socialmonitor.service;

import com.congresstracker.socialmonitor.domain.AccountStatus;
import com.congresstracker.socialmonitor.domain.Platform;
import com.congresstracker.socialmonitor.dto.DashboardDtos.DashboardStats;
import com.congresstracker.socialmonitor.dto.DashboardDtos.MemberRanking;
import com.congresstracker.socialmonitor.dto.DashboardDtos.NotificationResponse;
import com.congresstracker.socialmonitor.dto.PostDtos.PostResponse;
import com.congresstracker.socialmonitor.repository.MemberRepository;
import com.congresstracker.socialmonitor.repository.NotificationRepository;
import com.congresstracker.socialmonitor.repository.SocialAccountRepository;
import com.congresstracker.socialmonitor.repository.SocialPostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final MemberRepository memberRepository;
    private final SocialAccountRepository socialAccountRepository;
    private final SocialPostRepository socialPostRepository;
    private final NotificationRepository notificationRepository;

    public DashboardService(MemberRepository memberRepository,
                            SocialAccountRepository socialAccountRepository,
                            SocialPostRepository socialPostRepository,
                            NotificationRepository notificationRepository) {
        this.memberRepository = memberRepository;
        this.socialAccountRepository = socialAccountRepository;
        this.socialPostRepository = socialPostRepository;
        this.notificationRepository = notificationRepository;
    }

    public DashboardStats stats() {
        LocalDate today = LocalDate.now();
        Map<Platform, Long> platformPosts = new LinkedHashMap<>();
        Arrays.stream(Platform.values()).forEach(platform ->
                platformPosts.put(platform, socialPostRepository.countByPlatform(platform)));

        List<MemberRanking> rankings = socialPostRepository.memberRankings(PageRequest.of(0, 100)).stream()
                .map(row -> new MemberRanking(
                        (Long) row[0],
                        (String) row[1],
                        (String) row[2],
                        (String) row[3],
                        (Long) row[4],
                        (Long) row[5]
                ))
                .toList();

        List<MemberRanking> leastActive = rankings.stream()
                .sorted(Comparator.comparingLong(MemberRanking::postCount)
                        .thenComparingLong(MemberRanking::engagement))
                .limit(5)
                .toList();

        return new DashboardStats(
                memberRepository.count(),
                socialAccountRepository.countByStatus(AccountStatus.CONNECTED),
                socialAccountRepository.countByStatus(AccountStatus.PENDING),
                socialPostRepository.countByPostedAtAfter(today.atStartOfDay()),
                socialPostRepository.countByPostedAtAfter(today.minusDays(7).atStartOfDay()),
                socialPostRepository.countByPostedAtAfter(LocalDateTime.now().withDayOfMonth(1).toLocalDate().atStartOfDay()),
                platformPosts,
                rankings.stream().limit(5).toList(),
                leastActive,
                socialPostRepository.findRecentPosts(PageRequest.of(0, 20)).stream().map(PostResponse::from).toList(),
                notificationRepository.findTop10ByOrderByCreatedAtDesc().stream().map(NotificationResponse::from).toList()
        );
    }
}
