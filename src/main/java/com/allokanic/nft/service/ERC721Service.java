package com.allokanic.nft.service;

import com.allokanic.nft.model.*;
import com.allokanic.nft.model.token721.dto.Mint721TokenRequest;
import com.allokanic.nft.model.token721.dto.Owner;
import com.allokanic.nft.model.token721.dto.Transfer721Request;
import com.allokanic.nft.token.ERC721Tradable;
import com.allokanic.nft.util.Loader;
import com.allokanic.nft.message.Log;
import com.allokanic.nft.message.Signature;
import com.allokanic.nft.message.Success;
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

import static com.allokanic.nft.message.Error.*;

@Service
@Slf4j
public class ERC721Service implements NFTBaseService, IERC721Service {
    private final Loader loader;
    private final Set<String> contractAddresses = new HashSet<>();

    @Autowired
    public ERC721Service(Loader loader, Web3j web3j) {
        this.loader = loader;
        web3j.ethLogFlowable(new EthFilter()).subscribe(event -> {
            List<String> topics = event.getTopics();
            if (topics.size() == 4 && topics.get(0).equals(Signature.TRANSFER_SIGNATURE_ERC721)
            ) {
                Address from = TypeDecoder.decodeAddress(topics.get(1));
                Address to = TypeDecoder.decodeAddress(topics.get(2));
                BigInteger tokenId = new BigInteger(topics.get(3).substring(2), 16);
                if (TypeDecoder.decodeAddress(topics.get(1)).equals(new Address(BigInteger.valueOf(0)))) {
                    log.info(Log.MINT_EVENT_ERC721.formatted(tokenId.toString(), to, event.getAddress()));
                } else {
                    log.info(Log.TRANSFER_EVENT_ERC721.formatted(tokenId.toString(), from, to, event.getAddress()));
                }
            }
        });
    }

    @Override
    public String deploy(DeployContractRequest request) {
        try {
            ERC721Tradable token = loader.deploy721(request);
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
        ERC721Tradable contract = loader.loadToken721(request.getContractAddress());
        try {
            return new GetURIResponse(contract.tokenURI(request.getTokenId()).send(), null);
        } catch (Exception e) {
            return new GetURIResponse(null, "execution reverted: ERC721: invalid token ID");
        }
    }

    @Override
    public OwnerReponse getOwnerOf(OwnerRequest request) {
        if (!contractAddresses.contains(request.getContractAddress())) {
            return new OwnerReponse(INCORRECT_ADDRESS, null);
        }
        ERC721Tradable token = loader.loadToken721(request.getContractAddress());
        try {
            return new OwnerReponse(null, token.ownerOf(new BigInteger(String.valueOf(request.getTokenId()))).send());
        } catch (Exception e) {
            return new OwnerReponse("execution reverted: ERC721: invalid token ID", null);
        }
    }

    @Override
    public String mint(Mint721TokenRequest request) {
        if (!contractAddresses.contains(request.getContractAddress())) {
            return INCORRECT_ADDRESS;
        }
        ERC721Tradable token = loader.loadToken721(request.getContractAddress());
        try {
            token.safeMint(request.getToAddress(), request.getUri()).send();
            return Success.SUCCESS;
        } catch (Exception e) {
            return DEFAULT_ERROR;
        }
    }

    @Override
    public String transfer(Transfer721Request request) {
        if (!contractAddresses.contains(request.getContractAddress())) {
            return INCORRECT_ADDRESS;
        }
        ERC721Tradable token = loader.loadToken721(request.getContractAddress());
        try {
            token.safeTransferFrom(request.getAddressFrom(), request.getAddressTo(), request.getTokenId()).send();
            return Success.SUCCESS;
        } catch (Exception e) {
            return TRANSFER_ERROR;
        }
    }

    public List<Owner> getAllOwners(String contractAddress) {
        if (!contractAddresses.contains(contractAddress)) {
            return null;
        }
        BigInteger total = getTotalTokens(contractAddress);
        List<Owner> result = new LinkedList<>();
        for (int i = 1; i <= total.intValue(); ++i) {
            var request = new OwnerRequest(contractAddress, (long) i);
            result.add(new Owner(getOwnerOf(request).getAddress(), String.valueOf(i)));
        }
        return result;
    }

    public BigInteger getTotalTokens(String contractAddress) {
        if (!contractAddresses.contains(contractAddress)) {
            return null;
        }
        ERC721Tradable token = loader.loadToken721(contractAddress);
        try {
            return token.getTokenAmount().send();
        } catch (Exception e) {
            return BigInteger.valueOf(-1L);
        }
    }
}
