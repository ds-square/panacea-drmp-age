package org.panacea.drmp.age.repository.network;


import org.panacea.drmp.age.domain.graph.NDPrivilege;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
//@RepositoryRestResource(collectionResourceRel = "networkNodes", path = "networkNodes")
public interface NDPrivilegeRepository extends CrudRepository<NDPrivilege, Long> {

	NDPrivilege findByUuid(@Param("uuid") String uuid);

	@Query("MATCH (p1:NDPrivilege)<-[r:EXPLOIT]-(p2:NDPrivilege) RETURN p1,r,p2")
    Collection<NDPrivilege> networkLayerGraph();

    @Query("MATCH (s:NDPrivilege) WHERE s.uuid IN {sourceIds} WITH collect(s) as srcs MATCH (t) WHERE t.uuid IN {targetIds} WITH srcs, collect(t) as trgts CALL agprocedures.allpathstotarget(trgts,{sourceId:srcs }) YIELD path RETURN [rel in reverse(relationships(path)) | [startNode(rel).uuid, rel.vulnerabilities, endNode(rel).uuid]] as paths;")
    Collection<Collection<Collection<Object>>> allPathsSourceToTargetUUIDS(@Param("sourceIds") List<String> sourcesIdList, @Param("targetIds") List<String> targetsIdList);

    @Query("MATCH (s:NDPrivilege) WHERE s.deviceId IN {sourceId} WITH collect(s) as srcs MATCH (t) WHERE t.deviceId IN {targetId} WITH srcs, collect(t) as trgts CALL agprocedures.allpathstotarget(trgts,{source:srcs }) YIELD path RETURN [rel in reverse(relationships(path)) | [startNode(rel).uuid, rel.vulnerabilities, endNode(rel).uuid]] as paths;")
    Collection<Collection<Collection<Object>>> allPathsSourceToTargetIds(@Param("sourceId") List<String> sourceIdList, @Param("targetId") List<String> targetHostnameList);

    @Query("MATCH (t:NDPrivilege) WHERE t.uuid IN {targetIds} WITH collect(t) as trgts CALL agprocedures.allpathstotarget(trgts,{ }) YIELD path RETURN [rel in reverse(relationships(path)) | [startNode(rel).uuid, rel.vulnerabilities, endNode(rel).uuid]] as paths;")
    Collection<Collection<Collection<Object>>> allPathsToTargetUUIDS(@Param("targetIds") List<String> targetIdList);

    @Query("MATCH (t:NDPrivilege) WHERE t.deviceId IN {targetId} WITH collect(t) as trgts CALL agprocedures.allpathstotarget(trgts,{ }) YIELD path RETURN [rel in reverse(relationships(path)) | [startNode(rel).uuid, rel.vulnerabilities, endNode(rel).uuid]] as paths;")
    Collection<Collection<Collection<Object>>> allPathsToTargetIds(@Param("targetId") List<String> targetIdList);

}
