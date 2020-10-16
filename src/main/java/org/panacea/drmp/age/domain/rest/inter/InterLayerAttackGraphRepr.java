package org.panacea.drmp.age.domain.rest.inter;

import org.panacea.drmp.age.domain.graph.ACredential;

import java.util.List;

public class InterLayerAttackGraphRepr {
	private String environment;
	private String fileType;
	private String snapshotId;
	private String snapshotTime;
	private List<ACredential> nodes;
	private List<InterLayerEdgeRepr> humanAccessEdges;
	private List<InterLayerEdgeRepr> accessNetworkEdges;


	public List<ACredential> getNodes() {
		return nodes;
	}

	public List<InterLayerEdgeRepr> getHumanAccessEdges() {
		return humanAccessEdges;
	}

	public List<InterLayerEdgeRepr> getAccessNetworkEdges() {
		return accessNetworkEdges;
	}

	@Override
	public String toString() {
		return "InterLayerAttackGraphRepr{" +
				"environment='" + environment + '\'' +
				", fileType='" + fileType + '\'' +
				", snapshotId='" + snapshotId + '\'' +
				", snapshotTime='" + snapshotTime + '\'' +
				", nodes=" + nodes +
				", humanAccessEdges=" + humanAccessEdges +
				", accessNetworkEdges=" + accessNetworkEdges +
				'}';
	}
}
