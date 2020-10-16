package org.panacea.drmp.age.domain.graph;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public abstract class AGMultilayerNode {


	@Id
	@GeneratedValue
	public Long id;
	@Index
	public String name;

    @Index(unique = true)
    public String uuid;


    public AGMultilayerNode() {
    }


    public AGMultilayerNode(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }
}
