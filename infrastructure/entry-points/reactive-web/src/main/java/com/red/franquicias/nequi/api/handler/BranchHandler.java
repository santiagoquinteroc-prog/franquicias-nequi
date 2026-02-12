package com.red.franquicias.nequi.api.handler;


import com.red.franquicias.nequi.api.dto.BranchRequest;
import com.red.franquicias.nequi.api.dto.BranchResponse;
import com.red.franquicias.nequi.api.mapper.BranchMapper;
import com.red.franquicias.nequi.api.validation.RequestValidator;
import com.red.franquicias.nequi.usecase.createbranch.CreateBranchNameUseCase;
import com.red.franquicias.nequi.usecase.updatebranch.UpdateBranchNameUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private final RequestValidator requestValidator;

    public BranchHandler(CreateBranchNameUseCase createBranchUseCase, UpdateBranchNameUseCase updateBranchNameUseCase, RequestValidator requestValidator) {
        this.createBranchUseCase = createBranchUseCase;
        this.updateBranchNameUseCase = updateBranchNameUseCase;
        this.requestValidator = requestValidator;
    }


    @Operation(summary = "Create branch", description = "Creates a new branch for a franchise")
    @ApiResponse(responseCode = "201", description = "Branch created successfully", content = @Content(schema = @Schema(implementation = BranchResponse.class)))
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "404", description = "Franchise not found")
    @ApiResponse(responseCode = "409", description = "Duplicate branch name")
    public Mono<ServerResponse> create(ServerRequest request) {
        Long franchiseId = requestValidator.pathLong(request, "franchiseId");

        return request.bodyToMono(BranchRequest.class)
                .flatMap(requestValidator::validate)
                .flatMap(branchRequest -> {
                    var branch = BranchMapper.toDomain(branchRequest, franchiseId);
                    return createBranchUseCase.create(branch);
                })
                .flatMap(branch -> ServerResponse.status(201)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(BranchMapper.toResponse(branch)));
    }

    @Operation(summary = "Update branch name", description = "Updates the name of an existing branch")
    @ApiResponse(responseCode = "200", description = "Name updated successfully", content = @Content(schema = @Schema(implementation = BranchResponse.class)))
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "404", description = "Franchise or branch not found")
    @ApiResponse(responseCode = "409", description = "Duplicate branch name")
    public Mono<ServerResponse> updateName(ServerRequest request) {
        Long franchiseId = requestValidator.pathLong(request, "franchiseId");
        Long branchId = requestValidator.pathLong(request, "branchId");

        return request.bodyToMono(BranchRequest.class)
                .flatMap(requestValidator::validate)
                .flatMap(body -> updateBranchNameUseCase.updateName(branchId, franchiseId, body.name()))
                .flatMap(branch -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(BranchMapper.toResponse(branch)));
    }
}

