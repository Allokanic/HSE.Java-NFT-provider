package com.allokanic.nft.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeployContractRequest {
    private String name;
    private String symbol;
}
