package com.red.franquicias.nequi.usecase.removeproduct;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import com.red.franquicias.nequi.exception.BusinessException;

import com.red.franquicias.nequi.model.branch.gateways.BranchRepository;
import com.red.franquicias.nequi.model.franchise.gateways.FranchiseRepository;

import com.red.franquicias.nequi.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class RemoveProductUseCase {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    public Mono<Void> remove(Long productId, Long branchId, Long franchiseId) {

        return franchiseRepository.findByIdFranchise(franchiseId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND)))
                .flatMap(franchise ->
                        branchRepository.findByIdAndFranchiseId(branchId, franchiseId)
                                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BRANCH_NOT_FOUND)))
                                .flatMap(branch ->
                                        productRepository.findByIdAndBranchId(productId, branchId)
                                                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND)))
                                                .flatMap(product -> productRepository.deleteById(productId))
                                )
                );
    }
}
