// SPDX-License-Identifier: MIT
pragma solidity ^0.8.9;

 import "./abstract/ERC721.sol";
 import "./abstract/ERC721URIStorage.sol";
 import "./abstract/Ownable.sol";
 import "./libs/Counters.sol";

contract ERC721Tradable is ERC721URIStorage, Ownable {
    using Counters for Counters.Counter;

    Counters.Counter private _tokenIdCounter;

    constructor(string memory tokenName, string memory tokenSymbol) ERC721(tokenName, tokenSymbol) {}

    function safeMint(address to, string memory uri) 
    public {
        uint256 tokenId = _tokenIdCounter.current();
        _tokenIdCounter.increment();
        _safeMint(to, tokenId);
        _setTokenURI(tokenId, uri);
    }

    function _burn(uint256 tokenId) 
    internal override (ERC721URIStorage) {
        super._burn(tokenId);
    }

    function tokenURI(uint256 tokenId)
        public view override (ERC721URIStorage) returns (string memory) {
        return super.tokenURI(tokenId);
    }

    function getTokenAmount() public view returns(uint256){
        return _tokenIdCounter.current();
    }
}