package com.red.franquicias.nequi.r2dbc.product;

import com.red.franquicias.nequi.logging.AdapterLogger;
import com.red.franquicias.nequi.model.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductReactiveRepositoryAdapterTest {

    @Mock
    ProductReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @Mock
    AdapterLogger adapterLogger;

    ProductReactiveRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ProductReactiveRepositoryAdapter(repository, mapper, adapterLogger);
        when(adapterLogger.startTimer()).thenReturn(0L);
        when(adapterLogger.calculateDuration(anyLong())).thenReturn(100L);
        org.mockito.Mockito.lenient().doNothing().when(adapterLogger).outboundRequest(anyString(), anyString(), anyString());
        org.mockito.Mockito.lenient().doNothing().when(adapterLogger).outboundResponse(anyString(), anyString(), anyString(), anyLong());
        org.mockito.Mockito.lenient().doNothing().when(adapterLogger).error(anyString(), anyString(), any(Throwable.class), anyString());
    }

    @Test
    void mustFindValueById() {
        ProductEntity entity = new ProductEntity();
        Product domain = new Product();

        when(repository.findById(1L)).thenReturn(Mono.just(entity));
        when(mapper.map(eq(entity), eq(Product.class))).thenReturn(domain);

        Mono<Product> result = adapter.findById(1L);

        StepVerifier.create(result)
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        ProductEntity entity1 = new ProductEntity();
        Product domain1 = new Product();

        when(repository.findAll()).thenReturn(Flux.just(entity1));
        when(mapper.map(eq(entity1), eq(Product.class))).thenReturn(domain1);

        Flux<Product> result = adapter.findAll();

        StepVerifier.create(result)
                .expectNext(domain1)
                .verifyComplete();
    }

    @Test
    void mustFindByExample() {
        ProductEntity probe = new ProductEntity();
        ProductEntity entity1 = new ProductEntity();
        Product domain1 = new Product();

        when(mapper.map(any(Product.class), eq(ProductEntity.class))).thenReturn(probe);

        when(repository.findAll(any(org.springframework.data.domain.Example.class))).thenReturn(Flux.just(entity1));
        when(mapper.map(eq(entity1), eq(Product.class))).thenReturn(domain1);

        Flux<Product> result = adapter.findByExample(new Product());

        StepVerifier.create(result)
                .expectNext(domain1)
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        ProductEntity savedEntity = new ProductEntity();
        Product domainToSave = new Product();
        Product expectedDomain = new Product();

        when(mapper.map(eq(domainToSave), eq(ProductEntity.class))).thenReturn(savedEntity);
        when(repository.save(eq(savedEntity))).thenReturn(Mono.just(savedEntity));
        when(mapper.map(eq(savedEntity), eq(Product.class))).thenReturn(expectedDomain);

        Mono<Product> result = adapter.save(domainToSave);

        StepVerifier.create(result)
                .expectNext(expectedDomain)
                .verifyComplete();
    }

    @Test
    void saveProduct_shouldSaveAndReturnProduct() {
        ProductEntity savedEntity = new ProductEntity();
        Product domainToSave = new Product();
        Product expectedDomain = new Product();

        when(mapper.map(eq(domainToSave), eq(ProductEntity.class))).thenReturn(savedEntity);
        when(repository.save(eq(savedEntity))).thenReturn(Mono.just(savedEntity));
        when(mapper.map(eq(savedEntity), eq(Product.class))).thenReturn(expectedDomain);

        Mono<Product> result = adapter.saveProduct(domainToSave);

        StepVerifier.create(result)
                .expectNext(expectedDomain)
                .verifyComplete();
    }

    @Test
    void findByIdProduct_shouldReturnProduct() {
        ProductEntity entity = new ProductEntity();
        Product domain = new Product();

        when(repository.findById(1L)).thenReturn(Mono.just(entity));
        when(mapper.map(eq(entity), eq(Product.class))).thenReturn(domain);

        Mono<Product> result = adapter.findByIdProduct(1L);

        StepVerifier.create(result)
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void findByIdAndBranchId_shouldReturnProduct() {
        ProductEntity entity = new ProductEntity();
        Product domain = new Product();

        when(repository.findByIdAndBranchId(1L, 10L)).thenReturn(Mono.just(entity));
        when(mapper.map(eq(entity), eq(Product.class))).thenReturn(domain);

        Mono<Product> result = adapter.findByIdAndBranchId(1L, 10L);

        StepVerifier.create(result)
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void existsByNameAndBranchId_shouldReturnTrue() {
        ProductEntity entity = new ProductEntity();

        when(repository.findByNameAndBranchId("Product A", 10L)).thenReturn(Mono.just(entity));

        Mono<Boolean> result = adapter.existsByNameAndBranchId("Product A", 10L);

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void existsByNameAndBranchId_shouldReturnFalse() {
        when(repository.findByNameAndBranchId("Product A", 10L)).thenReturn(Mono.empty());

        Mono<Boolean> result = adapter.existsByNameAndBranchId("Product A", 10L);

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void deleteById_shouldDeleteProduct() {
        when(repository.deleteById(1L)).thenReturn(Mono.empty());

        Mono<Void> result = adapter.deleteById(1L);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void findTopByBranchIdOrderByStockDesc_shouldReturnTopProduct() {
        ProductEntity entity = new ProductEntity();
        Product domain = new Product();

        when(repository.findTopByBranchIdOrderByStockDesc(10L)).thenReturn(Mono.just(entity));
        when(mapper.map(eq(entity), eq(Product.class))).thenReturn(domain);

        Mono<Product> result = adapter.findTopByBranchIdOrderByStockDesc(10L);

        StepVerifier.create(result)
                .expectNext(domain)
                .verifyComplete();
    }
}
