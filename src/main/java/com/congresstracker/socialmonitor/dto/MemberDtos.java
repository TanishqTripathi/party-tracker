package com.congresstracker.socialmonitor.dto;

import com.congresstracker.socialmonitor.domain.AccountStatus;
import com.congresstracker.socialmonitor.domain.Member;
import com.congresstracker.socialmonitor.domain.Platform;
import com.congresstracker.socialmonitor.domain.SocialAccount;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.List;

public final class MemberDtos {

    private MemberDtos() {
    }

    public record MemberRequest(
            @NotBlank String name,
            String partyRole,
            String district,
            String constituency,
            String phone,
            @Email String email,
            String instagramUsername,
            String xUsername
    ) {
    }

    public record AccountResponse(
            Long id,
            Platform platform,
            String username,
            AccountStatus status,
            Instant lastSyncedAt
    ) {
        public static AccountResponse from(SocialAccount account) {
            return new AccountResponse(
                    account.getId(),
                    account.getPlatform(),
                    account.getUsername(),
                    account.getStatus(),
                    account.getLastSyncedAt()
            );
        }
    }

    public record MemberResponse(
            Long id,
            String name,
            String partyRole,
            String district,
            String constituency,
            String phone,
            String email,
            List<AccountResponse> accounts
    ) {
        public static MemberResponse from(Member member) {
            return new MemberResponse(
                    member.getId(),
                    member.getName(),
                    member.getPartyRole(),
                    member.getDistrict(),
                    member.getConstituency(),
                    member.getPhone(),
                    member.getEmail(),
                    member.getSocialAccounts().stream().map(AccountResponse::from).toList()
            );
        }
    }
}
