package com.red.franquicias.nequi.r2dbc.product;

import com.red.franquicias.nequi.model.product.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductReactiveRepositoryAdapterTest {

    @Mock
    ProductReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @InjectMocks
    ProductReactiveRepositoryAdapter adapter;

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
}
