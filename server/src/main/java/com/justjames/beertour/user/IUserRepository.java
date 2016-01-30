package com.justjames.beertour.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User,Integer> {
	
	public User findByEmail(String email);

}
