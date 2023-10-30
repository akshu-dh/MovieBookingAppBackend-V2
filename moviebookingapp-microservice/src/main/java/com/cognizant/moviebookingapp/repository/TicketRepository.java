package com.cognizant.moviebookingapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognizant.moviebookingapp.model.Ticket;


public interface TicketRepository extends JpaRepository<Ticket, String>{
	
	List<Ticket> findByUserName(String userId);

}
