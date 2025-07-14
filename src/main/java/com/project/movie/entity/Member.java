package com.project.movie.entity;

import org.hibernate.validator.constraints.UniqueElements;

import com.project.movie.constant.MemberRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "m_member")
public class Member extends BaseEntity {

    @Id
    @SequenceGenerator(name = "movie_member_seq_gen", sequenceName = "movie_member_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_member_seq_gen")
    private Long mid;

    @Column(unique = true)
    private String email;
    private String password;
    private String nickname;

    @Enumerated(EnumType.STRING)
    private MemberRole role;
}
