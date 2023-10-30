package com.moviebooking.auth.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Role {
	@Id
	private String id;
	
	private String name;
    
	//private Long uid;
	
	public Role() {

	}
    
	public Role(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
