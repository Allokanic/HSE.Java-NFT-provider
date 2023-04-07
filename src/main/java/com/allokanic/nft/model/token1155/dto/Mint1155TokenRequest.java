package com.allokanic.nft.model.token1155.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mint1155TokenRequest {
    private String contractAddress;
    private String toAddress;
    private BigInteger amount;
    private String privateKey;
    private BigInteger tokenId;
}
