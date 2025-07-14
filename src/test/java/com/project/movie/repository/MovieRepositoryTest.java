package com.project.movie.repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.project.movie.dto.PageRequestDTO;
import com.project.movie.entity.Movie;
import com.project.movie.entity.MovieImage;
import com.querydsl.core.Tuple;

@SpringBootTest
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieImageRepository movieImageRepository;

    // @Test
    // public void insertMovie() {
    // IntStream.rangeClosed(1, 100).forEach(i -> {
    // Movie movie = Movie.builder()
    // .title("Movie " + i)
    // .build();

    // movieRepository.save(movie);

    // // 임의의 이미지
    // int count = (int) (Math.random() * 5) + 1;

    // for (int j = 0; j < count; j++) {
    // MovieImage movieImage = MovieImage.builder()
    // .uuid(UUID.randomUUID().toString())
    // .movie(movie)
    // .imgName("test" + j + ".jpg")
    // .build();
    // movieImageRepository.save(movieImage);
    // }
    // });
    // }

    // @Test
    // public void testListPage() {

    // PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC,
    // "mno"));

    // Page<Object[]> result = movieRepository.getListPage(pageRequest);

    // for (Object[] objects : result) {
    // System.out.println(Arrays.toString(objects));
    // }
    // }

    // @Test
    // public void testListPage2() {
    // // native query

    // PageRequest pageRequest = PageRequest.of(0, 10);

    // Page<Object[]> result = movieRepository.getListPage2(pageRequest);

    // for (Object[] objects : result) {
    // System.out.println(Arrays.toString(objects));
    // }
    // }

    // @Test
    // public void testGetMovieWithAll() {

    // List<Object[]> result = movieRepository.getMovieWithAll(103L);

    // for (Object[] objects : result) {
    // System.out.println(Arrays.toString(objects));
    // }
    // }

    // @Test
    // public void testGetMovieRow() {

    // List<Object[]> result = movieImageRepository.getMovieRow(100L);
    // for (Object[] objects : result) {

    // System.out.println(Arrays.toString(objects));

    // }
    // }

    // @Test
    // public void testGetTotalList() {

    // // 지금은 Sort.by("mno").descending() sort 가 들어가 있음
    // // Pageable pageable = PageRequest.of(0, 10);

    // // Page<Object[]> result = movieImageRepository.getTotalList(pageable);

    // // for (Object[] objects : result) {
    // // System.out.println(Arrays.toString(objects));
    // // }

    // // 검색 기능 들어온 후
    // PageRequestDTO requestDTO = PageRequestDTO.builder()
    // .keyword("Movie")
    // .type("title")
    // .page(1)
    // .size(10)
    // .build();

    // Page<Object[]> result =
    // movieImageRepository.getTotalList2(requestDTO.getType(),
    // requestDTO.getKeyword(),
    // requestDTO.getPageable(Sort.by("mno").descending()));

    // for (Object[] objects : result) {
    // System.out.println(Arrays.toString(objects));
    // }
    // }

    // @Test
    // public void testGetOldFiles() {

    // List<MovieImage> list = movieImageRepository.getOldFilImages();
    // for (MovieImage movieImage : list) {
    // System.out.println(movieImage);
    // }
    // }
}
