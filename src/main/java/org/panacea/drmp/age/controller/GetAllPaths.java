package org.panacea.drmp.age.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.panacea.drmp.age.domain.rest.requests.PathsSourceToTargetRequest;
import org.panacea.drmp.age.domain.rest.requests.PathsToTargetRequest;
import org.panacea.drmp.age.service.PathExtractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping(path = "/age/paths", produces = "application/json")
@Tag(name = "/age/paths", description = "Path Generation API")

public class GetAllPaths {
    @Autowired
    PathExtractionService pathExtractionService;

    @Operation(summary = "Get multilayer attack paths from list of ids to list of ids")
    @PostMapping("/multi-layer/source-target/uuid/")
    public AttackPathsResponse allMultilayerPathsSourceToTargetIds(@Valid @RequestBody PathsSourceToTargetRequest request) {
        return pathExtractionService.extractMultilayerPathsSourceTargetIds(request);
    }

    @Operation(summary = "Get multilayer attack paths from list of (host)names to list of (host)names")
    @PostMapping("/multi-layer/source-target/names/")
    public AttackPathsResponse allMultilayerPathsSourceToTargetNames(@Valid @RequestBody PathsSourceToTargetRequest request) {
        return pathExtractionService.extractMultilayerPathsSourceTargetNames(request);
    }

	@Operation(summary = "Get multilayer attack paths to list of target ids")
	@PostMapping("/multi-layer/target/uuid/")
	public AttackPathsResponse allMultilayerPathsToTargetIds(@Valid @RequestBody PathsToTargetRequest request) {
		return pathExtractionService.extractMultilayerPathsToTargetIds(request);
	}

	@Operation(summary = "Get multilayer attack paths to list of target (host)names")
	@PostMapping("/multi-layer/target/names/")
	public AttackPathsResponse allMultilayerPathsToTargetNames(@Valid @RequestBody PathsToTargetRequest request) {
		return pathExtractionService.extractMultilayerPathsToTargetNames(request);
	}

//	@Operation(summary = "Get all network-layer attack paths from list of source-ids to list of target ids")
//	@PostMapping("/network/source-target/uuid/")
//	public AttackPathsResponse allPathsSourceToTargetIds(@Valid @RequestBody PathsSourceToTargetRequest request) {
//		return pathExtractionService.extractPathsSourceTargetIds(request);
//	}
//
//	@Operation(summary = "Get all network-layer attack paths from list of device hostnames to list of device hostnames")
//	@PostMapping("/network/source-target/hostname/")
//	public AttackPathsResponse allPathsSourceToTargetHostnames(@Valid @RequestBody PathsSourceToTargetRequest request) {
//		return pathExtractionService.extractPathsSourceTargetHostnames(request);
//	}
//
//	@Operation(summary = "Get all network-layer attack paths from all devices to list of target device ids")
//	@PostMapping("/network/target/uuid/")
//	public AttackPathsResponse allPathsToTargetIds(@Valid @RequestBody PathsToTargetRequest request) {
//		return pathExtractionService.extractPathsToTargetIds(request);
//	}
//
//	@Operation(summary = "Get all network-layer attack paths from all devices to list of device hostnames")
//	@PostMapping("/network/target/hostname/")
//	public AttackPathsResponse allPathsToTargetHostnames(@Valid @RequestBody PathsToTargetRequest request) {
//		return pathExtractionService.extractPathsToTargetHostnames(request);
//	}



	// Response object
	@Value
	@Schema(description = "Contains the list of returned paths")
	public static class AttackPathsResponse{
		@Schema(description = "list of paths")
		private Collection<Collection<Collection<Object>>> paths;
	}
}
