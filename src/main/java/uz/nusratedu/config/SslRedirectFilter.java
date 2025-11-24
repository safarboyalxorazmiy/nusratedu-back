package uz.nusratedu.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Filter to detect and handle SSL/TLS requests on non-SSL ports.
 * This prevents the "Invalid character found in method name" errors
 * when HTTPS requests are sent to HTTP-only ports.
 */
@Slf4j
@Component
@Order(1)  // Execute before Spring Security filters
public class SslRedirectFilter implements Filter {

    @Value("${server.port:8080}")
    private int serverPort;

    @Value("${server.ssl.enabled:false}")
    private boolean sslEnabled;

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpRequest && response instanceof HttpServletResponse httpResponse) {

            // Check if this looks like an SSL handshake on a non-SSL port
            String method = httpRequest.getMethod();
            String protocol = httpRequest.getHeader("X-Forwarded-Proto");

            // In dev mode, if we detect HTTPS attempt on HTTP port
            if (!sslEnabled && serverPort != 443) {
                // Check for common indicators of HTTPS on HTTP
                if (protocol != null && "https".equalsIgnoreCase(protocol)) {
                    log.warn("HTTPS request received on HTTP port {}. Redirecting to HTTP.", serverPort);

                    // Build the correct HTTP URL
                    String correctUrl = buildHttpUrl(httpRequest);
                    httpResponse.sendRedirect(correctUrl);
                    return;
                }

                // Check if request looks malformed (potential SSL handshake)
                String requestUri = httpRequest.getRequestURI();
                if (requestUri != null && requestUri.contains("\u0016\u0003")) {
                    log.warn("SSL handshake detected on non-SSL port. Returning error response.");
                    httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    httpResponse.setContentType("text/plain");
                    httpResponse.getWriter().write("This port does not support HTTPS. Please use HTTP instead.");
                    return;
                }
            }

            // In production, enforce HTTPS
            if ("prod".equalsIgnoreCase(activeProfile) && sslEnabled) {
                String proto = httpRequest.getHeader("X-Forwarded-Proto");
                if (proto == null) {
                    proto = httpRequest.getScheme();
                }

                if (!"https".equalsIgnoreCase(proto)) {
                    log.info("HTTP request in production mode. Redirecting to HTTPS.");
                    String httpsUrl = buildHttpsUrl(httpRequest);
                    httpResponse.sendRedirect(httpsUrl);
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    private String buildHttpUrl(HttpServletRequest request) {
        StringBuilder url = new StringBuilder("http://");
        url.append(request.getServerName());
        if (serverPort != 80) {
            url.append(":").append(serverPort);
        }
        url.append(request.getRequestURI());
        if (request.getQueryString() != null) {
            url.append("?").append(request.getQueryString());
        }
        return url.toString();
    }

    private String buildHttpsUrl(HttpServletRequest request) {
        StringBuilder url = new StringBuilder("https://");
        url.append(request.getServerName());
        // Don't add port for standard HTTPS
        url.append(request.getRequestURI());
        if (request.getQueryString() != null) {
            url.append("?").append(request.getQueryString());
        }
        return url.toString();
    }
}