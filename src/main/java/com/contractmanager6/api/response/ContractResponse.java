package com.contractmanager6.api.response;

import lombok.Data;

@Data
public class ContractResponse {
    private Long id;
    private UserName contractor; // loginId of User
    private String company; // name of contracted company
    private String timestamp;
}

