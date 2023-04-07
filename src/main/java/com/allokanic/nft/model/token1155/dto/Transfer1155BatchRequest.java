package com.allokanic.nft.model.token1155.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transfer1155BatchRequest {
    String addressFrom;
    String addressTo;
    List<BigInteger> tokenIds;
    String contractAddress;
    List<BigInteger> amounts;
    String privateKey;
}
