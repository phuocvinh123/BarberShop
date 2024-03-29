package com.cg.service.hairDetailService.hairDetailResponse;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HairDetailListResponse {
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private List<String> images;
}
