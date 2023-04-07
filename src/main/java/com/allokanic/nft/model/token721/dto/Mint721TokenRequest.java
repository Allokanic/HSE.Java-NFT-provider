package com.allokanic.nft.model.token721.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mint721TokenRequest {
    private String contractAddress;
    private String toAddress;
    private String uri;
}
