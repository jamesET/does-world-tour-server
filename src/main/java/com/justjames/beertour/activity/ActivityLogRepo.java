package com.justjames.beertour.activity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepo extends JpaRepository<ActivityLog,Integer>, ActivityLogRepoCustom {
	
	Page<ActivityLog> findByUserId(Integer userId,Pageable pr);
	

}
