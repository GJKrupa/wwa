package uk.me.krupa.wwa.service;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by krupagj on 19/03/2014.
 */
@Configuration
@ComponentScan(basePackages = "uk.me.krupa.wwa.service")
@EnableTransactionManagement(mode = AdviceMode.PROXY)
public class ServiceConfig {

}
