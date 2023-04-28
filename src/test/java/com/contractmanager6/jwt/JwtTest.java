package com.contractmanager6.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class JwtTest {

    private static final String SECRET_KEY = "suhong";

    @Test
    void testJwtGeneration() {
        // requirements
        long currentTimeMillis = System.currentTimeMillis();
        long durationMillis = 1000 * 60 * 5; // jwt duration: 5 minutes
        SignatureAlgorithm algorithm = SignatureAlgorithm.HS256; // hashing algorithm: hmac sha 256
        String subject = "a string I want to convert to jwt";


        Date now = new Date(currentTimeMillis);
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, algorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setSubject(subject)
                .signWith(algorithm, signingKey);

        //if it has been specified, let's add the expiration
        if (durationMillis > 0) {
            long expMillis = currentTimeMillis + durationMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        String result = builder.compact();
        System.out.println(result);

        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(result).getBody();
        System.out.println(claims.getSubject());
    }

    @Test
    void testJwtWithJavaJwt2() {
        String token = "";
        try {
            Date expireTime = new Date();
            expireTime.setTime(expireTime.getTime() + 600000l);

            Algorithm algorithm = Algorithm.HMAC256("secret");
            token = JWT.create()
                    .withIssuer("auth0")
                    .withExpiresAt(expireTime)
                    .withSubject("A random string")
                    .sign(algorithm);

        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
            Assertions.fail("Couldn't convert Claims");
        }

        System.out.println(token);

//        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhdXRoMCJ9.izVguZPRsBQ5Rqw6dhMvcIwy8_9lQnrO3vpxGwPCuzs";
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
            Assertions.fail("Invalid jwt");
        }
    }
}
