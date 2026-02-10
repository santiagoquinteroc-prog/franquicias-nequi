package com.red.franquicias.nequi.api.mapper;


import com.red.franquicias.nequi.api.dto.FranchiseRequest;
import com.red.franquicias.nequi.api.dto.FranchiseResponse;
import com.red.franquicias.nequi.model.franchise.Franchise;

public class FranchiseMapper {
    public static Franchise toDomain(FranchiseRequest request) {
        return new Franchise(null, request.name());
    }

    public static FranchiseResponse toResponse(Franchise franchise) {
        return new FranchiseResponse(franchise.getId(), franchise.getName());
    }
}



