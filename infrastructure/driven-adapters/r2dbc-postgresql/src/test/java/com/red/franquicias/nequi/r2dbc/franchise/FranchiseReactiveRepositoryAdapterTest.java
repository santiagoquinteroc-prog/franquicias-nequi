package com.red.franquicias.nequi.r2dbc.franchise;

import com.red.franquicias.nequi.logging.AdapterLogger;
import com.red.franquicias.nequi.model.franchise.Franchise;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FranchiseReactiveRepositoryAdapterTest {

    @Mock
    FranchiseReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @Mock
    AdapterLogger adapterLogger;

    FranchiseReactiveRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new FranchiseReactiveRepositoryAdapter(repository, mapper, adapterLogger);
        when(adapterLogger.startTimer()).thenReturn(0L);
        when(adapterLogger.calculateDuration(anyLong())).thenReturn(100L);
        org.mockito.Mockito.lenient().doNothing().when(adapterLogger).outboundRequest(any(), any(), any());
        org.mockito.Mockito.lenient().doNothing().when(adapterLogger).outboundResponse(any(), any(), any(), anyLong());
        org.mockito.Mockito.lenient().doNothing().when(adapterLogger).error(any(), any(), any(Throwable.class), any());
    }

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

    @Test
    void saveFranchise_shouldSaveAndReturnFranchise() {
        FranchiseEntity savedEntity = new FranchiseEntity();
        Franchise domainToSave = new Franchise();
        Franchise expectedDomain = new Franchise();

        when(mapper.map(eq(domainToSave), eq(FranchiseEntity.class))).thenReturn(savedEntity);
        when(repository.save(eq(savedEntity))).thenReturn(Mono.just(savedEntity));
        when(mapper.map(eq(savedEntity), eq(Franchise.class))).thenReturn(expectedDomain);

        Mono<Franchise> result = adapter.saveFranchise(domainToSave);

        StepVerifier.create(result)
                .expectNext(expectedDomain)
                .verifyComplete();
    }

    @Test
    void findByIdFranchise_shouldReturnFranchise() {
        FranchiseEntity entity = new FranchiseEntity();
        Franchise domain = new Franchise();

        when(repository.findById(1L)).thenReturn(Mono.just(entity));
        when(mapper.map(eq(entity), eq(Franchise.class))).thenReturn(domain);

        Mono<Franchise> result = adapter.findByIdFranchise(1L);

        StepVerifier.create(result)
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void existsByNameFranchise_shouldReturnTrue() {
        FranchiseEntity entity = new FranchiseEntity();

        when(repository.findByName("Franchise A")).thenReturn(Mono.just(entity));

        Mono<Boolean> result = adapter.existsByNameFranchise("Franchise A");

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void existsByNameFranchise_shouldReturnFalse() {
        when(repository.findByName("Franchise A")).thenReturn(Mono.empty());

        Mono<Boolean> result = adapter.existsByNameFranchise("Franchise A");

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
    }
}
