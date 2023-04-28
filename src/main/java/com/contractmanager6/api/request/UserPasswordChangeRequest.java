package com.contractmanager6.api.request;

import lombok.Data;

@Data
public class UserPasswordChangeRequest {
    private String id;
    private String password;
}
