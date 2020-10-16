package org.panacea.drmp.age.procedures.util.network_only;

import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.TraversalBranch;

public class MyNotUnique extends MyAbstractUniquenessFilter {
	MyNotUnique() {
		super((MyPrimitiveTypeFetcher)null);
	}

	public boolean check(TraversalBranch source) {
		return true;
	}

	public boolean checkFull(Path path) {
		return true;
	}
}
