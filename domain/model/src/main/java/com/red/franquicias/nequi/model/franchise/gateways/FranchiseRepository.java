package com.red.franquicias.nequi.model.franchise.gateways;

import com.red.franquicias.nequi.model.franchise.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {
    Mono<Franchise> saveFranchise(Franchise franchise);

    Mono<Franchise> findByIdFranchise(Long id);

    Mono<Boolean> existsByNameFranchise(String name);
}
