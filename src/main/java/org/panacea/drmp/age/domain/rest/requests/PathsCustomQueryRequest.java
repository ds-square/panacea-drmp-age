package org.panacea.drmp.age.domain.rest.requests;

import lombok.Data;

@Data
public class PathsCustomQueryRequest {
	String Query;


	public String getQuery() {
		return Query;
	}
}
