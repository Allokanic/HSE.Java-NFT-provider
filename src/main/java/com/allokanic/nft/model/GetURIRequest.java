package com.allokanic.nft.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class GetURIRequest {
    String contractAddress;
    BigInteger tokenId;
}
