package com.congresstracker.socialmonitor.web;

import com.congresstracker.socialmonitor.domain.Platform;
import com.congresstracker.socialmonitor.dto.PostDtos.PostResponse;
import com.congresstracker.socialmonitor.repository.SocialPostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final SocialPostRepository postRepository;

    public PostController(SocialPostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping
    public List<PostResponse> list(@RequestParam(defaultValue = "") String q,
                                   @RequestParam(required = false) Platform platform,
                                   @RequestParam(defaultValue = "100") int limit) {
        int safeLimit = Math.max(1, Math.min(limit, 500));
        return postRepository.search(q, platform, PageRequest.of(0, safeLimit)).stream()
                .map(PostResponse::from)
                .toList();
    }
}
