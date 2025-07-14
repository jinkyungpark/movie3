package com.project.movie.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
@Getter
public class AuthMemberDTO extends User {

    private MemberDTO memberDTO;

    public AuthMemberDTO(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    // clubMember.getRoleSet().stream().map(role -> new
    // SimpleGrantedAuthority("ROLE_"+role.name()))

    public AuthMemberDTO(MemberDTO memberDTO) {
        super(memberDTO.getEmail(), memberDTO.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + memberDTO.getMemberRole())));
        this.memberDTO = memberDTO;
    }
}
