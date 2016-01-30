package com.justjames.beertour.beer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerRepository extends JpaRepository<Beer,Integer> {

}
