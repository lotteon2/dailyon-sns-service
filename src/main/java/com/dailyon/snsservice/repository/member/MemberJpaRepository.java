package com.dailyon.snsservice.repository.member;

import com.dailyon.snsservice.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {}
