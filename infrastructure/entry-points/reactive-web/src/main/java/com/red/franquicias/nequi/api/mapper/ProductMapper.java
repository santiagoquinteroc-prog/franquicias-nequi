package com.red.franquicias.nequi.api.mapper;


import com.red.franquicias.nequi.api.dto.*;
import com.red.franquicias.nequi.model.product.BranchTopProduct;
import com.red.franquicias.nequi.model.product.Product;
import com.red.franquicias.nequi.model.product.ProductInfo;
import com.red.franquicias.nequi.model.product.TopProductsResult;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {
    public static Product toDomain(ProductRequest request, Long branchId) {
        return new Product(null, branchId, request.name(), request.stock());
    }

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getBranchId(), product.getName(), product.getStock());
    }


    public static TopProductsResponse toTopProductsResponse(TopProductsResult result) {
        List<BranchTopProductResponse> results = result.results().stream()
                .map(ProductMapper::toBranchTopProductResponse)
                .collect(Collectors.toList());
        return new TopProductsResponse(result.franchiseId(), result.franchiseName(), results);
    }

    private static BranchTopProductResponse toBranchTopProductResponse(BranchTopProduct branchTopProduct) {
        ProductInfo productInfo = branchTopProduct.product();
        ProductInfoResponse productInfoResponse = new ProductInfoResponse(
                productInfo.productId(),
                productInfo.name(),
                productInfo.stock()
        );
        return new BranchTopProductResponse(
                branchTopProduct.branchId(),
                branchTopProduct.branchName(),
                productInfoResponse
        );
    }
}

