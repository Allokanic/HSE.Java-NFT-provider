package com.allokanic.nft.util;

import com.allokanic.nft.model.DeployContractRequest;
import com.allokanic.nft.token.ERC1155Tradable;
import com.allokanic.nft.token.ERC721Tradable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.DefaultGasProvider;

@Service
public class Loader {
    private final Web3j web3j;
    @Value("${alchemy.wallet-private-key}")
    private String defaultPrivateKey;

    @Autowired
    public Loader(Web3j web3j) {this.web3j = web3j;}

    public ERC721Tradable loadToken721(String contractAddress) {
        return ERC721Tradable.load(
                contractAddress,
                web3j,
                Credentials.create(defaultPrivateKey),
                new DefaultGasProvider());
    }

    public ERC1155Tradable loadToken1155(String contractAddress, String privateKey) {
        return ERC1155Tradable.load(
                contractAddress,
                web3j,
                Credentials.create(privateKey == null ? defaultPrivateKey : privateKey),
                new DefaultGasProvider());
    }

    public ERC721Tradable deploy721(DeployContractRequest request) throws Exception {
        return ERC721Tradable.deploy(
                web3j,
                Credentials.create(defaultPrivateKey),
                new DefaultGasProvider(),
                request.getName(),
                request.getSymbol()).send();
    }

    public ERC1155Tradable deploy1155(DeployContractRequest request) throws Exception {
        return ERC1155Tradable.deploy(
                web3j,
                Credentials.create(defaultPrivateKey),
                new DefaultGasProvider(),
                request.getName(),
                request.getSymbol(),
                "").send();
    }
}
