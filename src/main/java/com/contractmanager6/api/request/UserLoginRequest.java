package com.contractmanager6.api.request;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String id;
    private String password;
}
