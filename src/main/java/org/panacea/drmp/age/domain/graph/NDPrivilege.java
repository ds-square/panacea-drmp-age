package org.panacea.drmp.age.domain.graph;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.neo4j.ogm.annotation.*;

import java.util.HashSet;
import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,include = JsonTypeInfo.As.PROPERTY,property = "privLevel",defaultImpl = Void.class)
@JsonSubTypes({
		@JsonSubTypes.Type(name="NONE",value = NDNone.class),
		@JsonSubTypes.Type(name="USER",value = NDUser.class),
		@JsonSubTypes.Type(name="ROOT",value = NDRoot.class),
})
@NodeEntity
public abstract class NDPrivilege extends AGMultilayerNode {

	@Index
	public String deviceId;

	public String privLevel;

	@JsonIgnoreProperties("fromPrivilege")//IMPORTANT! Avoids Infinite nesting exception
	@Relationship(type = "EXPLOIT", direction = Relationship.OUTGOING)
	public Set<NExploit> exploits;


	public Set<NExploit> getExploits() {
		return exploits;
	}

	public void addNExploit(NExploit exploit) {
		if (this.exploits == null) {
			this.exploits = new HashSet<>();
		}
		this.exploits.add(exploit);
	}

	public NDPrivilege(String uuid, String deviceId, String privLevel) {
		super(uuid,deviceId);
		this.privLevel = privLevel;
		this.deviceId = deviceId;
	}


	@Override
	public String toString() {
		return "NDPrivilege{" +
				"id=" + id +
				", deviceId='" + deviceId + '\'' +
				", uuid='" + uuid + '\'' +
				", privLevel='" + privLevel + '\'' +
				", exploits=" + ((exploits!=null)? exploits.toString():"null") +
				'}';
	}
}
