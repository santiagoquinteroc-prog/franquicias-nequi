package com.red.franquicias.nequi.r2dbc.branch;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface BranchReactiveRepository extends ReactiveCrudRepository<BranchEntity, Long>, ReactiveQueryByExampleExecutor<BranchEntity> {

    Mono<BranchEntity> findByIdAndFranchiseId(Long id, Long franchiseId);

    Mono<BranchEntity> findByNameAndFranchiseId(String name, Long franchiseId);

    Flux<BranchEntity> findByFranchiseId(Long franchiseId);

}
