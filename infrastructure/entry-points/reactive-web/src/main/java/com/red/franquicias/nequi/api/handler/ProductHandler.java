package com.red.franquicias.nequi.api.handler;

import com.red.franquicias.nequi.api.dto.*;
import com.red.franquicias.nequi.api.mapper.ProductMapper;
import com.red.franquicias.nequi.api.validation.RequestValidator;
import com.red.franquicias.nequi.logging.AdapterLogger;
import com.red.franquicias.nequi.model.product.Product;
import com.red.franquicias.nequi.usecase.createproduct.CreateProductNameUseCase;
import com.red.franquicias.nequi.usecase.gettopproductsbyfranchise.GetTopProductsByFranchiseUseCase;
import com.red.franquicias.nequi.usecase.removeproduct.RemoveProductUseCase;
import com.red.franquicias.nequi.usecase.updateproduct.UpdateProductNameUseCase;
import com.red.franquicias.nequi.usecase.updateproductstock.UpdateProductStockUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Tag(name = "Products", description = "API for product management")
public class ProductHandler {
    private final CreateProductNameUseCase createProductUseCase;
    private final UpdateProductNameUseCase updateProductNameUseCase;
    private final UpdateProductStockUseCase updateProductStockUseCase;
    private final RemoveProductUseCase removeProductUseCase;
    private final GetTopProductsByFranchiseUseCase getTopProductsByFranchiseUseCase;
    private final RequestValidator requestValidator;
    private final AdapterLogger adapterLogger;

    public ProductHandler(CreateProductNameUseCase createProductUseCase, UpdateProductNameUseCase updateProductNameUseCase, UpdateProductStockUseCase updateProductStockUseCase, RemoveProductUseCase removeProductUseCase, GetTopProductsByFranchiseUseCase getTopProductsByFranchiseUseCase, RequestValidator requestValidator, AdapterLogger adapterLogger) {
        this.createProductUseCase = createProductUseCase;
        this.updateProductNameUseCase = updateProductNameUseCase;
        this.updateProductStockUseCase = updateProductStockUseCase;
        this.removeProductUseCase = removeProductUseCase;
        this.getTopProductsByFranchiseUseCase = getTopProductsByFranchiseUseCase;
        this.requestValidator = requestValidator;
        this.adapterLogger = adapterLogger;
    }

    @Operation(summary = "Create product", description = "Creates a new product in a branch")
    @ApiResponse(responseCode = "201", description = "Product created successfully", content = @Content(schema = @Schema(implementation = ProductResponse.class)))
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "404", description = "Franchise or branch not found")
    @ApiResponse(responseCode = "409", description = "Duplicate product name in branch")
    public Mono<ServerResponse> create(ServerRequest request) {
        long startTime = adapterLogger.startTimer();
        Long franchiseId = requestValidator.pathLong(request, "franchiseId");
        Long branchId = requestValidator.pathLong(request, "branchId");
        adapterLogger.inboundStart("ProductHandler", "create", "franchiseId=" + franchiseId + " branchId=" + branchId);

        return request.bodyToMono(ProductRequest.class)
                .flatMap(requestValidator::validate)
                .flatMap(productRequest -> {
                    adapterLogger.outboundRequest("CreateProductUseCase", "create", "name=" + productRequest.name());
                    Product product = ProductMapper.toDomain(productRequest, branchId);
                    return createProductUseCase.create(product, franchiseId);
                })
                .flatMap(product -> {
                    long duration = adapterLogger.calculateDuration(startTime);
                    adapterLogger.inboundEnd("ProductHandler", "create", "productId=" + product.getId(), duration);
                    return ServerResponse.status(HttpStatus.CREATED)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(ProductMapper.toResponse(product));
                });
    }

    @Operation(summary = "Update product name", description = "Updates the name of an existing product")
    @ApiResponse(responseCode = "200", description = "Name updated successfully", content = @Content(schema = @Schema(implementation = ProductResponse.class)))
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "404", description = "Franchise, branch or product not found")
    @ApiResponse(responseCode = "409", description = "Duplicate product name in branch")
    public Mono<ServerResponse> updateName(ServerRequest request) {
        long startTime = adapterLogger.startTimer();
        Long franchiseId = requestValidator.pathLong(request, "franchiseId");
        Long branchId = requestValidator.pathLong(request, "branchId");
        Long productId = requestValidator.pathLong(request, "productId");
        adapterLogger.inboundStart("ProductHandler", "updateName", "productId=" + productId + " branchId=" + branchId);

        return request.bodyToMono(UpdateProductNameRequest.class)
                .flatMap(requestValidator::validate)
                .flatMap(updateRequest -> {
                    adapterLogger.outboundRequest("UpdateProductNameUseCase", "updateName", "name=" + updateRequest.name());
                    return updateProductNameUseCase.updateName(productId, branchId, franchiseId, updateRequest.name());
                })
                .flatMap(product -> {
                    long duration = adapterLogger.calculateDuration(startTime);
                    adapterLogger.inboundEnd("ProductHandler", "updateName", "productId=" + product.getId(), duration);
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(ProductMapper.toResponse(product));
                });
    }


    @Operation(summary = "Update product stock", description = "Updates the stock of an existing product")
    @ApiResponse(responseCode = "200", description = "Stock updated successfully", content = @Content(schema = @Schema(implementation = ProductResponse.class)))
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "404", description = "Franchise, branch or product not found")
    public Mono<ServerResponse> updateStock(ServerRequest request) {
        long startTime = adapterLogger.startTimer();
        Long franchiseId = requestValidator.pathLong(request, "franchiseId");
        Long branchId = requestValidator.pathLong(request, "branchId");
        Long productId = requestValidator.pathLong(request, "productId");
        adapterLogger.inboundStart("ProductHandler", "updateStock", "productId=" + productId + " branchId=" + branchId);

        return request.bodyToMono(UpdateProductStockRequest.class)
                .flatMap(requestValidator::validate)
                .flatMap(updateRequest -> {
                    adapterLogger.outboundRequest("UpdateProductStockUseCase", "updateStock", "stock=" + updateRequest.stock());
                    return updateProductStockUseCase.updateStock(productId, branchId, franchiseId, updateRequest.stock());
                })
                .flatMap(product -> {
                    long duration = adapterLogger.calculateDuration(startTime);
                    adapterLogger.inboundEnd("ProductHandler", "updateStock", "productId=" + product.getId(), duration);
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(ProductMapper.toResponse(product));
                });
    }

    @Operation(summary = "Delete product", description = "Deletes a product from a branch")
    @ApiResponse(responseCode = "204", description = "Product deleted successfully")
    @ApiResponse(responseCode = "404", description = "Franchise, branch or product not found")
    public Mono<ServerResponse> remove(ServerRequest request) {
        long startTime = adapterLogger.startTimer();
        Long franchiseId = requestValidator.pathLong(request, "franchiseId");
        Long branchId = requestValidator.pathLong(request, "branchId");
        Long productId = requestValidator.pathLong(request, "productId");
        adapterLogger.inboundStart("ProductHandler", "remove", "productId=" + productId);

        return removeProductUseCase.remove(productId, branchId, franchiseId)
                .then(Mono.defer(() -> {
                    long duration = adapterLogger.calculateDuration(startTime);
                    adapterLogger.inboundEnd("ProductHandler", "remove", "productId=" + productId, duration);
                    return ServerResponse.noContent().build();
                }));
    }


    @Operation(summary = "Get top products by branch", description = "Gets the product with the highest stock for each branch of a franchise")
    @ApiResponse(responseCode = "200", description = "Top products list retrieved successfully", content = @Content(schema = @Schema(implementation = TopProductsResponse.class)))
    @ApiResponse(responseCode = "404", description = "Franchise not found")
    public Mono<ServerResponse> getTopProducts(ServerRequest request) {
        long startTime = adapterLogger.startTimer();
        Long franchiseId = requestValidator.pathLong(request, "franchiseId");
        adapterLogger.inboundStart("ProductHandler", "getTopProducts", "franchiseId=" + franchiseId);

        return getTopProductsByFranchiseUseCase.getTopProducts(franchiseId)
                .map(result -> {
                    adapterLogger.outboundResponse("GetTopProductsByFranchiseUseCase", "getTopProducts", "result=success", 0);
                    return ProductMapper.toTopProductsResponse(result);
                })
                .flatMap(response -> {
                    long duration = adapterLogger.calculateDuration(startTime);
                    adapterLogger.inboundEnd("ProductHandler", "getTopProducts", "franchiseId=" + franchiseId, duration);
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(response);
                });
    }
}

