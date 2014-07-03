package uk.me.krupa.wwa.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.neo4j.aspects.support.node.Neo4jNodeBacking;
import org.springframework.data.neo4j.aspects.support.relationship.Neo4jRelationshipBacking;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import uk.me.krupa.wwa.entity.EntityConfig;
import uk.me.krupa.wwa.fgs.FgsConfig;
import uk.me.krupa.wwa.lifecycle.LifecycleConfig;
import uk.me.krupa.wwa.repository.RepositoryConfig;
import uk.me.krupa.wwa.service.ServiceConfig;
import uk.me.krupa.wwa.ui.controller.ControllerConfig;
import uk.me.krupa.wwa.ui.security.SecurityConfig;
import uk.me.krupa.wwa.ui.security.SpringSocialConfig;
import uk.me.krupa.wwa.ui.sockets.WebSocketConfig;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.File;

@Configuration
@Import({
        EntityConfig.class,
        RepositoryConfig.class,
        ControllerConfig.class,
        SecurityConfig.class,
        FgsConfig.class,
        ServiceConfig.class,
        SpringSocialConfig.class,
        WebSocketConfig.class,
        LifecycleConfig.class
})
@PropertySource("file:${java:global/wwa/config/dir}/wwa.properties")
@EnableAspectJAutoProxy
@EnableLoadTimeWeaving(aspectjWeaving = EnableLoadTimeWeaving.AspectJWeaving.ENABLED)
@EnableWebMvc
public class MasterConfig {

    @Bean
    public Neo4jNodeBacking neo4jNodeBacking() {
        return new Neo4jNodeBacking();
    }

    @Bean
    public Neo4jRelationshipBacking neo4jRelationshipBacking() {
        return new Neo4jRelationshipBacking();
    }

    @Bean(name = "applicationProperties")
    public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() throws NamingException {
        PropertyPlaceholderConfigurer placeholderConfigurer = new PropertyPlaceholderConfigurer();
        final String configDir = (String) new InitialContext().lookup("java:global/wwa/config/dir");
        placeholderConfigurer.setLocations(new Resource[] {
                new FileSystemResource(new File(new File(configDir), "wwa.properties"))
        });
        return placeholderConfigurer;
    }

    @Bean(name = "springMessages")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("/uk/me/krupa/wwa/ui/messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

}
