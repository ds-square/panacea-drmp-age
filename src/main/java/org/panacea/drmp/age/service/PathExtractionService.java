package org.panacea.drmp.age.service;

import org.panacea.drmp.age.controller.GetAllPaths;
import org.panacea.drmp.age.domain.rest.requests.PathsCustomQueryRequest;
import org.panacea.drmp.age.domain.rest.requests.PathsSourceToTargetRequest;
import org.panacea.drmp.age.domain.rest.requests.PathsToTargetRequest;


public interface PathExtractionService {
	GetAllPaths.AttackPathsResponse extractPathsSourceTargetUUIds(PathsSourceToTargetRequest query);
	GetAllPaths.AttackPathsResponse extractPathsSourceTargetIds(PathsSourceToTargetRequest query);

	GetAllPaths.AttackPathsResponse extractPathsToTargetUUIds(PathsToTargetRequest query);
	GetAllPaths.AttackPathsResponse extractPathsToTargetIds(PathsToTargetRequest query);

	GetAllPaths.AttackPathsResponse extractMultilayerPathsSourceTargetIds(PathsSourceToTargetRequest query);
	GetAllPaths.AttackPathsResponse extractMultilayerPathsSourceTargetNames(PathsSourceToTargetRequest query);

	GetAllPaths.AttackPathsResponse extractMultilayerPathsToTargetIds(PathsToTargetRequest query);
	GetAllPaths.AttackPathsResponse extractMultilayerPathsToTargetNames(PathsToTargetRequest query);

	void extractPathsForQuery(PathsCustomQueryRequest query);
}
