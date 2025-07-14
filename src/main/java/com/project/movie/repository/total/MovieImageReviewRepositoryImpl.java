package com.project.movie.repository.total;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.project.movie.entity.Movie;
import com.project.movie.entity.MovieImage;
import com.project.movie.entity.QMovie;
import com.project.movie.entity.QMovieImage;
import com.project.movie.entity.QReview;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class MovieImageReviewRepositoryImpl extends QuerydslRepositorySupport implements MovieImageReviewRepository {

        public MovieImageReviewRepositoryImpl() {
                super(MovieImage.class);
        }

        @Override
        public Page<Object[]> getTotalList(String type, String keyword, Pageable pageable) {
                log.info("getList");

                QMovie movie = QMovie.movie;
                QReview review = QReview.review;
                QMovieImage movieImage = QMovieImage.movieImage;

                JPQLQuery<MovieImage> query = from(movieImage);
                query.leftJoin(movie).on(movie.eq(movieImage.movie));

                JPQLQuery<Tuple> tuple2 = query
                                .select(movie, movieImage,
                                                JPAExpressions.select(review.countDistinct()).from(review)
                                                                .where(review.movie.eq(movieImage.movie)),
                                                JPAExpressions.select(review.grade.avg().round()).from(review)
                                                                .where(review.movie.eq(movieImage.movie)))
                                .where(movieImage.inum.in(
                                                JPAExpressions.select(movieImage.inum.min()).from(movieImage)
                                                                .groupBy(movieImage.movie)));
                // group by 를 사용 안하고 where 절을 이용해도 가능한듯
                // JPAExpressions.select(movieImage.inum.min()).from(movieImage)
                // .where(movieImage.movie.eq(movie))))
                // .orderBy(movie.mno.desc());

                BooleanBuilder builder = new BooleanBuilder();
                BooleanExpression expression = movie.mno.gt(0L);
                builder.and(expression);

                // 검색 타입이 있는 경우
                BooleanBuilder conditionBuilder = new BooleanBuilder();
                if (type.contains("title")) {
                        conditionBuilder.or(movie.title.contains(keyword));
                }

                builder.and(conditionBuilder);
                tuple2.where(builder);

                // order by
                Sort sort = pageable.getSort();
                sort.stream().forEach(order -> {
                        // com.querydsl.core.types.Order
                        Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                        String prop = order.getProperty();
                        // PathBuilder : Sort 객체 속성 - bno or title 이런 것들 지정
                        PathBuilder<Movie> orderByExpression = new PathBuilder<>(Movie.class, "movie");
                        // Sort 객체 사용 불가로 OrderSpecifier() 사용
                        // com.querydsl.core.types.OrderSpecifier.OrderSpecifier(Order order, Expression
                        // target)
                        tuple2.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
                });

                // page 처리
                tuple2.offset(pageable.getOffset());
                tuple2.limit(pageable.getPageSize());

                List<Tuple> result = tuple2.fetch();
                log.info(result);

                // 전체 개수-따로 구하지 않고 바로 알아낼 수 있음
                long count = tuple2.fetchCount();

                // Page<Object []> 인터페이스 임 => new PageImpl<> 로 만든 것
                // PageImpl(List<T> content, Pageable pageable, long total)
                return new PageImpl<>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable,
                                count);
        }

        @Override
        public Page<Object[]> getTotalList2(String type, String keyword, Pageable pageable) {
                log.info("getList");

                QMovie movie = QMovie.movie;
                QReview review = QReview.review;
                QMovieImage movieImage = QMovieImage.movieImage;

                JPQLQuery<MovieImage> query = from(movieImage);
                query.leftJoin(movie).on(movie.eq(movieImage.movie));

                JPQLQuery<Tuple> tuple2 = query
                                .select(movie, movieImage,
                                                JPAExpressions.select(review.countDistinct()).from(review)
                                                                .where(review.movie.eq(movieImage.movie)),
                                                JPAExpressions.select(review.grade.avg().round()).from(review)
                                                                .where(review.movie.eq(movieImage.movie)));

                BooleanBuilder builder = new BooleanBuilder();
                BooleanExpression expression = movie.mno.gt(0L);
                builder.and(expression);

                // 검색 타입이 있는 경우
                BooleanBuilder conditionBuilder = new BooleanBuilder();
                if (type.contains("title")) {
                        conditionBuilder.or(movie.title.contains(keyword));
                }

                builder.and(conditionBuilder);
                tuple2.where(builder);

                // order by
                Sort sort = pageable.getSort();
                sort.stream().forEach(order -> {
                        // com.querydsl.core.types.Order
                        Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                        String prop = order.getProperty();
                        // PathBuilder : Sort 객체 속성 - bno or title 이런 것들 지정
                        PathBuilder<Movie> orderByExpression = new PathBuilder<>(Movie.class, "movie");
                        // Sort 객체 사용 불가로 OrderSpecifier() 사용
                        // com.querydsl.core.types.OrderSpecifier.OrderSpecifier(Order order, Expression
                        // target)
                        tuple2.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
                });

                // page 처리
                tuple2.offset(pageable.getOffset());
                tuple2.limit(pageable.getPageSize());

                List<Tuple> result = tuple2.fetch();
                log.info(result);

                // 전체 개수-따로 구하지 않고 바로 알아낼 수 있음
                long count = tuple2.fetchCount();

                // Page<Object []> 인터페이스 임 => new PageImpl<> 로 만든 것
                // PageImpl(List<T> content, Pageable pageable, long total)
                return new PageImpl<>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable,
                                count);
        }

        @Override
        public List<Object[]> getMovieRow(Long mno) {
                QMovie movie = QMovie.movie;
                QReview review = QReview.review;
                QMovieImage movieImage = QMovieImage.movieImage;

                JPQLQuery<MovieImage> query = from(movieImage);
                query.leftJoin(movie).on(movie.eq(movieImage.movie));

                JPQLQuery<Tuple> tuple2 = query
                                .select(movie, movieImage,
                                                JPAExpressions.select(review.countDistinct()).from(review)
                                                                .where(review.movie.eq(movieImage.movie)),
                                                JPAExpressions.select(review.grade.avg().round()).from(review)
                                                                .where(review.movie.eq(movieImage.movie)))
                                .where(movieImage.movie.mno.eq(mno))
                                .orderBy(movieImage.inum.desc());

                List<Tuple> result = tuple2.fetch();

                List<Object[]> list = result.stream().map(t -> t.toArray()).collect(Collectors.toList());
                return list;
        }
}
