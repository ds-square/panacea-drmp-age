package org.panacea.drmp.age.procedures.util.network_only;
import org.neo4j.graphdb.traversal.BidirectionalUniquenessFilter;
import org.neo4j.graphdb.traversal.TraversalBranch;


abstract class MyAbstractUniquenessFilter implements BidirectionalUniquenessFilter
{
	final MyPrimitiveTypeFetcher type;

	protected MyAbstractUniquenessFilter(MyPrimitiveTypeFetcher type) {
		this.type = type;
	}

	public boolean checkFirst( TraversalBranch branch )
	{
		return check(branch);
	}
}


