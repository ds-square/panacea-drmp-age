package org.panacea.drmp.age.domain.graph;

import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "GIVES_PRIVILEGE")
public class ANGivesPrivilege {
	@Id
	@GeneratedValue
	public Long id;


	@StartNode
	public ACredential fromCredential;

	@EndNode
	public NDPrivilege toPrivilege;


	public ANGivesPrivilege() {
	}

	public ANGivesPrivilege(ACredential fromCredential, NDPrivilege toPrivilege) {
		this.fromCredential = fromCredential;
		this.toPrivilege = toPrivilege;
	}


	@Override
	public String toString() {
		return "ANGivesPrivilege{" +
				"id=" + id +
				", fromCredential=" + fromCredential +
				", toPrivilege=" + toPrivilege +
				'}';
	}
}
