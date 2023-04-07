package com.allokanic.nft.controller;

import com.allokanic.nft.model.*;
import com.allokanic.nft.model.token721.dto.Mint721TokenRequest;
import com.allokanic.nft.model.token721.dto.Owner;
import com.allokanic.nft.model.token721.dto.Transfer721Request;
import com.allokanic.nft.service.ERC721Service;
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

import java.math.BigInteger;
import java.util.List;

@RestController
@Tag(name = "ERC721 Contract")
@RequestMapping("/token721")
@RequiredArgsConstructor
public class ERC721Controller {
    private final ERC721Service service;

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
            summary = "Mint new ERC721 token")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(
                            schema = @Schema(implementation = CreateMintTransferResponse.class),
                            mediaType = "application/json") })})
    @PostMapping("/mint")
    public ResponseEntity<CreateMintTransferResponse> mint(@Parameter @RequestBody Mint721TokenRequest request) {
        return ResponseEntity.ok(new CreateMintTransferResponse(service.mint(request)));
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
            summary = "Transfer ERC721 token")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(
                            schema = @Schema(implementation = CreateMintTransferResponse.class),
                            mediaType = "application/json") })})
    @PostMapping("/transfer")
    public ResponseEntity<CreateMintTransferResponse> transfer(@RequestBody Transfer721Request request) throws Exception {
        return ResponseEntity.ok(new CreateMintTransferResponse(service.transfer(request)));
    }

    @Operation(
            summary = "Get total amount of produced tokens")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200")})
    @GetMapping("total/{contractAddress}")
    public ResponseEntity<BigInteger> getTotalTokenAmounts(@Parameter @PathVariable String contractAddress) {
        return ResponseEntity.ok(service.getTotalTokens(contractAddress));
    }

    @Operation(
            summary = "Get all owners by contract address")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(
                            schema = @Schema(implementation = List.class),
                            mediaType = "application/json") })})
    @GetMapping("/owner/{contractAddress}")
    public ResponseEntity<List<Owner>> getAllOwners(@Parameter @PathVariable String contractAddress) {
        return ResponseEntity.ok(service.getAllOwners(contractAddress));
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
}
