package uk.me.krupa.wwa.ui.security;

import org.socialsignin.springsocial.security.signin.SpringSocialSecurityAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.social.UserIdSource;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.security.SpringSocialConfigurer;
import uk.me.krupa.wwa.service.user.UserService;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String REMEMBER_ME_KEY = "THEWINDANDTHERAIN";
    private static final int REMEMBER_ME_DURATION = 60 * 60 * 24 * 30;
    private static final String REMEMBER_ME_COOKIE_NAME = "GDJEIEMAJD";

    @Autowired
    private UserService userService;

    @Override
    @SuppressWarnings("unchecked")
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .disable()
                .rememberMe()
                    .key(REMEMBER_ME_KEY)
                    .tokenValiditySeconds(REMEMBER_ME_DURATION)
                    .rememberMeServices(rememberMeServices())
                .and()
                .formLogin()
                    .loginPage("/login.xhtml")
                    .loginProcessingUrl("/login/authenticate")
                    .defaultSuccessUrl("/index.xhtml", true)
                    .failureUrl("/login.xhtml?error=bad_credentials")
                    .permitAll()
                .and()
                .addFilterBefore(springSocialSecurityAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout()
                    .deleteCookies("JSESSIONID", REMEMBER_ME_COOKIE_NAME)
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login.xhtml")
                .and()
                .authorizeRequests()
                    //Anyone can access the urls
                    .antMatchers(
                            "/auth/**",
                            "/login",
                            "/login/authenticate",
                            "/login.xhtml",
                            "/signup.xhtml",
                            "/connect/**",
                            "/signin/**",
                            "/signup/**",
                            "/javax.faces.resource/**",
                            "/user/register/**"
                    ).permitAll()
                    .anyRequest().authenticated()
                .and()
                    .apply((SecurityConfigurer) (new SpringSocialConfigurer()));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, UserDetailsService userDetailsService) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public LoginUrlAuthenticationEntryPoint springSocialSecurityEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint("/auth");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public UserIdSource userIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Bean
    public SpringSocialSecurityAuthenticationFilter springSocialSecurityAuthenticationFilter() {
        SpringSocialSecurityAuthenticationFilter filter = new SpringSocialSecurityAuthenticationFilter();
        filter.setSessionAuthenticationStrategy(new ChangeSessionIdAuthenticationStrategy());
        filter.setRememberMeServices(rememberMeServices());
        return filter;
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(REMEMBER_ME_KEY, userService);
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setTokenValiditySeconds(REMEMBER_ME_DURATION);
        rememberMeServices.setCookieName(REMEMBER_ME_COOKIE_NAME);
        return rememberMeServices;
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        AuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        return new ProviderManager(Collections.singletonList(provider));
    }
}
