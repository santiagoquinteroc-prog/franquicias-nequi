package com.red.franquicias.nequi.usecase.createbranch;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import com.red.franquicias.nequi.exception.BusinessException;
import com.red.franquicias.nequi.exception.TechnicalException;
import com.red.franquicias.nequi.model.branch.Branch;
import com.red.franquicias.nequi.model.branch.gateways.BranchRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateBranchNameUseCase {

    private final BranchRepository branchRepository;

    public Mono<Branch> create(Branch branch) {
        return branchRepository.findByIdBranch(branch.getFranchiseId())
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND)))
                .flatMap(franchise ->
                        branchRepository.existsByNameAndFranchiseId(branch.getName(), branch.getFranchiseId())
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Mono.error(new BusinessException(
                                                TechnicalMessage.BRANCH_NAME_ALREADY_EXISTS
                                        ));
                                    }
                                    return branchRepository.saveBranch(branch);
                                })
                )
                .onErrorMap(
                        ex -> !(ex instanceof BusinessException),
                        ex -> new TechnicalException(ex, TechnicalMessage.BRANCH_CREATE_ERROR)
                );
    }
}
