package com.red.franquicias.nequi.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TechnicalMessage {
    FRANCHISE_NAME_ALREADY_EXISTS("409", "Franchise name already exists", "name"),
    FRANCHISE_CREATE_ERROR("500", "Error creating franchise", null),
    FRANCHISE_UPDATE_NAME_ERROR("500", "Error updating franchise name", null),
    FRANCHISE_NOT_FOUND("404", "Franchise not found", "franchiseId"),
    BRANCH_NAME_ALREADY_EXISTS("409", "Branch name already exists in this franchise", "name"),
    BRANCH_UPDATE_ERROR("500", "Error updating branch name", null),
    BRANCH_CREATE_ERROR("500", "Error creating branch", null),
    BRANCH_NOT_FOUND("404", "Branch not found or does not belong to franchise", "branchId"),
    PRODUCT_NOT_FOUND("404", "Product not found", "productId"),
    PRODUCT_NAME_ALREADY_EXISTS("409", "Product name already exists in this branch", "name"),
    INVALID_BRANCH_NAME("400", "Branch name is required", "name"),
    INVALID_FRANCHISE_ID("400", "Franchise id is required", "franchiseId"),
    TOP_PRODUCTS_QUERY_ERROR("500", "Error retrieving top products", null),
    INVALID_FRANCHISE_NAME("400", "Franchise name is required", "name"),
    INVALID_BRANCH_ID("400", "Branch id is required", "branchId"),
    INVALID_PRODUCT_ID("400", "Product id is required", "productId"),
    INVALID_PRODUCT_NAME("400", "Product name is required", "name"),
    INVALID_PRODUCT_STOCK("400", "Product stock is required", "stock"),
    INVALID_PRODUCT_STOCK_NEGATIVE("400", "Product stock must be zero or greater", "stock"),
    PRODUCT_CREATE_ERROR("500", "Error creating product", null),
    PRODUCT_UPDATE_NAME_ERROR("500", "Error updating product name", null),
    PRODUCT_UPDATE_STOCK_ERROR("500", "Error updating product stock", null),
    PRODUCT_REMOVE_ERROR("500", "Error removing product", null);

    private final String code;
    private final String message;
    private final String param;
}