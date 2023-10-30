package com.cognizant.moviebookingapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognizant.moviebookingapp.model.Movie;


public interface MovieRepository extends JpaRepository<Movie, Long> {

	Optional<Movie> findByMovieId(Long movieId);

	boolean existsByMovieId(Long movieId);

	boolean existsByMovieName(String movieName);

	Optional<Movie> findByMovieName(String movieName);

}
