package uk.me.krupa.wwa.lifecycle;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import uk.me.krupa.wwa.service.initialisation.InitialisationService;

/**
 * Listener that runs code when the application starts and stops.
 */
@Component
public class LifecycleListener implements ApplicationListener {

    @SuppressWarnings("deprecation")
    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ContextRefreshedEvent) {
            InitialisationService initialisationService = ((ContextRefreshedEvent) applicationEvent).getApplicationContext().getBean(InitialisationService.class);
            initialisationService.initialise();
        }
    }
}
