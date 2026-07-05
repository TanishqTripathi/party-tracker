package com.congresstracker.socialmonitor.repository;

import com.congresstracker.socialmonitor.domain.AccountStatus;
import com.congresstracker.socialmonitor.domain.Platform;
import com.congresstracker.socialmonitor.domain.SocialAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    long countByStatus(AccountStatus status);

    Optional<SocialAccount> findByMemberIdAndPlatform(Long memberId, Platform platform);
}
