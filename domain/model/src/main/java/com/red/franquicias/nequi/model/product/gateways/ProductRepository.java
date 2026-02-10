package com.red.franquicias.nequi.model.product.gateways;

import com.red.franquicias.nequi.model.product.BranchTopProductRow;
import com.red.franquicias.nequi.model.product.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Mono<Product> saveProduct(Product product);

    Mono<Product> findByIdProduct(Long id);

    Mono<Product> findByIdAndBranchId(Long id, Long branchId);

    Mono<Boolean> existsByNameAndBranchId(String name, Long branchId);

    Mono<Void> deleteById(Long id);

    Mono<Product> findTopByBranchIdOrderByStockDesc(Long branchId);

    Flux<BranchTopProductRow> findTopProductsByFranchiseId(Long franchiseId);
}
