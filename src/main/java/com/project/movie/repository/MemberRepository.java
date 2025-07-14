package com.project.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.project.movie.dto.PasswordDTO;
import com.project.movie.entity.Member;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    // 프로필 수정
    @Modifying
    @Query("update Member m set nickname=:nickname where m.email = :email")
    void updateNickName(String nickname, String email);

    // 비밀번호 수정 - 이걸 사용하려 했으나, 어차피 한번 찾아서 현재 비밀번호가 일치하는지 확인한 후
    // 수정을 해야 하니 repository 에서 기본적으로 사용하는 메소드 사용
    // @Modifying
    // @Query("update Member m set password=:newPassword where m.email = :email")
    // int updatePassword(String newPassword, String email);
}
