// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

import "./abstract/Ownable.sol";
import "./sub/ERC1155.sol";
import "./lib/SafeMath.sol";
import "./lib/Strings.sol";

import "./abstract/ContentMixin.sol";

contract ERC1155Tradable is ContextMixin, ERC1155, Ownable {
    using Strings for string;
    using SafeMath for uint256;

    mapping (uint256 => address) public creators;
    mapping (uint256 => uint256) public tokenSupply;
    mapping (uint256 => string) customUri;
    
    string public name;
    string public symbol;
    
    modifier creatorOnly(uint256 _id) {
        require(creators[_id] == _msgSender(), "ERC1155Tradable#creatorOnly: ONLY_CREATOR_ALLOWED");
        _;
    }

    modifier ownersOnly(uint256 _id) {
        require(balanceOf(_msgSender(), _id) > 0, "ERC1155Tradable#ownersOnly: ONLY_OWNERS_ALLOWED");
        _;
    }

    constructor(string memory _name, string memory _symbol, string memory _uri) ERC1155(_uri) {
        name = _name;
        symbol = _symbol;
    }

    function uri(uint256 _id) override public view returns (string memory) {
        require(_exists(_id), "ERC1155Tradable#uri: NONEXISTENT_TOKEN");
        bytes memory customUriBytes = bytes(customUri[_id]);
        if (customUriBytes.length > 0) {
            return customUri[_id];
        } else {
            return super.uri(_id);
        }
    }

    function totalSupply(uint256 _id) public view returns (uint256) {
        return tokenSupply[_id];
    }

    function setURI(string memory _newURI) public onlyOwner {
        _setURI(_newURI);
    }

    function setCustomURI(uint256 _tokenId, string memory _newURI) public creatorOnly(_tokenId) {
        customUri[_tokenId] = _newURI;
        emit URI(_newURI, _tokenId);
    }

    function create(address _initialOwner, uint256 _id, uint256 _initialSupply, string memory _uri, bytes memory _data) 
    public returns (uint256) {
        require(!_exists(_id), "token _id already exists");
        creators[_id] = _msgSender();

        if (bytes(_uri).length > 0) {
            customUri[_id] = _uri;
            emit URI(_uri, _id);
        }

        _mint(_initialOwner, _id, _initialSupply, _data);

        tokenSupply[_id] = _initialSupply;
        return _id;
    }

    function mint(address _to, uint256 _id, uint256 _quantity, bytes memory _data) virtual public creatorOnly(_id) {
        _mint(_to, _id, _quantity, _data);
        tokenSupply[_id] = tokenSupply[_id].add(_quantity);
    }

    function batchMint(address _to, uint256[] memory _ids, uint256[] memory _quantities, bytes memory _data) public {
        for (uint256 i = 0; i < _ids.length; i++) {
        uint256 _id = _ids[i];
        require(creators[_id] == _msgSender(), "ERC1155Tradable#batchMint: ONLY_CREATOR_ALLOWED");
        uint256 quantity = _quantities[i];
        tokenSupply[_id] = tokenSupply[_id].add(quantity);
        }
        _mintBatch(_to, _ids, _quantities, _data);
    }

    function setCreator(address _to, uint256[] memory _ids) public {
        require(_to != address(0), "ERC1155Tradable#setCreator: INVALID_ADDRESS.");
        for (uint256 i = 0; i < _ids.length; i++) {
            uint256 id = _ids[i];
            _setCreator(_to, id);
        }
    }

    function creatorOf(uint256 tokenId) public view virtual returns (address) {
        address owner = creators[tokenId];
        require(owner != address(0), "ERC1155: invalid token ID");
        return owner;
    }

    function _setCreator(address _to, uint256 _id) internal creatorOnly(_id) {
        creators[_id] = _to;
    }

    function _exists(uint256 _id) internal view returns (bool) {
        return creators[_id] != address(0);
    }

    function exists(uint256 _id) external view returns (bool) {
        return _exists(_id);
    }

    function _msgSender() internal override view returns (address sender) {
        return ContextMixin.msgSender();
    }
}