package com.cognizant.moviebookingapp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.moviebookingapp.exception.AuthorizationException;
import com.cognizant.moviebookingapp.kafka.KafkaProducer;
//import com.cognizant.moviebookingapp.kafka.KafkaProducer;
import com.cognizant.moviebookingapp.model.Movie;
import com.cognizant.moviebookingapp.model.Ticket;
import com.cognizant.moviebookingapp.service.MovieService;
import com.cognizant.moviebookingapp.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1.0/moviebooking")
public class MovieController {

	@Autowired
	MovieService movieService;

	@Autowired
	TicketService ticketService;

	@Autowired
	AuthService authService;

	@Autowired
	private KafkaProducer stringProducer;

	/// role:admin
	@PostMapping("/addmovie")
	@Operation(summary = "add movie into db(admin)")
	public ResponseEntity<?> addMovies(@RequestBody Movie movie,
			@Parameter(hidden = true) @RequestHeader("Authorization") String token) throws AuthorizationException {
		Map<String, String> userInfo = authService.validateToken(token);
		if (userInfo.containsValue("ROLE_ADMIN")) {
//			stringProducer.sendMessage("new movie added by admin--> movie: " + movie.getMovieName()); 
//			when kafka runs uncomment me
			return movieService.addMovie(movie);
		} else {
			throw new AuthorizationException("Access Denied");
		}

	}

	@GetMapping("/getAllMovies")
	@Operation(summary = "get all available movies(admin+customer)")
	public ResponseEntity<List<Movie>> getAllMovies(
			@Parameter(hidden = true) @RequestHeader("Authorization") String token) throws AuthorizationException {
		Map<String, String> userInfo = authService.validateToken(token);
		if (userInfo.containsValue("ROLE_ADMIN") || userInfo.containsValue("ROLE_CUSTOMER")) {
			return movieService.getAllMovies();
		} else {
			throw new AuthorizationException("Access Denied");
		}

	}

	@GetMapping("/movies/search/{movieId}")
	@Operation(summary = "searching movie by it(admin+customer)")
	public ResponseEntity<?> searchMovieById(@PathVariable(value = "movieId") Long movieId,
			@Parameter(hidden = true) @RequestHeader("Authorization") String token) throws AuthorizationException {
		Map<String, String> userInfo = authService.validateToken(token);
		if (userInfo.containsValue("ROLE_ADMIN") || userInfo.containsValue("ROLE_CUSTOMER")) {
			return movieService.searchMovieById(movieId);
		} else {
			throw new AuthorizationException("Access Denied");
		}

	}

	@DeleteMapping("/delete/{movieId}")
	@Operation(summary = "for deletion of movie(admin)")
	public ResponseEntity<?> deleteMovieById(@PathVariable(value = "movieId") Long movieId,
			@Parameter(hidden = true) @RequestHeader("Authorization") String token) throws AuthorizationException {
		Map<String, String> userInfo = authService.validateToken(token);
		if (userInfo.containsValue("ROLE_ADMIN")) {
//			stringProducer.sendMessage("movie deleted by admin: " + movieId); 
//			when kafka runs uncomment me
			return movieService.deleteMovie(movieId);
		} else {
			throw new AuthorizationException("Access Denied");
		}

	}

	@GetMapping("/getAllTickets")
	@Operation(summary = "for listing all the tickets booked by users(admin)")
	public ResponseEntity<?> getAllTickets(@Parameter(hidden = true) @RequestHeader("Authorization") String token)
			throws AuthorizationException {
		Map<String, String> userInfo = authService.validateToken(token);
		if (userInfo.containsValue("ROLE_ADMIN")) {
			return ticketService.getAllTickets();
		} else {
			throw new AuthorizationException("Access Denied");
		}

	}

	@PostMapping("/book/{movieName}")
	@Operation(summary = "for book a ticket for a movie(admin+customer)")
	public ResponseEntity<?> bookMovie(@PathVariable(value = "movieName") String movieName, @RequestBody Ticket ticket,
			@Parameter(hidden = true) @RequestHeader("Authorization") String token) throws AuthorizationException {
		Map<String, String> userInfo = authService.validateToken(token);
		if (userInfo.containsValue("ROLE_ADMIN") || userInfo.containsValue("ROLE_CUSTOMER")) {

			String userId = userInfo.keySet().iterator().next();
			ticket.setUserName(userId);
//			stringProducer.sendMessage("movie booked by userId " + userId + " movie: " + movieName); 
//			when kafka runs uncomment me
			return ticketService.bookMovie(movieName, ticket);
		} else {
			throw new AuthorizationException("Access Denied");
		}

	}

	@GetMapping("/getUserTickets/{userId}") // no need of userId here--fix--if user is already logged in
	@Operation(summary = "for customer to see the booked tickets for a movie(admin+customer)")
	public ResponseEntity<?> getTicketsByUserId(@PathVariable(value = "userId") String userId,
			@Parameter(hidden = true) @RequestHeader("Authorization") String token) throws AuthorizationException {
		Map<String, String> userInfo = authService.validateToken(token);
		if (userInfo.containsValue("ROLE_ADMIN") || userInfo.containsValue("ROLE_CUSTOMER")) {
			return ticketService.getTicketsUser(userId);
		} else {
			throw new AuthorizationException("Access Denied");
		}

	}
	@PutMapping("/{movieName}/update/{sumTickets}")
    @Operation(summary = "for update of tickets into a movie(admin)")
    public ResponseEntity<?> updateTicketStatus(@PathVariable(value = "movieName") String movieName,
            @PathVariable(value = "sumTickets") int sumTickets) {
        return movieService.updateMovie(movieName, sumTickets);
    }

}