package org.panacea.drmp.age.domain.rest.human;

import org.panacea.drmp.age.domain.graph.HPrivilege;

import java.util.List;

public class HumanLayerAttackGraphRepr {
	private String environment;
	private String fileType;
	private String snapshotId;
	private String snapshotTime;
	private List<HPrivilege> nodes;
	private List<HExploitRepr> edges;


	public List<HPrivilege> getNodes() {
		return nodes;
	}

	public List<HExploitRepr> getEdges() {
		return edges;
	}
}
