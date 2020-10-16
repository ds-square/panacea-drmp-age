package org.panacea.drmp.age.domain.graph;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class ACredential extends AGMultilayerNode {

	public String credentialType;


	@JsonIgnoreProperties("fromCredential")//IMPORTANT! Avoids Infinite nesting exception
	@Relationship(type = "GIVES_PRIVILEGE", direction = Relationship.OUTGOING)
	public Set<ANGivesPrivilege> privileges;

	public ACredential(String uuid, String credentialType) {
		super(uuid,uuid);
		this.credentialType = credentialType;
	}

	public Set<ANGivesPrivilege> getPrivileges() {
		return privileges;
	}

	public void addANGivesPrivilege(ANGivesPrivilege access) {
		if (this.privileges == null) {
			this.privileges = new HashSet<>();
		}
		this.privileges.add(access);
	}

}
