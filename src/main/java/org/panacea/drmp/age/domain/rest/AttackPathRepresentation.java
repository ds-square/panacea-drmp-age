package org.panacea.drmp.age.domain.rest;

import java.util.Collection;


public class AttackPathRepresentation {
	public Collection<Collection<Object>> path;

	public AttackPathRepresentation(Collection<Collection<Object>> path) {
		this.path = path;
	}
}
