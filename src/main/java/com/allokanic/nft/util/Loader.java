package com.allokanic.nft.util;

import com.allokanic.nft.model.DeployContractRequest;
import com.allokanic.nft.token.ERC1155Tradable;
import com.allokanic.nft.token.ERC721Tradable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.tx.gas.DefaultGasProvider;

import java.util.List;

import static com.allokanic.nft.token.ERC721Tradable.FUNC_SUPPORTSINTERFACE;

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

    public boolean supportsInterface(String address, byte[] interfaceId) {
        Function function = new Function(FUNC_SUPPORTSINTERFACE,
                List.of(new Bytes4(interfaceId)),
                List.of(new TypeReference<Bool>() {}));
        String encodedFunction = FunctionEncoder.encode(function);
        var ethCall = web3j.ethCall(
                Transaction.createEthCallTransaction(null, address, encodedFunction),
                DefaultBlockParameter.valueOf("latest"));
        try {
            var result = ethCall.send().getValue();
            var decodedResult = FunctionReturnDecoder.decode(result, function.getOutputParameters());
            return (boolean)decodedResult.get(0).getValue();
        } catch (Exception e){
            return false;
        }
    }
}
