package uk.me.krupa.wwa.ui.jsf;

import org.springframework.mobile.device.Device;

import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.application.ViewResource;
import javax.faces.context.FacesContext;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by krupagj on 07/05/2014.
 */
public class MobileAwareResourceHandler extends ResourceHandlerWrapper {

    private ResourceHandler wrapped;

    public MobileAwareResourceHandler(ResourceHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ViewResource createViewResource(FacesContext context, String resourceName) {

        Deque<String> pathsToCheck = new LinkedList<>();

        pathsToCheck.push(resourceName);
        pathsToCheck.push("/WEB-INF/pages/desktop" + resourceName);

        Device device = MobileDeviceDetectorFilter.getDevice();
        if (device != null && device.isMobile()) {
            pathsToCheck.push("/WEB-INF/pages/mobile" + resourceName);
        } else if (device != null && device.isTablet()) {
            pathsToCheck.push("/WEB-INF/pages/mobile" + resourceName);
            pathsToCheck.push("/WEB-INF/pages/tablet" + resourceName);
        }

        ViewResource thingy = null;
        while (thingy == null && !pathsToCheck.isEmpty()) {
            thingy = wrapped.createViewResource(context, pathsToCheck.pop());
        }

        return thingy;
    }

    @Override
    public ResourceHandler getWrapped() {
        return wrapped;
    }
}
