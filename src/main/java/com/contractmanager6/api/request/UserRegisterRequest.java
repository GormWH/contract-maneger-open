package com.contractmanager6.api.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserRegisterRequest {
    @NotBlank
    private String id;
    @NotBlank
    private String password;
    private Boolean isAdmin;
}
