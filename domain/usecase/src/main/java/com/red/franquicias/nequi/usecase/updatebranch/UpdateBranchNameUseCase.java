package com.red.franquicias.nequi.usecase.updatebranch;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import com.red.franquicias.nequi.exception.BusinessException;
import com.red.franquicias.nequi.exception.TechnicalException;
import com.red.franquicias.nequi.model.branch.Branch;
import com.red.franquicias.nequi.model.branch.gateways.BranchRepository;
import com.red.franquicias.nequi.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateBranchNameUseCase {

    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    public Mono<Branch> updateName(
            Long branchId,
            Long franchiseId,
            String name
    ) {
        return franchiseRepository.findByIdFranchise(franchiseId)
                .switchIfEmpty(Mono.error(
                        new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND)
                ))
                .flatMap(franchise ->
                        branchRepository.findByIdAndFranchiseId(branchId, franchiseId)
                                .switchIfEmpty(Mono.error(
                                        new BusinessException(TechnicalMessage.BRANCH_NOT_FOUND)
                                ))
                                .flatMap(existing ->
                                        branchRepository.existsByNameAndFranchiseId(name, franchiseId)
                                                .flatMap(exists -> {
                                                    if (exists && !existing.getName().equals(name)) {
                                                        return Mono.error(new BusinessException(
                                                                TechnicalMessage.BRANCH_NAME_ALREADY_EXISTS
                                                        ));
                                                    }
                                                    existing.setName(name);
                                                    return branchRepository.saveBranch(existing);
                                                })
                                )
                )
                .onErrorMap(
                        ex -> !(ex instanceof BusinessException),
                        ex -> new TechnicalException(ex, TechnicalMessage.BRANCH_UPDATE_ERROR)
                );
    }

}
