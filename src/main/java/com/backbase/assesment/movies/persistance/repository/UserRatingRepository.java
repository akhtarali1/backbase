package com.backbase.assesment.movies.persistance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backbase.assesment.movies.domain.MovieRating;
import com.backbase.assesment.movies.persistance.entity.UserRating;

/**
 * User rating entity repository
 *
 * @author Akhtar
 */
@Repository
public interface UserRatingRepository extends JpaRepository<UserRating, Long> {

    /**
     * Retrieve already existing user rating details
     *
     * @param userId  logged-in user Id
     * @param movieId OMDB movie Id
     * @return existing user rating details
     */
    Optional<UserRating> findByUserIdAndMovieId(String userId, String movieId);

    /**
     * Retrieve average movie rating details by consolidating all ratings of the movie
     *
     * @param movieId OMDB movie Id
     * @return movie rating by consolidating all rating of the movie
     */
    @Query("select new com.backbase.assesment.movies.domain.MovieRating(user.movieId, AVG(user.rating), COUNT(user.rating)) from UserRating user where user.movieId = :movieId GROUP BY user.movieId")
    Optional<MovieRating> getRatingCountAndAverage(String movieId);

    /**
     * Retrieve top movie details based on count and highest rated movies
     *
     * @param pageable to retrieve limited results
     * @return list of movie rating details matching the count
     */
    @Query("select new com.backbase.assesment.movies.domain.MovieRating(user.movieId, AVG(user.rating), COUNT(user.rating)) from UserRating user GROUP BY user.movieId order by AVG(user.rating) DESC")
    List<MovieRating> getTopMovieRatings(Pageable pageable);
}
