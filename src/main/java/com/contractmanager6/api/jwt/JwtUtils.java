package com.contractmanager6.api.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.contractmanager6.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {
    public static final String AUTHORIZATION = "Authorization";

    private final String JWT_SECRET = "suhong";
    private final Long JWT_EXPIRATION = 1000L * 60L * 30L; // 5분

    private final Algorithm ALGORITHM = Algorithm.HMAC256(JWT_SECRET);

    public String getJwtFromRequestHeader(HttpServletRequest request) {
        String jwt = request.getHeader(AUTHORIZATION);
        log.info("jwt value = {}", jwt);
        if (jwt == null || jwt.isBlank()) {
            return null;
        }
        return jwt;
    }

    public void generateJwtResponse(User user, HttpServletResponse response) {
        String jwt = generateTokenFromLoginId(user.getLoginId());
        response.addHeader(AUTHORIZATION, jwt);
    }

    public String getLoginIdFromJwtToken(String jwt) throws JWTVerificationException {
        JWTVerifier jwtVerifier = JWT.require(ALGORITHM).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
        return decodedJWT.getSubject();
//        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String jwt) {
        try {
            JWTVerifier jwtVerifier = JWT.require(ALGORITHM).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
        } catch (JWTVerificationException e) {
            log.info(e.getMessage());
            return false;
        }
        return true;
    }

    public String generateTokenFromLoginId(String username) {
        long currentTimeMillis = System.currentTimeMillis();
        long expireTimeMillis = currentTimeMillis + JWT_EXPIRATION;

        return JWT.create()
                .withExpiresAt(new Date(expireTimeMillis))
                .withSubject(username)
                .sign(ALGORITHM);
    }
}
