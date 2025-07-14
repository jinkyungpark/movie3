package com.project.movie.service;

import org.springframework.transaction.annotation.Transactional;

import com.project.movie.dto.MemberDTO;
import com.project.movie.dto.PasswordDTO;
import com.project.movie.entity.Member;

public interface MemberService {

    String register(MemberDTO insertDto) throws IllegalStateException;

    // 닉네임 수정
    void nickNameUpdate(MemberDTO upDto);

    // 비밀번호 수정
    void passwordUpdate(PasswordDTO passwordDTO);

    void leave(MemberDTO leavMemberDTO);

    // Entity --> DTO 로 변환
    public default MemberDTO entityToDto(Member member) {

        MemberDTO dto = MemberDTO.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .memberRole(member.getRole())
                .build();
        return dto;
    }

    // DTO --> Entity 로 변환
    public default Member dtoToEntity(MemberDTO dto) {

        Member entity = Member.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .password(dto.getPassword())
                .role(dto.getMemberRole())
                .build();
        return entity;
    }
}