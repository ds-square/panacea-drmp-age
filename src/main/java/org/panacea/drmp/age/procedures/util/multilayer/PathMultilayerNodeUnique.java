package org.panacea.drmp.age.procedures.util.multilayer;

import lombok.extern.slf4j.Slf4j;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.TraversalBranch;
import org.panacea.drmp.age.domain.graph.ACredential;
import org.panacea.drmp.age.domain.graph.NDPrivilege;

@Slf4j
class PathMultilayerNodeUnique extends MyMultilayerAbstractUniquenessFilter {



	PathMultilayerNodeUnique(MyMultilayerNodePropertyFetcher type) {
		super(type);
	}

	public boolean check(TraversalBranch source) {
		//GENERAL RULES
		// do not allow duplicate ids on any path

        //HUMAN Rules
        // do not allow same employeeId (name) on any path (not always implied by the general rule e.g., O@r1 ---> O@d1 ---> U@r1)

        // NETWORK
        // allow only privilege escalation on same hostname between subsequent nodes on a path
        //

        //log.info("" +(this.type.getName(source)));

        // if node is first of path or is of type Credential
        if (source.length() <= 0 || this.type.hasLabel(source, ACredential.class.getSimpleName())) {
            return true;
        }

        long idToCompare = this.type.getId(source);

        //String hostNameToCompare = this.type.getHostname(source);
        String nameToCompare = this.type.getName(source);

        //if(source.length() >= 1 && this.type.hostnameEquals(source.parent(),hostNameToCompare)){
        //if(this.type.hostnameEquals(source.parent(),hostNameToCompare)){
		if(this.type.hasLabel(source, NDPrivilege.class.getSimpleName()) && this.type.nameEquals(source.parent(),nameToCompare)){

			// check if node i am adding follows correct privilege_escalation: prev node
			// having same hostname must have lower privileges (note that we go backward)


		return isIncreasingPrivilege(this.type.getPrivLevel(source),this.type.getPrivLevel(source.parent()) ); // // OR we must be at 1 step from target (self-edge) || source.length() == 1;
		}

		// new node does not have same hostname of prev or prev does not exist
		do {
			if (source.length() <= 0) {
				return true;
			}

			source = source.parent();
		} while(!(this.type.idEquals(source, idToCompare) || this.type.nameEquals(source,nameToCompare)));

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
