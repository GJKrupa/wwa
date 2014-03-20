package uk.me.krupa.wwa.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import uk.me.krupa.wwa.service.ServiceConfig;
import uk.me.krupa.wwa.ui.controller.ControllerConfig;
import uk.me.krupa.wwa.ui.security.SecurityConfig;
import uk.me.krupa.wwa.ui.security.SpringSocialConfig;

@Configuration
@Import({
        ControllerConfig.class,
        SecurityConfig.class,
        ServiceConfig.class,
        SpringSocialConfig.class
})
@PropertySource("classpath:application.properties")
public class MasterConfig {

    @Bean
    public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertyPlaceholderConfigurer();
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("/messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

}
