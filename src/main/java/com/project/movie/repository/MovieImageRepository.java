package com.project.movie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.project.movie.entity.Movie;
import com.project.movie.entity.MovieImage;
import com.project.movie.repository.total.MovieImageReviewRepository;

public interface MovieImageRepository extends JpaRepository<MovieImage, Long>, MovieImageReviewRepository {

    // Movie 를 기준으로 한 제거 메소드 생성
    @Modifying
    @Query("delete from MovieImage mi where mi.movie=:movie")
    void deleteByMovie(Movie movie);

    // 어제 날짜의 첨부 리스트 가져오기
    @Query(value = "select * from Movie_Image mi where mi.path = to_char(sysdate-1, 'yyyy\\mm\\dd')", nativeQuery = true)
    List<MovieImage> getOldFilImages();
}
