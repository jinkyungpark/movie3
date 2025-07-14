package com.project.movie.dto;

import com.project.movie.constant.MemberRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MemberDTO {

    private Long mid;

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "필수요소입니다.")
    private String email;

    @NotBlank(message = "비밀번호를 확인해 주세요")
    private String password;

    @NotBlank(message = "닉네임을 확인해 주세요")
    private String nickname;

    private MemberRole memberRole;
}
