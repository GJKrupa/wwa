package uk.me.krupa.wwa.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.neo4j.aspects.support.node.Neo4jNodeBacking;
import org.springframework.data.neo4j.aspects.support.relationship.Neo4jRelationshipBacking;
import uk.me.krupa.wwa.entity.EntityConfig;
import uk.me.krupa.wwa.lifecycle.LifecycleConfig;
import uk.me.krupa.wwa.repository.RepositoryConfig;
import uk.me.krupa.wwa.service.ServiceConfig;
import uk.me.krupa.wwa.ui.controller.ControllerConfig;
import uk.me.krupa.wwa.ui.security.SecurityConfig;
import uk.me.krupa.wwa.ui.security.SpringSocialConfig;

@Configuration
@Import({
        EntityConfig.class,
        RepositoryConfig.class,
        ControllerConfig.class,
        SecurityConfig.class,
        ServiceConfig.class,
        SpringSocialConfig.class,
        LifecycleConfig.class
})
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy
@EnableLoadTimeWeaving(aspectjWeaving = EnableLoadTimeWeaving.AspectJWeaving.ENABLED)
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
    public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer placeholderConfigurer = new PropertyPlaceholderConfigurer();
        placeholderConfigurer.setLocations(new Resource[] {
                new ClassPathResource("application.properties")
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
