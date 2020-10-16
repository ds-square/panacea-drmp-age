package org.panacea.drmp.age.procedures.util.network_only;

import lombok.extern.slf4j.Slf4j;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.TraversalBranch;

@Slf4j
class PathNodeHostnameUnique extends MyAbstractUniquenessFilter {



	PathNodeHostnameUnique(MyPrimitiveTypeFetcher type) {
		super(type);
	}

	public boolean check(TraversalBranch source) {

		//do not allow duplicate ids
		// allow only privilege escalation on same hostname between subsequent nodes

		//log.info("" +(this.type.getId(source)));
		long idToCompare = this.type.getId(source);
		String hostNameToCompare = this.type.getHostname(source);

		if(source.length() >= 1 && this.type.hostnameEquals(source.parent(),hostNameToCompare)){

			// check if node i am adding follows correct privilege_escalation: prev node
			// having same hostname must have lower privileges (note that we go backward)
		return isIncreasingPrivilege(this.type.getPrivLevel(source),this.type.getPrivLevel(source.parent()));
		}

		// new node does not have same hostname of prev or prev does not exist
		do {
			if (source.length() <= 0) {
				return true;
			}

			source = source.parent();
		} while(!(this.type.idEquals(source, idToCompare) || this.type.hostnameEquals(source,hostNameToCompare)));

		return false;
	}

	public boolean checkFull(Path path) {

		return !this.type.containsDuplicates(path);
	}


	private boolean isIncreasingPrivilege(String fromPriv, String toPriv) {
		switch (toPriv) {
			case "USER":
				return fromPriv.equals("NONE");
			case "ROOT":
				return (fromPriv.equals("USER") || fromPriv.equals("NONE"));
			default:
				return false;
		}
	}
}
