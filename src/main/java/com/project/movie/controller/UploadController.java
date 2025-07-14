package com.project.movie.controller;

import org.springframework.web.multipart.MultipartFile;

import com.project.movie.dto.UploadResultDTO;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@Controller
@RequestMapping("/upload")
public class UploadController {

    // application.properties 에 있는 값
    @Value("${com.project.movie.upload.path}")
    private String uploadPath;

    // @GetMapping("/uploadEx")
    // public void uploadEx() {
    // log.info("test uploadEx 요청 ");
    // }

    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {

        List<UploadResultDTO> uploadResultDTOs = new ArrayList<>();

        for (MultipartFile uploadFile : uploadFiles) {

            // 이미지 파일 여부
            if (!uploadFile.getContentType().startsWith("image")) {
                log.warn("이 파일은 이미지 파일이 아닙니다.");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
            log.info("fileName " + fileName);

            // 년/월/일 폴더에 맞춰 파일 경로 설정하기
            // 날짜 폴더 생성
            String saveFolderPath = makeFolder();
            // UUID 생성
            String uuid = UUID.randomUUID().toString();
            // 저장할 파일 이름 중간에 "_" 를 이용해서 구분
            String saveName = uploadPath + File.separator + saveFolderPath + File.separator + uuid + "_" + fileName;
            // 파일이 저장되는 최종 경로
            Path savePath = Paths.get(saveName);

            try {
                // 원본 파일 저장
                uploadFile.transferTo(savePath);

                // 썸네일 이름 생성
                // d:\\upload\\\2024\\01\\16\\s_10234fedfde_test.jpg
                String thumbailSaveName = uploadPath + File.separator + saveFolderPath + File.separator + "s_" + uuid
                        + "_" + fileName;
                File thumbnailFile = new File(thumbailSaveName);
                // 썸네일 생성
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);

            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }
            // 저장된 파일 정보 저장하기
            uploadResultDTOs.add(new UploadResultDTO(fileName, uuid, saveFolderPath));
        }
        return new ResponseEntity<>(uploadResultDTOs, HttpStatus.OK);
    }

    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName) {
        log.info("파일 삭제 요청 {}", fileName);
        String srcFileName = null;
        try {

            srcFileName = URLDecoder.decode(fileName, "utf-8");
            // 원본 파일 삭제
            File file = new File(uploadPath + File.separator + srcFileName);
            boolean result = file.delete();

            // 섬네일 파일 삭제
            File thumbnail = new File(file.getParent(), "s_" + file.getName());
            result = thumbnail.delete();

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName, String size) {
        log.info("포스터 요청 {}", fileName);

        ResponseEntity<byte[]> result = null;
        try {

            String srcFileName = URLDecoder.decode(fileName, "utf-8");
            log.info("srcFileName: " + srcFileName);

            File file = new File(uploadPath + File.separator + srcFileName);
            log.info("file: " + file);

            // 썸네일에서 s_ 를 제거하면 원본파일
            if (size != null && size.equals("1")) {
                file = new File(file.getParent(), file.getName().substring(2));
            }

            HttpHeaders header = new HttpHeaders();
            header.add("Content-Type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),
                    header, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    private String makeFolder() {
        // 오늘 날짜를 구하고 원하는 형식에 맞춰 변경
        // LocalDate.now() => 2024-01-16
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = dateStr.replace("/", File.separator);

        // 실제 폴더를 만드는 부분
        File uploadPathFolder = new File(uploadPath, folderPath);
        if (!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }
}
