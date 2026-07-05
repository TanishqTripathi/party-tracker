package com.congresstracker.socialmonitor.service;

import com.congresstracker.socialmonitor.domain.AccountStatus;
import com.congresstracker.socialmonitor.domain.Notification;
import com.congresstracker.socialmonitor.domain.SocialAccount;
import com.congresstracker.socialmonitor.domain.SocialPost;
import com.congresstracker.socialmonitor.repository.NotificationRepository;
import com.congresstracker.socialmonitor.repository.SocialAccountRepository;
import com.congresstracker.socialmonitor.repository.SocialPostRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class MockSyncService {

    private final SocialAccountRepository accountRepository;
    private final SocialPostRepository postRepository;
    private final NotificationRepository notificationRepository;
    private final Random random = new Random(42);

    public MockSyncService(SocialAccountRepository accountRepository,
                           SocialPostRepository postRepository,
                           NotificationRepository notificationRepository) {
        this.accountRepository = accountRepository;
        this.postRepository = postRepository;
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    @Scheduled(fixedRate = 900_000)
    public void syncConnectedAccounts() {
        simulateSync();
    }

    @Transactional
    public int simulateSync() {
        List<SocialAccount> accounts = accountRepository.findAll().stream()
                .filter(account -> account.getStatus() == AccountStatus.CONNECTED)
                .limit(12)
                .toList();

        List<SocialPost> posts = postRepository.findAll();
        posts.forEach(post -> {
            post.setLikeCount(post.getLikeCount() + random.nextInt(25));
            post.setCommentCount(post.getCommentCount() + random.nextInt(6));
            post.setShareCount(post.getShareCount() + random.nextInt(4));
            post.setViewCount(post.getViewCount() + random.nextInt(100));
        });

        accounts.forEach(account -> {
            account.setLastSyncedAt(Instant.now());
            if (random.nextInt(4) == 0) {
                SocialPost newPost = new SocialPost(
                        account.getMember(),
                        account.getPlatform(),
                        account.getUsername(),
                        "mock-" + account.getId() + "-" + Instant.now().toEpochMilli(),
                        "Campaign update from " + account.getMember().getConstituency(),
                        "IMAGE",
                        "https://picsum.photos/seed/" + account.getId() + "/640/480",
                        "https://example.com/" + account.getPlatform().name().toLowerCase() + "/" + account.getUsername(),
                        LocalDateTime.now().minusMinutes(random.nextInt(180)),
                        50 + random.nextInt(500),
                        5 + random.nextInt(80),
                        random.nextInt(40),
                        500 + random.nextInt(5000)
                );
                postRepository.save(newPost);
                notificationRepository.save(new Notification(
                        "NEW_POST",
                        "New " + account.getPlatform() + " post",
                        account.getMember().getName() + " published a new post.",
                        Instant.now()
                ));
            }
        });

        return accounts.size();
    }
}
