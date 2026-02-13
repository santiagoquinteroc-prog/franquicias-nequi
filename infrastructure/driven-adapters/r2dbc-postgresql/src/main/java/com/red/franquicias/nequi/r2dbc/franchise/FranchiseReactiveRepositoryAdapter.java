package com.red.franquicias.nequi.r2dbc.franchise;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import com.red.franquicias.nequi.exception.R2dbcExceptionMapper;
import com.red.franquicias.nequi.logging.AdapterLogger;
import com.red.franquicias.nequi.model.franchise.Franchise;
import com.red.franquicias.nequi.model.franchise.gateways.FranchiseRepository;
import com.red.franquicias.nequi.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class FranchiseReactiveRepositoryAdapter extends ReactiveAdapterOperations<Franchise, FranchiseEntity, Long, FranchiseReactiveRepository> implements FranchiseRepository {

    private final AdapterLogger adapterLogger;

    public FranchiseReactiveRepositoryAdapter(FranchiseReactiveRepository repository, ObjectMapper mapper, AdapterLogger adapterLogger) {
        super(repository, mapper, d -> mapper.map(d, Franchise.class));
        this.adapterLogger = adapterLogger;
    }

    @Override
    public Mono<Franchise> saveFranchise(Franchise franchise) {
        long startTime = adapterLogger.startTimer();
        adapterLogger.outboundRequest("FranchiseRepository", "saveFranchise", "franchiseId=" + franchise.getId());

        FranchiseEntity entity = this.toData(franchise);
        return repository.save(entity)
                .map(this::toEntity)
                .doOnNext(result -> {
                    long duration = adapterLogger.calculateDuration(startTime);
                    adapterLogger.outboundResponse("FranchiseRepository", "saveFranchise", "franchiseId=" + result.getId(), duration);
                })
                .doOnError(error -> adapterLogger.error("FranchiseRepository", "saveFranchise", error, "franchiseId=" + franchise.getId()))
                .onErrorMap(this::mapException);
    }

    @Override
    public Mono<Franchise> findByIdFranchise(Long id) {
        long startTime = adapterLogger.startTimer();
        adapterLogger.outboundRequest("FranchiseRepository", "findByIdFranchise", "franchiseId=" + id);

        return repository.findById(id)
                .map(this::toEntity)
                .doOnNext(result -> {
                    long duration = adapterLogger.calculateDuration(startTime);
                    adapterLogger.outboundResponse("FranchiseRepository", "findByIdFranchise", "franchiseId=" + result.getId(), duration);
                })
                .doOnError(error -> adapterLogger.error("FranchiseRepository", "findByIdFranchise", error, "franchiseId=" + id))
                .onErrorMap(this::mapException);
    }

    @Override
    public Mono<Boolean> existsByNameFranchise(String name) {
        long startTime = adapterLogger.startTimer();
        adapterLogger.outboundRequest("FranchiseRepository", "existsByNameFranchise", "name=" + name);

        return repository.findByName(name)
                .hasElement()
                .doOnNext(exists -> {
                    long duration = adapterLogger.calculateDuration(startTime);
                    adapterLogger.outboundResponse("FranchiseRepository", "existsByNameFranchise", "exists=" + exists, duration);
                })
                .doOnError(error -> adapterLogger.error("FranchiseRepository", "existsByNameFranchise", error, "name=" + name))
                .onErrorMap(this::mapException);
    }

    private Throwable mapException(Throwable throwable) {
        return R2dbcExceptionMapper.mapToBusinessOrTechnical(
                throwable,
                TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS,
                TechnicalMessage.FRANCHISE_NOT_FOUND,
                TechnicalMessage.FRANCHISE_CREATE_ERROR
        );
    }
}

