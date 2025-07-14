package com.project.movie.controller;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@Controller
public class HomeController {

    @GetMapping("/")
    public String getMethodName() {
        return "redirect:/movie/list";
    }

    @ResponseBody
    @GetMapping("/auth")
    public Authentication getAuthenticaionInfo() {
        log.info("auth");

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        return authentication;
    }

    @GetMapping("/access-denied")
    public void getAccessDenied() {

    }

    @GetMapping("/error")
    public String pageNotFound() {
        // 이 경로는 무조건 지켜야 함
        return "except/url404";
    }

}
