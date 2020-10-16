package org.panacea.drmp.age.repository.inter;


import org.panacea.drmp.age.domain.graph.ACredential;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "accessNodes", path = "accessNodes")
public interface ACredentialRepository extends CrudRepository<ACredential, Long> {

	ACredential findByUuid(@Param("uuid") String uuid);

}
