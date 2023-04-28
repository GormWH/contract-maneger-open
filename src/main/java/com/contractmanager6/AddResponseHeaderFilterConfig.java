package com.contractmanager6;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class AddResponseHeaderFilterConfig implements Filter {

    private List<String> allowOrigins;

    public AddResponseHeaderFilterConfig() {
        allowOrigins = new ArrayList<>();
        allowOrigins.add("http://localhost:3000");
        allowOrigins.add("https://contract-manager.vercel.app");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String origin = httpServletRequest.getHeader("Origin");
        log.info("origin = {}", origin);
        if (allowOrigins.contains(origin)) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.addHeader("access-control-allow-origin", origin);
            httpServletResponse.addHeader("access-control-allow-credentials", "true");
            httpServletResponse.addHeader("access-control-allow-headers", "content-type, authorization");
            httpServletResponse.addHeader("access-control-allow-methods", "GET, POST, PUT, DELETE, PATCH");
            chain.doFilter(request, response);
        }

    }

}
