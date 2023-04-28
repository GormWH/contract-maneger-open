package com.contractmanager6;

import com.contractmanager6.api.interceptor.AdminCheckInterceptor;
import com.contractmanager6.api.interceptor.UserCheckInterceptor;
import com.contractmanager6.api.jwt.JwtUtils;
import com.contractmanager6.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserCheckInterceptor(userRepository, jwtUtils))
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/user", "/user/login", "/user/logout", "/css/**", "/*.ico", "/error");

        registry.addInterceptor(new AdminCheckInterceptor(userRepository, jwtUtils))
                .order(2)
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods();
    }
}
