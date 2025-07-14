package com.project.movie.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.movie.dto.ReviewDTO;
import com.project.movie.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // 영화의 모든 리뷰 가져오기
    @GetMapping("/{mno}/all")
    public ResponseEntity<List<ReviewDTO>> getList(@PathVariable Long mno) {
        log.info("review total List " + mno);
        return new ResponseEntity<>(reviewService.getListOfMovie(mno), HttpStatus.OK);
    }

    // 수정할 특정 영화 가져오기(get 방식은 클라이언트 단에서 body를 가질 수 없음)
    @GetMapping("/{mno}/{reviewNo}")
    public ResponseEntity<ReviewDTO> getReply(@PathVariable Long reviewNo) {
        log.info("review row " + reviewNo);
        return new ResponseEntity<>(reviewService.getRowMovie(reviewNo), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{mno}")
    public ResponseEntity<Long> reviewInsert(@RequestBody ReviewDTO reviewDTO) {
        log.info("review insert " + reviewDTO);
        return new ResponseEntity<>(reviewService.addReview(reviewDTO), HttpStatus.OK);
    }

    @PreAuthorize("authentication.name == #email")
    @DeleteMapping("/{mno}/{reviewNo}")
    public ResponseEntity<Long> reviewDelete(@PathVariable Long reviewNo, String email) {
        log.info("review delete {}-{} ", reviewNo, email);
        reviewService.removeReview(reviewNo);
        return new ResponseEntity<>(reviewNo, HttpStatus.OK);
    }

    @PreAuthorize("authentication.name == #reviewDTO.email")
    @PutMapping("{mno}/{replyNo}")
    public ResponseEntity<Long> reviewUpdate(@PathVariable Long replyNo, @RequestBody ReviewDTO reviewDTO) {
        log.info("review update {}, {}", replyNo, reviewDTO);
        reviewService.modifyReview(reviewDTO);
        return new ResponseEntity<>(replyNo, HttpStatus.OK);
    }
}
