package com.red.franquicias.nequi.usecase.updatefranchise;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import com.red.franquicias.nequi.exception.BusinessException;
import com.red.franquicias.nequi.exception.TechnicalException;
import com.red.franquicias.nequi.model.franchise.Franchise;
import com.red.franquicias.nequi.model.franchise.gateways.FranchiseRepository;
import com.red.franquicias.nequi.usecase.validation.UseCaseValidations;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateFranchiseNameUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> updateName(Long id, String name) {

        return UseCaseValidations.requireNotNull(id, TechnicalMessage.INVALID_FRANCHISE_ID)
                .then(UseCaseValidations.requireNotBlank(name, TechnicalMessage.INVALID_FRANCHISE_NAME))
                .then(Mono.defer(() -> {
                    String normalizedName = name.trim();

                    return franchiseRepository.findByIdFranchise(id)
                            .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND)))
                            .flatMap(existing -> {
                                if (existing.getName() != null &&
                                    existing.getName().trim().equalsIgnoreCase(normalizedName)) {
                                    existing.setName(normalizedName);
                                    return franchiseRepository.saveFranchise(existing);
                                }

                                return franchiseRepository.existsByNameFranchise(normalizedName)
                                        .flatMap(exists -> {
                                            if (exists) {
                                                return Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS));
                                            }
                                            existing.setName(normalizedName);
                                            return franchiseRepository.saveFranchise(existing);
                                        });
                            });
                }))
                .onErrorMap(
                        ex -> !(ex instanceof BusinessException) && !(ex instanceof TechnicalException),
                        ex -> new TechnicalException(ex, TechnicalMessage.FRANCHISE_UPDATE_NAME_ERROR)
                );
    }
}

