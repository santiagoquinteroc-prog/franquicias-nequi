package com.red.franquicias.nequi.r2dbc.branch;

import com.red.franquicias.nequi.model.branch.Branch;
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
class BranchReactiveRepositoryAdapterTest {

    @Mock
    BranchReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @InjectMocks
    BranchReactiveRepositoryAdapter adapter;

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
}
