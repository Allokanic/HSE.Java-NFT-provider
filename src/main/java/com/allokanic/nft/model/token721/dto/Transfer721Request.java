package com.allokanic.nft.model.token721.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transfer721Request {
    String addressFrom;
    String addressTo;
    BigInteger tokenId;
    String contractAddress;
    String privateKey;
}
