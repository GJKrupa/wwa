package uk.me.krupa.wwa.ui.jsf;

import org.springframework.mobile.device.Device;

import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.application.ViewResource;
import javax.faces.context.FacesContext;

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

        String prefixedPath;
        String desktopPath = "/WEB-INF/pages/desktop" + resourceName;
        Device device = MobileDeviceDetectorFilter.getDevice();
        if (device == null || device.isNormal()) {
            prefixedPath = desktopPath;
        } else if (device.isMobile()) {
            prefixedPath = "/WEB-INF/pages/mobile" + resourceName;
        } else {
            prefixedPath = "/WEB-INF/pages/tablet" + resourceName;
        }

        ViewResource thingy = wrapped.createViewResource(context, prefixedPath);
        if (thingy == null) {
            thingy = wrapped.createViewResource(context, desktopPath);
        }
        if (thingy == null) {
            thingy = wrapped.createViewResource(context, resourceName);
        }

        return thingy;
    }

    @Override
    public ResourceHandler getWrapped() {
        return wrapped;
    }
}
