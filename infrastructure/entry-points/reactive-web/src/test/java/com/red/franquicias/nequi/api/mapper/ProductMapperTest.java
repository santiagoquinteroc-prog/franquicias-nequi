package com.red.franquicias.nequi.api.mapper;

import com.red.franquicias.nequi.api.dto.ProductRequest;
import com.red.franquicias.nequi.api.dto.ProductResponse;
import com.red.franquicias.nequi.api.dto.TopProductsResponse;
import com.red.franquicias.nequi.model.product.BranchTopProduct;
import com.red.franquicias.nequi.model.product.Product;
import com.red.franquicias.nequi.model.product.ProductInfo;
import com.red.franquicias.nequi.model.product.TopProductsResult;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductMapperTest {

    @Test
    void toDomain_withValidRequest_shouldMapToDomain() {
        Long branchId = 1L;
        ProductRequest request = new ProductRequest("Test Product", 100);
        Product result = ProductMapper.toDomain(request, branchId);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNull();
        assertThat(result.getBranchId()).isEqualTo(branchId);
        assertThat(result.getName()).isEqualTo("Test Product");
        assertThat(result.getStock()).isEqualTo(100);
    }

    @Test
    void toDomain_withDifferentBranchIds_shouldMapCorrectly() {
        Long[] branchIds = {1L, 100L, 999L};
        ProductRequest request = new ProductRequest("Product", 50);

        for (Long branchId : branchIds) {
            Product result = ProductMapper.toDomain(request, branchId);
            assertThat(result.getBranchId()).isEqualTo(branchId);
        }
    }

    @Test
    void toDomain_withDifferentStocks_shouldMapCorrectly() {
        int[] stocks = {0, 50, 1000};

        for (int stock : stocks) {
            ProductRequest request = new ProductRequest("Product", stock);
            Product result = ProductMapper.toDomain(request, 1L);
            assertThat(result.getStock()).isEqualTo(stock);
        }
    }

    @Test
    void toResponse_withValidProduct_shouldMapToResponse() {
        Product product = new Product(1L, 10L, "Test Product", 100);
        ProductResponse result = ProductMapper.toResponse(product);
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.branchId()).isEqualTo(10L);
        assertThat(result.name()).isEqualTo("Test Product");
        assertThat(result.stock()).isEqualTo(100);
    }

    @Test
    void toResponse_withDifferentIds_shouldMapCorrectly() {
        Long[] ids = {1L, 100L, 999L};

        for (Long id : ids) {
            Product product = new Product(id, 5L, "Test", 50);
            ProductResponse result = ProductMapper.toResponse(product);
            assertThat(result.id()).isEqualTo(id);
        }
    }

    @Test
    void toResponse_withZeroStock_shouldMapCorrectly() {
        Product product = new Product(1L, 1L, "Test Product", 0);
        ProductResponse result = ProductMapper.toResponse(product);
        assertThat(result.stock()).isEqualTo(0);
    }

    @Test
    void mappingRoundTrip_shouldPreserveData() {
        Long branchId = 42L;
        String originalName = "Round Trip Test";
        int originalStock = 75;
        ProductRequest request = new ProductRequest(originalName, originalStock);

        Product domain = ProductMapper.toDomain(request, branchId);
        Product withId = new Product(1L, domain.getBranchId(), domain.getName(), domain.getStock());
        ProductResponse response = ProductMapper.toResponse(withId);

        assertThat(response.name()).isEqualTo(originalName);
        assertThat(response.branchId()).isEqualTo(branchId);
        assertThat(response.stock()).isEqualTo(originalStock);
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    void toResponse_withNullValues_shouldHandleCorrectly() {
        Product product = new Product(null, 1L, "Test", 50);
        ProductResponse result = ProductMapper.toResponse(product);
        assertThat(result.id()).isNull();
        assertThat(result.branchId()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Test");
        assertThat(result.stock()).isEqualTo(50);
    }

    @Test
    void toTopProductsResponse_withValidResult_shouldMapCorrectly() {
        ProductInfo productInfo = new ProductInfo(1L, "Top Product", 150);
        BranchTopProduct branchTopProduct = new BranchTopProduct(1L, "Branch A", productInfo);
        TopProductsResult result = new TopProductsResult(1L, "Franchise A", List.of(branchTopProduct));

        // Act
        TopProductsResponse response = ProductMapper.toTopProductsResponse(result);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.franchiseId()).isEqualTo(1L);
        assertThat(response.franchiseName()).isEqualTo("Franchise A");
        assertThat(response.results()).hasSize(1);
        assertThat(response.results().get(0).branchId()).isEqualTo(1L);
        assertThat(response.results().get(0).branchName()).isEqualTo("Branch A");
    }

    @Test
    void toTopProductsResponse_withEmptyResults_shouldMapCorrectly() {
        // Arrange
        TopProductsResult result = new TopProductsResult(1L, "Franchise A", Collections.emptyList());
        TopProductsResponse response = ProductMapper.toTopProductsResponse(result);
        assertThat(response).isNotNull();
        assertThat(response.franchiseId()).isEqualTo(1L);
        assertThat(response.results()).isEmpty();
    }

    @Test
    void toTopProductsResponse_withMultipleBranches_shouldMapAll() {
        List<BranchTopProduct> branches = List.of(
                new BranchTopProduct(1L, "Branch A", new ProductInfo(1L, "Product A", 100)),
                new BranchTopProduct(2L, "Branch B", new ProductInfo(2L, "Product B", 200))
        );
        TopProductsResult result = new TopProductsResult(1L, "Franchise A", branches);
        TopProductsResponse response = ProductMapper.toTopProductsResponse(result);
        assertThat(response.results()).hasSize(2);
        assertThat(response.results().get(0).branchId()).isEqualTo(1L);
        assertThat(response.results().get(1).branchId()).isEqualTo(2L);
    }
}

