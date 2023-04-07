package com.allokanic.nft.service;

import com.allokanic.nft.model.CreateMintTransferResponse;
import com.allokanic.nft.model.token1155.dto.*;

public interface IERC1155Service {
    CreateMintTransferResponse createToken(CreateTokenRequest request);
    CreateMintTransferResponse mintToken(Mint1155TokenRequest request);
    CreateMintTransferResponse transferToken(Transfer1155Request request);
    CreateMintTransferResponse transferTokenBatch(Transfer1155BatchRequest request);
    CreateMintTransferResponse mintTokenBatch(Mint1155BatchTokenRequest request);
}
