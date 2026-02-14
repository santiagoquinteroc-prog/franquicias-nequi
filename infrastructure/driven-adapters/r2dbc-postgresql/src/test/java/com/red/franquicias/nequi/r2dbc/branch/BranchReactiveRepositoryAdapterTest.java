package com.red.franquicias.nequi.r2dbc.branch;

import com.red.franquicias.nequi.logging.AdapterLogger;
import com.red.franquicias.nequi.model.branch.Branch;
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
class BranchReactiveRepositoryAdapterTest {

    @Mock
    BranchReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @Mock
    AdapterLogger adapterLogger;

    BranchReactiveRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new BranchReactiveRepositoryAdapter(repository, mapper, adapterLogger);
        when(adapterLogger.startTimer()).thenReturn(0L);
        when(adapterLogger.calculateDuration(anyLong())).thenReturn(100L);
        org.mockito.Mockito.lenient().doNothing().when(adapterLogger).outboundRequest(any(), any(), any());
        org.mockito.Mockito.lenient().doNothing().when(adapterLogger).outboundResponse(any(), any(), any(), anyLong());
        org.mockito.Mockito.lenient().doNothing().when(adapterLogger).error(any(), any(), any(Throwable.class), any());
    }

    @Test
    void mustFindValueById() {

        BranchEntity entity = new BranchEntity();
        Branch domain = new Branch();

        when(repository.findById(1L)).thenReturn(Mono.just(entity));
        when(mapper.map(eq(entity), eq(Branch.class))).thenReturn(domain);


        Mono<Branch> result = adapter.findById(1L);


        StepVerifier.create(result)
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        BranchEntity entity1 = new BranchEntity();
        Branch domain1 = new Branch();

        when(repository.findAll()).thenReturn(Flux.just(entity1));
        when(mapper.map(eq(entity1), eq(Branch.class))).thenReturn(domain1);

        Flux<Branch> result = adapter.findAll();

        StepVerifier.create(result)
                .expectNext(domain1)
                .verifyComplete();
    }

    @Test
    void mustFindByExample() {
        BranchEntity probe = new BranchEntity();
        BranchEntity entity1 = new BranchEntity();
        Branch domain1 = new Branch();


        when(mapper.map(eq(new Branch()), eq(BranchEntity.class))).thenReturn(probe);
        when(repository.findAll(any(org.springframework.data.domain.Example.class))).thenReturn(Flux.just(entity1));
        when(mapper.map(eq(entity1), eq(Branch.class))).thenReturn(domain1);


        Flux<Branch> result = adapter.findByExample(new Branch());

        StepVerifier.create(result)
                .expectNext(domain1)
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        BranchEntity savedEntity = new BranchEntity();
        Branch domainToSave = new Branch();
        Branch expectedDomain = new Branch();


        when(mapper.map(eq(domainToSave), eq(BranchEntity.class))).thenReturn(savedEntity);
        when(repository.save(eq(savedEntity))).thenReturn(Mono.just(savedEntity));
        when(mapper.map(eq(savedEntity), eq(Branch.class))).thenReturn(expectedDomain);

        Mono<Branch> result = adapter.save(domainToSave);

        StepVerifier.create(result)
                .expectNext(expectedDomain)
                .verifyComplete();
    }

    @Test
    void saveBranch_shouldSaveAndReturnBranch() {
        BranchEntity savedEntity = new BranchEntity();
        Branch domainToSave = new Branch();
        Branch expectedDomain = new Branch();

        when(mapper.map(eq(domainToSave), eq(BranchEntity.class))).thenReturn(savedEntity);
        when(repository.save(eq(savedEntity))).thenReturn(Mono.just(savedEntity));
        when(mapper.map(eq(savedEntity), eq(Branch.class))).thenReturn(expectedDomain);

        Mono<Branch> result = adapter.saveBranch(domainToSave);

        StepVerifier.create(result)
                .expectNext(expectedDomain)
                .verifyComplete();
    }

    @Test
    void findByIdBranch_shouldReturnBranch() {
        BranchEntity entity = new BranchEntity();
        Branch domain = new Branch();

        when(repository.findById(1L)).thenReturn(Mono.just(entity));
        when(mapper.map(eq(entity), eq(Branch.class))).thenReturn(domain);

        Mono<Branch> result = adapter.findByIdBranch(1L);

        StepVerifier.create(result)
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void findByIdAndFranchiseId_shouldReturnBranch() {
        BranchEntity entity = new BranchEntity();
        Branch domain = new Branch();

        when(repository.findByIdAndFranchiseId(1L, 10L)).thenReturn(Mono.just(entity));
        when(mapper.map(eq(entity), eq(Branch.class))).thenReturn(domain);

        Mono<Branch> result = adapter.findByIdAndFranchiseId(1L, 10L);

        StepVerifier.create(result)
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void existsByNameAndFranchiseId_shouldReturnTrue() {
        BranchEntity entity = new BranchEntity();

        when(repository.findByNameAndFranchiseId("Branch A", 10L)).thenReturn(Mono.just(entity));

        Mono<Boolean> result = adapter.existsByNameAndFranchiseId("Branch A", 10L);

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void existsByNameAndFranchiseId_shouldReturnFalse() {
        when(repository.findByNameAndFranchiseId("Branch A", 10L)).thenReturn(Mono.empty());

        Mono<Boolean> result = adapter.existsByNameAndFranchiseId("Branch A", 10L);

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void findByFranchiseId_shouldReturnBranches() {
        BranchEntity entity1 = new BranchEntity();
        Branch domain1 = new Branch();

        when(repository.findByFranchiseId(10L)).thenReturn(Flux.just(entity1));
        when(mapper.map(eq(entity1), eq(Branch.class))).thenReturn(domain1);

        Flux<Branch> result = adapter.findByFranchiseId(10L);

        StepVerifier.create(result)
                .expectNext(domain1)
                .verifyComplete();
    }
}
