package com.project.movie.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.movie.dto.MovieDTO;
import com.project.movie.dto.MovieImageDTO;
import com.project.movie.dto.PageRequestDTO;
import com.project.movie.dto.PageResultDTO;
import com.project.movie.entity.Movie;
import com.project.movie.entity.MovieImage;
import com.project.movie.repository.MovieImageRepository;
import com.project.movie.repository.MovieRepository;
import com.project.movie.repository.ReviewRepository;
import com.querydsl.core.Tuple;

import lombok.RequiredArgsConstructor;

// 영화 수정
@Service
@RequiredArgsConstructor
public class MovieServieImpl implements MovieServie {

    private final MovieRepository movieRepository;
    private final MovieImageRepository movieImageRepository;
    private final ReviewRepository reviewRepository;

    // 영화 수정
    @Override
    @Transactional
    public Long modify(MovieDTO movieDTO) {

        Map<String, Object> entityMap = dtoToEntity(movieDTO);
        Movie movie = (Movie) entityMap.get("movie");

        // 수정하기 위해 받아온 이미지
        List<MovieImage> movieImages = (List<MovieImage>) entityMap.get("imgList");

        // 기존 이미지는 제거
        movieImageRepository.deleteByMovie(movie);

        // 이미지 다시 추가
        movieImages.forEach(movieImage -> {
            movieImageRepository.save(movieImage);
        });
        return movie.getMno();
    }

    // 영화 삭제
    @Override
    @Transactional
    public void movieRemove(Long mno) {

        Movie movie = Movie.builder().mno(mno).build();

        // 이미지 제거
        movieImageRepository.deleteByMovie(movie);

        // 리뷰 제거
        reviewRepository.deleteByMovie(movie);

        // 영화 제거
        movieRepository.delete(movie);
    }

    /**
     * 100 번 영화에 대해서 조회하면 아래와 같이 나옴
     * [2024-01-14 21:57:02.436174, 294, 2024-01-14 21:57:02.436174, 100, test0.jpg,
     * null, ccc2a14c-30cf-4094-89d9-6ceb6deef0cb, 2, 1.0]
     * [2024-01-14 21:57:02.437174, 295, 2024-01-14 21:57:02.437174, 100, test1.jpg,
     * null, 57255967-d2fc-470f-aae5-d33d218ef7f9, 2, 1.0]
     * [2024-01-14 21:57:02.438174, 296, 2024-01-14 21:57:02.438174, 100, test2.jpg,
     * null, 3e919574-ca4e-463d-864f-98d0ba650b15, 2, 1.0]
     * [2024-01-14 21:57:02.439174, 297, 2024-01-14 21:57:02.439174, 100, test3.jpg,
     * null, f770d45c-11d9-486a-9be9-287233907560, 2, 1.0]
     * [2024-01-14 21:57:02.440174, 298, 2024-01-14 21:57:02.440174, 100, test4.jpg,
     * null, bb9bfa44-45bf-43a6-b0c8-ea27dbc52734, 2, 1.0]
     *
     * 영화 상세 조회
     */
    @Override
    public MovieDTO getMovie(Long mno) {

        List<Object[]> result = movieImageRepository.getMovieRow(mno);

        // Movie 엔티티는 가장 앞에 존재하며 모든 Row 가 동일한 값
        Movie movie = (Movie) result.get(0)[0];
        // 영화의 image 개수만큼 MovieImage 객체 필요
        List<MovieImage> movieImages = result.stream().map(en -> (MovieImage) en[1]).collect(Collectors.toList());

        // List<MovieImage> movieImages = new ArrayList<>();
        // result.forEach(arr -> {
        // MovieImage movieImage = (MovieImage)arr[1];
        // movieImages.add(movieImage);
        // });

        Long reviewCnt = (Long) result.get(0)[2]; // 리뷰개수-모든 row 동일
        Double avg = (Double) result.get(0)[3]; // 평균평점-모든 row 동일

        return entiesToMovieDTO(movie, movieImages, reviewCnt, avg);
    }

    // 영화 등록
    @Override
    @Transactional
    public Long register(MovieDTO movieDTO) {

        Map<String, Object> entityMap = dtoToEntity(movieDTO);
        Movie movie = (Movie) entityMap.get("movie");
        List<MovieImage> movieImages = (List<MovieImage>) entityMap.get("imgList");

        movieRepository.save(movie);
        movieImages.forEach(movieImage -> {
            movieImageRepository.save(movieImage);
        });
        return movie.getMno();
    }

    @Override
    public PageResultDTO<MovieDTO> getList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
                Sort.by("mno").descending());

        Page<Object[]> result = movieImageRepository.getTotalList(pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(), pageable);
        // [
        // [Movie(mno=100, title=Movie 100), MovieImage(inum=300,
        // uuid=674034e0-5ed1-4141-9681-9a0e5c6464f6, imgName=test0.jpg, path=null,
        // ord=0), 2, 2.0]
        // ]
        //
        Function<Object[], MovieDTO> function = (en -> entiesToMovieDTO((Movie) en[0],
                (List<MovieImage>) Arrays.asList((MovieImage) en[1]), (Long) en[2],
                (Double) en[3]));

        List<MovieDTO> dtoList = result.stream().map(function).collect(Collectors.toList());
        Long totalCount = result.getTotalElements();

        PageResultDTO<MovieDTO> pageResultDTO = PageResultDTO.<MovieDTO>withAll()
                .dtoList(dtoList)
                .totalCount(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();
        return pageResultDTO;
    }

    /*
     * (List<MovieImage>)Arrays.asList((MovieImage)en[1]
     * 전체 목록 시에는 MovieImage 하나만 넘어오나 Movie 개별 조회 일 때는 포스터가 여러장일 수 있음
     * 리스트로 만들어서 처리해줘야 메소드를 2개 안 만들 수 있음
     */

    // @Override
    // public PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO) {

    // Page<Object[]> result =
    // movieImageRepository.getTotalList2(requestDTO.getType(),
    // requestDTO.getKeyword(),
    // requestDTO.getPageable(Sort.by("mno").descending()));

    // List<Object[]> contents = result.getContent();
    // List<MovieDTO> movieDTOs = new ArrayList<>();

    // // Movie 부터 담고
    // contents.forEach(en -> {
    // Movie movie = (Movie) en[0];
    // MovieDTO dto2 = MovieDTO.builder().mno(movie.getMno()).build();

    // if (!movieDTOs.contains(dto2)) {
    // movieDTOs.add(dto2);
    // }
    // });

    // contents.forEach(en -> {
    // MovieImage movieImage = (MovieImage) en[1];

    // movieDTOs.forEach(dto -> {
    // if (dto.getMno() == movieImage.getMovie().getMno()) {
    // dto.getMovieImages().add(MovieImageDTO.builder().inum(movieImage.getInum()).build());
    // }
    // });
    // });

    // return new PageResultDTO<>(result, movieDTOs);
    // }

    // 목록 - max(mi.inum) 일 때
    // @Override
    // public PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO
    // pageRequestDTO) {

    // Pageable pageable = pageRequestDTO.getPageable(Sort.by("mno").descending());
    // Page<Object[]> result = movieRepository.getListPage(pageable); //
    // // [Movie(mno=94, title=Movie 94), 3.5, 2, 279]

    // Function<Object[], MovieDTO> function = (arr -> entiesToMovieDTO(
    // (Movie) arr[0],
    // (Double) arr[1],
    // (Long) arr[2],
    // (Long) arr[3]));

    // return new PageResultDTO<>(result, function);
    // }
}
