package org.panacea.drmp.age.procedures.util.multilayer;

import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.TraversalBranch;

public class MyMultilayerNotUnique extends MyMultilayerAbstractUniquenessFilter {
	MyMultilayerNotUnique() {
		super((MyMultilayerNodePropertyFetcher)null);
	}

	public boolean check(TraversalBranch source) {
		return true;
	}

	public boolean checkFull(Path path) {
		return true;
	}
}
