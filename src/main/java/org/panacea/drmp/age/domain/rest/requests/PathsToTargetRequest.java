package org.panacea.drmp.age.domain.rest.requests;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class PathsToTargetRequest {
	@NotNull(message = "Request must have a non empty list of targets")
    @NotEmpty(message = "Request must have a non empty list of targets")
    @ArraySchema(arraySchema = @Schema(description = "List of targets of the paths to retrieve"))
    List<String> targets;
	public List<String> getTargets() {
		return targets;
	}




}