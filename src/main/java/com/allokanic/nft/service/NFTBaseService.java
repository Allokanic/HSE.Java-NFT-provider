package com.allokanic.nft.service;

import com.allokanic.nft.model.DeployContractRequest;
import com.allokanic.nft.model.GetURIRequest;
import com.allokanic.nft.model.GetURIResponse;
import com.allokanic.nft.model.OwnerReponse;
import com.allokanic.nft.model.OwnerRequest;

public interface NFTBaseService {
    String deploy(DeployContractRequest request);
    GetURIResponse getUri(GetURIRequest request);
    OwnerReponse getOwnerOf(OwnerRequest request);
}
