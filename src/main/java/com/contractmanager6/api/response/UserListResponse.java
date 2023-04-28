package com.contractmanager6.api.response;

import lombok.Data;

import java.util.List;

@Data
public class UserListResponse {
    private List<UserResponse> users;
}
