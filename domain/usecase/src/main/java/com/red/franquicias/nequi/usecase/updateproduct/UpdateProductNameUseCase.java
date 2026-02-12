package com.red.franquicias.nequi.usecase.updateproduct;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import com.red.franquicias.nequi.exception.BusinessException;
import com.red.franquicias.nequi.exception.TechnicalException;
import com.red.franquicias.nequi.model.branch.gateways.BranchRepository;
import com.red.franquicias.nequi.model.franchise.gateways.FranchiseRepository;
import com.red.franquicias.nequi.model.product.Product;
import com.red.franquicias.nequi.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import com.red.franquicias.nequi.usecase.validation.UseCaseValidations;

@RequiredArgsConstructor
public class UpdateProductNameUseCase {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;


    public Mono<Product> updateName(Long productId, Long branchId, Long franchiseId, String name) {

        return UseCaseValidations.requireNotNull(productId, TechnicalMessage.INVALID_PRODUCT_ID)
                .then(UseCaseValidations.requireNotNull(branchId, TechnicalMessage.INVALID_BRANCH_ID))
                .then(UseCaseValidations.requireNotNull(franchiseId, TechnicalMessage.INVALID_FRANCHISE_ID))
                .then(UseCaseValidations.requireNotBlank(name, TechnicalMessage.INVALID_PRODUCT_NAME))
                .then(Mono.defer(() -> {
                    String normalizedName = name.trim();

                    return franchiseRepository.findByIdFranchise(franchiseId)
                            .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND)))
                            .flatMap(franchise -> branchRepository.findByIdAndFranchiseId(branchId, franchiseId)
                                    .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BRANCH_NOT_FOUND)))
                            )
                            .flatMap(branch -> productRepository.findByIdAndBranchId(productId, branchId)
                                    .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND)))
                            )
                            .flatMap(existing -> {
                                if (existing.getName() != null &&
                                    existing.getName().trim().equalsIgnoreCase(normalizedName)) {
                                    existing.setName(normalizedName);
                                    return productRepository.saveProduct(existing);
                                }

                                return productRepository.existsByNameAndBranchId(normalizedName, branchId)
                                        .flatMap(exists -> {
                                            if (exists) {
                                                return Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NAME_ALREADY_EXISTS));
                                            }
                                            existing.setName(normalizedName);
                                            return productRepository.saveProduct(existing);
                                        });
                            });
                }))
                .onErrorMap(
                        ex -> !(ex instanceof BusinessException) && !(ex instanceof TechnicalException),
                        ex -> new TechnicalException(ex, TechnicalMessage.PRODUCT_UPDATE_NAME_ERROR)
                );
    }
}
