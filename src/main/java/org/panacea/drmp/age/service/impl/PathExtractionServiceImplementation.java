package org.panacea.drmp.age.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.neo4j.graphdb.GraphDatabaseService;
import org.panacea.drmp.age.controller.GetAllPaths;
import org.panacea.drmp.age.domain.rest.requests.PathsCustomQueryRequest;
import org.panacea.drmp.age.domain.rest.requests.PathsSourceToTargetRequest;
import org.panacea.drmp.age.domain.rest.requests.PathsToTargetRequest;
import org.panacea.drmp.age.repository.AGMultilayerNodeRepository;
import org.panacea.drmp.age.repository.network.NDPrivilegeRepository;
import org.panacea.drmp.age.service.PathExtractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
public class PathExtractionServiceImplementation implements PathExtractionService {

    @Autowired
    GraphDatabaseService graphDatabaseService;

    @Autowired
    NDPrivilegeRepository privilegeRepository;

    @Autowired
    AGMultilayerNodeRepository multilayerNodeRepository;

	@Override
	public GetAllPaths.AttackPathsResponse extractPathsSourceTargetUUIds(PathsSourceToTargetRequest query) {
		Collection<Collection<Collection<Object>>> paths = privilegeRepository.allPathsSourceToTargetUUIDS(query.getSources(),query.getTargets());
		return new GetAllPaths.AttackPathsResponse(paths);
	}

	@Override
	public GetAllPaths.AttackPathsResponse extractPathsSourceTargetIds(PathsSourceToTargetRequest query) {
		Collection<Collection<Collection<Object>>> paths = privilegeRepository.allPathsSourceToTargetIds(query.getSources(),query.getTargets());
		return new GetAllPaths.AttackPathsResponse(paths);
	}

	@Override
	public GetAllPaths.AttackPathsResponse extractPathsToTargetUUIds(PathsToTargetRequest query) {
		Collection<Collection<Collection<Object>>> paths = privilegeRepository.allPathsToTargetUUIDS(query.getTargets());
		return new GetAllPaths.AttackPathsResponse(paths);
	}

	@Override
	public GetAllPaths.AttackPathsResponse extractPathsToTargetIds(PathsToTargetRequest query) {
		Collection<Collection<Collection<Object>>> paths = privilegeRepository.allPathsToTargetIds(query.getTargets());
		return new GetAllPaths.AttackPathsResponse(paths);
	}

	// Multilayer calls

	@Override
	public GetAllPaths.AttackPathsResponse extractMultilayerPathsSourceTargetIds(PathsSourceToTargetRequest query) {
		Collection<Collection<Collection<Object>>> paths = multilayerNodeRepository.allMultilayerPathsSourceToTargetIds(query.getSources(),query.getTargets());
		return new GetAllPaths.AttackPathsResponse(paths);
	}
	@Override
	public GetAllPaths.AttackPathsResponse extractMultilayerPathsSourceTargetNames(PathsSourceToTargetRequest query) {
		log.info("[AGE] Performing multilayer attack paths generation from sources: " + query.getSources() + " to targets: " + query.getTargets());
		Collection<Collection<Collection<Object>>> paths = multilayerNodeRepository.allMultilayerPathsSourceToTargetNames(query.getSources(),query.getTargets());
		return new GetAllPaths.AttackPathsResponse(paths);
	}

	@Override
	public GetAllPaths.AttackPathsResponse extractMultilayerPathsToTargetIds(PathsToTargetRequest query) {
		Collection<Collection<Collection<Object>>> paths = multilayerNodeRepository.allMultilayerPathsToTargetIds(query.getTargets());
		return new GetAllPaths.AttackPathsResponse(paths);
	}

	@Override
	public GetAllPaths.AttackPathsResponse extractMultilayerPathsToTargetNames(PathsToTargetRequest query) {
		Collection<Collection<Collection<Object>>> paths = multilayerNodeRepository.allMultilayerPathsToTargetNames(query.getTargets());
		return new GetAllPaths.AttackPathsResponse(paths);
	}

	@Override
	public void extractPathsForQuery(PathsCustomQueryRequest query) {

	}
}
