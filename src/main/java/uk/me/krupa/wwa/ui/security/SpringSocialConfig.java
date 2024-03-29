package uk.me.krupa.wwa.ui.security;

import org.socialsignin.springsocial.security.signin.SpringSocialSecurityAuthenticationFactory;
import org.socialsignin.springsocial.security.signin.SpringSocialSecuritySignInService;
import org.socialsignin.springsocial.security.userauthorities.SimpleUserAuthoritiesService;
import org.socialsignin.springsocial.security.userauthorities.UserAuthoritiesService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableSocial
@EnableWebMvc
@ComponentScan(basePackages = {
        "net.petrikainulainen.spring.social.signinmvc.common.controller",
        "net.petrikainulainen.spring.social.signinmvc.security.controller",
        "net.petrikainulainen.spring.social.signinmvc.user.controller"
})
public class SpringSocialConfig implements SocialConfigurer {
    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer config, Environment env) {
        config.addConnectionFactory(new TwitterConnectionFactory(
                env.getProperty("twitter.consumer.key"),
                env.getProperty("twitter.consumer.secret")
        ));
        config.addConnectionFactory(new FacebookConnectionFactory(
                env.getProperty("facebook.app.id"),
                env.getProperty("facebook.app.secret")
        ));
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(final ConnectionFactoryLocator connectionFactoryLocator) {
        InMemoryUsersConnectionRepository repository = new InMemoryUsersConnectionRepository(connectionFactoryLocator);
        repository.setConnectionSignUp(socialConnectionSignup());
        return repository;
    }

    @Bean
    public SocialConnectionSignup socialConnectionSignup() {
        return new SocialConnectionSignup();
    }

    @Bean
    public SignInAdapter signInAdapter() {
        return new SpringSocialSecuritySignInService();
    }

    @Bean
    public ProviderSignInController providerSignInController(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository connectionRepository, SignInAdapter signinAdapter) {
        ProviderSignInController controller = new ProviderSignInController(connectionFactoryLocator, connectionRepository, signinAdapter);
        controller.setSignInUrl("/signin");
        controller.setPostSignInUrl("/authenticate");
        return controller;
    }

    @Bean
    public SpringSocialSecurityAuthenticationFactory springSocialSecurityAuthenticationFactory() {
        SpringSocialSecurityAuthenticationFactory factory = new SpringSocialSecurityAuthenticationFactory();
        return factory;
    }

    @Bean
    public UserAuthoritiesService userAuthoritiesService() {
        return new SimpleUserAuthoritiesService();
    }
}
