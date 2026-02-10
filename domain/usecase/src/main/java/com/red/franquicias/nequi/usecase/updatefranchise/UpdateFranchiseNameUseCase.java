package com.red.franquicias.nequi.usecase.updatefranchise;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import com.red.franquicias.nequi.exception.BusinessException;
import com.red.franquicias.nequi.exception.TechnicalException;
import com.red.franquicias.nequi.model.franchise.Franchise;
import com.red.franquicias.nequi.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateFranchiseNameUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> updateName(Long id, String name) {
        return franchiseRepository.findByIdFranchise(id)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND)))
                .flatMap(existing ->
                        franchiseRepository.existsByNameFranchise(name)
                                .flatMap(exists -> {
                                    if (exists && !existing.getName().equals(name)) {
                                        return Mono.error(new BusinessException(
                                                TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS
                                        ));
                                    }
                                    existing.setName(name);
                                    return franchiseRepository.saveFranchise(existing);
                                })
                )
                .onErrorMap(
                        ex -> !(ex instanceof BusinessException),
                        ex -> new TechnicalException(ex, TechnicalMessage.FRANCHISE_UPDATE_NAME_ERROR)
                );
    }
}
