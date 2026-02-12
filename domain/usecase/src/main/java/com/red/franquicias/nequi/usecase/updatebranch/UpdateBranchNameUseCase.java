package com.red.franquicias.nequi.usecase.updatebranch;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import com.red.franquicias.nequi.exception.BusinessException;
import com.red.franquicias.nequi.exception.TechnicalException;
import com.red.franquicias.nequi.model.branch.Branch;
import com.red.franquicias.nequi.model.branch.gateways.BranchRepository;
import com.red.franquicias.nequi.model.franchise.gateways.FranchiseRepository;
import com.red.franquicias.nequi.usecase.validation.UseCaseValidations;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateBranchNameUseCase {

    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    public Mono<Branch> updateName(Long branchId, Long franchiseId, String name) {

        return UseCaseValidations.requireNotNull(branchId, TechnicalMessage.INVALID_BRANCH_ID)
                .then(UseCaseValidations.requireNotNull(franchiseId, TechnicalMessage.INVALID_FRANCHISE_ID))
                .then(UseCaseValidations.requireNotBlank(name, TechnicalMessage.INVALID_BRANCH_NAME))
                .then(Mono.defer(() -> {
                    String normalizedName = name.trim();

                    return franchiseRepository.findByIdFranchise(franchiseId)
                            .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND)))
                            .then(branchRepository.findByIdAndFranchiseId(branchId, franchiseId)
                                    .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BRANCH_NOT_FOUND)))
                            )
                            .flatMap(existing -> {

                                if (existing.getName() != null && existing.getName().trim().equalsIgnoreCase(normalizedName)) {
                                    existing.setName(normalizedName);
                                    return branchRepository.saveBranch(existing);
                                }

                                return branchRepository.existsByNameAndFranchiseId(normalizedName, franchiseId)
                                        .flatMap(exists -> {
                                            if (exists) {
                                                return Mono.error(new BusinessException(TechnicalMessage.BRANCH_NAME_ALREADY_EXISTS));
                                            }
                                            existing.setName(normalizedName);
                                            return branchRepository.saveBranch(existing);
                                        });
                            });
                }))
                .onErrorMap(
                        ex -> !(ex instanceof BusinessException) && !(ex instanceof TechnicalException),
                        ex -> new TechnicalException(ex, TechnicalMessage.BRANCH_UPDATE_ERROR)
                );
    }
}
