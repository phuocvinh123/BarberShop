package com.cg.service.hairDetailService.hairDetailResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HairDetailResponse {
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private List<String> images;
}
