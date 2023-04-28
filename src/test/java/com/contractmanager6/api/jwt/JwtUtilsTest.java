package com.contractmanager6.api.jwt;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private final JwtUtils jwtUtils = new JwtUtils();

    @Test
    void getJwtFromRequestHeader() {
    }

    @Test
    void generateJwtResponse() {
    }

    @Test
    void getLoginIdFromJwtToken() {
    }

    @Test
    void validateJwtToken() {
        System.out.println(jwtUtils.validateJwtToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTY2NDM1MjMwNH0.pUy6un4sMgr7XYT-NEhqLT6l5fxrei9CV6n8QwogXZU"));
    }

    @Test
    void generateTokenFromLoginId() {
    }

    @Test
    void overallTest() {
        String token = jwtUtils.generateTokenFromLoginId("4kidsp");
        System.out.println(token);
        Assertions.assertThat(jwtUtils.validateJwtToken(token)).isTrue();
    }
}