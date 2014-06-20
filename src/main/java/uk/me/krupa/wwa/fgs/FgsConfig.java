package uk.me.krupa.wwa.fgs;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Created by krupagj on 17/06/2014.
 */
@Configuration
@ComponentScan(basePackages = "uk.me.krupa.wwa.fgs")
public class FgsConfig {

    private static final String[] DOZER_MAPPING = {
            "/uk/me/krupa/wwa/fgs/dozerMapping.xml"
    };

    @Bean
    public Mapper mapper() {
        Mapper mapper = new DozerBeanMapper(Arrays.asList(DOZER_MAPPING));
        return mapper;
    }

}
