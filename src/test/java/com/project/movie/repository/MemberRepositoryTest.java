package com.project.movie.repository;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.movie.constant.MemberRole;
import com.project.movie.entity.Member;
import com.project.movie.entity.Movie;

@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // @Test
    // public void insertMember() {

    // IntStream.rangeClosed(1, 100).forEach(i -> {
    // Member member = Member.builder()
    // .email("r" + i + "@naver.com")
    // .password(passwordEncoder.encode("1111"))
    // .role(MemberRole.MEMBER)
    // .nickname("reviewer" + i)
    // .build();

    // memberRepository.save(member);
    // });

    // Member member = Member.builder()
    // .email("test33@naver.com")
    // .password(passwordEncoder.encode("1111"))
    // .role(MemberRole.MEMBER)
    // .nickname("flower")
    // .build();

    // memberRepository.save(member);
    // }
}
