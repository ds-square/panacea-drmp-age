package org.panacea.drmp.age.domain.graph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class HOwn extends HPrivilege{



	@JsonCreator
	public HOwn(@JsonProperty("employeeId") String employeeId, @JsonProperty("uuid") String uuid) {
		super(uuid,employeeId,"OWN");
	}
}
