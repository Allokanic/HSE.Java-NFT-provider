package com.allokanic.nft.model.token1155.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTokenRequest {
    private String contractAddress;
    private String toAddress;
    private BigInteger initialAmount;
    private String uri;
    private BigInteger tokenId;
    private String privateKey;
}
