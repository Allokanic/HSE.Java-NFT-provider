package com.allokanic.nft.model.token1155.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mint1155BatchTokenRequest {
    private String contractAddress;
    private String toAddress;
    private List<BigInteger> amounts;
    private String privateKey;
    private List<BigInteger> tokenIds;
}
