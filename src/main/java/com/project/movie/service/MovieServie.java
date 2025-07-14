package com.project.movie.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import com.project.movie.dto.MovieDTO;
import com.project.movie.dto.MovieImageDTO;
import com.project.movie.dto.PageRequestDTO;
import com.project.movie.dto.PageResultDTO;
import com.project.movie.entity.Movie;
import com.project.movie.entity.MovieImage;

public interface MovieServie {

    // 영화 수정
    Long modify(MovieDTO movieDTO);

    // 영화 삭제
    void movieRemove(Long mno);

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
    MovieDTO getMovie(Long mno);

    // 영화 등록
    Long register(MovieDTO movieDTO);

    public PageResultDTO<MovieDTO> getList(PageRequestDTO pageRequestDTO);

    // entity => DTO 변환
    public default MovieDTO entiesToMovieDTO(Movie movie, List<MovieImage> movieImages, Long reviewCnt, Double avg) {

        // Movie Entity => MovieDTO
        MovieDTO movieDTO = MovieDTO.builder()
                .mno(movie.getMno())
                .title(movie.getTitle())
                .createdDate(movie.getCreatedDate())
                .lastModifiedDate(movie.getLastModifiedDate())
                .build();

        // 해당 영화의 이미지리스트 처리
        List<MovieImageDTO> movieImageDTOs = movieImages.stream().map(movieImage -> {
            return MovieImageDTO.builder()
                    .inum(movieImage.getInum())
                    .uuid(movieImage.getUuid())
                    .imgName(movieImage.getImgName())
                    .path(movieImage.getPath())
                    .build();
        }).collect(Collectors.toList());

        movieDTO.setMovieImages(movieImageDTOs);
        movieDTO.setAvg(avg != null ? avg : 0.0D);
        movieDTO.setReviewCnt(reviewCnt);

        return movieDTO;
    }

    // public default MovieDTO entiesToMovieDTO2(Movie movie, MovieImage movieImage,
    // Long reviewCnt, Double avg) {

    // // Movie Entity => MovieDTO
    // MovieDTO movieDTO = MovieDTO.builder()
    // .mno(movie.getMno())
    // .title(movie.getTitle())
    // .createdDate(movie.getCreatedDate())
    // .lastModifiedDate(movie.getLastModifiedDate())
    // .build();

    // // 해당 영화의 이미지리스트 처리
    // MovieImageDTO movieImageDTOs = MovieImageDTO.builder()
    // .inum(movieImage.getInum())
    // .uuid(movieImage.getUuid())
    // .imgName(movieImage.getImgName())
    // .path(movieImage.getPath())
    // .build();

    // movieDTO.getMovieImages().add(movieImageDTOs);
    // movieDTO.setAvg(avg != null ? avg : 0.0D);
    // movieDTO.setReviewCnt(reviewCnt);

    // return movieDTO;
    // }

    public default Map<String, Object> dtoToEntity(MovieDTO movieDTO) {
        Map<String, Object> entityMap = new HashMap<>();

        Movie movie = Movie.builder()
                .mno(movieDTO.getMno())
                .title(movieDTO.getTitle())
                .build();

        entityMap.put("movie", movie);

        List<MovieImageDTO> imageDTOs = movieDTO.getMovieImages();
        if (imageDTOs != null && imageDTOs.size() > 0) {
            List<MovieImage> movieImages = imageDTOs.stream().map(movieImageDTO -> {
                MovieImage movieImage = MovieImage.builder()
                        .path(movieImageDTO.getPath())
                        .imgName(movieImageDTO.getImgName())
                        .uuid(movieImageDTO.getUuid())
                        .movie(movie)
                        .build();
                return movieImage;
            }).collect(Collectors.toList());

            entityMap.put("imgList", movieImages);
        }
        return entityMap;
    }

}