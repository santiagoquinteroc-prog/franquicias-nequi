package com.red.franquicias.nequi.model.branch;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Branch {
    private Long id;
    private Long franchiseId;
    private String name;
}
