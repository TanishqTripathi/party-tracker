package com.congresstracker.socialmonitor.dto;

import com.congresstracker.socialmonitor.domain.Platform;
import com.congresstracker.socialmonitor.domain.SocialPost;

import java.time.LocalDateTime;

public final class PostDtos {

    private PostDtos() {
    }

    public record PostResponse(
            Long id,
            Long memberId,
            String memberName,
            String district,
            String constituency,
            Platform platform,
            String username,
            String platformPostId,
            String caption,
            String mediaType,
            String mediaUrl,
            String permalink,
            LocalDateTime postedAt,
            long likeCount,
            long commentCount,
            long shareCount,
            long viewCount,
            long engagement
    ) {
        public static PostResponse from(SocialPost post) {
            return new PostResponse(
                    post.getId(),
                    post.getMember().getId(),
                    post.getMember().getName(),
                    post.getMember().getDistrict(),
                    post.getMember().getConstituency(),
                    post.getPlatform(),
                    post.getUsername(),
                    post.getPlatformPostId(),
                    post.getCaption(),
                    post.getMediaType(),
                    post.getMediaUrl(),
                    post.getPermalink(),
                    post.getPostedAt(),
                    post.getLikeCount(),
                    post.getCommentCount(),
                    post.getShareCount(),
                    post.getViewCount(),
                    post.getLikeCount() + post.getCommentCount() + post.getShareCount()
            );
        }
    }
}
