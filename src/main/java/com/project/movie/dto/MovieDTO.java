package com.project.movie.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MovieDTO {
    private Long mno;

    private String title;

    // MovieImage 가져와야 함
    @Builder.Default
    private List<MovieImageDTO> movieImages = new ArrayList<>();

    // 평점
    private double avg;

    private Long reviewCnt;

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
