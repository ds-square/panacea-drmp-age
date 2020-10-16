package org.panacea.drmp.age.domain.graph;

import org.neo4j.ogm.annotation.*;


@RelationshipEntity(type = "TO_CRED")
public class HAToCred {

	@Id
	@GeneratedValue
	public Long id;


	@StartNode
	public HPrivilege fromHuman;

	@EndNode
	public ACredential toCredential;


	public HAToCred() {
	}

	public HAToCred(HPrivilege fromHuman, ACredential toCredential) {
		this.fromHuman = fromHuman;
		this.toCredential = toCredential;
	}


	@Override
	public String toString() {
		return "HAToCred{" +
				"id=" + id +
				", fromHuman=" + fromHuman +
				", toCredential=" + toCredential +
				'}';
	}
}
