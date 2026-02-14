package com.red.franquicias.nequi.api.router;

import com.red.franquicias.nequi.api.handler.BranchHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BranchRouterTest {

    @Mock
    private BranchHandler handler;

    private BranchRouter branchRouter;

    @BeforeEach
    void setUp() {
        branchRouter = new BranchRouter();
    }

    @Test
    void branchRouter_shouldBeInstantiable() {
        assertThat(branchRouter).isNotNull();
    }

    @Test
    void branchRoutes_shouldReturnRouterFunction() {
        RouterFunction<ServerResponse> result = branchRouter.branchRoutes(handler);

        assertThat(result).isNotNull();
    }
}

