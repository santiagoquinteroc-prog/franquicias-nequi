package com.red.franquicias.nequi.r2dbc.branch;

import com.red.franquicias.nequi.model.branch.Branch;
import com.red.franquicias.nequi.model.branch.gateways.BranchRepository;
import com.red.franquicias.nequi.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class BranchReactiveRepositoryAdapter extends ReactiveAdapterOperations<Branch, BranchEntity, Long, BranchReactiveRepository> implements BranchRepository {

    public BranchReactiveRepositoryAdapter(BranchReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Branch.class));
    }

    @Override
    public Mono<Branch> saveBranch(Branch branch) {
        return this.repository.save(this.toData(branch))
                .map(this::toEntity);
    }

    @Override
    public Mono<Branch> findByIdBranch(Long id) {
        return this.repository.findById(id)
                .map(this::toEntity);
    }

    @Override
    public Mono<Branch> findByIdAndFranchiseId(Long id, Long franchiseId) {
        return this.repository.findByIdAndFranchiseId(id, franchiseId)
                .map(this::toEntity);
    }

    @Override
    public Mono<Boolean> existsByNameAndFranchiseId(String name, Long franchiseId) {
        return this.repository.findByNameAndFranchiseId(name, franchiseId)
                .hasElement();
    }

    @Override
    public Flux<Branch> findByFranchiseId(Long franchiseId) {
        return this.repository.findByFranchiseId(franchiseId)
                .map(this::toEntity);
    }
}
