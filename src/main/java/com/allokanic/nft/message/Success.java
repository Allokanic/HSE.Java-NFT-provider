package com.allokanic.nft.message;

public class Success {
    public static final String SUCCESS = "Operation success!";
    public static final String SUCCESS_MINT_1155 = "Token with id %d was successfully minted to address %s (contract %s, amount = %d)";
    public static final String SUCCESS_CREATE_1155 = "Token with id %d was successfully created and sent to %s (contract %s, amount = %d)";
    public static final String SUCCESS_TRANSFER_1155 =
            "Token with id %d was successfully transferred from %s to %s (amount = %d, contract address = %s)";
    public static final String SUCCESS_TRANSFER_BATCH_1155 =
            "Tokens with ids %s was successfully transferred from %s to %s (amounts = %s, contract address = %s)";
    public static final String SUCCESS_MINT_BATCH_1155 = "Tokens with ids %s was successfully minted to address %s (contract %s, amounts = %s)";
}
