package com.red.franquicias.nequi.api.mapper;


import com.red.franquicias.nequi.api.dto.BranchRequest;
import com.red.franquicias.nequi.api.dto.BranchResponse;
import com.red.franquicias.nequi.model.branch.Branch;

public class BranchMapper {
    public static Branch toDomain(BranchRequest request, Long franchiseId) {
        return new Branch(null, franchiseId, request.name());
    }

    public static BranchResponse toResponse(Branch branch) {
        return new BranchResponse(branch.getId(), branch.getFranchiseId(), branch.getName());
    }
}


