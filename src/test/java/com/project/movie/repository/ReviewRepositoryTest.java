package com.project.movie.repository;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import com.project.movie.entity.Member;
import com.project.movie.entity.Movie;
import com.project.movie.entity.Review;

import jakarta.transaction.Transactional;

@SpringBootTest
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MemberRepository memberRepository;

    // @Test
    // public void insertReview() {

    // IntStream.rangeClosed(121, 220).forEach(i -> {

    // // 영화번호 임의 추출
    // Long mno = (long) (Math.random() * 100) + 121;

    // // 리뷰 사용자 임의 추출
    // Long mid = (long) (Math.random() * 100) + 200;
    // Member member = Member.builder().mid(mid).build();

    // Review review = Review.builder()
    // .movie(Movie.builder().mno(mno).build())
    // .member(member)
    // .grade((int) (Math.random() * 5) + 1)
    // .text("꿈이란 무엇인가에 대한 이야기")
    // .build();

    // reviewRepository.save(review);
    // });
    // }

    // // @Transactional // Lazy 방식 => @EntityGraph 사용 시 필요없음
    // @Test
    // public void testFindByMovie() {
    // Movie movie = Movie.builder().mno(1L).build();
    // List<Review> reviews = reviewRepository.findByMovie(movie);

    // reviews.forEach(review -> {
    // // review 의 member 를 통해서 member 의 email 얻어내기
    // System.out.println(review.getReviewNo());
    // System.out.println(review.getText());
    // System.out.println(review.getGrade());
    // System.out.println(review.getMember().getEmail());
    // });
    // }

    // @Commit // 테스트할 때 delete 가 반영 안됨
    // @Transactional
    // @Test
    // public void testDeleteByMember() {

    // Member member = Member.builder().mid(6L).build();

    // reviewRepository.deleteByMember(member);
    // memberRepository.delete(member);
    // }
}
