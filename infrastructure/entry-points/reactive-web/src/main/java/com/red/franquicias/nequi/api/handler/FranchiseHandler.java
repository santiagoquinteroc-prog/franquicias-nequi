package com.red.franquicias.nequi.api.handler;

import com.red.franquicias.nequi.api.dto.FranchiseRequest;
import com.red.franquicias.nequi.api.dto.FranchiseResponse;
import com.red.franquicias.nequi.api.mapper.FranchiseMapper;
import com.red.franquicias.nequi.api.validation.RequestValidator;
import com.red.franquicias.nequi.model.franchise.Franchise;
import com.red.franquicias.nequi.usecase.createfranchise.CreateFranchiseUseCase;
import com.red.franquicias.nequi.usecase.updatefranchise.UpdateFranchiseNameUseCase;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Tag(name = "Franchises", description = "API for franchise management")
public class FranchiseHandler {

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final UpdateFranchiseNameUseCase updateFranchiseNameUseCase;
    private final CircuitBreaker franchiseCircuitBreaker;
    private final RequestValidator requestValidator;

    public FranchiseHandler(CreateFranchiseUseCase createFranchiseUseCase,
                            UpdateFranchiseNameUseCase updateFranchiseNameUseCase,
                            @Qualifier("franchiseCircuitBreaker") CircuitBreaker franchiseCircuitBreaker, RequestValidator requestValidator) {
        this.createFranchiseUseCase = createFranchiseUseCase;
        this.updateFranchiseNameUseCase = updateFranchiseNameUseCase;
        this.franchiseCircuitBreaker = franchiseCircuitBreaker;
        this.requestValidator = requestValidator;
    }

    @Operation(summary = "Create franchise", description = "Creates a new franchise")
    @ApiResponse(responseCode = "201", description = "Franchise created successfully",
            content = @Content(schema = @Schema(implementation = FranchiseResponse.class)))
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "409", description = "Duplicate franchise name")
    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(FranchiseRequest.class)
                .flatMap(requestValidator::validate)
                .flatMap(franchiseRequest -> {
                    String cid = MDC.get("correlationId");
                    log.info("[{}] handler create franchise {}", cid, franchiseRequest.name());

                    Franchise franchise = FranchiseMapper.toDomain(franchiseRequest);

                    return createFranchiseUseCase.create(franchise)
                            .transformDeferred(CircuitBreakerOperator.of(franchiseCircuitBreaker));
                })
                .flatMap(franchise -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(FranchiseMapper.toResponse(franchise)));
    }

    @Operation(summary = "Update franchise name", description = "Updates the name of an existing franchise")
    @ApiResponse(responseCode = "200", description = "Name updated successfully",
            content = @Content(schema = @Schema(implementation = FranchiseResponse.class)))
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "404", description = "Franchise not found")
    @ApiResponse(responseCode = "409", description = "Duplicate franchise name")
    public Mono<ServerResponse> updateName(ServerRequest request) {
        Long id = requestValidator.pathLong(request, "id");

        return request.bodyToMono(FranchiseRequest.class)
                .flatMap(requestValidator::validate)
                .flatMap(franchiseRequest ->
                        updateFranchiseNameUseCase.updateName(id, franchiseRequest.name())
                                .transformDeferred(CircuitBreakerOperator.of(franchiseCircuitBreaker))
                )
                .flatMap(franchise -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(FranchiseMapper.toResponse(franchise)));
    }
}
