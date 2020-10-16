package org.panacea.drmp.age.domain.graph;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.neo4j.ogm.annotation.*;

import java.util.HashSet;
import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,include = JsonTypeInfo.As.PROPERTY,property = "privLevel",defaultImpl = Void.class)
@JsonSubTypes({
        @JsonSubTypes.Type(name="OWN",value = HOwn.class),
        @JsonSubTypes.Type(name="USE",value = HUse.class),
        @JsonSubTypes.Type(name="EXECUTE",value = HExecute.class),

})
@NodeEntity
public abstract class HPrivilege extends AGMultilayerNode{

    @Index
    public String employeeId;


    public String privLevel;

    public HPrivilege(String uuid, String employeeId, String privLevel) {
        super(uuid,employeeId);
        this.privLevel = privLevel;
        this.employeeId = employeeId;
    }


    @JsonIgnoreProperties("fromPrivilege")//IMPORTANT! Avoids Infinite nesting exception
    @Relationship(type = "HEXPLOIT", direction = Relationship.OUTGOING)
    public Set<HExploit> exploits;


    public Set<HExploit> getExploits() {
        return exploits;
    }

    public void addHExploit(HExploit exploit) {
        if (this.exploits == null) {
            this.exploits = new HashSet<>();
        }
        this.exploits.add(exploit);
    }


    @JsonIgnoreProperties("fromPrivilege")//IMPORTANT! Avoids Infinite nesting exception
    @Relationship(type = "TO_CRED", direction = Relationship.OUTGOING)
    public Set<HAToCred> credentials;

    public void addHAToCred(HAToCred credential) {
        if (this.credentials == null) {
            this.credentials = new HashSet<>();
        }
        this.credentials.add(credential);
    }


}
