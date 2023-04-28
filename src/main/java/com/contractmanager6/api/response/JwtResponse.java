package com.contractmanager6.api.response;

import lombok.Data;

@Data
public class JwtResponse {
    private String message;
    private String authentication;
    private UserResponse user;
}
