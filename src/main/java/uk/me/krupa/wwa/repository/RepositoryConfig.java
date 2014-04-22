package uk.me.krupa.wwa.repository;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.kernel.impl.transaction.SpringTransactionManager;
import org.neo4j.server.WrappingNeoServerBootstrapper;
import org.neo4j.shell.ShellSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.neo4j.aspects.config.Neo4jAspectConfiguration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.transaction.TransactionManager;

/**
 * Created by krupagj on 21/03/2014.
 */
@Configuration
@EnableNeo4jRepositories(basePackages = "uk.me.krupa.wwa.repository")
public class RepositoryConfig extends Neo4jAspectConfiguration {

    public RepositoryConfig() {
        setBasePackage("uk.me.krupa.wwa.entity");
    }

    @Bean(name = "graphDatabaseService")
    @Value("${database.location}")
    public GraphDatabaseService embeddedGraphDatabase(String location) {
        GraphDatabaseAPI graphDatabaseService = (GraphDatabaseAPI) new GraphDatabaseFactory()
                .newEmbeddedDatabaseBuilder(location)
                .setConfig(ShellSettings.remote_shell_enabled, "true")
                .newGraphDatabase();
        new WrappingNeoServerBootstrapper(graphDatabaseService).start();
        return graphDatabaseService;
    }

//    @Bean(name = "neo4jTemplate")
//    @Autowired
//    public Neo4jTemplate neo4jTemplate(GraphDatabaseService graphDatabaseService) {
//        return new Neo4jTemplate(graphDatabaseService);
//    }

}
