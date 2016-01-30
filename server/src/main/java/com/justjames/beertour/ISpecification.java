package com.justjames.beertour;

public interface ISpecification<M> {
    boolean IsSatisfiedBy(M candidate);
}
