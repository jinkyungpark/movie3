package com.project.movie.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.project.movie.dto.MovieImageDTO;
import com.project.movie.entity.MovieImage;
import com.project.movie.repository.MovieImageRepository;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class FileCheckTask {

    @Autowired
    private MovieImageRepository movieImageRepository;

    // application.properties 에 있는 값
    @Value("${com.project.movie.upload.path}")
    private String uploadPath;

    // 전일자 폴더의 리스트 추출한 후 비교
    private String getFolderYesterDay() {

        // 어제날짜 추출
        LocalDate yesterday = LocalDate.now().minusDays(1);

        String str = yesterday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return str.replace("-", File.separator); // 2021-06-20 => 2021\06\20
    }

    // 데이터베이스랑 일치하지 않는 파일이 존재한다면 삭제

    @Scheduled(cron = "* * 2 * * *")
    public void checkFiles() {
        log.info("file check task 시작....");

        // 데이터베이스 어제날짜 파일 리스트 가져오기
        List<MovieImage> oldMovieImageList = movieImageRepository.findAll();

        // 해당 영화의 이미지리스트 처리
        List<MovieImageDTO> movieImageDTOs = oldMovieImageList.stream().map(movieImage -> {
            return MovieImageDTO.builder()
                    .inum(movieImage.getInum())
                    .uuid(movieImage.getUuid())
                    .imgName(movieImage.getImgName())
                    .path(movieImage.getPath())
                    .build();
        }).collect(Collectors.toList());

        List<Path> fileListPaths = movieImageDTOs.stream()
                .map(dto -> Paths.get(uploadPath, dto.getImageURL(), dto.getUuid() + "_" + dto.getImgName()))
                .collect(Collectors.toList());

        // List<Path> => e:\\upload\\2021\\06\\20\\dcdfdfee2ddfdf_1.jpg

        // 데이터베이스 파일 목록을 기준으로 썸네일 이미지 경로를 추출하여
        // 데이터베이스 파일 목록에 추가한다.
        movieImageDTOs.stream()
                .map(dto -> Paths.get(uploadPath, dto.getImageURL(), "s_" + dto.getUuid() + "_" + dto.getImgName()))
                .forEach(p -> fileListPaths.add(p));

        // 삭제(폴더목록과 데이터베이스 파일목록을 비교한 후 삭제)

        // 어제날짜의 파일 목록 가져오기
        // ① 어제날짜 폴더 구하기
        File targetDir = Paths.get(uploadPath, getFolderYesterDay()).toFile();
        // ② targetDir에 접근 후 파일 목록 가져온 후 데이터베이스 파일 목록과 일치하지 않는다면
        // removeFiles 에 추가
        File[] removeFiles = targetDir.listFiles(f -> fileListPaths.contains(f.toPath()) == false);

        // removeFiles 에 있는 내용 디렉토리에서 삭제
        if (removeFiles != null) {
            for (File remove : removeFiles) {
                log.warn(remove.getAbsolutePath());
                remove.delete();
            }
        }
    }
}
