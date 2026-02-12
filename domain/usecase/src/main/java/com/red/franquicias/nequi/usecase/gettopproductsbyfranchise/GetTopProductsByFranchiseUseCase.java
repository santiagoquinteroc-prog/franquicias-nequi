package com.red.franquicias.nequi.usecase.gettopproductsbyfranchise;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import com.red.franquicias.nequi.exception.BusinessException;
import com.red.franquicias.nequi.exception.TechnicalException;
import com.red.franquicias.nequi.model.product.BranchTopProduct;
import com.red.franquicias.nequi.model.product.BranchTopProductRow;
import com.red.franquicias.nequi.model.product.ProductInfo;
import com.red.franquicias.nequi.model.product.TopProductsResult;
import com.red.franquicias.nequi.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import com.red.franquicias.nequi.usecase.validation.UseCaseValidations;
import java.util.List;

@RequiredArgsConstructor
public class GetTopProductsByFranchiseUseCase {

    private final ProductRepository productRepository;


    public Mono<TopProductsResult> getTopProducts(Long franchiseId) {

        return UseCaseValidations.requireNotNull(franchiseId, TechnicalMessage.INVALID_FRANCHISE_ID)
                .then(productRepository.findTopProductsByFranchiseId(franchiseId)
                        .collectList()
                        .flatMap(rows -> {
                            if (rows.isEmpty()) {
                                return Mono.error(new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND));
                            }

                            BranchTopProductRow first = rows.get(0);

                            List<BranchTopProduct> results = rows.stream()
                                    .filter(r -> r.product_id() != null)
                                    .map(r -> new BranchTopProduct(
                                            r.branch_id(),
                                            r.branch_name(),
                                            new ProductInfo(
                                                    r.product_id(),
                                                    r.product_name(),
                                                    r.stock()
                                            )
                                    ))
                                    .toList();

                            return Mono.just(new TopProductsResult(
                                    first.franchise_id(),
                                    first.franchise_name(),
                                    results
                            ));
                        })
                )
                .onErrorMap(
                        ex -> !(ex instanceof BusinessException) && !(ex instanceof TechnicalException),
                        ex -> new TechnicalException(ex, TechnicalMessage.TOP_PRODUCTS_QUERY_ERROR)
                );
    }
}
