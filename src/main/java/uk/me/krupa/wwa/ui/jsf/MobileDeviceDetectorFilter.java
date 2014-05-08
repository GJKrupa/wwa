package uk.me.krupa.wwa.ui.jsf;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by krupagj on 07/05/2014.
 */
public class MobileDeviceDetectorFilter extends OncePerRequestFilter {

    private static ThreadLocal<Device> device = new ThreadLocal<>();

    private final DeviceResolver deviceResolver = new LiteDeviceResolver();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getSession(true).getAttribute("mobile") != null) {
            device.set(new MobeDevice());
        } else {
            Device detectedDevice = deviceResolver.resolveDevice(request);
            this.device.set(detectedDevice);
        }
        filterChain.doFilter(request, response);
    }

    public static Device getDevice() {
        return device.get();
    }

    class MobeDevice implements Device {

        @Override
        public boolean isNormal() {
            return false;
        }

        @Override
        public boolean isMobile() {
            return true;
        }

        @Override
        public boolean isTablet() {
            return false;
        }
    }
}
