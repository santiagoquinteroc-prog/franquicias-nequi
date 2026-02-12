package com.red.franquicias.nequi.usecase.updateproductstock;

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
public class UpdateProductStockUseCase {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    public Mono<Product> updateStock(Long productId, Long branchId, Long franchiseId, Integer stock) {

        return UseCaseValidations.requireNotNull(productId, TechnicalMessage.INVALID_PRODUCT_ID)
                .then(UseCaseValidations.requireNotNull(branchId, TechnicalMessage.INVALID_BRANCH_ID))
                .then(UseCaseValidations.requireNotNull(franchiseId, TechnicalMessage.INVALID_FRANCHISE_ID))
                .then(UseCaseValidations.requireNotNull(stock, TechnicalMessage.INVALID_PRODUCT_STOCK))
                .then(Mono.defer(() -> {
                    if (stock < 0) {
                        return Mono.error(new BusinessException(TechnicalMessage.INVALID_PRODUCT_STOCK_NEGATIVE));
                    }

                    return franchiseRepository.findByIdFranchise(franchiseId)
                            .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND)))
                            .flatMap(franchise -> branchRepository.findByIdAndFranchiseId(branchId, franchiseId)
                                    .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BRANCH_NOT_FOUND)))
                            )
                            .flatMap(branch -> productRepository.findByIdAndBranchId(productId, branchId)
                                    .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PRODUCT_NOT_FOUND)))
                            )
                            .flatMap(existing -> {
                                existing.setStock(stock);
                                return productRepository.saveProduct(existing);
                            });
                }))
                .onErrorMap(
                        ex -> !(ex instanceof BusinessException) && !(ex instanceof TechnicalException),
                        ex -> new TechnicalException(ex, TechnicalMessage.PRODUCT_UPDATE_STOCK_ERROR)
                );
    }
}
