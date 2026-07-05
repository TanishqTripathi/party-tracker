package com.congresstracker.socialmonitor.repository;

import com.congresstracker.socialmonitor.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Override
    @EntityGraph(attributePaths = "socialAccounts")
    List<Member> findAll();

    @EntityGraph(attributePaths = "socialAccounts")
    Optional<Member> findWithSocialAccountsById(Long id);
}
