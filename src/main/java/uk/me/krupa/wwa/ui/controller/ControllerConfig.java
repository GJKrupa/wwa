package uk.me.krupa.wwa.ui.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

@Configuration
@ComponentScan(basePackages = "uk.me.krupa.wwa.ui.controller")
public class ControllerConfig {

    @Bean
    public ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver() {
        return new ExceptionHandlerExceptionResolver();
    }

}
