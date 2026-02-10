package com.red.franquicias.nequi.r2dbc.product;

import com.red.franquicias.nequi.model.product.BranchTopProductRow;
import com.red.franquicias.nequi.model.product.Product;
import com.red.franquicias.nequi.model.product.gateways.ProductRepository;
import com.red.franquicias.nequi.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ProductReactiveRepositoryAdapter extends ReactiveAdapterOperations<Product, ProductEntity, Long, ProductReactiveRepository> implements ProductRepository {

    public ProductReactiveRepositoryAdapter(ProductReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Product.class));
    }

    @Override
    public Mono<Product> saveProduct(Product product) {
        ProductEntity entity = this.toData(product);
        return repository.save(entity)
                .map(this::toEntity);
    }

    @Override
    public Mono<Product> findByIdProduct(Long id) {
        return repository.findById(id)
                .map(this::toEntity);
    }

    @Override
    public Mono<Product> findByIdAndBranchId(Long id, Long branchId) {
        return repository.findByIdAndBranchId(id, branchId)
                .map(this::toEntity);
    }

    @Override
    public Mono<Boolean> existsByNameAndBranchId(String name, Long branchId) {
        return repository.findByNameAndBranchId(name, branchId)
                .hasElement();
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<Product> findTopByBranchIdOrderByStockDesc(Long branchId) {
        return repository.findTopByBranchIdOrderByStockDesc(branchId)
                .map(this::toEntity);
    }

    @Override
    public Flux<BranchTopProductRow> findTopProductsByFranchiseId(Long franchiseId) {
        return repository.findTopProductsByFranchiseId(franchiseId);
    }
}
