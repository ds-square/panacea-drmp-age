package org.panacea.drmp.age.domain.graph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class NDNone extends NDPrivilege {



	@JsonCreator
	public NDNone(@JsonProperty("deviceId") String deviceId, @JsonProperty("uuid") String uuid) {
		super(uuid,deviceId,"NONE");
	}



	@Override
	public String toString() {
		return "NDNone{" +
				"id=" + id +
				", deviceId='" + deviceId + '\'' +
				", uuid='" + uuid + '\'' +
				", privLevel = NONE"+
				'}';
	}

}
