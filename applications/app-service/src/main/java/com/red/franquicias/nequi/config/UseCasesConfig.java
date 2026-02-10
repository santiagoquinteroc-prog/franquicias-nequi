package com.red.franquicias.nequi.config;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

    @Bean
    public CreateBranchNameUseCase createBranchNameUseCase(BranchRepository branchRepository) {
        return new CreateBranchNameUseCase(branchRepository);
    }

    @Bean
    public UpdateBranchNameUseCase updateBranchNameUseCase(BranchRepository branchRepository, FranchiseRepository franchiseRepository) {
        return new UpdateBranchNameUseCase(branchRepository, franchiseRepository);
    }

    @Bean
    public CreateProductNameUseCase createProductNameUseCase(ProductRepository productRepository, BranchRepository branchRepository, FranchiseRepository franchiseRepository) {
        return new CreateProductNameUseCase(productRepository, branchRepository, franchiseRepository);
    }

    @Bean
    public CreateFranchiseUseCase createFranchiseUseCase(FranchiseRepository franchiseRepository) {
        return new CreateFranchiseUseCase(franchiseRepository);
    }

    @Bean
    public UpdateFranchiseNameUseCase updateFranchiseNameUseCase(FranchiseRepository franchiseRepository) {
        return new UpdateFranchiseNameUseCase(franchiseRepository);
    }

    @Bean
    public UpdateProductNameUseCase updateProductNameUseCase(ProductRepository productRepository, BranchRepository branchRepository, FranchiseRepository franchiseRepository) {
        return new UpdateProductNameUseCase(productRepository, branchRepository, franchiseRepository);
    }

    @Bean
    public UpdateProductStockUseCase updateProductStockUseCase(ProductRepository productRepository, BranchRepository branchRepository, FranchiseRepository franchiseRepository) {
        return new UpdateProductStockUseCase(productRepository, branchRepository, franchiseRepository);
    }

    @Bean
    public RemoveProductUseCase removeProductUseCase(ProductRepository productRepository, BranchRepository branchRepository, FranchiseRepository franchiseRepository) {
        return new RemoveProductUseCase(productRepository, branchRepository, franchiseRepository);
    }

    @Bean
    public GetTopProductsByFranchiseUseCase getTopProductsByFranchiseUseCase(ProductRepository productRepository) {
        return new GetTopProductsByFranchiseUseCase(productRepository);
    }
}
