package com.allokanic.nft.service;

import com.allokanic.nft.model.*;
import com.allokanic.nft.model.token1155.dto.*;
import com.allokanic.nft.token.ERC1155Tradable;
import com.allokanic.nft.util.Loader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.TypeDecoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.EthFilter;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.allokanic.nft.message.Error.DEPLOY_ERROR;
import static com.allokanic.nft.message.Error.INCORRECT_ADDRESS;
import static com.allokanic.nft.message.Log.*;
import static com.allokanic.nft.message.Signature.TRANSFER_BATCH_SIGNATURE_ERC1155;
import static com.allokanic.nft.message.Signature.TRANSFER_SIGNATURE_ERC1155;
import static com.allokanic.nft.message.Success.*;

@Slf4j
@Service
public class ERC1155Service implements NFTBaseService, IERC1155Service {
    private final Set<String> contractAddresses = new HashSet<>();
    private final Loader loader;

    @Autowired
    public ERC1155Service(Web3j web3j, Loader loader) {
        this.loader = loader;

        web3j.ethLogFlowable(new EthFilter()).subscribe(event -> {
            List<String> topics = event.getTopics();
            if (topics.size() == 4 && topics.get(0).equals(TRANSFER_BATCH_SIGNATURE_ERC1155)) {
                List<BigInteger> ids = new LinkedList<>();
                List<BigInteger> amounts = new LinkedList<>();

                System.out.println(event.getData());
                String data = event.getData().substring(130);

                String[] usefulData = data.split("(?<=\\G.{64})");

                BigInteger arrSize = new BigInteger(usefulData[0], 16);
                arrSize = arrSize.add(BigInteger.ONE);
                for (BigInteger i = BigInteger.valueOf(1); i.compareTo(arrSize) < 0; i = i.add(BigInteger.ONE)) {
                    ids.add(new BigInteger(usefulData[i.intValue()], 16));
                    amounts.add(new BigInteger(usefulData[i.intValue() + arrSize.intValue()], 16));
                }

                Address from = TypeDecoder.decodeAddress(topics.get(2));
                Address to = TypeDecoder.decodeAddress(topics.get(3));
                if (from.equals(new Address(BigInteger.valueOf(0)))) {
                    log.info(MINT_BATCH_EVENT_ERC1155.formatted(
                            ids.toString(),
                            to,
                            event.getAddress(),
                            amounts.toString()));
                } else {
                    log.info(TRANSFER_BATCH_EVENT_ERC1155.formatted(
                            ids.toString(),
                            from,
                            to,
                            event.getAddress(),
                            amounts.toString()));
                }
            }
            else if (topics.size() == 4 && topics.get(0).equals(TRANSFER_SIGNATURE_ERC1155) && event.getData().length() == 130) {
                String data = event.getData().substring(2);

                BigInteger tokenId = new BigInteger(data.substring(0, 64), 16);
                BigInteger tokenAmount = new BigInteger(data.substring(64), 16);

                Address from = TypeDecoder.decodeAddress(topics.get(2));
                Address to = TypeDecoder.decodeAddress(topics.get(3));

                if (from.equals(new Address(BigInteger.valueOf(0)))) {
                    log.info(MINT_EVENT_ERC1155.formatted(
                            tokenId,
                            to,
                            event.getAddress(),
                            tokenAmount));
                } else {
                    log.info(TRANSFER_EVENT_ERC1155.formatted(
                            tokenId,
                            from,
                            to,
                            event.getAddress(),
                            tokenAmount));
                }
            }
        });
    }

    @Override
    public String deploy(DeployContractRequest request) {
        try {
            ERC1155Tradable token = loader.deploy1155(request);
            contractAddresses.add(token.getContractAddress());
            return token.getContractAddress();
        } catch (Exception e) {
            return DEPLOY_ERROR;
        }
    }

    @Override
    public GetURIResponse getUri(GetURIRequest request) {
        if (!contractAddresses.contains(request.getContractAddress())) {
            return new GetURIResponse(null, INCORRECT_ADDRESS);
        }
        ERC1155Tradable token = loader.loadToken1155(request.getContractAddress(), null);
        try {
            return new GetURIResponse(token.uri(request.getTokenId()).send(), null);
        } catch (Exception e) {
            return new GetURIResponse(null, "execution reverted: ERC721: invalid token ID");
        }
    }

    @Override
    public OwnerReponse getOwnerOf(OwnerRequest request) {
        if (!contractAddresses.contains(request.getContractAddress())) {
            return new OwnerReponse(INCORRECT_ADDRESS, null);
        }
        ERC1155Tradable token = loader.loadToken1155(request.getContractAddress(), null);
        try {
            return new OwnerReponse(null, token.creatorOf(new BigInteger(String.valueOf(request.getTokenId()))).send());
        } catch (Exception e) {
            return new OwnerReponse("execution reverted: ERC721: invalid token ID", null);
        }
    }

    @Override
    public CreateMintTransferResponse createToken(CreateTokenRequest request) {
        if (!contractAddresses.contains(request.getContractAddress())) {
            return new CreateMintTransferResponse(INCORRECT_ADDRESS);
        }
        ERC1155Tradable token = loader.loadToken1155(request.getContractAddress(), request.getPrivateKey());
        try {
            token.create(request.getToAddress(),
                        request.getTokenId(),
                        request.getInitialAmount(),
                        request.getUri(),
                        new byte[0]).send();
            return new CreateMintTransferResponse(SUCCESS_CREATE_1155.formatted(
                    request.getTokenId(),
                    request.getToAddress(),
                    request.getContractAddress(),
                    request.getInitialAmount()
            ));
        } catch (Exception e) {
            return new CreateMintTransferResponse(e.getMessage());
        }
    }

    @Override
    public CreateMintTransferResponse mintToken(Mint1155TokenRequest request) {
        if (!contractAddresses.contains(request.getContractAddress())) {
            return new CreateMintTransferResponse(INCORRECT_ADDRESS);
        }
        ERC1155Tradable token = loader.loadToken1155(request.getContractAddress(), request.getPrivateKey());
        try {
            token.mint(
                    request.getToAddress(),
                    request.getTokenId(),
                    request.getAmount(),
                    new byte[0]
            ).send();
            return new CreateMintTransferResponse(SUCCESS_MINT_1155.formatted(
                    request.getTokenId(),
                    request.getToAddress(),
                    request.getContractAddress(),
                    request.getAmount()));
        } catch (Exception e) {
            return new CreateMintTransferResponse(e.getMessage());
        }
    }

    @Override
    public CreateMintTransferResponse mintTokenBatch(Mint1155BatchTokenRequest request) {
        if (!contractAddresses.contains(request.getContractAddress())) {
            return new CreateMintTransferResponse(INCORRECT_ADDRESS);
        }
        ERC1155Tradable token = loader.loadToken1155(request.getContractAddress(), request.getPrivateKey());
        try {
            token.batchMint(
                    request.getToAddress(),
                    request.getTokenIds(),
                    request.getAmounts(),
                    new byte[0]
            ).send();
            return new CreateMintTransferResponse(SUCCESS_MINT_BATCH_1155.formatted(
                    request.getTokenIds(),
                    request.getToAddress(),
                    request.getContractAddress(),
                    request.getAmounts()));
        } catch (Exception e) {
            return new CreateMintTransferResponse(e.getMessage());
        }
    }

    @Override
    public CreateMintTransferResponse transferToken(Transfer1155Request request) {
        if (!contractAddresses.contains(request.getContractAddress())) {
            return new CreateMintTransferResponse(INCORRECT_ADDRESS);
        }
        ERC1155Tradable token = loader.loadToken1155(request.getContractAddress(), request.getPrivateKey());
        try {
            token.safeTransferFrom(
                    request.getAddressFrom(),
                    request.getAddressTo(),
                    request.getTokenId(),
                    request.getAmount(),
                    new byte[0]).send();
            return new CreateMintTransferResponse(SUCCESS_TRANSFER_1155.formatted(
                    request.getTokenId(),
                    request.getAddressFrom(),
                    request.getAddressTo(),
                    request.getAmount(),
                    request.getContractAddress()));
        } catch (Exception e) {
            return new CreateMintTransferResponse(e.getMessage());
        }
    }

    @Override
    public CreateMintTransferResponse transferTokenBatch(Transfer1155BatchRequest request) {
        if (!contractAddresses.contains(request.getContractAddress())) {
            return new CreateMintTransferResponse(INCORRECT_ADDRESS);
        }
        if (request.getTokenIds().size() != request.getAmounts().size()) {
            return new CreateMintTransferResponse("Token ids and amounts size should be equal!");
        }
        ERC1155Tradable token = loader.loadToken1155(request.getContractAddress(), request.getPrivateKey());
        try {
            token.safeBatchTransferFrom(
                    request.getAddressFrom(),
                    request.getAddressTo(),
                    request.getTokenIds(),
                    request.getAmounts(),
                    new byte[0]).send();
            return new CreateMintTransferResponse(SUCCESS_TRANSFER_BATCH_1155.formatted(
                    request.getTokenIds().toString(),
                    request.getAddressFrom(),
                    request.getAddressTo(),
                    request.getAmounts().toString(),
                    request.getContractAddress()));
        } catch (Exception e) {
            return new CreateMintTransferResponse(e.getMessage());
        }
    }
}
