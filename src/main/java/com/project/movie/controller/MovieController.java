package com.project.movie.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.movie.dto.MovieDTO;
import com.project.movie.dto.PageRequestDTO;
import com.project.movie.dto.PageResultDTO;
import com.project.movie.service.MovieServie;
import com.project.movie.service.MovieServieImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {

    private final MovieServie movieService;

    @PostMapping("/modify")
    public String postMovieModify(MovieDTO movieDTO, @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO,
            RedirectAttributes rttr) {
        log.info("영화 수정 {} {}", movieDTO, pageRequestDTO);

        Long mno = movieService.modify(movieDTO);

        rttr.addAttribute("mno", mno);
        rttr.addAttribute("page", pageRequestDTO.getPage());
        rttr.addAttribute("type", pageRequestDTO.getType());
        rttr.addAttribute("keyword", pageRequestDTO.getKeyword());
        return "redirect:/movie/read";
    }

    @PostMapping("/remove")
    public String getMethodName(@RequestParam Long mno, PageRequestDTO pageRequestDTO, RedirectAttributes rttr) {
        log.info("영화 삭제 요청 {},{}", mno, pageRequestDTO);

        movieService.movieRemove(mno);

        rttr.addAttribute("page", pageRequestDTO.getPage());
        rttr.addAttribute("type", pageRequestDTO.getType());
        rttr.addAttribute("keyword", pageRequestDTO.getKeyword());

        return "redirect:/movie/list";
    }

    @GetMapping({ "/read", "/modify" })
    public void getMovie(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Model model, Long mno) {
        log.info("영화 정보 {}", pageRequestDTO);

        MovieDTO movieDTO = movieService.getMovie(mno);
        model.addAttribute("movieDTO", movieDTO);
    }

    @GetMapping("/list")
    public void getMovieList(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Model model) {
        log.info("영화 리스트 요청");

        PageResultDTO<MovieDTO> result = movieService.getList(pageRequestDTO);
        model.addAttribute("result", result);
    }

    // 화면 보여주기
    @GetMapping("/register")
    public void getMovieRegister() {
        log.info("영화 등록");
    }

    // 영화 정보 저장
    @PreAuthorize("hasRole('ADMIN')") // ADMIN 만 등록
    // @PreAuthorize("isAuthenticated()") // 로그인 되었는지만 확인
    @PostMapping("/register")
    public String postMovieRegister(MovieDTO movieDTO, RedirectAttributes rttr) {
        log.info("영화 등록 {}", movieDTO);

        Long mno = movieService.register(movieDTO);

        rttr.addFlashAttribute("msg", mno);
        return "redirect:/movie/list";
    }

}
