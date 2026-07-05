package com.congresstracker.socialmonitor.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

@Entity
public class SocialAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    private Platform platform;

    @NotBlank
    private String username;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    private Instant lastSyncedAt;

    protected SocialAccount() {
    }

    public SocialAccount(Member member, Platform platform, String username, AccountStatus status) {
        this.member = member;
        this.platform = platform;
        this.username = username;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Platform getPlatform() {
        return platform;
    }

    public String getUsername() {
        return username;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Instant getLastSyncedAt() {
        return lastSyncedAt;
    }

    public void setLastSyncedAt(Instant lastSyncedAt) {
        this.lastSyncedAt = lastSyncedAt;
    }
}
