package com.congresstracker.socialmonitor.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"platform", "platformPostId"}))
public class SocialPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    private Platform platform;

    private String username;
    private String platformPostId;
    private String caption;
    private String mediaType;
    private String mediaUrl;
    private String permalink;
    private LocalDateTime postedAt;
    private long likeCount;
    private long commentCount;
    private long shareCount;
    private long viewCount;

    protected SocialPost() {
    }

    public SocialPost(Member member, Platform platform, String username, String platformPostId, String caption,
                      String mediaType, String mediaUrl, String permalink, LocalDateTime postedAt,
                      long likeCount, long commentCount, long shareCount, long viewCount) {
        this.member = member;
        this.platform = platform;
        this.username = username;
        this.platformPostId = platformPostId;
        this.caption = caption;
        this.mediaType = mediaType;
        this.mediaUrl = mediaUrl;
        this.permalink = permalink;
        this.postedAt = postedAt;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.shareCount = shareCount;
        this.viewCount = viewCount;
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

    public String getPlatformPostId() {
        return platformPostId;
    }

    public String getCaption() {
        return caption;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getPermalink() {
        return permalink;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public long getShareCount() {
        return shareCount;
    }

    public void setShareCount(long shareCount) {
        this.shareCount = shareCount;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }
}
