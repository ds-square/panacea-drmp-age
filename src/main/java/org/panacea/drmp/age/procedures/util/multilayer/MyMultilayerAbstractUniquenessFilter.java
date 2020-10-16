package org.panacea.drmp.age.procedures.util.multilayer;
import org.neo4j.graphdb.traversal.BidirectionalUniquenessFilter;
import org.neo4j.graphdb.traversal.TraversalBranch;

abstract class MyMultilayerAbstractUniquenessFilter implements BidirectionalUniquenessFilter
{
	final MyMultilayerNodePropertyFetcher type;

	protected MyMultilayerAbstractUniquenessFilter(MyMultilayerNodePropertyFetcher type) {
		this.type = type;
	}

	public boolean checkFirst( TraversalBranch branch )
	{
		return check(branch);
	}
}


