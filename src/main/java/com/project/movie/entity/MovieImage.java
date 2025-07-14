package com.project.movie.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
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
@ToString(exclude = "movie")
@Entity
public class MovieImage extends BaseEntity {

    @Id
    @SequenceGenerator(name = "movie_image_seq_gen", sequenceName = "movie_image_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_image_seq_gen")
    private Long inum;

    private String uuid;

    private String imgName;

    private String path; // 년/월/일 폴더구조

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

}
