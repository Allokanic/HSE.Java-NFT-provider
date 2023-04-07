package com.allokanic.nft.controller;

import com.allokanic.nft.model.*;
import com.allokanic.nft.model.token1155.dto.*;
import com.allokanic.nft.service.ERC1155Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "ERC1155 Contract")
@RequestMapping("/token1155")
@RequiredArgsConstructor
public class ERC1155Controller {
    private final ERC1155Service service;

    @Operation(
            summary = "Deploy new contract")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(
                            schema = @Schema(implementation = DeployContractResponse.class),
                            mediaType = "application/json") })})
    @PostMapping("/deploy")
    public ResponseEntity<DeployContractResponse> deploy(@Parameter @RequestBody DeployContractRequest request) {
        return ResponseEntity.ok(new DeployContractResponse(service.deploy(request)));
    }

    @Operation(
            summary = "Get uri by tokenId and contractAddress")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(
                            schema = @Schema(implementation = GetURIResponse.class),
                            mediaType = "application/json") })})
    @PostMapping("/uri")
    public ResponseEntity<GetURIResponse> getUri(@Parameter @RequestBody GetURIRequest request) {
        return ResponseEntity.ok(service.getUri(request));
    }

    @Operation(
            summary = "Owner of token in specific contract")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(
                            schema = @Schema(implementation = OwnerReponse.class),
                            mediaType = "application/json") })})
    @PostMapping("/owner")
    public ResponseEntity<OwnerReponse> getOwnerOf(@Parameter @RequestBody OwnerRequest request) {
        return ResponseEntity.ok(service.getOwnerOf(request));
    }

    @Operation(
            summary = "Mint new ERC1155 token")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(
                            schema = @Schema(implementation = CreateMintTransferResponse.class),
                            mediaType = "application/json") })})
    @PostMapping("/mint")
    public ResponseEntity<CreateMintTransferResponse> mint(@Parameter @RequestBody Mint1155TokenRequest request) {
        return ResponseEntity.ok(service.mintToken(request));
    }

    @Operation(
            summary = "Mint batch of new ERC1155 tokens")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(
                            schema = @Schema(implementation = CreateMintTransferResponse.class),
                            mediaType = "application/json") })})
    @PostMapping("/mint/batch")
    public ResponseEntity<CreateMintTransferResponse> mint(@Parameter @RequestBody Mint1155BatchTokenRequest request) {
        return ResponseEntity.ok(service.mintTokenBatch(request));
    }

    @Operation(
            summary = "Create new ERC1155 token")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(
                            schema = @Schema(implementation = CreateMintTransferResponse.class),
                            mediaType = "application/json") })})
    @PostMapping("/create")
    public ResponseEntity<CreateMintTransferResponse> create(@Parameter @RequestBody CreateTokenRequest request) {
        return ResponseEntity.ok(service.createToken(request));
    }

    @Operation(
            summary = "Transfer ERC1155 token")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(
                            schema = @Schema(implementation = CreateMintTransferResponse.class),
                            mediaType = "application/json") })})
    @PostMapping("/transfer")
    public ResponseEntity<CreateMintTransferResponse> transfer(@RequestBody Transfer1155Request request) throws Exception {
        return ResponseEntity.ok(service.transferToken(request));
    }

    @Operation(
            summary = "Transfer batch of ERC1155 tokens")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(
                            schema = @Schema(implementation = CreateMintTransferResponse.class),
                            mediaType = "application/json") })})
    @PostMapping("/transfer/batch")
    public ResponseEntity<CreateMintTransferResponse> transferBatch(@RequestBody Transfer1155BatchRequest request) {
        return ResponseEntity.ok(service.transferTokenBatch(request));
    }

}
