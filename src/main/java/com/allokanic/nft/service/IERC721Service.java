package com.allokanic.nft.service;

import com.allokanic.nft.model.token721.dto.Mint721TokenRequest;
import com.allokanic.nft.model.token721.dto.Transfer721Request;

public interface IERC721Service {
    String mint(Mint721TokenRequest request);
    String transfer(Transfer721Request request);
}
