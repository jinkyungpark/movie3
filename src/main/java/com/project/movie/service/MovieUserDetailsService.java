package com.project.movie.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.project.movie.constant.MemberRole;
import com.project.movie.dto.AuthMemberDTO;
import com.project.movie.dto.MemberDTO;
import com.project.movie.dto.PasswordDTO;
import com.project.movie.entity.Member;
import com.project.movie.repository.MemberRepository;
import com.project.movie.repository.ReviewRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class MovieUserDetailsService implements UserDetailsService, MemberService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("UserDetailsService {}", email);

        Optional<Member> result = memberRepository.findByEmail(email);

        if (!result.isPresent())
            throw new UsernameNotFoundException("Check Email");

        Member member = result.get();

        log.info("----------------");
        log.info(member);

        MemberDTO memberDTO = MemberDTO.builder()
                .mid(member.getMid())
                .email(member.getEmail())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .memberRole(member.getRole())
                .build();

        return new AuthMemberDTO(memberDTO);
    }

    @Override
    public String register(MemberDTO insertDto) throws IllegalStateException {

        // 중복 이메일 확인
        validateDuplicateMember(insertDto.getEmail());

        // 비밀번호 암호화
        insertDto.setPassword(passwordEncoder.encode(insertDto.getPassword()));
        // 권한 변경
        insertDto.setMemberRole(MemberRole.ADMIN);

        log.info("삽입 전 {}", insertDto);

        Member member = memberRepository.save(dtoToEntity(insertDto));
        return member.getEmail();
    }

    // 이메일 중복 여부
    private void validateDuplicateMember(String email) {
        Optional<Member> result = memberRepository.findByEmail(email);

        if (result.isPresent()) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Transactional
    @Override
    public void nickNameUpdate(MemberDTO upDto) {
        memberRepository.updateNickName(upDto.getNickname(), upDto.getEmail());
    }

    @Override
    public void passwordUpdate(PasswordDTO passwordDTO) throws IllegalStateException {

        Member member = memberRepository.findByEmail(passwordDTO.getEmail()).get();

        if (!passwordEncoder.matches(passwordDTO.getCurrentPassword(), member.getPassword()))
            throw new IllegalStateException("현재 비밀번호가 다릅니다.");

        // 현재 로그인 정보에 있는 비밀번호가 동일하면
        if (passwordEncoder.matches(passwordDTO.getCurrentPassword(), member.getPassword())) {
            member.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
            memberRepository.save(member);
        }
    }

    @Override
    public void leave(MemberDTO leavMemberDTO) {
        Member member = Member.builder().email(leavMemberDTO.getEmail()).build();

        reviewRepository.deleteByMember(member);
        memberRepository.delete(dtoToEntity(leavMemberDTO));
    }

}
