package uk.me.krupa.wwa.ui.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller("config")
@Scope("singleton")
@PropertySource("file:${java:global/wwa/config/dir}/wwa.properties")
public class StaticConfigController {

    @Value("${analytics.token:#{null}}")
    private String analyticsToken;

    public boolean isAnalyticsEnabled() {
        return analyticsToken != null;
    }

    public String getAnalyticsToken() {
        return analyticsToken;
    }
}
