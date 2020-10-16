package org.panacea.drmp.age.config;

import lombok.extern.slf4j.Slf4j;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.internal.kernel.api.exceptions.KernelException;
import org.neo4j.kernel.configuration.BoltConnector;
import org.neo4j.kernel.impl.proc.Procedures;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.panacea.drmp.age.procedures.AllPathsToTargetMultilayer;
import org.panacea.drmp.age.procedures.AllPathsToTargetNetworkOnly;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static org.neo4j.graphdb.DependencyResolver.SelectionStrategy.FIRST;

@Configuration
@ComponentScan
@EnableNeo4jRepositories(basePackages = "org.panacea.drmp.age.repository")
@EnableTransactionManagement
@Slf4j
public class MyConfiguration {

	@Bean
	public SessionFactory sessionFactory() {
		// with domain entity base package(s)
		EmbeddedDriver driver = new EmbeddedDriver(this.graphDatabaseService(), this.configuration());
		return new SessionFactory(driver, "org.panacea.drmp.age.domain");
	}

	public org.neo4j.ogm.config.Configuration configuration() {


		//SessionFactory sessionFactory = new SessionFactory(driver, "com.mycompany.app.domainclasses");
		org.neo4j.ogm.config.Configuration configuration = new org.neo4j.ogm.config.Configuration.Builder().build();



		return configuration;
	}

	@Bean
	GraphDatabaseService graphDatabaseService() {
		/*
		FIXME: use permanent datastore
		GraphDatabaseService databaseService = new GraphDatabaseFactory()
				.newEmbeddedDatabaseBuilder(new File("/var/tmp/neo4j.db"))
				.newGraphDatabase();


		 */

		BoltConnector connector = new BoltConnector("localhost:7688");

		//GraphDatabaseSettings.BoltConnector connector = new GraphDatabaseSettings.BoltConnector("(bolt-connector-integration-tests)")
		//GraphDatabaseService databaseService = new TestGraphDatabaseFactory().newImpermanentDatabase();
		GraphDatabaseService databaseService = new TestGraphDatabaseFactory().newImpermanentDatabaseBuilder()
				.setConfig(connector.type, "BOLT" )
				.setConfig(connector.enabled, "true" )
				.setConfig(connector.listen_address, "localhost:7688" )
				.setConfig(GraphDatabaseSettings.auth_enabled, "false").newGraphDatabase();



		System.out.println(databaseService.toString());

		Procedures proceduresService = ((GraphDatabaseAPI) databaseService).getDependencyResolver().resolveDependency(Procedures.class, FIRST);
		try {

			proceduresService.registerProcedure(AllPathsToTargetNetworkOnly.class, true);
			proceduresService.registerProcedure(AllPathsToTargetMultilayer.class, true);
			log.info("[Configuration - Procedures registration: OK] ");
		}catch(KernelException e){
			log.info("[Configuration - Procedures registration failure] "+e.toString());
		}

		return databaseService;
	}

	@Bean
	public Neo4jTransactionManager transactionManager() {
		return new Neo4jTransactionManager(this.sessionFactory());
	}
}
