package com.red.franquicias.nequi.r2dbc.product;

import com.red.franquicias.nequi.logging.AdapterLogger;
import com.red.franquicias.nequi.model.product.BranchTopProductRow;
import com.red.franquicias.nequi.model.product.Product;
import com.red.franquicias.nequi.model.product.gateways.ProductRepository;
import com.red.franquicias.nequi.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class ProductReactiveRepositoryAdapter extends ReactiveAdapterOperations<Product, ProductEntity, Long, ProductReactiveRepository> implements ProductRepository {

    private final AdapterLogger adapterLogger;

    public ProductReactiveRepositoryAdapter(ProductReactiveRepository repository, ObjectMapper mapper, AdapterLogger adapterLogger) {
        super(repository, mapper, d -> mapper.map(d, Product.class));
        this.adapterLogger = adapterLogger;
    }

    @Override
    public Mono<Product> saveProduct(Product product) {
        long startTime = adapterLogger.startTimer();
        adapterLogger.outboundRequest("ProductRepository", "saveProduct", "productId=" + product.getId() + " branchId=" + product.getBranchId());

        ProductEntity entity = this.toData(product);
        return repository.save(entity)
                .map(this::toEntity)
                .doOnNext(result -> {
                    long duration = adapterLogger.calculateDuration(startTime);
                    adapterLogger.outboundResponse("ProductRepository", "saveProduct", "productId=" + result.getId(), duration);
                })
                .doOnError(error -> adapterLogger.error("ProductRepository", "saveProduct", (Exception) error, "productId=" + product.getId()));
    }

    @Override
    public Mono<Product> findByIdProduct(Long id) {
        long startTime = adapterLogger.startTimer();
        adapterLogger.outboundRequest("ProductRepository", "findByIdProduct", "productId=" + id);

        return repository.findById(id)
                .map(this::toEntity)
                .doOnNext(result -> {
                    long duration = adapterLogger.calculateDuration(startTime);
                    adapterLogger.outboundResponse("ProductRepository", "findByIdProduct", "productId=" + result.getId(), duration);
                })
                .doOnError(error -> adapterLogger.error("ProductRepository", "findByIdProduct", (Exception) error, "productId=" + id));
    }

    @Override
    public Mono<Product> findByIdAndBranchId(Long id, Long branchId) {
        long startTime = adapterLogger.startTimer();
        adapterLogger.outboundRequest("ProductRepository", "findByIdAndBranchId", "productId=" + id + " branchId=" + branchId);

        return repository.findByIdAndBranchId(id, branchId)
                .map(this::toEntity)
                .doOnNext(result -> {
                    long duration = adapterLogger.calculateDuration(startTime);
                    adapterLogger.outboundResponse("ProductRepository", "findByIdAndBranchId", "productId=" + result.getId(), duration);
                })
                .doOnError(error -> adapterLogger.error("ProductRepository", "findByIdAndBranchId", (Exception) error, "productId=" + id + " branchId=" + branchId));
    }

    @Override
    public Mono<Boolean> existsByNameAndBranchId(String name, Long branchId) {
        long startTime = adapterLogger.startTimer();
        adapterLogger.outboundRequest("ProductRepository", "existsByNameAndBranchId", "name=" + name + " branchId=" + branchId);

        return repository.findByNameAndBranchId(name, branchId)
                .hasElement()
                .doOnNext(exists -> {
                    long duration = adapterLogger.calculateDuration(startTime);
                    adapterLogger.outboundResponse("ProductRepository", "existsByNameAndBranchId", "exists=" + exists, duration);
                })
                .doOnError(error -> adapterLogger.error("ProductRepository", "existsByNameAndBranchId", (Exception) error, "name=" + name + " branchId=" + branchId));
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        long startTime = adapterLogger.startTimer();
        adapterLogger.outboundRequest("ProductRepository", "deleteById", "productId=" + id);

        return repository.deleteById(id)
                .doFinally(signalType -> {
                    long duration = adapterLogger.calculateDuration(startTime);
                    if (signalType.toString().equals("ON_COMPLETE")) {
                        adapterLogger.outboundResponse("ProductRepository", "deleteById", "productId=" + id, duration);
                    }
                })
                .doOnError(error -> adapterLogger.error("ProductRepository", "deleteById", (Exception) error, "productId=" + id));
    }

    @Override
    public Mono<Product> findTopByBranchIdOrderByStockDesc(Long branchId) {
        long startTime = adapterLogger.startTimer();
        adapterLogger.outboundRequest("ProductRepository", "findTopByBranchIdOrderByStockDesc", "branchId=" + branchId);

        return repository.findTopByBranchIdOrderByStockDesc(branchId)
                .map(this::toEntity)
                .doOnNext(result -> {
                    long duration = adapterLogger.calculateDuration(startTime);
                    adapterLogger.outboundResponse("ProductRepository", "findTopByBranchIdOrderByStockDesc", "productId=" + result.getId(), duration);
                })
                .doOnError(error -> adapterLogger.error("ProductRepository", "findTopByBranchIdOrderByStockDesc", (Exception) error, "branchId=" + branchId));
    }

    @Override
    public Flux<BranchTopProductRow> findTopProductsByFranchiseId(Long franchiseId) {
        long startTime = adapterLogger.startTimer();
        adapterLogger.outboundRequest("ProductRepository", "findTopProductsByFranchiseId", "franchiseId=" + franchiseId);

        return repository.findTopProductsByFranchiseId(franchiseId)
                .doFinally(signalType -> {
                    long duration = adapterLogger.calculateDuration(startTime);
                    if (signalType.toString().equals("ON_COMPLETE")) {
                        adapterLogger.outboundResponse("ProductRepository", "findTopProductsByFranchiseId", "franchiseId=" + franchiseId, duration);
                    }
                })
                .doOnError(error -> adapterLogger.error("ProductRepository", "findTopProductsByFranchiseId", (Exception) error, "franchiseId=" + franchiseId));
    }
}

