package org.panacea.drmp.age.domain.graph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class NDRoot extends NDPrivilege {

	@JsonCreator
	public NDRoot(@JsonProperty("deviceId") String deviceId, @JsonProperty("uuid") String uuid) {
		super(uuid,deviceId,"ROOT");
	}



	@Override
	public String toString() {
		return "NDRoot{" +
				"id=" + id +
				", deviceId='" + deviceId + '\'' +
				", uuid='" + uuid + '\'' +
				", privLevel = ROOT" +
				'}';
	}


}
