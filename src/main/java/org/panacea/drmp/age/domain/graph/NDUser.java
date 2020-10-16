package org.panacea.drmp.age.domain.graph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class NDUser extends NDPrivilege {


	@JsonCreator
	public NDUser(@JsonProperty("deviceId") String deviceId, @JsonProperty("uuid") String uuid) {
		super(uuid,deviceId,"USER");
	}



	@Override
	public String toString() {
		return "NDUser{" +
				"id=" + id +
				", deviceId='" + deviceId + '\'' +
				", uuid='" + uuid + '\'' +
				", privLevel = USER" +

				'}';
	}
}
