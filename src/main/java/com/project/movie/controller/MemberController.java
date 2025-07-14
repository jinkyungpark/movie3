package com.project.movie.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.movie.dto.AuthMemberDTO;
import com.project.movie.dto.MemberDTO;
import com.project.movie.dto.PasswordDTO;
import com.project.movie.service.MemberService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService service;

    @GetMapping("/login")
    public void getLogin() {
        log.info("로그인 요청");
    }

    @GetMapping("/register")
    public void getRegister(MemberDTO memberDTO) {
        log.info("회원가입 폼 요청");
    }

    @PostMapping("/register")
    public String postRegister(@Valid MemberDTO memberDTO, BindingResult result, RedirectAttributes rttr) {
        log.info("회원가입 요청 {}", memberDTO);

        if (result.hasErrors()) {
            return "member/register";
        }

        try {
            service.register(memberDTO);
        } catch (Exception e) {
            log.info(e.getMessage());
            // rttr.addAttribute("error", "error");
            rttr.addFlashAttribute("error", e.getMessage());
            return "redirect:/member/register";
        }
        return "redirect:/member/login";

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public void getProfile() {
        log.info("프로필 폼 요청");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit")
    public String getEdit() {
        log.info("프로필 수정");
        return "member/edit-profile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/edit/password")
    public String postPassword(PasswordDTO passwordDTO, HttpSession session, RedirectAttributes rttr) {

        try {
            service.passwordUpdate(passwordDTO);
        } catch (Exception e) {
            log.info(e.getMessage());
            // rttr.addAttribute("error", "error");
            rttr.addFlashAttribute("error", e.getMessage());
            return "redirect:/member/edit";
        }
        // 세션해제
        session.invalidate();
        return "redirect:/member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/edit/nickname")
    public String postNickName(MemberDTO memberDTO) {

        service.nickNameUpdate(memberDTO);

        // 닉네임 변경 후 세션에 있는 닉네임 값 업데이트
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        AuthMemberDTO authMemberDTO = (AuthMemberDTO) authentication.getPrincipal();
        authMemberDTO.getMemberDTO().setNickname(memberDTO.getNickname());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/member/profile";
    }

    // 회원탈퇴
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/leave")
    public void getLeave(MemberDTO memberDTO) {
        log.info("회원탈퇴 폼 요청");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/leave")
    public String posLeave(@Valid MemberDTO memberDTO, BindingResult result, HttpSession session) {
        log.info("회원 탈퇴 요청 {}", memberDTO);

        if (result.hasErrors()) {
            return "member/leave";
        }

        service.leave(memberDTO);
        session.invalidate();

        return "redirect:/";
    }

}
