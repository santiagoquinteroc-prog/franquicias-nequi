package com.red.franquicias.nequi.usecase.createproduct;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import com.red.franquicias.nequi.exception.BusinessException;
import com.red.franquicias.nequi.exception.TechnicalException;
import com.red.franquicias.nequi.model.branch.gateways.BranchRepository;
import com.red.franquicias.nequi.model.franchise.gateways.FranchiseRepository;
import com.red.franquicias.nequi.model.product.Product;
import com.red.franquicias.nequi.usecase.validation.UseCaseValidations;
import com.red.franquicias.nequi.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateProductNameUseCase {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;


    public Mono<Product> create(Product product, Long franchiseId) {

        return UseCaseValidations.requireNotNull(product, TechnicalMessage.INVALID_PRODUCT_NAME)
                .then(UseCaseValidations.requireNotNull(franchiseId, TechnicalMessage.INVALID_FRANCHISE_ID))
                .then(UseCaseValidations.requireNotNull(product.getBranchId(), TechnicalMessage.INVALID_BRANCH_ID))
                .then(UseCaseValidations.requireNotBlank(product.getName(), TechnicalMessage.INVALID_PRODUCT_NAME))
                .then(Mono.defer(() -> {
                    product.setName(product.getName().trim());

                    return franchiseRepository.findByIdFranchise(franchiseId)
                            .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND)))
                            .flatMap(franchise -> branchRepository.findByIdAndFranchiseId(product.getBranchId(), franchiseId)
                                    .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BRANCH_NOT_FOUND)))
                            )
                            .flatMap(branch -> productRepository.existsByNameAndBranchId(product.getName(), product.getBranchId()))
                            .flatMap(exists -> {
                                if (exists) {
                                    return Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NAME_ALREADY_EXISTS));
                                }
                                return productRepository.saveProduct(product);
                            });
                }))
                .onErrorMap(
                        ex -> !(ex instanceof BusinessException) && !(ex instanceof TechnicalException),
                        ex -> new TechnicalException(ex, TechnicalMessage.PRODUCT_CREATE_ERROR)
                );
    }
}
