package com.congresstracker.socialmonitor.web;

import com.congresstracker.socialmonitor.domain.Platform;
import com.congresstracker.socialmonitor.dto.MemberDtos.MemberRequest;
import com.congresstracker.socialmonitor.dto.MemberDtos.MemberResponse;
import com.congresstracker.socialmonitor.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public List<MemberResponse> list() {
        return memberService.findAll().stream().map(MemberResponse::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse create(@Valid @RequestBody MemberRequest request) {
        return MemberResponse.from(memberService.create(request));
    }

    @PostMapping("/{memberId}/mock-connect/{platform}")
    public MemberResponse mockConnect(@PathVariable Long memberId, @PathVariable Platform platform) {
        return MemberResponse.from(memberService.mockConnect(memberId, platform));
    }
}
