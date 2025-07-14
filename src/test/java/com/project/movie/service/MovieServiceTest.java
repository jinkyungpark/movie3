package com.project.movie.service;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.project.movie.dto.MovieDTO;
import com.project.movie.dto.PageRequestDTO;
import com.project.movie.dto.PageResultDTO;

@SpringBootTest
public class MovieServiceTest {

    @Autowired
    private MovieServie movieServie;

    // @Test
    // public void testGetList() {

    // PageRequestDTO pageRequestDTO =
    // PageRequestDTO.builder().page(1).size(10).build();

    // PageResultDTO<MovieDTO, Object[]> result =
    // movieServie.getList(pageRequestDTO);

    // for (MovieDTO mDto : result.getDtoList()) {
    // System.out.println(mDto);
    // }
    // }

    // @Test
    // public void testGetList2() {

    // PageRequestDTO pageRequestDTO =
    // PageRequestDTO.builder().page(1).size(10).type("").keyword("").build();

    // PageResultDTO<MovieDTO, Object[]> result =
    // movieServie.getList(pageRequestDTO);

    // for (MovieDTO mDto : result.getDtoList()) {
    // System.out.println(mDto);
    // }
    // }

    // @Test
    // public void testGetRow() {

    // MovieDTO mDto = movieServie.getMovie(263L);
    // System.out.println(mDto);
    // }
}
