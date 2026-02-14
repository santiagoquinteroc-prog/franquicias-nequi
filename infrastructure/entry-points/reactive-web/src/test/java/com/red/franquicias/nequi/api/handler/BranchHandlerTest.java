package com.red.franquicias.nequi.api.handler;

import com.red.franquicias.nequi.api.dto.BranchRequest;
import com.red.franquicias.nequi.api.validation.RequestValidator;
import com.red.franquicias.nequi.logging.AdapterLogger;
import com.red.franquicias.nequi.model.branch.Branch;
import com.red.franquicias.nequi.usecase.createbranch.CreateBranchNameUseCase;
import com.red.franquicias.nequi.usecase.updatebranch.UpdateBranchNameUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BranchHandlerTest {

    @Mock
    private CreateBranchNameUseCase createBranchUseCase;

    @Mock
    private UpdateBranchNameUseCase updateBranchNameUseCase;

    @Mock
    private RequestValidator requestValidator;

    @Mock
    private AdapterLogger adapterLogger;

    private BranchHandler branchHandler;

    @BeforeEach
    void setUp() {
        branchHandler = new BranchHandler(
                createBranchUseCase,
                updateBranchNameUseCase,
                requestValidator,
                adapterLogger
        );
        when(adapterLogger.startTimer()).thenReturn(0L);
        when(adapterLogger.calculateDuration(anyLong())).thenReturn(100L);
    }

    @Test
    void branchHandler_shouldBeInstantiable() {
        assertThat(branchHandler).isNotNull();
    }

    @Test
    void create_shouldReturnCreatedBranch() {
        BranchRequest request = new BranchRequest("Test Branch");
        Branch branch = new Branch();
        branch.setId(1L);
        branch.setName("Test Branch");
        branch.setFranchiseId(10L);

        MockServerRequest serverRequest = MockServerRequest.builder()
                .pathVariable("franchiseId", "10")
                .body(Mono.just(request));

        when(requestValidator.pathLong(any(), eq("franchiseId"))).thenReturn(10L);
        when(requestValidator.validate(any(BranchRequest.class))).thenReturn(Mono.just(request));
        when(createBranchUseCase.create(any(Branch.class))).thenReturn(Mono.just(branch));

        Mono<ServerResponse> result = branchHandler.create(serverRequest);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.statusCode().value()).isEqualTo(201);
                })
                .verifyComplete();
    }

    @Test
    void updateName_shouldReturnUpdatedBranch() {
        BranchRequest request = new BranchRequest("Updated Branch");
        Branch branch = new Branch();
        branch.setId(1L);
        branch.setName("Updated Branch");
        branch.setFranchiseId(10L);

        MockServerRequest serverRequest = MockServerRequest.builder()
                .pathVariable("franchiseId", "10")
                .pathVariable("branchId", "1")
                .body(Mono.just(request));

        when(requestValidator.pathLong(any(), eq("franchiseId"))).thenReturn(10L);
        when(requestValidator.pathLong(any(), eq("branchId"))).thenReturn(1L);
        when(requestValidator.validate(any(BranchRequest.class))).thenReturn(Mono.just(request));
        when(updateBranchNameUseCase.updateName(eq(1L), eq(10L), anyString())).thenReturn(Mono.just(branch));

        Mono<ServerResponse> result = branchHandler.updateName(serverRequest);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.statusCode().value()).isEqualTo(200);
                })
                .verifyComplete();
    }
}

