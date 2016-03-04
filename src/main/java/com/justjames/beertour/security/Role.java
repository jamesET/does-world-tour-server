package com.justjames.beertour.security;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

public enum Role {
	CUSTOMER(
		ImmutableSet.of(
			Permission.ListOwner.toString()
		)
	),
	ADMIN(
		ImmutableSet.of(
			Permission.ListOwner.toString(),
			Permission.ListAdmin.toString(),
			Permission.UserAdmin.toString(),
			Permission.BeerAdmin.toString()
		)
	);
	
	private ImmutableSet<String> permissions;
	
	Role(ImmutableSet<String> permissions) {
		this.permissions = permissions;
	}
	
	public Set<String> getPermissions() {
		return this.permissions;
	}
	
}
