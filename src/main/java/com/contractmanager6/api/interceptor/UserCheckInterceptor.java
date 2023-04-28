package com.contractmanager6.api.interceptor;

import com.contractmanager6.api.jwt.JwtUtils;
import com.contractmanager6.domain.user.User;
import com.contractmanager6.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class UserCheckInterceptor implements HandlerInterceptor {

    public static final String AUTHENTICATION = "Authentication";

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jwt = jwtUtils.getJwtFromRequestHeader(request);
        log.info("Authorization: {}", jwt);
        try {
            if (jwt == null) {
                log.info("토큰 정보 없음");
                return false;
            }
            if (jwtUtils.validateJwtToken(jwt)) {
                log.info("유효토큰");
                String loginId = jwtUtils.getLoginIdFromJwtToken(jwt);
                Optional<User> loginUserOptional = userRepository.findByLoginId(loginId);
                if (loginUserOptional.isEmpty()) {
                    log.info("유효토큰이지만 해당 유저가 없음");
                    throw new Exception("다시 로그인 해주세요."); // jwt는 유효한데 해당하는 유저가 없을
                } else {
                    log.info("유효토큰 + 유저 존재함!");
                    return true;
                }
            }
            log.info("토큰은 있는데 무효");
            return false;
        } catch (Exception e) {
            throw new Exception("인증 정보가 존재하지 않습니다.");
        } finally {
            log.info("preHandle: finally");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
