package com.red.franquicias.nequi.usecase.createfranchise;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import com.red.franquicias.nequi.exception.BusinessException;
import com.red.franquicias.nequi.exception.TechnicalException;
import com.red.franquicias.nequi.model.franchise.Franchise;
import com.red.franquicias.nequi.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateFranchiseUseCase {

    private final FranchiseRepository franchiseRepository;


    public Mono<Franchise> create(Franchise franchise) {
        return franchiseRepository.existsByNameFranchise(franchise.getName())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new BusinessException(
                                TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS
                        ));
                    }
                    return franchiseRepository.saveFranchise(franchise);
                })
                .onErrorMap(
                        ex -> !(ex instanceof BusinessException),
                        ex -> new TechnicalException(ex, TechnicalMessage.FRANCHISE_CREATE_ERROR)
                );
    }
}
