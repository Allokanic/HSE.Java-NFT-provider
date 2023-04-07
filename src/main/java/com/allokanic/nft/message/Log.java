package com.allokanic.nft.message;

public class Log {
    public static final String TRANSFER_EVENT_ERC721 = "ERC721 update: ✅Token with id %s was transferred from %s to %s at contract %s";
    public static final String MINT_EVENT_ERC721 = "ERC721 update: \uD83D\uDCE9Token with id %s was minted to %s at contract %s";
    public static final String TRANSFER_EVENT_ERC1155 = "ERC1155 update: ✅Token with id %s was transferred from %s to %s at contract %s (count = %s)";
    public static final String MINT_EVENT_ERC1155 = "ERC1155 update: \uD83D\uDCE9Token with id %s was minted to %s at contract %s (count = %s)";
    public static final String TRANSFER_BATCH_EVENT_ERC1155 = "ERC1155 update: ✅Tokens with ids %s was transferred from %s to %s at contract %s (counts = %s)";
    public static final String MINT_BATCH_EVENT_ERC1155 = "ERC1155 update: \uD83D\uDCE9Tokens with ids %s was minted to %s at contract %s (counts = %s)";
}
