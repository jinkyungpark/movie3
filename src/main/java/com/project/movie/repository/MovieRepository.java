package com.project.movie.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.movie.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

       // [단계 1] movie 와 review 테이블 조인해서 리뷰의 개수와 리뷰의 평균 평점 구하기
       // coalesce() 널이 아닌 첫번째 인자의 값 리턴
       // r.grade 가 널이라면 0을 리턴
       // @Query("select m, avg(coalesce(r.grade,0)), count(distinct r) from " +
       // "Movie m left join Review r on r.movie = m group by m")
       // Page<Object[]> getListPage(Pageable pageable);

       // [단계 2] movie + review + movieImage 조인
       // movie image는 여러 장이 있기 때문에 가장 번호가 나중인 이미지 가져오기
       // 교재에서는 max(mi) 만 했는데 오라클에서는 오류남
       // max(mi.inum) 시 N+1 문제는 안 일어나나 실제 필요한 건 MovieImage 의 다른 값들도
       // 필요함(path, name, uuid..) => [[],[]] 결과가 여러 엔티티로 나오는 형태는 안됨

       @Query("select m, avg(coalesce(r.grade,0)), count(distinct r), max(mi.inum)  from " +
                     "Movie m left outer join MovieImage mi on mi.movie = m " +
                     "left outer join Review r on r.movie = m group by m")
       Page<Object[]> getListPage(Pageable pageable);

       // @Query(value = "SELECT
       // m.MNO,m.TITLE,m.CREATED_DATE,mi.IMG_NAME,mi.path,mi.uuid,A.grade,A.review_cnt
       // " +
       // "FROM MOVIE m LEFT OUTER JOIN MOVIE_IMAGE mi ON mi.MOVIE_MNO = m.MNO LEFT
       // JOIN " +
       // "(SELECT r.movie_mno AS r_mno, avg(COALESCE(r.grade, 0)) AS grade, " +
       // "count(r.MOVIE_MNO) AS review_cnt FROM REVIEW r GROUP BY r.MOVIE_MNO) A ON
       // m.MNO = A.r_mno ", nativeQuery = true)
       // 하다가 안된 코드 - editor 에서는 되나 여기서는
       @Query(value = "SELECT m.mno, m.title, (SELECT COUNT(DISTINCT r.review_no) FROM review r " +
                     "WHERE r.MOVIE_MNO = m.MNO) review_cnt,(SELECT AVG(coalesce(r.GRADE,0)) FROM REVIEW r WHERE r.MOVIE_MNO = m.MNO) grade_avg "
                     +
                     " FROM	movie m", nativeQuery = true)

       // from join 구문 사용
       // @Query(value = " SELECT * FROM MOVIE m LEFT JOIN (SELECT r.MOVIE_MNO,
       // COUNT(*), AVG(r.GRADE) " +
       // " FROM REVIEW r GROUP BY r.MOVIE_MNO) r1 ON m.MNO = r1.movie_mno " +
       // " LEFT JOIN ( SELECT mi2.* FROM MOVIE_IMAGE mi2 WHERE mi2.INUM " +
       // " IN (SELECT min(mi.INUM) FROM MOVIE_IMAGE mi GROUP BY mi.MOVIE_MNO)) A ON
       // m.MNO = A.movie_mno", nativeQuery = true)
       Page<Object[]> getListPage2(Pageable pageable);

       // [103, 위시, 2024-01-16 22:37:57.123885, wish1.jpg, 2024\01\16,
       // 6ac2828e-f6cb-4d06-a4cc-74a63c59d1d2, null, null]
       // [103, 위시, 2024-01-16 22:37:57.123885, wish3.jpg, 2024\01\16,
       // 4b08815e-86b8-4a08-ab54-b801e7475fa0, null, null]
       // [103, 위시, 2024-01-16 22:37:57.123885, wish2.jpg, 2024\01\16,
       // 6662b045-006b-43c3-8762-b185fea6bdc5, null, null]

       // 특정 영화 조회
       // [단계 1] movie 와 movieImage 조인
       // @Query("select m, mi from Movie m " +
       // " left outer join MovieImage mi on mi.movie = m " +
       // " where m.mno = :mno")
       // List<Object[]> getMovieWithAll(Long mno);

       // [단계 2]
       @Query(value = "select mi.*, A.avg_grade, A.review_cnt from Movie m " +
                     "left outer join movie_image mi on mi.movie_mno = m.mno " +
                     "left outer join (select r.movie_mno as mno, avg(coalesce(r.grade,0)) as avg_grade, count(distinct r.REVIEW_NO) as review_cnt "
                     +
                     "from Review r where r.movie_mno = :mno group by r.movie_mno ) A on A.mno = m.mno " +
                     "where m.mno = :mno", nativeQuery = true)
       List<Object[]> getMovieWithAll(Long mno);
}
