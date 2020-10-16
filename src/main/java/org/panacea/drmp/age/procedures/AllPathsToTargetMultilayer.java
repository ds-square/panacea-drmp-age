package org.panacea.drmp.age.procedures;

import apoc.path.LabelSequenceEvaluator;
import apoc.path.NodeEvaluators;
import apoc.path.RelationshipSequenceExpander;
import apoc.result.PathResult;
import apoc.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.procedure.*;
import org.panacea.drmp.age.procedures.util.multilayer.MyMultilayerUniqueness;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
public class AllPathsToTargetMultilayer {


	//TODO: if I end up in a none node I should stop the search

	// Only static fields and @Context-annotated fields are allowed in
	// Procedure classes.

	//private static final field...
	private static final MyMultilayerUniqueness UNIQUENESS = MyMultilayerUniqueness.MULTILAYER_NODE_PATH;//NODE_GLOBAL

	// This field declares that we need a GraphDatabaseService
	// as context when any procedure in this class is invoked
	@Context
	public GraphDatabaseService db;


	// APOC 3.5.0.4
	public static Traverser traverse(TraversalDescription traversalDescription, Iterable<Node> startNodes, String pathFilter, String labelFilter, long minLevel, long maxLevel, MyMultilayerUniqueness uniqueness, boolean bfs, boolean filterStartNode, EnumMap<NodeFilter, List<Node>> nodeFilter, String sequence, boolean beginSequenceAtStart) {
		TraversalDescription td = bfs ? traversalDescription.breadthFirst() : traversalDescription.depthFirst();
		if (sequence != null && !sequence.trim().isEmpty()) {
			String[] sequenceSteps = sequence.split(",");
			List<String> labelSequenceList = new ArrayList();
			List<String> relSequenceList = new ArrayList();

			for (int index = 0; index < sequenceSteps.length; ++index) {
				List<String> seq = (beginSequenceAtStart ? index : index - 1) % 2 == 0 ? labelSequenceList : relSequenceList;
				seq.add(sequenceSteps[index]);
			}

			td = td.expand(new RelationshipSequenceExpander(relSequenceList, beginSequenceAtStart));
			td = td.evaluator(new LabelSequenceEvaluator(labelSequenceList, filterStartNode, beginSequenceAtStart, (int) minLevel));
		} else {
			if (pathFilter != null && !pathFilter.trim().isEmpty()) {
				td = td.expand(new RelationshipSequenceExpander(pathFilter.trim(), beginSequenceAtStart));
			}

			if (labelFilter != null && sequence == null && !labelFilter.trim().isEmpty()) {
				td = td.evaluator(new LabelSequenceEvaluator(labelFilter.trim(), filterStartNode, beginSequenceAtStart, (int) minLevel));
			}
		}

		if (minLevel != -1L) {
			td = td.evaluator(Evaluators.fromDepth((int) minLevel));
		}

		if (maxLevel != -1L) {
			td = td.evaluator(Evaluators.toDepth((int) maxLevel));
		}

		if (nodeFilter != null && !nodeFilter.isEmpty()) {
			List<Node> endNodes = (List) nodeFilter.getOrDefault(NodeFilter.END_NODES, Collections.EMPTY_LIST);
			List<Node> terminatorNodes = (List) nodeFilter.getOrDefault(NodeFilter.TERMINATOR_NODES, Collections.EMPTY_LIST);
			List<Node> blacklistNodes = (List) nodeFilter.getOrDefault(NodeFilter.BLACKLIST_NODES, Collections.EMPTY_LIST);
			Object whitelistNodes;
			if (nodeFilter.containsKey(NodeFilter.WHITELIST_NODES)) {
				whitelistNodes = new ArrayList(nodeFilter.get(NodeFilter.WHITELIST_NODES));
			} else {
				whitelistNodes = Collections.EMPTY_LIST;
			}

			if (!blacklistNodes.isEmpty()) {
				td = td.evaluator(NodeEvaluators.blacklistNodeEvaluator(false, 0, blacklistNodes));
			}

			Evaluator endAndTerminatorNodeEvaluator = NodeEvaluators.endAndTerminatorNodeEvaluator(false, 0, endNodes, terminatorNodes);
			if (endAndTerminatorNodeEvaluator != null) {
				td = td.evaluator(endAndTerminatorNodeEvaluator);
			}

			if (!((List) whitelistNodes).isEmpty()) {
				((List) whitelistNodes).addAll(endNodes);
				((List) whitelistNodes).addAll(terminatorNodes);
				td = td.evaluator(NodeEvaluators.whitelistNodeEvaluator(false, 0, (List) whitelistNodes));
			}
		}

		td = td.uniqueness(uniqueness);
		return td.traverse(startNodes);
	}

	@Procedure(name = "agprocedures.allmultilayerpathstotarget", mode = Mode.READ)
	@Description("Return all multilayer paths ending on the matched target nodes")
	public Stream<PathResult> allMultilayerPathsTarget(@Name("start") Object start, @Name("config") Map<String, Object> config) throws Exception {


		/* Corresponding Cypher Query example

		 * MATCH (t:AGMultilayerNode {hostname:"VR3"})   --- selection of target is made using appropriate cypher statements
		 * CALL agprocedure.allpathstarget(t,{}) YIELD path
		 * RETURN path;
		 *
		 */

		//Standard UNIQUENESS di APOC
		//RELATIONSHIP_PATH [non termina e] produces loops over nodes (could work if I manually bookkeep nodes (or, better, hostnames))
		//RELATIONSHIP_LEVEL [non termina] e come PATH, torna indietro lungo uno stesso path rivisitando i nodi

		//NODE_GLOBAL e' conservativo, trova fondamentalmente alberi
		//RELATIONSHIP_GLOBAL e' conservativo, impedisce di  passare due volte su una relazione a diversi livelli
		//NODE_LEVEL non ha senso, aiuterebbe solo nei multigrafi


		Map<String, Object> expandConfig = new HashMap<String, Object>();
		expandConfig.put("minLevel", 1L);
		expandConfig.put("maxLevel", 10L);
		expandConfig.put("bfs",true);


		expandConfig.put("relationshipFilter", "<");
		expandConfig.put("labelFilter", ">NDPrivilege|>HPrivilege");

		//Source specification via uuid
		if(config.containsKey("sourceId")){
			expandConfig.put("terminatorNodes",config.get("sourceId"));
//			log.info("AllMultilayerPathsToTarget(ids) source: "+config.get("sourceId").toString() + " target: "+start.toString());

		}
		//Source specification via names (we must also follow edges to nodes with same device but increasing privileges)
		else if(config.containsKey("source")){
			expandConfig.put("endNodes",config.get("source"));
//			log.info("AllMultilayerPathsToTarget source (hostnames): "+config.get("source").toString() + " target: "+start.toString());
		}

		return this.expandConfigPrivate(start, expandConfig).map(PathResult::new);

	}

	// APOC 3.5.0.4
	private Stream<Path> explorePathPrivate(Iterable<Node> startNodes, String pathFilter, String labelFilter, long minLevel, long maxLevel, boolean bfs, MyMultilayerUniqueness uniqueness, boolean filterStartNode, long limit, EnumMap<NodeFilter, List<Node>> nodeFilter, String sequence, boolean beginSequenceAtStart) {
		Traverser traverser = traverse(this.db.traversalDescription(), startNodes, pathFilter, labelFilter, minLevel, maxLevel, uniqueness, bfs, filterStartNode, nodeFilter, sequence, beginSequenceAtStart);
		return limit == -1L ? traverser.stream() : traverser.stream().limit(limit);
	}
	// APOC 3.5.0.4
	private List<Node> startToNodes(Object start) throws Exception {
		if (start == null) {
			return Collections.emptyList();
		} else if (start instanceof Node) {
			return Collections.singletonList((Node)start);
		} else if (start instanceof Number) {
			return Collections.singletonList(this.db.getNodeById(((Number)start).longValue()));
		} else {
			if (start instanceof List) {
				List list = (List)start;
				if (list.isEmpty()) {
					return Collections.emptyList();
				}

				Object first = list.get(0);
				if (first instanceof Node) {
					return list;
				}

				if (first instanceof Number) {
					List<Node> nodes = new ArrayList();
					Iterator var5 = list.iterator();

					while(var5.hasNext()) {
						Number n = (Number)var5.next();
						nodes.add(this.db.getNodeById(n.longValue()));
					}

					return nodes;
				}
			}

			throw new Exception("Unsupported data type for start parameter a Node or an Identifier (long) of a Node must be given!");
		}
	}
	// APOC 3.5.0.4
	private MyMultilayerUniqueness getUniqueness(String uniqueness) {
		MyMultilayerUniqueness[] var2 = MyMultilayerUniqueness.values();
		int var3 = var2.length;

		for(int var4 = 0; var4 < var3; ++var4) {
			MyMultilayerUniqueness u = var2[var4];
			if (u.name().equalsIgnoreCase(uniqueness)) {
				return u;
			}
		}

		return UNIQUENESS;
	}

	// APOC 3.5.0.4
	private Stream<Path> expandConfigPrivate(@Name("start") Object start, @Name("config") Map<String, Object> config) throws Exception {
		List<Node> nodes = this.startToNodes(start);
		String uniqueness = (String) config.getOrDefault("uniqueness", UNIQUENESS.name());
		log.info("ExpandConfig using UNIQUENESS: " + uniqueness);
		String relationshipFilter = (String) config.getOrDefault("relationshipFilter", null);
		String labelFilter = (String) config.getOrDefault("labelFilter", null);
		long minLevel = Util.toLong(config.getOrDefault("minLevel", "-1"));
		long maxLevel = Util.toLong(config.getOrDefault("maxLevel", "-1"));
		boolean bfs = Util.toBoolean(config.getOrDefault("bfs", true));
		boolean filterStartNode = Util.toBoolean(config.getOrDefault("filterStartNode", false));
		long limit = Util.toLong(config.getOrDefault("limit", "-1"));
		boolean optional = Util.toBoolean(config.getOrDefault("optional", false));
		String sequence = (String) config.getOrDefault("sequence", null);
		boolean beginSequenceAtStart = Util.toBoolean(config.getOrDefault("beginSequenceAtStart", true));
		List<Node> endNodes = this.startToNodes(config.get("endNodes"));
		List<Node> terminatorNodes = this.startToNodes(config.get("terminatorNodes"));
		List<Node> whitelistNodes = this.startToNodes(config.get("whitelistNodes"));
		List<Node> blacklistNodes = this.startToNodes(config.get("blacklistNodes"));
		EnumMap<NodeFilter, List<Node>> nodeFilter = new EnumMap(NodeFilter.class);
		if (endNodes != null && !endNodes.isEmpty()) {
			nodeFilter.put(NodeFilter.END_NODES, endNodes);
		}

		if (terminatorNodes != null && !terminatorNodes.isEmpty()) {
			nodeFilter.put(NodeFilter.TERMINATOR_NODES, terminatorNodes);
		}

		if (whitelistNodes != null && !whitelistNodes.isEmpty()) {
			nodeFilter.put(NodeFilter.WHITELIST_NODES, whitelistNodes);
		}

		if (blacklistNodes != null && !blacklistNodes.isEmpty()) {
			nodeFilter.put(NodeFilter.BLACKLIST_NODES, blacklistNodes);
		}

		Stream<Path> results = this.explorePathPrivate(nodes, relationshipFilter, labelFilter, minLevel, maxLevel, bfs, this.getUniqueness(uniqueness), filterStartNode, limit, nodeFilter, sequence, beginSequenceAtStart);

		return optional ? this.optionalStream(results) : results;

	}

	// APOC 3.5.0.4
	private Stream<Path> optionalStream(Stream<Path> stream) {
		Iterator<Path> itr = stream.iterator();
		Stream optionalStream;
		if (itr.hasNext()) {
			optionalStream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(itr, 0), false);
		} else {
			List<Path> listOfNull = new ArrayList();
			listOfNull.add(null);// was (Object)
			optionalStream = listOfNull.stream();
		}

		return optionalStream;
	}

	// APOC 3.5.0.4
	enum NodeFilter {
		WHITELIST_NODES,
		BLACKLIST_NODES,
		END_NODES,
		TERMINATOR_NODES;

		NodeFilter() {
		}
	}
}
