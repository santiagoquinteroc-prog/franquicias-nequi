package com.red.franquicias.nequi.api.handler;

import com.red.franquicias.nequi.api.dto.BranchRequest;
import com.red.franquicias.nequi.api.dto.BranchResponse;
import com.red.franquicias.nequi.api.mapper.BranchMapper;
import com.red.franquicias.nequi.api.validation.RequestValidator;
import com.red.franquicias.nequi.logging.AdapterLogger;
import com.red.franquicias.nequi.usecase.createbranch.CreateBranchNameUseCase;
import com.red.franquicias.nequi.usecase.updatebranch.UpdateBranchNameUseCase;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Component
@Tag(name = "Branches", description = "API for branch management")
public class BranchHandler {
    private final CreateBranchNameUseCase createBranchUseCase;
    private final UpdateBranchNameUseCase updateBranchNameUseCase;
    private final CircuitBreaker branchCircuitBreaker;
    private final RequestValidator requestValidator;
    private final AdapterLogger adapterLogger;

    public BranchHandler(CreateBranchNameUseCase createBranchUseCase,
                        UpdateBranchNameUseCase updateBranchNameUseCase,
                        @Qualifier("branchCircuitBreaker") CircuitBreaker branchCircuitBreaker,
                        RequestValidator requestValidator,
                        AdapterLogger adapterLogger) {
        this.createBranchUseCase = createBranchUseCase;
        this.updateBranchNameUseCase = updateBranchNameUseCase;
        this.branchCircuitBreaker = branchCircuitBreaker;
        this.requestValidator = requestValidator;
        this.adapterLogger = adapterLogger;
    }


    @Operation(summary = "Create branch", description = "Creates a new branch for a franchise")
    @ApiResponse(responseCode = "201", description = "Branch created successfully", content = @Content(schema = @Schema(implementation = BranchResponse.class)))
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "404", description = "Franchise not found")
    @ApiResponse(responseCode = "409", description = "Duplicate branch name")
    public Mono<ServerResponse> create(ServerRequest request) {
        long startTime = adapterLogger.startTimer();
        Long franchiseId = requestValidator.pathLong(request, "franchiseId");
        adapterLogger.inboundStart("BranchHandler", "create", "franchiseId=" + franchiseId);

        return request.bodyToMono(BranchRequest.class)
                .flatMap(requestValidator::validate)
                .flatMap(branchRequest -> {
                    adapterLogger.outboundRequest("CreateBranchUseCase", "create", "name=" + branchRequest.name());
                    var branch = BranchMapper.toDomain(branchRequest, franchiseId);
                    return createBranchUseCase.create(branch)
                            .transformDeferred(CircuitBreakerOperator.of(branchCircuitBreaker));
                })
                .flatMap(branch -> {
                    long duration = adapterLogger.calculateDuration(startTime);
                    adapterLogger.inboundEnd("BranchHandler", "create", "branchId=" + branch.getId(), duration);
                    return ServerResponse.status(201)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(BranchMapper.toResponse(branch));
                });
    }

    @Operation(summary = "Update branch name", description = "Updates the name of an existing branch")
    @ApiResponse(responseCode = "200", description = "Name updated successfully", content = @Content(schema = @Schema(implementation = BranchResponse.class)))
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "404", description = "Franchise or branch not found")
    @ApiResponse(responseCode = "409", description = "Duplicate branch name")
    public Mono<ServerResponse> updateName(ServerRequest request) {
        long startTime = adapterLogger.startTimer();
        Long franchiseId = requestValidator.pathLong(request, "franchiseId");
        Long branchId = requestValidator.pathLong(request, "branchId");
        adapterLogger.inboundStart("BranchHandler", "updateName", "branchId=" + branchId + " franchiseId=" + franchiseId);

        return request.bodyToMono(BranchRequest.class)
                .flatMap(requestValidator::validate)
                .flatMap(body -> {
                    adapterLogger.outboundRequest("UpdateBranchUseCase", "updateName", "name=" + body.name());
                    return updateBranchNameUseCase.updateName(branchId, franchiseId, body.name())
                            .transformDeferred(CircuitBreakerOperator.of(branchCircuitBreaker));
                })
                .flatMap(branch -> {
                    long duration = adapterLogger.calculateDuration(startTime);
                    adapterLogger.inboundEnd("BranchHandler", "updateName", "branchId=" + branch.getId(), duration);
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(BranchMapper.toResponse(branch));
                });
    }
}

