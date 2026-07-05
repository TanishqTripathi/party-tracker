package com.congresstracker.socialmonitor.service;

import com.congresstracker.socialmonitor.domain.AccountStatus;
import com.congresstracker.socialmonitor.domain.Member;
import com.congresstracker.socialmonitor.domain.Platform;
import com.congresstracker.socialmonitor.domain.SocialAccount;
import com.congresstracker.socialmonitor.dto.MemberDtos.MemberRequest;
import com.congresstracker.socialmonitor.repository.MemberRepository;
import com.congresstracker.socialmonitor.repository.SocialAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final SocialAccountRepository socialAccountRepository;

    public MemberService(MemberRepository memberRepository, SocialAccountRepository socialAccountRepository) {
        this.memberRepository = memberRepository;
        this.socialAccountRepository = socialAccountRepository;
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Transactional
    public Member create(MemberRequest request) {
        Member member = new Member(
                request.name(),
                request.partyRole(),
                request.district(),
                request.constituency(),
                request.phone(),
                request.email()
        );

        if (hasText(request.instagramUsername())) {
            member.getSocialAccounts().add(new SocialAccount(
                    member,
                    Platform.INSTAGRAM,
                    request.instagramUsername(),
                    AccountStatus.PENDING
            ));
        }
        if (hasText(request.xUsername())) {
            member.getSocialAccounts().add(new SocialAccount(
                    member,
                    Platform.X,
                    request.xUsername(),
                    AccountStatus.PENDING
            ));
        }

        return memberRepository.save(member);
    }

    @Transactional
    public Member mockConnect(Long memberId, Platform platform) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));

        SocialAccount account = socialAccountRepository.findByMemberIdAndPlatform(memberId, platform)
                .orElseGet(() -> new SocialAccount(
                        member,
                        platform,
                        defaultUsername(member, platform),
                        AccountStatus.PENDING
                ));

        account.setStatus(AccountStatus.CONNECTED);
        account.setLastSyncedAt(Instant.now());
        socialAccountRepository.save(account);

        return memberRepository.findWithSocialAccountsById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String defaultUsername(Member member, Platform platform) {
        String slug = member.getName().toLowerCase().replaceAll("[^a-z0-9]+", "_").replaceAll("^_|_$", "");
        if (slug.isBlank()) {
            slug = "member_" + member.getId();
        }
        return platform == Platform.INSTAGRAM ? slug + "_ig" : slug + "_x";
    }
}
