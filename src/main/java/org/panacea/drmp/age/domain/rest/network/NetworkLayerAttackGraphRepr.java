package org.panacea.drmp.age.domain.rest.network;

import org.panacea.drmp.age.domain.graph.NDPrivilege;

import java.util.List;

public class NetworkLayerAttackGraphRepr {
	private String environment;
	private String fileType;
	private String snapshotId;
	private String snapshotTime;
	private List<NDPrivilege> nodes;
	private List<NExploitRepr> edges;


	public List<NDPrivilege> getNodes() {
		return nodes;
	}

	public List<NExploitRepr> getEdges() {
		return edges;
	}
}
