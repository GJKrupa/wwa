package uk.me.krupa.wwa.repository;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.shell.ShellSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.aspects.config.Neo4jAspectConfiguration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;

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
        return graphDatabaseService;
    }

//    @Bean(name = "wrappingNeoServerBootstrapper")
//    @Autowired
//    public WrappingNeoServerBootstrapper wrappingNeoServerBootstrapper(GraphDatabaseService graphDatabaseService) {
//        WrappingNeoServerBootstrapper wrappingNeoServerBootstrapper = new WrappingNeoServerBootstrapper(
//                (GraphDatabaseAPI) graphDatabaseService);
//        wrappingNeoServerBootstrapper.start();
//        return wrappingNeoServerBootstrapper;
//    }

}
