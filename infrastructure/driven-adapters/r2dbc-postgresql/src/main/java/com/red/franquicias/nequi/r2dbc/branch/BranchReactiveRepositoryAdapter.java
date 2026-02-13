package com.red.franquicias.nequi.r2dbc.branch;

import com.red.franquicias.nequi.logging.AdapterLogger;
import com.red.franquicias.nequi.model.branch.Branch;
import com.red.franquicias.nequi.model.branch.gateways.BranchRepository;
import com.red.franquicias.nequi.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class BranchReactiveRepositoryAdapter extends ReactiveAdapterOperations<Branch, BranchEntity, Long, BranchReactiveRepository> implements BranchRepository {

    private final AdapterLogger adapterLogger;

    public BranchReactiveRepositoryAdapter(BranchReactiveRepository repository, ObjectMapper mapper, AdapterLogger adapterLogger) {
        super(repository, mapper, d -> mapper.map(d, Branch.class));
        this.adapterLogger = adapterLogger;
    }

    @Override
    public Mono<Branch> saveBranch(Branch branch) {
        long startTime = adapterLogger.startTimer();
        adapterLogger.outboundRequest("BranchRepository", "saveBranch", "branchId=" + branch.getId() + " franchiseId=" + branch.getFranchiseId());

        return this.repository.save(this.toData(branch))
                .map(this::toEntity)
                .doOnNext(result -> {
                    long duration = adapterLogger.calculateDuration(startTime);
                    adapterLogger.outboundResponse("BranchRepository", "saveBranch", "branchId=" + result.getId(), duration);
                })
                .doOnError(error -> adapterLogger.error("BranchRepository", "saveBranch", (Exception) error, "branchId=" + branch.getId()));
    }

    @Override
    public Mono<Branch> findByIdBranch(Long id) {
        long startTime = adapterLogger.startTimer();
        adapterLogger.outboundRequest("BranchRepository", "findByIdBranch", "branchId=" + id);

        return this.repository.findById(id)
                .map(this::toEntity)
                .doOnNext(result -> {
                    long duration = adapterLogger.calculateDuration(startTime);
                    adapterLogger.outboundResponse("BranchRepository", "findByIdBranch", "branchId=" + result.getId(), duration);
                })
                .doOnError(error -> adapterLogger.error("BranchRepository", "findByIdBranch", (Exception) error, "branchId=" + id));
    }

    @Override
    public Mono<Branch> findByIdAndFranchiseId(Long id, Long franchiseId) {
        long startTime = adapterLogger.startTimer();
        adapterLogger.outboundRequest("BranchRepository", "findByIdAndFranchiseId", "branchId=" + id + " franchiseId=" + franchiseId);

        return this.repository.findByIdAndFranchiseId(id, franchiseId)
                .map(this::toEntity)
                .doOnNext(result -> {
                    long duration = adapterLogger.calculateDuration(startTime);
                    adapterLogger.outboundResponse("BranchRepository", "findByIdAndFranchiseId", "branchId=" + result.getId(), duration);
                })
                .doOnError(error -> adapterLogger.error("BranchRepository", "findByIdAndFranchiseId", (Exception) error, "branchId=" + id + " franchiseId=" + franchiseId));
    }

    @Override
    public Mono<Boolean> existsByNameAndFranchiseId(String name, Long franchiseId) {
        long startTime = adapterLogger.startTimer();
        adapterLogger.outboundRequest("BranchRepository", "existsByNameAndFranchiseId", "name=" + name + " franchiseId=" + franchiseId);

        return this.repository.findByNameAndFranchiseId(name, franchiseId)
                .hasElement()
                .doOnNext(exists -> {
                    long duration = adapterLogger.calculateDuration(startTime);
                    adapterLogger.outboundResponse("BranchRepository", "existsByNameAndFranchiseId", "exists=" + exists, duration);
                })
                .doOnError(error -> adapterLogger.error("BranchRepository", "existsByNameAndFranchiseId", (Exception) error, "name=" + name + " franchiseId=" + franchiseId));
    }

    @Override
    public Flux<Branch> findByFranchiseId(Long franchiseId) {
        long startTime = adapterLogger.startTimer();
        adapterLogger.outboundRequest("BranchRepository", "findByFranchiseId", "franchiseId=" + franchiseId);

        return this.repository.findByFranchiseId(franchiseId)
                .map(this::toEntity)
                .doFinally(signalType -> {
                    long duration = adapterLogger.calculateDuration(startTime);
                    if (signalType.toString().equals("ON_COMPLETE")) {
                        adapterLogger.outboundResponse("BranchRepository", "findByFranchiseId", "franchiseId=" + franchiseId, duration);
                    }
                })
                .doOnError(error -> adapterLogger.error("BranchRepository", "findByFranchiseId", (Exception) error, "franchiseId=" + franchiseId));
    }
}
