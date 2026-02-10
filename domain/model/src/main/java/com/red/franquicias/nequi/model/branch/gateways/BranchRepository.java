package com.red.franquicias.nequi.model.branch.gateways;

import com.red.franquicias.nequi.model.branch.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository {

    Mono<Branch> saveBranch(Branch branch);

    Mono<Branch> findByIdBranch(Long id);

    Mono<Branch> findByIdAndFranchiseId(Long id, Long franchiseId);

    Mono<Boolean> existsByNameAndFranchiseId(String name, Long franchiseId);

    Flux<Branch> findByFranchiseId(Long franchiseId);
}
