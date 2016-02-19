package com.justjames.beertour.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
	
	public User findByEmail(String email);
	
	public List<User> findAllByOrderByNameAsc();

}
