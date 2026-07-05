package com.congresstracker.socialmonitor.repository;

import com.congresstracker.socialmonitor.domain.Platform;
import com.congresstracker.socialmonitor.domain.SocialPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SocialPostRepository extends JpaRepository<SocialPost, Long> {
    long countByPostedAtAfter(LocalDateTime postedAt);

    long countByPlatform(Platform platform);

    @Query("""
            select p from SocialPost p
            join fetch p.member
            order by p.postedAt desc
            """)
    List<SocialPost> findRecentPosts(Pageable pageable);

    @Query("""
            select p from SocialPost p
            join fetch p.member m
            where (:platform is null or p.platform = :platform)
              and (lower(m.name) like lower(concat('%', :query, '%'))
                or lower(m.district) like lower(concat('%', :query, '%'))
                or lower(m.constituency) like lower(concat('%', :query, '%'))
                or lower(p.username) like lower(concat('%', :query, '%')))
            order by p.postedAt desc
            """)
    List<SocialPost> search(String query, Platform platform, Pageable pageable);

    @Query("""
            select m.id, m.name, m.district, m.constituency, count(p.id), coalesce(sum(p.likeCount + p.commentCount + p.shareCount), 0)
            from SocialPost p
            join p.member m
            group by m.id, m.name, m.district, m.constituency
            order by count(p.id) desc, coalesce(sum(p.likeCount + p.commentCount + p.shareCount), 0) desc
            """)
    List<Object[]> memberRankings(Pageable pageable);
}
