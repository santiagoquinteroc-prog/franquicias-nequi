package com.red.franquicias.nequi.config;

import com.red.franquicias.nequi.logging.AdapterLogger;
import com.red.franquicias.nequi.model.branch.gateways.BranchRepository;
import com.red.franquicias.nequi.model.franchise.gateways.FranchiseRepository;
import com.red.franquicias.nequi.model.product.gateways.ProductRepository;
import com.red.franquicias.nequi.usecase.createbranch.CreateBranchNameUseCase;
import com.red.franquicias.nequi.usecase.createfranchise.CreateFranchiseUseCase;
import com.red.franquicias.nequi.usecase.createproduct.CreateProductNameUseCase;
import com.red.franquicias.nequi.usecase.gettopproductsbyfranchise.GetTopProductsByFranchiseUseCase;
import com.red.franquicias.nequi.usecase.removeproduct.RemoveProductUseCase;
import com.red.franquicias.nequi.usecase.updatebranch.UpdateBranchNameUseCase;
import com.red.franquicias.nequi.usecase.updatefranchise.UpdateFranchiseNameUseCase;
import com.red.franquicias.nequi.usecase.updateproduct.UpdateProductNameUseCase;
import com.red.franquicias.nequi.usecase.updateproductstock.UpdateProductStockUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

class UseCasesConfigTest {

    private UseCasesConfig useCasesConfig;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private FranchiseRepository franchiseRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCasesConfig = new UseCasesConfig();
    }

    @Test
    void useCasesConfig_shouldBeInstantiable() {
        assertThat(UseCasesConfig.class).isNotNull();
        assertThat(useCasesConfig).isNotNull();
    }

    @Test
    void adapterLogger_shouldReturnAdapterLogger() {
        AdapterLogger result = useCasesConfig.adapterLogger();

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(AdapterLogger.class);
    }

    @Test
    void createBranchNameUseCase_shouldReturnUseCase() {
        CreateBranchNameUseCase result = useCasesConfig.createBranchNameUseCase(branchRepository);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(CreateBranchNameUseCase.class);
    }

    @Test
    void updateBranchNameUseCase_shouldReturnUseCase() {
        UpdateBranchNameUseCase result = useCasesConfig.updateBranchNameUseCase(branchRepository, franchiseRepository);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(UpdateBranchNameUseCase.class);
    }

    @Test
    void createProductNameUseCase_shouldReturnUseCase() {
        CreateProductNameUseCase result = useCasesConfig.createProductNameUseCase(productRepository, branchRepository, franchiseRepository);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(CreateProductNameUseCase.class);
    }

    @Test
    void createFranchiseUseCase_shouldReturnUseCase() {
        CreateFranchiseUseCase result = useCasesConfig.createFranchiseUseCase(franchiseRepository);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(CreateFranchiseUseCase.class);
    }

    @Test
    void updateFranchiseNameUseCase_shouldReturnUseCase() {
        UpdateFranchiseNameUseCase result = useCasesConfig.updateFranchiseNameUseCase(franchiseRepository);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(UpdateFranchiseNameUseCase.class);
    }

    @Test
    void updateProductNameUseCase_shouldReturnUseCase() {
        UpdateProductNameUseCase result = useCasesConfig.updateProductNameUseCase(productRepository, branchRepository, franchiseRepository);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(UpdateProductNameUseCase.class);
    }

    @Test
    void updateProductStockUseCase_shouldReturnUseCase() {
        UpdateProductStockUseCase result = useCasesConfig.updateProductStockUseCase(productRepository, branchRepository, franchiseRepository);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(UpdateProductStockUseCase.class);
    }

    @Test
    void removeProductUseCase_shouldReturnUseCase() {
        RemoveProductUseCase result = useCasesConfig.removeProductUseCase(productRepository, branchRepository, franchiseRepository);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(RemoveProductUseCase.class);
    }

    @Test
    void getTopProductsByFranchiseUseCase_shouldReturnUseCase() {
        GetTopProductsByFranchiseUseCase result = useCasesConfig.getTopProductsByFranchiseUseCase(productRepository);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(GetTopProductsByFranchiseUseCase.class);
    }
}


