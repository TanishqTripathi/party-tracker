package com.congresstracker.socialmonitor.config;

import com.congresstracker.socialmonitor.domain.AccountStatus;
import com.congresstracker.socialmonitor.domain.Member;
import com.congresstracker.socialmonitor.domain.Notification;
import com.congresstracker.socialmonitor.domain.Platform;
import com.congresstracker.socialmonitor.domain.SocialAccount;
import com.congresstracker.socialmonitor.domain.SocialPost;
import com.congresstracker.socialmonitor.repository.MemberRepository;
import com.congresstracker.socialmonitor.repository.NotificationRepository;
import com.congresstracker.socialmonitor.repository.SocialPostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Configuration
public class DataSeeder {

    private static final List<String> DISTRICTS = List.of(
            "New Delhi", "Mumbai", "Pune", "Jaipur", "Lucknow",
            "Bhopal", "Ahmedabad", "Bengaluru", "Hyderabad", "Chennai"
    );

    private static final List<String> ROLES = List.of(
            "District President", "Youth Leader", "Ward Coordinator", "Media Convenor", "Campaign Volunteer"
    );

    @Bean
    CommandLineRunner seed(MemberRepository memberRepository,
                           SocialPostRepository postRepository,
                           NotificationRepository notificationRepository) {
        return args -> {
            if (memberRepository.count() > 0) {
                return;
            }

            Random random = new Random(7);
            for (int i = 1; i <= 100; i++) {
                String district = DISTRICTS.get((i - 1) % DISTRICTS.size());
                String constituency = district + " Constituency " + (((i - 1) % 5) + 1);
                Member member = new Member(
                        "Party Member " + i,
                        ROLES.get((i - 1) % ROLES.size()),
                        district,
                        constituency,
                        "+91-90000-" + String.format("%05d", i),
                        "member" + i + "@party.example"
                );

                AccountStatus instagramStatus = i % 6 == 0 ? AccountStatus.PENDING : AccountStatus.CONNECTED;
                AccountStatus xStatus = i % 5 == 0 ? AccountStatus.PENDING : AccountStatus.CONNECTED;
                SocialAccount instagram = new SocialAccount(member, Platform.INSTAGRAM, "party_member_" + i, instagramStatus);
                SocialAccount x = new SocialAccount(member, Platform.X, "member" + i + "_official", xStatus);
                instagram.setLastSyncedAt(Instant.now().minusSeconds(random.nextInt(20_000)));
                x.setLastSyncedAt(Instant.now().minusSeconds(random.nextInt(20_000)));
                member.getSocialAccounts().add(instagram);
                member.getSocialAccounts().add(x);

                memberRepository.save(member);

                int postCount = 1 + random.nextInt(5);
                for (int post = 1; post <= postCount; post++) {
                    Platform platform = post % 2 == 0 ? Platform.X : Platform.INSTAGRAM;
                    String username = platform == Platform.INSTAGRAM ? instagram.getUsername() : x.getUsername();
                    LocalDateTime postedAt = LocalDateTime.now()
                            .minusDays(random.nextInt(28))
                            .minusHours(random.nextInt(24));
                    postRepository.save(new SocialPost(
                            member,
                            platform,
                            username,
                            platform.name().toLowerCase() + "-" + i + "-" + post,
                            sampleCaption(i, constituency, post),
                            platform == Platform.INSTAGRAM ? "IMAGE" : "TEXT",
                            "https://picsum.photos/seed/member-" + i + "-" + post + "/640/480",
                            "https://example.com/" + platform.name().toLowerCase() + "/" + username + "/" + post,
                            postedAt,
                            40 + random.nextInt(2_500),
                            random.nextInt(240),
                            random.nextInt(140),
                            300 + random.nextInt(25_000)
                    ));
                }
            }

            notificationRepository.save(new Notification(
                    "SYSTEM",
                    "MVP data loaded",
                    "Seeded 100 members with Instagram and X accounts.",
                    Instant.now()
            ));
            notificationRepository.save(new Notification(
                    "SYNC",
                    "Mock scheduler active",
                    "Engagement metrics will refresh every 15 minutes while the backend runs.",
                    Instant.now()
            ));
        };
    }

    private String sampleCaption(int memberNumber, String constituency, int postNumber) {
        return "Update " + postNumber + " from Member " + memberNumber
                + " covering local outreach in " + constituency + ".";
    }
}
