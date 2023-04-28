package com.contractmanager6.api.request;

import lombok.Data;

@Data
public class ContractRequest {
    private String contractor; // loginId of User
    private String company; // name of contracted company
    private String timestamp;
}
