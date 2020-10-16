package org.panacea.drmp.age.repository;


import org.panacea.drmp.age.domain.graph.AGMultilayerNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AGMultilayerNodeRepository extends CrudRepository<AGMultilayerNode, Long> {

	@Query("MATCH (s) WHERE s.uuid IN {sourceIds} WITH collect(s) as srcs MATCH (t) WHERE t.uuid IN {targetIds} WITH srcs, collect(t) as trgts CALL agprocedures.allmultilayerpathstotarget(trgts,{sourceId:srcs }) YIELD path RETURN [rel in reverse(relationships(path)) | [startNode(rel).uuid, coalesce(rel.vulnerabilities,[]), endNode(rel).uuid]] as paths;")
    Collection<Collection<Collection<Object>>> allMultilayerPathsSourceToTargetIds(@Param("sourceIds") List<String> sourcesIdList, @Param("targetIds") List<String> targetsIdList);

    @Query("MATCH (s) WHERE s.name IN {sourceNames} WITH collect(s) as srcs MATCH (t) WHERE t.name IN {targetNames} WITH srcs, collect(t) as trgts CALL agprocedures.allmultilayerpathstotarget(trgts,{source:srcs }) YIELD path RETURN [rel in reverse(relationships(path)) | [startNode(rel).uuid, coalesce(rel.vulnerabilities,[]), endNode(rel).uuid]] as paths;")
    Collection<Collection<Collection<Object>>> allMultilayerPathsSourceToTargetNames(@Param("sourceNames") List<String> sourceNamesList, @Param("targetNames") List<String> targetNamesList);

    @Query("MATCH (t) WHERE t.uuid IN {targetIds} WITH collect(t) as trgts CALL agprocedures.allmultilayerpathstotarget(trgts,{}) YIELD path RETURN [rel in reverse(relationships(path)) | [startNode(rel).uuid, coalesce(rel.vulnerabilities,[]), endNode(rel).uuid]] as paths;")
    Collection<Collection<Collection<Object>>> allMultilayerPathsToTargetIds(@Param("targetIds") List<String> targetsIdList);

    @Query("MATCH (t) WHERE t.name IN {targetNames} WITH collect(t) as trgts CALL agprocedures.allmultilayerpathstotarget(trgts,{}) YIELD path RETURN [rel in reverse(relationships(path)) | [startNode(rel).uuid, coalesce(rel.vulnerabilities,[]), endNode(rel).uuid]] as paths;")
    Collection<Collection<Collection<Object>>> allMultilayerPathsToTargetNames(@Param("targetNames") List<String> targetNamesList);

}
