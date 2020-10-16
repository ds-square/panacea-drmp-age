package org.panacea.drmp.age.domain.graph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class HUse extends HPrivilege{



	@JsonCreator
	public HUse(@JsonProperty("employeeId") String employeeId, @JsonProperty("uuid") String uuid) {
		super(uuid,employeeId,"USE");
	}
}
