package com.project.movie.entity;

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
@ToString(exclude = {"member","movie"})
@Entity
public class Review extends BaseEntity{

    @Id
	@SequenceGenerator(name = "movie_review_seq_gen", sequenceName = "review_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_review_seq_gen")
    private Long reviewNo;

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private int grade;

    private String text;
}
