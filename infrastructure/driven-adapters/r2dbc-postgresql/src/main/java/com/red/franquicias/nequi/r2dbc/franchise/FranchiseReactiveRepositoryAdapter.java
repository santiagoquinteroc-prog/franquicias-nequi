package com.red.franquicias.nequi.r2dbc.franchise;

import com.red.franquicias.nequi.model.franchise.Franchise;
import com.red.franquicias.nequi.model.franchise.gateways.FranchiseRepository;
import com.red.franquicias.nequi.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class FranchiseReactiveRepositoryAdapter extends ReactiveAdapterOperations<Franchise, FranchiseEntity, Long, FranchiseReactiveRepository> implements FranchiseRepository {

    public FranchiseReactiveRepositoryAdapter(FranchiseReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Franchise.class));
    }

    @Override
    public Mono<Franchise> saveFranchise(Franchise franchise) {
        FranchiseEntity entity = this.toData(franchise);
        return repository.save(entity)
                .map(this::toEntity);
    }

    @Override
    public Mono<Franchise> findByIdFranchise(Long id) {
        return repository.findById(id)
                .map(this::toEntity);
    }

    @Override
    public Mono<Boolean> existsByNameFranchise(String name) {
        return repository.findByName(name)
                .hasElement();
    }
}
