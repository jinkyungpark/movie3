package com.project.movie.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ReviewDTO {

    // 리뷰 번호
    private Long reviewNo;
    // 평점
    private int grade;
    // 감상
    private String text;
    private LocalDateTime createdDate, lastModifiedDate;

    // 관계 처리 movie 번호
    private Long mno;

    // member id
    private Long mid;
    // member nickname
    private String nickname;
    // member email
    private String email;

}
