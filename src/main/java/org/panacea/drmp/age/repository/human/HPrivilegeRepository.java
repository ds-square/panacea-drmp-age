package org.panacea.drmp.age.repository.human;


import org.panacea.drmp.age.domain.graph.HPrivilege;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "humanNodes", path = "humanNodes")
public interface HPrivilegeRepository extends CrudRepository<HPrivilege, Long> {

	HPrivilege findByUuid(@Param("uuid") String uuid);

}
