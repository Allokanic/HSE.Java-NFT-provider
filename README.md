# API
swagger default path: baseUrl/swagger-ui/index.html

Admin service for working with blockchain (supports all ETH network (mainnet, sepolia, goerli and etc.) Alchemy api as a provider can be changed to native geth client since api for interaction in web3j was developed for base ethereum API

# NFT instruction

## How to deploy your own nft?

#### Pre requirements:
- solc compiler
- web3j cli

1. create folder in resources/contract with your source code
2. in that directory use
> solc YourTokenFileName.sol --bin --abi --optimize -o ./compiled

After that in the directory with your contract file you'll find compiled resources 
that can be used to generate POJO wrapper class for working with web3j.

3. in the root project directory use
> web3j generate solidity 
> -b src/main/resources/contract/yourTokenFolder/compiled/YourTokenFileName.bin 
> -a src/main/resources/contract/yourTokenFolder/compiled/YourTokenFileName.abi 
> -o src/main/java -p com.allokanic.nft.token

If all is OK there will be ready to use java class of your contract that you can call.
If your token implements ERC721 you can use NFTSerivce721, just improve type of "token" field.

>Otherwise, you'll need to implement your own service to interact with your contract
>As an example all service with my test contact is already implemented



