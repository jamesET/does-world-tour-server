package com.justjames.beertour.beerlist;

import com.justjames.beertour.ISpecification;

public class BeerListCompleteSpec implements ISpecification<BeerList> {

	@Override
	public boolean IsSatisfiedBy(BeerList candidate) {
		if (candidate.getNumberRemaining() == 0) {
			return true;
		}
		return false;
	}

}
