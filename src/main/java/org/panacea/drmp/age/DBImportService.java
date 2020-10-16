package org.panacea.drmp.age;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.panacea.drmp.age.domain.graph.*;
import org.panacea.drmp.age.domain.rest.human.HExploitRepr;
import org.panacea.drmp.age.domain.rest.human.HumanLayerAttackGraphRepr;
import org.panacea.drmp.age.domain.rest.human.HumanVulnerability;
import org.panacea.drmp.age.domain.rest.inter.InterLayerAttackGraphRepr;
import org.panacea.drmp.age.domain.rest.inter.InterLayerEdgeRepr;
import org.panacea.drmp.age.domain.rest.network.NExploitRepr;
import org.panacea.drmp.age.domain.rest.network.NetworkLayerAttackGraphRepr;
import org.panacea.drmp.age.domain.rest.network.NetworkVulnerability;
import org.panacea.drmp.age.repository.human.HPrivilegeRepository;
import org.panacea.drmp.age.repository.inter.ACredentialRepository;
import org.panacea.drmp.age.repository.network.NDPrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Slf4j
@Component

public class DBImportService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NDPrivilegeRepository ndPrivilegeRepository;

    @Autowired
    private HPrivilegeRepository hPrivilegeRepository;

    @Autowired
	private ACredentialRepository aCredentialRepository;

	@Value("${NetworkLayerAttackGraphRepr.endpoint}")
	private String networkLayerAttackGraphReprURL;


	@Value("${NetworkLayerAttackGraphRepr.fn}")
	private String networkLayerAttackGraphReprFn;



	@Value("${HumanLayerAttackGraphRepr.endpoint}")
	private String humanLayerAttackGraphReprURL;


	@Value("${HumanLayerAttackGraphRepr.fn}")
	private String humanLayerAttackGraphReprFn;


	@Value("${InterLayerAttackGraphRepr.endpoint}")
	private String interLayerAttackGraphReprURL;


	@Value("${InterLayerAttackGraphRepr.fn}")
	private String interLayerAttackGraphReprFn;


	public NetworkLayerAttackGraphRepr performNetworkLayerAttackGraphRequest(String snapshotId){
		log.info("[AGE] GET networkLayerAttackGraph from http://172.16.100.131:8096/aggregator/networkLayerAttackGraph");
		ResponseEntity<NetworkLayerAttackGraphRepr> responseEntity = restTemplate.exchange(
				networkLayerAttackGraphReprURL + '/' + snapshotId, //+'/'+networkLayerAttackGraphReprFn,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<NetworkLayerAttackGraphRepr>() {
				});

		NetworkLayerAttackGraphRepr networkLayerAttackGraphRepr = responseEntity.getBody();

		return networkLayerAttackGraphRepr;
	}

	public HumanLayerAttackGraphRepr performHumanLayerAttackGraphRequest(String snapshotId) {
		log.info("[AGE] GET humanLayerAttackGraph from http://172.16.100.131:8102/human/humanLayerAttackGraph");
		ResponseEntity<HumanLayerAttackGraphRepr> responseEntity = restTemplate.exchange(
				humanLayerAttackGraphReprURL + '/' + snapshotId, //+'/'+humanLayerAttackGraphReprFn,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<HumanLayerAttackGraphRepr>() {
				});

		HumanLayerAttackGraphRepr humanLayerAttackGraphRepr = responseEntity.getBody();

		return humanLayerAttackGraphRepr;
	}


	public InterLayerAttackGraphRepr performInterLayerAttackGraphRequest(String snapshotId) {
		log.info("[AGE] GET interLayerAttackGraph from http://172.16.100.131:8105/inte-layer/interLayerAttackGraph");
		ResponseEntity<InterLayerAttackGraphRepr> responseEntity = restTemplate.exchange(
				interLayerAttackGraphReprURL + '/' + snapshotId, //+'/'+interLayerAttackGraphReprFn,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<InterLayerAttackGraphRepr>() {
				});

		InterLayerAttackGraphRepr interLayerAttackGraphRepr = responseEntity.getBody();

		return interLayerAttackGraphRepr;
	}

	@Synchronized
	public void importAttackGraph(String snapshotId){
		//Delete any previously imported data
		ndPrivilegeRepository.deleteAll();
		hPrivilegeRepository.deleteAll();
		aCredentialRepository.deleteAll();

		//Import Network Layer
		NetworkLayerAttackGraphRepr network_layer = performNetworkLayerAttackGraphRequest(snapshotId);
		ndPrivilegeRepository.saveAll(network_layer.getNodes());

		NExploit ex;

		for(NExploitRepr exploit:network_layer.getEdges()) {

			NDPrivilege src = ndPrivilegeRepository.findByUuid(exploit.source);
			NDPrivilege dst = ndPrivilegeRepository.findByUuid(exploit.destination);

			ex = new NExploit(src,dst);

			// Full vulnerability info, we should extract CVE id only
			Set<NetworkVulnerability> vulns = exploit.getVulnerabilities();

			for(NetworkVulnerability v: vulns){
				ex.addVulnerability(v.CVE);
			}

			src.addNExploit(ex);
			ndPrivilegeRepository.save(src);
		}


		//Import Human Layer
		HumanLayerAttackGraphRepr human_layer = performHumanLayerAttackGraphRequest(snapshotId);
		hPrivilegeRepository.saveAll(human_layer.getNodes());

		HExploit hex;

		for(HExploitRepr exploit:human_layer.getEdges()) {

			HPrivilege src = hPrivilegeRepository.findByUuid(exploit.source);
			HPrivilege dst = hPrivilegeRepository.findByUuid(exploit.destination);

			hex = new HExploit(src,dst);

			// Full vulnerability info, we should extract id only
			Set<HumanVulnerability> vulns = exploit.getVulnerabilities();

			for(HumanVulnerability v: vulns){
				hex.addVulnerability(v.vulnId);
			}

			src.addHExploit(hex);
			hPrivilegeRepository.save(src);
		}

		//Import Inter Layer. FIXME: validate if logic

		InterLayerAttackGraphRepr inter_layer = performInterLayerAttackGraphRequest(snapshotId);

		aCredentialRepository.saveAll(inter_layer.getNodes());

		HAToCred hToC;

		for(InterLayerEdgeRepr haEdge:inter_layer.getHumanAccessEdges()) {
			//try to find humanPrivilege
			HPrivilege src = hPrivilegeRepository.findByUuid(haEdge.source);
			ACredential dst = aCredentialRepository.findByUuid(haEdge.destination);

			if(src != null){
				//this human node has been inserted by the human layer, then it makes sense to add edge to credential
				hToC = new HAToCred(src,dst);
				src.addHAToCred(hToC);
				hPrivilegeRepository.save(src);
			}

		}

		ANGivesPrivilege cToP;
		for(InterLayerEdgeRepr anEdge:inter_layer.getAccessNetworkEdges()) {

			ACredential src = aCredentialRepository.findByUuid(anEdge.source);
			NDPrivilege dst = ndPrivilegeRepository.findByUuid(anEdge.destination);

			if(dst != null){
				//this privilege on device exists in the attack graph, then it makes sense to add edge to privilege
				cToP = new ANGivesPrivilege(src,dst);
				src.addANGivesPrivilege(cToP);
				aCredentialRepository.save(src);
			}
		}


		log.info("[AGE] All layers correctly imported into GraphDB (Neo4J)");

	}
}
