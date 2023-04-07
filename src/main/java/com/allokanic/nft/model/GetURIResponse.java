package com.allokanic.nft.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetURIResponse {
    String uri;
    String error;
}
