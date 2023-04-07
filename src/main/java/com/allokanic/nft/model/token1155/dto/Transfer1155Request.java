package com.allokanic.nft.model.token1155.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transfer1155Request {
    String addressFrom;
    String addressTo;
    BigInteger tokenId;
    String contractAddress;
    BigInteger amount;
    String privateKey;
}
