package com.allokanic.nft.model.token721.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Owner {
    String address;
    String tokenId;
}
