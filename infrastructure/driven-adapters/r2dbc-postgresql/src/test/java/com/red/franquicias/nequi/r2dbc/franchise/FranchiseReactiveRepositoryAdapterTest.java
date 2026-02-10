package com.red.franquicias.nequi.r2dbc.franchise;

import com.red.franquicias.nequi.model.franchise.Franchise;
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
class FranchiseReactiveRepositoryAdapterTest {

    @Mock
    FranchiseReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @InjectMocks
    FranchiseReactiveRepositoryAdapter adapter;

    @Test
    void mustFindValueById() {
        FranchiseEntity entity = new FranchiseEntity();
        Franchise domain = new Franchise();

        when(repository.findById(1L)).thenReturn(Mono.just(entity));
        when(mapper.map(eq(entity), eq(Franchise.class))).thenReturn(domain);

        Mono<Franchise> result = adapter.findById(1L);

        StepVerifier.create(result)
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        FranchiseEntity entity1 = new FranchiseEntity();
        Franchise domain1 = new Franchise();

        when(repository.findAll()).thenReturn(Flux.just(entity1));
        when(mapper.map(eq(entity1), eq(Franchise.class))).thenReturn(domain1);

        Flux<Franchise> result = adapter.findAll();

        StepVerifier.create(result)
                .expectNext(domain1)
                .verifyComplete();
    }

    @Test
    void mustFindByExample() {
        FranchiseEntity probe = new FranchiseEntity();
        FranchiseEntity entity1 = new FranchiseEntity();
        Franchise domain1 = new Franchise();

        // si tu ReactiveAdapterOperations arma Example desde el DOMAIN:
        when(mapper.map(eq(new Franchise()), eq(FranchiseEntity.class))).thenReturn(probe);

        when(repository.findAll(any(org.springframework.data.domain.Example.class))).thenReturn(Flux.just(entity1));
        when(mapper.map(eq(entity1), eq(Franchise.class))).thenReturn(domain1);

        Flux<Franchise> result = adapter.findByExample(new Franchise());

        StepVerifier.create(result)
                .expectNext(domain1)
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        FranchiseEntity savedEntity = new FranchiseEntity();
        Franchise domainToSave = new Franchise();
        Franchise expectedDomain = new Franchise();

        when(mapper.map(eq(domainToSave), eq(FranchiseEntity.class))).thenReturn(savedEntity);
        when(repository.save(eq(savedEntity))).thenReturn(Mono.just(savedEntity));
        when(mapper.map(eq(savedEntity), eq(Franchise.class))).thenReturn(expectedDomain);

        Mono<Franchise> result = adapter.save(domainToSave);

        StepVerifier.create(result)
                .expectNext(expectedDomain)
                .verifyComplete();
    }
}
