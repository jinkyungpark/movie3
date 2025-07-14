package com.project.movie.repository.total;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieImageReviewRepository {
    // 페이지 나누기 + 조인된 목록 정보
    Page<Object[]> getTotalList(String type, String keyword, Pageable pageable);

    Page<Object[]> getTotalList2(String type, String keyword, Pageable pageable);

    // 특정 영화 정보 조회(Movie,MovieImage,Review 필요)
    List<Object[]> getMovieRow(Long mno);
}
