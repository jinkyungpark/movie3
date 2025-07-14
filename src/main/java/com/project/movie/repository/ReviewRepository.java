package com.project.movie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;

import com.project.movie.entity.Member;
import com.project.movie.entity.Movie;
import com.project.movie.entity.Review;

public interface ReviewRepository extends JpaRepository<Review,Long>{  
    
    @Modifying
    @Query("delete from Review r where r.movie=:movie")
    void deleteByMovie(Movie movie);
    
    // 기본으로 제공되는 findById() 의 경우 Review 아이디를 기준으로 찾는 형태
    // 이기 때문에 review 기준임
    
    // 특정 영화의 모든 리뷰와 닉네임 가져오기

    // 영화 찾기
    @EntityGraph(attributePaths = {"member"}, type = EntityGraphType.FETCH)
    List<Review> findByMovie(Movie movie);  



    // 회원 삭제 시 리뷰 제거
    // 리뷰부터 먼저 제거 해야 함
    @Modifying
    @Query("delete from Review r where r.member=:member")
    void deleteByMember(Member member);
} 
