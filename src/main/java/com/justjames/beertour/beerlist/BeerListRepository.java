package com.justjames.beertour.beerlist;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerListRepository extends JpaRepository<BeerList,Integer> {
	
	public Collection<BeerList> findByUserId(Integer userId);
	
	public Collection<BeerList> findByFinishDateIsNull();
	
	public BeerList findByUserIdAndFinishDateIsNull(Integer userId);
	

}
