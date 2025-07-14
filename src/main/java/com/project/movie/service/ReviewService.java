package com.project.movie.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.project.movie.dto.MovieDTO;
import com.project.movie.dto.MovieImageDTO;
import com.project.movie.dto.ReviewDTO;
import com.project.movie.entity.Member;
import com.project.movie.entity.Movie;
import com.project.movie.entity.MovieImage;
import com.project.movie.entity.Review;
import com.project.movie.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // 영화의 모든 리뷰 가져오기
    public List<ReviewDTO> getListOfMovie(Long mno) {
        Movie movie = Movie.builder().mno(mno).build();
        List<Review> reviews = reviewRepository.findByMovie(movie);

        List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(review -> entieyToReviewDTO(review))
                .collect(Collectors.toList());
        return reviewDTOs;
    }

    // 영화의 특정 리뷰 가져오기
    public ReviewDTO getRowMovie(Long mno) {

        Review review = reviewRepository.findById(mno).get();
        ReviewDTO reviewDTO = entieyToReviewDTO(review);
        return reviewDTO;
    }

    // 영화의 특정 리뷰 등록
    public Long addReview(ReviewDTO reviewDTO) {

        Review review = dtoToEntity(reviewDTO);
        Review newReview = reviewRepository.save(review);
        return newReview.getReviewNo();
    }

    // 특정 영화 리뷰 수정
    public void modifyReview(ReviewDTO reviewDTO) {

        Optional<Review> result = reviewRepository.findById(reviewDTO.getReviewNo());

        result.ifPresent(review -> {
            review.setText(reviewDTO.getText());
            review.setGrade(reviewDTO.getGrade());
            reviewRepository.save(review);
        });
    }

    // 특정 영화 리뷰 삭제
    public void removeReview(Long reviewNo) {
        reviewRepository.deleteById(reviewNo);
    }

    // entity => DTO 변환
    private ReviewDTO entieyToReviewDTO(Review review) {

        // Movie Entity => MovieDTO
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .reviewNo(review.getReviewNo())
                .text(review.getText())
                .createdDate(review.getCreatedDate())
                .lastModifiedDate(review.getLastModifiedDate())
                .mno(review.getMovie().getMno())
                .mid(review.getMember().getMid())
                .nickname(review.getMember().getNickname())
                .email(review.getMember().getEmail())
                .grade(review.getGrade())
                .build();

        return reviewDTO;
    }

    private Review dtoToEntity(ReviewDTO reviewDTO) {

        Review review = Review.builder()
                .reviewNo(reviewDTO.getReviewNo())
                .movie(Movie.builder().mno(reviewDTO.getMno()).build())
                .member(Member.builder().mid(reviewDTO.getMid()).build())
                .text(reviewDTO.getText())
                .grade(reviewDTO.getGrade())
                .build();
        return review;
    }
}
